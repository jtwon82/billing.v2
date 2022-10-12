package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ExcConvSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.excconv.ExcConvCookieVal;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

public class DefExcConvAd extends CookieDef {

    public DefExcConvAd(String cookieKey) {
        super(cookieKey);
    }

    @Override
    public Object newObj() {
        return new ExcConvCookieVal();
    }

    @Override
    public SyncList<?> newList() {
        return new ExcConvSyncList<ExcConvCookieVal>(LIST_DELIMETER);
    }

    @Override
    public int getExpire() {
        return PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_EXPIRE");
    }

    @Override
    public void infuseLimit(MemoryObj<?> memoryObj) {
        memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_EXC_CONV_COUNT"));
        memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_EXC_CONV_BYTES"));
        memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_COUNT"));
        memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_BYTES"));
    }

    @Override
    public boolean isFixePose() {
        return false;
    }

    @Override
    public boolean isUseMongo() {
        return CIFunctionController.isUseMongoExcConvAd();
    }

    @Override
    public boolean isUseMongo(int mediaScriptNo) {
        return isUseMongo();
    }

    @Override
    public CodeConverter getCodeConverter() {
        return Base64Converter.getInstance();
    }
}
