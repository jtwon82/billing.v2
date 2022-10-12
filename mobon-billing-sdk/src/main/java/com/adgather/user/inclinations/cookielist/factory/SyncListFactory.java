package com.adgather.user.inclinations.cookielist.factory;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;

/**
 * 동기화리스트 객체 생성기
 * @author yhlim
 *
 */
public class SyncListFactory {
	private SyncListFactory() {}

	public static <E> SyncList<E> create(CookieDef cookieDef, SyncList<E> orgList) throws Exception{
		if(orgList == null)		throw new Exception("Org List Is Null.");
		
		return create(cookieDef, orgList.getCurTime(), orgList.getSyncTime());
	}
	
	public static <E> SyncList<E> create(CookieDef cookieDef, long curTime) throws Exception{
		return create(cookieDef, curTime, cookieDef instanceof RefactCookieDef ? 0 : curTime);
	}

	private static <E> SyncList<E> create(CookieDef cookieDef, long curTime, long syncTime) throws Exception{
		if(cookieDef == null)		throw new Exception("Cookie Define Is Null.");
		
		@SuppressWarnings("unchecked")
		SyncList<E> obj = (SyncList<E>)cookieDef.newList();
		if(obj == null)			throw new Exception("Cookie Define Is Not Set NewList.");
		
		if (obj != null) {
			obj.setCurTime(curTime);
			obj.setSyncTime(syncTime);
		}
		return obj;
	}
}
