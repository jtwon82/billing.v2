package com.adgather.user.inclinations.cookielist.old;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookielist.ObjectLSyncList;
import com.adgather.user.inclinations.cookieval.inct.InctUm;
import com.adgather.user.inclinations.cookieval.inct.old.InctUm20171107Before;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author yhlim
 *
 * @param <E>
 */
public class InctUm20171107BeforeSyncList extends ObjectLSyncList<InctUm> {
	private static final long serialVersionUID = -8798383082078679102L;
	private static final Logger logger = Logger.getLogger(InctUm20171107BeforeSyncList.class);

	public InctUm20171107BeforeSyncList(String delimeter) {
		super(delimeter);
	}
	
	public InctUm20171107BeforeSyncList(String delimeter, long curTime, long syncTime) {
		super(delimeter, curTime, syncTime);
	}

	private InctUm toBeforeObj(Object nowObj) {
		if(nowObj == null)	return null;
		if(!(nowObj instanceof InctUm))	return null;
		
		
		return new InctUm20171107Before((InctUm)nowObj);
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
	public boolean applyMod(InctUm element, int idx) {
		element = toBeforeObj(element);

		return super.applyMod(element, idx);
	}
	
	@Override
	public InctUm20171107BeforeSyncList clone() {
		InctUm20171107BeforeSyncList newList = new InctUm20171107BeforeSyncList(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((InctUm)((CookieVal)get(idx)).clone());
			}
		} catch (Exception e) {}
		
		return newList;
	}
}