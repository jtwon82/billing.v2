package com.adgather.util;

import java.sql.Timestamp;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        Timestamp[] dates = (Timestamp[]) value;
        Long[] result = new Long[dates.length];
        for (int index = 0; index < dates.length; index++) {
            result[index] = dates[index].getTime();
        }
        return result;
    }

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if (value instanceof Timestamp) {
			Timestamp date1 = (Timestamp) value;
			return date1.getTime();
		}
		return null;
	}
}