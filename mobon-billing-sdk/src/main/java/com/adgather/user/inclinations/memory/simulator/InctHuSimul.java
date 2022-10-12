package com.adgather.user.inclinations.memory.simulator;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctHu;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.util.ErrorLog;

/**
 * HU 대행자
 * @author yhlim
 *
 */
public class InctHuSimul extends SimulMemoryObj<OldInctHu> {
	private static final Logger logger = Logger.getLogger(InctHuSimul.class);
	
	public InctHuSimul(MemoryObj<OldInctHu> refactMemoryObject) {
		super(refactMemoryObject);
	}
	
	@Override
	public void applyAdd(Object element, boolean bAppendValue) {
		if(!(element instanceof InctHu))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyAdd(element, bAppendValue);
		} else {
			applyAdd(new OldInctHu((InctHu)element), bAppendValue);	
		}
	}

	@Override
	public void applyMod(Object element, boolean bAppendValue) {
		if(!(element instanceof InctHu))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyMod(element, bAppendValue);
		} else {
			applyMod(new OldInctHu((InctHu)element), bAppendValue);
		}
	}

	@Override
	public void applyDel(Object element) {
		if(!(element instanceof InctHu))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyDel(element);
		} else {
			applyDel(new OldInctHu((InctHu)element));
		}
	}
	
	@Override
	public SyncList<OldInctHu> getServiceData() {
		return _getFilteredList(super.refactMemoryObject.getServiceData(), -1);
	}
	
	@Override
	public SyncList<OldInctHu> getSaveCookieListData(int maxCnt) {
		return _getFilteredList(super.refactMemoryObject.getSaveSyncList(), maxCnt);
	}
	
	@Override
	public void setSavedMongoCnt() {
		savedMongoCnt = 0;
		
		SyncList<OldInctHu> newList = _getFilteredList(super.refactMemoryObject.getSaveSyncList(), refactMemoryObject.getMongoMaxCnt());
		if(newList == null)		return;
		
		savedMongoCnt = newList.size();
	}
	
	
	@SuppressWarnings("unchecked")
	private SyncList<OldInctHu> _getFilteredList(SyncList<OldInctHu> orgList, int maxCnt) {
		if(orgList == null)		return null;
		
		SyncList<OldInctHu> newList = null;
		try {
			newList = SyncListFactory.create(super.refactMemoryObject.getCookieDef(), orgList);
			
			for (int idx = 0; idx < orgList.size(); idx++) {
				newList.add(orgList.get(idx));
				
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
}
