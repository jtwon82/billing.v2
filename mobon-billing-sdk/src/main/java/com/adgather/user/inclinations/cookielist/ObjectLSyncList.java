package com.adgather.user.inclinations.cookielist;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author yhlim
 *
 * @param <E>
 */
public class ObjectLSyncList<E> extends ObjectSyncList<E> {
	private static final long serialVersionUID = -8798383082078679102L;
	private static final Logger logger = Logger.getLogger(ObjectLSyncList.class);

	public ObjectLSyncList(String delimeter) {
		super(delimeter);
	}
	
	public ObjectLSyncList(String delimeter, long curTime, long syncTime) {
		super(delimeter, curTime, syncTime);
	}

	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if(element == null)		return false;
		
		boolean bRes = false;
		String key = ((CookieVal)element).getKey();
		for (int idx = 0; idx < size(); idx++) {
			CookieVal obj = (CookieVal)get(idx);
			if ( key.equals(obj.getKey()) ) {
				remove(idx);
				obj.modValue(element, bAppendValue);
				add(0, (E)obj);
				bRes = true;
				break;
			}
		}
		setNeedUpdateAll(true);
		return bRes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ObjectLSyncList<E> clone() {
		ObjectLSyncList<E> newList = new ObjectLSyncList<E>(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((E)((CookieVal)get(idx)).clone());
			}
		} catch (Exception e) {}
		
		return newList;
	}
}