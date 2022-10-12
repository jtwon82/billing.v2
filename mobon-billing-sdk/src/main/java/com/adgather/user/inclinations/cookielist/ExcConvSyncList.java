package com.adgather.user.inclinations.cookielist;

import com.adgather.user.inclinations.cookieval.excconv.ExcConvCookieVal;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;

public class ExcConvSyncList<E extends ExcConvCookieVal> extends ObjectSyncList<E> {

    public ExcConvSyncList(String delimiter) {
        super(delimiter);
    }

    /**
     * 기존광고주(키) 값이 존재할 경우 전환제외만료시간만 UPDATE 처리
     * @param element
     * @param bAppendValue
     * @return
     */
    @Override
    public boolean applyMod(Object element, boolean bAppendValue) {
        if(element == null)		return false;

        boolean bRes = false;
        String key = ((CookieVal)element).getKey();

        for (int idx = 0; idx < size(); idx++) {
            CookieVal obj = get(idx);
            if (key.equals(obj.getKey())) {
                ExcConvCookieVal currentCookieVal = (ExcConvCookieVal)obj;
                ExcConvCookieVal newCookieVal = (ExcConvCookieVal)element;
                currentCookieVal.setExpireHours(newCookieVal.getExpireHours());
                obj.modValue(currentCookieVal, bAppendValue);
                bRes = true;
                break;
            }
        }
        return bRes;
    }

    @Override
    public ExcConvSyncList clone() {
        ExcConvSyncList newList = new ExcConvSyncList(DELIMETER);
        newList.setNeedUpdate(this);
        newList.setCurTime(this.curTime);
        newList.setSyncTime(this.syncTime);

        for (int idx = 0; idx < size(); idx++) {
            newList.add(get(idx));
        }

        return newList;
    }
}
