package com.adgather.user.inclinations.cookielist;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookielist.inter.CookieList;

/**
 * 동기화 기능이 포함된 리스트
 * @author yhlim
 *
 * @param <E>
 */
public abstract class SyncList<E> extends ArrayList<E> implements CookieList {
	private static final long serialVersionUID = 1L;

	/** static values  ************************************************/
	/** 기본 분할자 **/
	//protected static final String DELIMETER = "#";
	protected final String DELIMETER;
	/** values  ****************************************************/
	protected long curTime;			// 현재시간(데이터의 하나의 시점으로 확인하기 위한 용도)
	protected long syncTime;		// 데이터의 동기화된 시간
	private boolean bNeedUpdateCookie = false;
	private boolean bNeedUpdateMongo = false;	

	/** create methods *********************************************/
	public SyncList(String delimeter) {
		this.DELIMETER = delimeter;
	}
	/** get/set methods *(*******************************************/
	
	public long getCurTime() {
		return curTime;
	}
	public void setCurTime(long curTime) {
		this.curTime = curTime;
	}
	public long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}
	
	public boolean isNeedUpdateCookie() {
		return bNeedUpdateCookie;
	}
	public boolean isNeedUpdateMongo() {
		return bNeedUpdateMongo;
	}
	
	public void setLoadCookie() {
		// 몽고를 업데이트 할 수 있게 설정
		this.bNeedUpdateCookie = false;
		this.bNeedUpdateMongo = true;
	}
	
	public void setLoadMongo() {
		// 쿠키를 업데이트 할 수 있게 설정
		this.bNeedUpdateCookie = true;
		this.bNeedUpdateMongo = false;
	}
	
	public void setNeedUpdateCookie(boolean b) {
		// 쿠키 상태만 변경
		this.bNeedUpdateCookie = b;
	}
	public void setNeedUpdateMongo(boolean b) {
		// 몽고 상태만 변경
		this.bNeedUpdateMongo = b;
	}
	
	public void setNeedUpdateAll(boolean b) {
		// 전체 동일 적용
		this.bNeedUpdateCookie = b;
		this.bNeedUpdateMongo = b;
	}
	
	public void setNeedUpdate(SyncList<E> obj) {
		// 객체 복제를 위한 상태 복제
		this.bNeedUpdateCookie = obj.bNeedUpdateCookie;
		this.bNeedUpdateMongo = obj.bNeedUpdateMongo;
	}
	
	/**
	 * 2개의 객체의 동기화시간이 다른가확인
	 * @method isDeff
	 * @see
	 * @param obj1
	 * @param obj2
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isDeff(SyncList obj1, SyncList obj2) {
		if(obj1 == null && obj2 == null)		return false;
		if(obj1 == null) 							return true;
		if(obj2 == null)							return true;
		return obj1.getSyncTime() != obj2.getSyncTime();
	}
	
	@Override
	public abstract SyncList<E> clone();
	
	/*********************************************************************************/
	/** 동기화시간과 리스트데이터  분할 **/
	public String[] splitStepOne(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		if(StringUtils.contains(cookieValue, ")")) {
			String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, ")");
			if(strs == null || strs.length != 2)			throw new Exception("This String Is Not SynArrayList Format.");
			
			strs[0] = strs[0].replace("(", "");
			
			return strs;
			
		} else if (StringUtils.contains(cookieValue, "$")) {
			String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, "$");
			if(strs == null || strs.length != 2)			throw new Exception("This String Is Not SynArrayList Format.");
			
			return strs;
			
		} else {
			return null;
		}
	}
	
	/** 리스트 데이터 분할 **/
	public String[] splitStepTwo(String listValue) throws Exception {
		if(StringUtils.isEmpty(listValue))		throw new Exception("List Value Is Empty.");
		
		String[] strs = StringUtils.split(listValue, DELIMETER);
		if(strs == null)								throw new Exception("This String Is Not SynArrayList Format.");
		
		return strs;
	}
	
	/** 데이터 추가 **/
	public abstract boolean applyAdd(Object element, boolean bAppendValue, int maxCnt);
	
	/** 데이터 변경 **/
	public abstract boolean applyMod(Object element, boolean bAppendValue);
	
	/** 데이터 삭제 **/
	public abstract boolean applyDel(Object element);

	/** 데이터 삭제(index 위치의 데이터 삭제) **/	
	public boolean applyMod(E element, int idx) {
		if(element == null)		return false;
		if(idx >= size())			return false;
		
		set(idx, element);
		setNeedUpdateAll(true);
		return true;
	}

	/** 데이터 변경(index 위치의 데이터 변경) **/
	public boolean applyDel(int idx) {
		if(idx >= size())		return false;
		
		remove(idx);
		setNeedUpdateAll(true);
		return true;
	}
	
	/*********************************************************************************/
	
}
