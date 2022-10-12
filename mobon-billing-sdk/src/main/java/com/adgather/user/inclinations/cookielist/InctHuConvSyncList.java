package com.adgather.user.inclinations.cookielist;

import com.adgather.user.inclinations.cookieval.inct.InctHuConv;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuConvCtr;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author kwseo
 *
 * @param <E>
 */
public class InctHuConvSyncList<E extends InctHuConv> extends ObjectSyncList<E> {

	private static final long serialVersionUID = 8415685973490246863L;

	public InctHuConvSyncList(String delimeter) {
		super(delimeter);
	}
	
	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		if(element == null)		return false;
		
		boolean bRes = InctHuConvCtr.sortAdd(this, (E)element, maxCnt);
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
		
		boolean bRes = InctHuConvCtr.sortMod(this, (E)element);
		setNeedUpdateAll(true);
		
		return bRes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public InctHuConvSyncList<E> clone() {
		InctHuConvSyncList<E> newList = new InctHuConvSyncList<E>(DELIMETER);
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