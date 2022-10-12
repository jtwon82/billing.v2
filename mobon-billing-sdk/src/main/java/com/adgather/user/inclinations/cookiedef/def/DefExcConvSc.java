package com.adgather.user.inclinations.cookiedef.def;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.convert.inter.CodeConverter;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookielist.ExcConvScSyncList;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookieval.excconv.ExcConvScCookieVal;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.util.PropertyHandler;

public class DefExcConvSc extends CookieDef {

    public DefExcConvSc(String cookieKey) {
        super(cookieKey);
    }

    @Override
    public Object newObj() {
        return new ExcConvScCookieVal();
    }

    @Override
    public SyncList<?> newList() {
        return new ExcConvScSyncList<ExcConvScCookieVal>(LIST_DELIMETER);
    }

    @Override
    public int getExpire() {
        return PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_SC_EXPIRE");
    }

    @Override
    public void infuseLimit(MemoryObj<?> memoryObj) {
        memoryObj.setMongoMaxCnt(PropertyHandler.getInt("MONGO_INCT_EXC_CONV_SC_COUNT"));
        memoryObj.setMongoMaxLen(PropertyHandler.getInt("MONGO_INCT_EXC_CONV_SC_BYTES"));
        memoryObj.setCookieMaxCnt(PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_SC_COUNT"));
        memoryObj.setCookieMaxLen(PropertyHandler.getInt("COOKIE_INCT_EXC_CONV_SC_BYTES"));
    }

    @Override
    public boolean isFixePose() {
        return false;
    }

    @Override
    public boolean isUseMongo() {
        return CIFunctionController.isUseMongoExcConvSc();
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
