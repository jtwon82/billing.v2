package com.adgather.user.inclinations.memory.simulator;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.cookieval.inct.InctKl;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctKl;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.util.ErrorLog;
import com.adgather.util.HangulCharsetDetector;

/**
 * HU 대행자
 * @author yhlim
 *
 */
public class InctKlSimul extends SimulMemoryObj<OldInctKl> {
	private static final Logger logger = Logger.getLogger(InctKlSimul.class);
	
	public InctKlSimul(MemoryObj<OldInctKl> refactMemoryObject) {
		super(refactMemoryObject);
	}
	

	@Override
	public void applyAdd(Object element, boolean bAppendValue) {
		if(refactMemoryObject == null)	return;
		
		if (element instanceof InctKl) {
			refactMemoryObject.applyAdd(element, bAppendValue);
		}
	}

	@Override
	public void applyMod(Object element, boolean bAppendValue) {
		if(refactMemoryObject == null)	return;
		
		if (element instanceof InctKl) {
			refactMemoryObject.applyMod(element, bAppendValue);
		}
	}

	@Override
	public void applyDel(Object element) {
		if(refactMemoryObject == null)	return;
		
		if (element instanceof InctKl) {
			refactMemoryObject.applyDel(element);
		}
	}
	
	@Override
	public SyncList<OldInctKl> getServiceData() {
		return _getFilteredList(this.refactMemoryObject.getServiceData(), -1);
	}
	
	@Override
	public SyncList<OldInctKl> getSaveCookieListData(int maxCnt) {
		return _getFilteredList(this.refactMemoryObject.getSaveSyncList(), maxCnt);
	}
	
	@Override
	public void setSavedMongoCnt() {
		savedMongoCnt = 0;
		
		SyncList<OldInctKl> newList = _getFilteredList(this.refactMemoryObject.getSaveSyncList(), refactMemoryObject.getMongoMaxCnt());
		if(newList == null)		return;
		
		savedMongoCnt = newList.size();
	}
	
	
	private SyncList<OldInctKl> _getFilteredList(SyncList<OldInctKl> orgList, int maxCnt) {
		if(orgList == null)		return null;
		
		SyncList<OldInctKl> newList = null;
		try {
			newList = SyncListFactory.create(super.refactMemoryObject.getCookieDef(), orgList);
			
			for (int idx = 0; idx < orgList.size(); idx++) {
				OldInctKl org = orgList.get(idx);
				if(org == null) continue;
				
				if(HangulCharsetDetector.isBrokenString(org.getKeyword())) {
					continue;
				}
				
				newList.add(org);
				
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
