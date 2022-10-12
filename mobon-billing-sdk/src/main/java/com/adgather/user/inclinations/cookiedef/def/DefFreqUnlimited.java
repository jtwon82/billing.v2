package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ObjectSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.freq.FreqShops;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

public class DefFreqUnlimited extends CookieDef {
    public DefFreqUnlimited(String cookieKey) {
        super(cookieKey);
    }

    @Override
    public Object newObj() {
        return new FreqShops();
    }

    @Override
    public SyncList<?> newList() {
        return new ObjectSyncList<FreqShops>(LIST_DELIMETER);
    }

    @Override
    public int getExpire() {
        return PropertyHandler.getInt("COOKIE_FREQ_UNLIMITED_EXPIRE");
    }

    @Override
    public void infuseLimit(MemoryObj<?> memoryObj) {
        memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_FREQ_UNLIMITED_COUNT"));
        memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_FREQ_UNLIMITED_BYTES"));
    }

    @Override
    public boolean isFixePose() {
        return false;
    }

    @Override
    public boolean isUseMongo() {
        return false;
    }

    @Override
    public boolean isUseMongo(int mediaScriptNo) {
        return false;
    }

    @Override
    public CodeConverter getCodeConverter() {
        return null;
    }
}
