package com.mobon.billing.dump.utils;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dump.constants.AdvertisementMpgType;
import com.mobon.billing.dump.constants.AdvertisementType;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
public abstract class CommonUtils {
	
	private CommonUtils() {
        throw new AssertionError();
    }
	
	public static JSONObject stringToJSONObject(String strJson) {
		if(strJson == null)
			return null;

		JSONObject json = JSONObject.fromObject(strJson);
		return json;
	}
	
	public static String toProductType(String advrtsPrdtCode) {
		String result = "";
        Optional<AdvertisementMpgType> apo = AdvertisementMpgType.fromString(advrtsPrdtCode);
        
        if(apo.isPresent()) {
            AdvertisementMpgType ap = apo.get();
            result = ap.getAdvrtsPrdtCode();
        }

        return result;
	}

	public static String toAdGubunType(String advrtsTpCode) {
		
		String result = "";
        Optional<AdvertisementType> apo = AdvertisementType.fromString(advrtsTpCode);
        
        if(apo.isPresent()) {
        	AdvertisementType ap = apo.get();
            result = ap.getAdvrtsTpCode();
        }
        
		return result;
		
	}
	
	public static String[] toArray(String str, String limit) {
		if(StringUtils.isEmpty(str))		return null;
		String[] array = null;
		if(StringUtils.isEmpty(limit))	{
			array = new String[]{str};
		} else {
			array = StringUtils.split(str, limit);
		}

		return array;
	}
	
	/**
	 * @Method Name : objectToJsonString
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : 객체를 JSON포멧의 문자열 데이터로 변환하기 위한 매소드.
	 * @param Object JSON으로 변환할 객체.
     * @return String JSON포멧의 문자열 데이터.
	 */
    public static String objectToJsonString(Object object) {

        String result;

        try {

            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(object);

        } catch(JsonProcessingException e) {
            log.error("# ToJsonString Error");
            result = object.toString();
        }

        return result;
    }

}
