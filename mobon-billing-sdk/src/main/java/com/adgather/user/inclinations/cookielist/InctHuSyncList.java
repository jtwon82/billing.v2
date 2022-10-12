package com.adgather.user.inclinations.cookielist;

import com.adgather.user.inclinations.cookieval.inct.InctHu;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author yhlim
 *
 * @param <E>
 */
public class InctHuSyncList<E extends InctHu> extends ObjectSyncList<E> {
	private static final long serialVersionUID = -252118358486751904L;

	public InctHuSyncList(String delimeter) {
		super(delimeter);
	}
	
	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		if(element == null)		return false;
		
		boolean bRes = InctHuCtr.sortAdd(this, (E)element, maxCnt, InctHuCtr.getEarnerMinVisit(), InctHuCtr.getFrontCutCnt());
		setNeedUpdateAll(true);
		
		return bRes;
	}

	@Override
	public boolean applyDel(Object element) {
		return super.applyDel(element);
	}
	
	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if(element == null)		return false;
		
		boolean bRes = InctHuCtr.sortMod(this, (E)element);
		setNeedUpdateAll(true);
		
		return bRes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public InctHuSyncList<E> clone() {
		InctHuSyncList<E> newList = new InctHuSyncList<E>(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((E)get(idx).clone());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newList;
	}
}