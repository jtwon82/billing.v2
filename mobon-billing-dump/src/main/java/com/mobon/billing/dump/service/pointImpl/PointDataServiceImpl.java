package com.mobon.billing.dump.service.pointImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStandardStats;
import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.file.point.PointSummary;
import com.mobon.billing.dump.service.PointDataService;
import com.mobon.billing.dump.service.PointFileService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class PointDataServiceImpl implements PointDataService{
	
	@Resource
	private PointFileService pointFileService;
	
	@Resource 
	private PointDataSelectServiceImpl pointDataSelectServiceImpl;

	@Override
	public void resultDiffPointData(List<PointDataStats> pointDataList, List<MobCampMediaStats> campDataList) {
		
		PointSummary ps = new PointSummary();		
		Map<String, PointDataStats> transPointDataMap = ps.TransPointDataDiffListToMap(pointDataList);
		log.info("###TransPointDataSize###"+transPointDataMap.size());
		Map<String, MobCampMediaStats> transCampDataMap = ps.TransCampMediaDatatDiffListToMap(campDataList);		
		log.info("###transCampDataMapSize###"+transCampDataMap.size());
		Iterator<String> keys  = transPointDataMap.keySet().iterator();
		
		JSONArray resultData = new JSONArray();
		
		while(keys.hasNext()) {
			String key = keys.next();
			if (transCampDataMap.containsKey(key)) {
				BigDecimal logPoint = transPointDataMap.get(key).getPoint();
				BigDecimal prdPoint = transCampDataMap.get(key).getPoint();
				if (logPoint.compareTo(prdPoint) != 0) {
					JSONObject jObj = new JSONObject();
					jObj.put("STATS_DTTM", transPointDataMap.get(key).getId().getStatsDttm());
					jObj.put("SITE_CODE", transPointDataMap.get(key).getId().getSiteCode());
					jObj.put("MEDIA_ID", transPointDataMap.get(key).getId().getMediaId());
					jObj.put("LOG_POINT", transPointDataMap.get(key).getPoint());
					jObj.put("PRD_POINT", transCampDataMap.get(key).getPoint());
					jObj.put("DIFF_POINT", prdPoint.subtract(logPoint));
					
					resultData.add(jObj);
				}
			}
		}
		log.info("### JSONArrayList Size ### " + resultData.size());
		
		if (resultData.isEmpty()) {
			log.info("####POINT DATA IS SAME ####");
		} else {
			this.transTopicFile(resultData);
			
			//pointFileService.makeDiffResultFile(resultData);
		}
		
	}

	@SuppressWarnings("unchecked")
	private void transTopicFile(JSONArray resultData) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("HH");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		log.info("result Diff Point Data Size ::: - {}" , resultData.size());
		JSONArray makeFileData = new JSONArray();
		// Prd 기준 데이터 뽑기 
		for (int i = 0; i < resultData.size(); i++) {
			JSONObject obj = resultData.getJSONObject(i);

			List<MobCampMediaStandardStats> result = pointDataSelectServiceImpl.getDiffData(obj);

			if (result.size() == 0) {
				//특이 case1 :  해당 미차감 금액이 클릭당 차감액과 동일하지 않는 경우 가장 첫번째 지면에 해당 누락데이터를 쏜다.
				result =pointDataSelectServiceImpl.notEqDiffData(obj);
			} else if (result.size() > 1) {
				//특이 case2 : 클릭당 차감 포인트가 일치하는  데이터가 많은 경우 가장 첫번째 데이터에 해당 누락데이터를 쏜다.  
				MobCampMediaStandardStats dto = result.get(0);
				result = new ArrayList<MobCampMediaStandardStats>();
				result.add(dto);
			}
			
			//해당 데이터 1회 클릭당 소진 금액 / 금액 차이  => 몇 개의 로그가 생성되어야 되는지 확인가능
			try {
				for (MobCampMediaStandardStats dto : result) {
					BigDecimal prdAmt = dto.getAdvrtsAmt(); //DB 차감 포인트
					int clickCnt = dto.getClickCnt();	// DB 클릭 횟수 
					
					BigDecimal clickPerPrdPoint = prdAmt.divide(new BigDecimal(clickCnt),2,BigDecimal.ROUND_UP);
					
					BigDecimal minusDiffPoint = (BigDecimal) obj.get("DIFF_POINT");
					BigDecimal diffPoint = minusDiffPoint.multiply(new BigDecimal(-1));
					
					BigDecimal repeatCnt = diffPoint.divide(clickPerPrdPoint,0,BigDecimal.ROUND_UP);					
							
					log.debug("mediaId - {} , clickPerPrdPoint -{} , repeatCnt - {}", dto.getId().getMediaId(), clickPerPrdPoint, repeatCnt);
					boolean checkAdverIdRegDate = pointDataSelectServiceImpl.getAdverIdRegDate(format3.format(cal.getTime()), dto.getAdverId());
					if (checkAdverIdRegDate) {
						for (int j = 0 ; j < repeatCnt.intValue(); j++) {
							
							JSONObject topicObj = new JSONObject();
							topicObj.put("noCharge", true);
							topicObj.put("yyyymmdd", dto.getId().getStatsDttm());
							topicObj.put("platform", dto.getPltfomTpCode());
							topicObj.put("product", dto.getAdvrtsPrdtCode());
							topicObj.put("adGubun", dto.getAdvrtsTpCode());
							topicObj.put("site_code", dto.getId().getSiteCode());
							topicObj.put("media_code", dto.getScriptNo());
							topicObj.put("point", clickPerPrdPoint);
							topicObj.put("userId", dto.getAdverId());
							topicObj.put("scriptUserId", dto.getId().getMediaId());
							topicObj.put("className", "AdChargeData");
							topicObj.put("dumpType", "addCharge");
							topicObj.put("type", "V");
							topicObj.put("sendDate", format.format(cal.getTime()));
							topicObj.put("hh", format2.format(cal.getTime()));
							
							makeFileData.add(topicObj);
						}
					}
				}
				
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		log.info("####END TransTopic File####" + makeFileData.size());
		if (makeFileData.size() != 0) {
			pointFileService.makeDiffResultFile(makeFileData);				
		}		
	}
}
