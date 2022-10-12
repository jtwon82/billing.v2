package com.adgather.user.inclinations.memory;

import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;

/**
 * MemoryObject 대행자(신규 쿠키 기억장소의 대행자; 기존 쿠키가 이용)
 * 
 * @author yhlim
 *
 * @param <E>
 */
public abstract class SimulMemoryObj<E> extends MemoryObj<E> {
	protected MemoryObj<E> refactMemoryObject;

	public SimulMemoryObj(MemoryObj<E> refactMemoryObject) {
		super(null);
		this.refactMemoryObject = refactMemoryObject;
	}

	@Override
	public void applyAdd(Object element, boolean bAppendValue) {}

	@Override
	public void applyMod(Object element, boolean bAppendValue) {}

	@Override
	public void applyDel(Object element) {}
	
	@Override
	public CookieDef getCookieDef() {
		return refactMemoryObject.getCookieDef();
	}

	public abstract SyncList<E> getServiceData();

	public abstract SyncList<E> getSaveCookieListData(int maxCnt);

	public abstract void setSavedMongoCnt();
	
	
}
