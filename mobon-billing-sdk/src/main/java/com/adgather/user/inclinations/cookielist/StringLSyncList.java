package com.adgather.user.inclinations.cookielist;

/**
 * 최근순 정렬이 포함된 StringSyncList (기존에 있더라도 추가/변경한 것은 최근것으로 정렬)
 * @author yhlim
 *
 */
public class StringLSyncList extends StringSyncList {
	private static final long serialVersionUID = -7080342591966597998L;

	public StringLSyncList(String delimeter) {
		super(delimeter);
	}

	
	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if(element == null)		return false;
		
		boolean bRes = false;
		for (int idx = 0; idx < size(); idx++) {
			if( element.equals(get(idx)) ) {
				remove(idx);
				add(0, (String)element);
				bRes = true;
				break;
			}
		}
		setNeedUpdateAll(true);
		return bRes;
	}
	
	@Override
	public StringLSyncList clone() {
		StringLSyncList newList = new StringLSyncList(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		for (int idx = 0; idx < size(); idx++) {
			newList.add(get(idx));
		}
		
		return newList;
	}
	
}
