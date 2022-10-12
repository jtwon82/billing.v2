package com.adgather.user.inclinations.cookielist.old;

import com.adgather.user.inclinations.cookielist.InctHuSyncList;
import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.user.inclinations.cookieval.inct.old.InctHu20171107Before;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author yhlim
 *
 * @param <E>
 */
public class InctHu20171107BeforeSyncList extends InctHuSyncList<InctHu> {
	private static final long serialVersionUID = -252118358486751904L;

	public InctHu20171107BeforeSyncList(String delimeter) {
		super(delimeter);
	}

	private InctHu toBeforeObj(Object nowObj) {
		if(nowObj == null)	return null;
		if(!(nowObj instanceof InctHu))	return null;
		
		
		return new InctHu20171107Before((InctHu)nowObj);
	}

	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		element = toBeforeObj(element);
		
		return super.applyAdd(element, bAppendValue, maxCnt);
	}
	
	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		element = toBeforeObj(element);
		
		return super.applyMod(element, bAppendValue);
	}
	
	@Override
	public boolean applyMod(InctHu element, int idx) {
		element = toBeforeObj(element);

		return super.applyMod(element, idx);
	}

	@Override
	public InctHu20171107BeforeSyncList clone() {
		InctHu20171107BeforeSyncList newList = new InctHu20171107BeforeSyncList(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((InctHu)get(idx).clone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newList;
	}
}