package com.adgather.user.inclinations.memory.simulator;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.util.ErrorLog;

/**
 * HU 대행자
 * @author yhlim
 *
 */
public class InctUmSimul extends SimulMemoryObj<String> {
	private static final Logger logger = Logger.getLogger(InctUmSimul.class);
	
	public InctUmSimul(MemoryObj<String> refactMemoryObject) {
		super(refactMemoryObject);
	}
	

	@Override
	public void applyAdd(Object element, boolean bAppendValue) {
		if(!(element instanceof String))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyAdd(element, bAppendValue);
		} else {
			applyAdd(new String((String)element), bAppendValue);
		}
	}

	@Override
	public void applyMod(Object element, boolean bAppendValue) {
		if(!(element instanceof String))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyMod(element, bAppendValue);
		} else {
			applyMod(new String((String)element), bAppendValue);
		}
	}

	@Override
	public void applyDel(Object element) {
		if(!(element instanceof String))	return;
		
		if(refactMemoryObject!=null) {
			refactMemoryObject.applyDel(element);
		} else {
			applyDel(new String((String)element));
		}
	}
	
	@Override
	public SyncList<String> getServiceData() {
		return _getFilteredList(this.refactMemoryObject.getServiceData(), -1);
	}
	
	@Override
	public SyncList<String> getSaveCookieListData(int maxCnt) {
		return _getFilteredList(this.refactMemoryObject.getSaveSyncList(), maxCnt);
	}
	
	@Override
	public void setSavedMongoCnt() {
		savedMongoCnt = 0;
		
		SyncList<String> newList = _getFilteredList(this.refactMemoryObject.getSaveSyncList(), refactMemoryObject.getMongoMaxCnt());
		if(newList == null)		return;
		
		savedMongoCnt = newList.size();
	}
	
	
	private SyncList<String> _getFilteredList(SyncList<String> orgList, int maxCnt) {
		if(orgList == null)		return null;
		
		SyncList<String> newList = null;
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
