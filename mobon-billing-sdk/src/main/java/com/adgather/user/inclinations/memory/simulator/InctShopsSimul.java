package com.adgather.user.inclinations.memory.simulator;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.cookieval.inct.InctShops;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctShopLog;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.util.ErrorLog;

/**
 * CW 샵로그 대행자
 * @author yhlim
 *
 */
public abstract class InctShopsSimul extends SimulMemoryObj<OldInctShopLog> {
	private static final Logger logger = Logger.getLogger(InctShopsSimul.class);
	
	public InctShopsSimul(MemoryObj<OldInctShopLog> refactMemoryObject) {
		super(refactMemoryObject);
	}
	

	@Override
	public void applyAdd(Object element, boolean bAppendValue) {
		if(element instanceof OldInctShopLog) {
			refactMemoryObject.applyAdd(element, bAppendValue);
		} else if (element instanceof InctShops) {
			applyAdd(new OldInctShopLog((InctShops)element), bAppendValue);	
		}
	}

	@Override
	public void applyMod(Object element, boolean bAppendValue) {
		if (element instanceof OldInctShopLog) {
			refactMemoryObject.applyMod(element, bAppendValue);
		} else if (element instanceof InctShops) {
			applyMod(new OldInctShopLog((InctShops) element), bAppendValue);
		}
	}

	@Override
	public void applyDel(Object element) {
		if (element instanceof OldInctShopLog) {
			refactMemoryObject.applyDel(element);
		} else if (element instanceof InctShops) {
			applyDel(new OldInctShopLog((InctShops) element));
		}
	}
	
	
	@Override
	public SyncList<OldInctShopLog> getServiceData() {
		SyncList<OldInctShopLog> orgList = this.refactMemoryObject.getServiceData();
		if(orgList.size() == 0)		return null;	
		
		return _getFilteredList(orgList, -1);
	}
	
	@Override
	public SyncList<OldInctShopLog> getSaveCookieListData(int maxCnt) {
		SyncList<OldInctShopLog> orgList = this.refactMemoryObject.getSaveSyncList();
		if(orgList == null)		return null;
		
		return _getFilteredList(orgList, maxCnt);
	}
	
	@Override
	public void setSavedMongoCnt() {
		savedMongoCnt = 0;
		
		SyncList<OldInctShopLog> orgList = this.refactMemoryObject.getSaveSyncList();
		if(orgList.size() == 0)	return;
		
		SyncList<OldInctShopLog> newList = _getFilteredList(orgList, refactMemoryObject.getMongoMaxCnt());
		if(newList == null) 	return;
		
		savedMongoCnt = newList.size();
	}
	
	
	/** cw용 shopLog만 추출, -1이면 전체, 그외 maxCnt 제한 **/
	@SuppressWarnings("unchecked")
	private SyncList<OldInctShopLog> _getFilteredList(SyncList<OldInctShopLog> orgList, int maxCnt) {
		if(orgList == null)		return null;
		
		SyncList<OldInctShopLog> newList = null;
		try {
			newList = SyncListFactory.create(super.refactMemoryObject.getCookieDef(), orgList);
			
			for (int idx = 0; idx < orgList.size(); idx++) {
				OldInctShopLog obj = orgList.get(idx);
				if(isTargetShopLog(obj)) {
					newList.add(obj);
				}
				
				// -1 이면 전체, 그렇지 않을 경우 maxCnt까지
				if(maxCnt >= 0  && newList.size() >= maxCnt ) {
					break;
				}
			}
			
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		return newList;
	} 
	
	
	protected abstract boolean isTargetShopLog(OldInctShopLog obj);
}
