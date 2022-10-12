package com.mobon.billing.core.config;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

@Configuration
@Component
public class GsonConfig {

	@Bean
	public Gson getGson() {
		return new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new MapDeserializer()).create();
	}
}

class MapDeserializer implements JsonDeserializer<Map<String, Object>> {
	@Override
	public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
		
		Map<String, Object> map = Maps.newLinkedHashMap();
		for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
			JsonElement el = entry.getValue();
			Object o = null;
			
			String key = entry.getKey();
			switch( key ) {
			case "key":
			case "yyyymmdd":
			case "sdate":
			case "gb":
			case "GB":
			case "adGubun":
			case "platform":
			case "product":
			case "site_code":
			case "siteCode":
			case "sc":
			case "s":
			case "mc":
			case "media_code":
			case "scriptNo":
			case "scriptId":
			case "scriptUserId":
			case "advertiserId":
			case "u":
			case "userId":
			case "mediasiteNo":
			case "type":
			case "kno":
				
			case "clickChk":
			case "viewCnt":
			case "viewCnt1":
			case "viewcnt1":
			case "viewCnt2":
			case "viewcnt2":
			case "viewCnt3":
			case "viewcnt3":
			case "ago_viewcnt1":
			case "ago_viewcnt2":
			case "mpoint":
			case "ppoint":
			case "point":

			case "CATE1":
			case "IMGPATH":
			case "INSERT_BOTH":
			case "PCODE":
			case "PNM":
			case "PRICE":
			case "RDATE":
			case "URL":
			case "prdtPrmct":
			case "soldOut":
			case "mdPcode":
				
			case "ymdhms":
			case "hh":
			case "no":
			case "NO":
			case "sendDate":
			case "keyIp":
			case "ip":
			case "className":
			case "dumpType":
			case "mobonlinkcate":
			case "pCode":
			case "cookieDirect":
			case "cookieInDirect":
			case "direct":
			case "inflow_route":
			case "last_click_time":
			case "ordCode":
			case "ordQty":
			case "ordRFUrl":
			case "price":
			case "flagST":
			case "chargeAble":
			case "useYmdhms":
			case "realIp":
			case "rgnIpCnt":
			case "newIpCnt":
			case "plAdviewCnt":
			case "plMediaViewCnt":
			case "bHandlingStatsMobon":
			case "bHandlingStatsPointMobon":
			case "nearYn":
			case "nearCode":
			case "fromApp":
			case "intgYn":
			case "omitType":
			case "kwrdSeq":
			case "adcSeq":
			case "ctgrNo":
				if( key.indexOf("cnt")>0 || key.indexOf("Cnt")>0 ) {
					try {
						o = el.getAsInt();
					} catch (Exception ignored) {
					}
				}
				
				if (o == null)
					try {
						o = el.getAsString();
					} catch (Exception ignored) {
					}
				
				break;
			default:
				
			}
			
			map.put(key, o);
		}
		return map;
	}
}
