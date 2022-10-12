package com.mobon.billing.dump.file.apptrgt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.file.apptrgt.data.AppTrgtAdverMediaFileDataVO;
import com.mobon.billing.dump.domainmodel.apptrgt.AppTrgtAdverMediaStats;
import com.mobon.billing.dump.domainmodel.apptrgt.key.AppTrgtAdverMediaStatsKey;

/**
 * @FileName : 
 * @Project : mobon-billing-dump
 * @Date :  
 * @Author 
 * @Comment : 
 */
public class AppTrgtSummary {
	
	private Map<String, AppTrgtAdverMediaStats> appTrgtAdverMediaStatsMap = new HashMap<String, AppTrgtAdverMediaStats>();

	/**
	 * @Method Name : 
	 * @Date : 
	 * @Author : 
	 * @Comment : 파일 라인을 도메인별로 Summary한다.
	 * @param fLine
	 * @return
	 */
	public Map<String, AppTrgtAdverMediaStats> AppTrgtAdverMediaDataset(AppTrgtAdverMediaFileDataVO fLine) {
		
		if(!StringUtils.isEmpty(fLine.getStatsDttm())) {
			
			AppTrgtAdverMediaStats appTrgtAdverMediaStats = appTrgtAdverMediaStatsMap.getOrDefault(
					fLine.getAppTrgtAdverMediaStatsKey(), //+fLine.getAction(),
					AppTrgtAdverMediaStats.builder().id(AppTrgtAdverMediaStatsKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.adverId(fLine.getAdverId())
						.mediaScriptNo(fLine.getMediaScriptNo())
						.build())
					.totEprsCnt(0)
					.parEprsCnt(0)
					.clickCnt(0)
					.advrtesAmt(BigDecimal.ZERO)
					.mediaPymntAmt(BigDecimal.ZERO)
					.dplkAmt(BigDecimal.ZERO)
					.regUserId("BATCH")
					.build()
					);
			
			appTrgtAdverMediaStats.addTotEprsCnt(fLine.totEprsCnt); //"V".equals(fLine.getAction())?fLine.totEprsCnt:0);
			appTrgtAdverMediaStats.addParEprsCnt(fLine.parEprsCnt); //"V".equals(fLine.getAction())?fLine.parEprsCnt:0);
			appTrgtAdverMediaStats.addClickCnt(fLine.clickCnt); //"C".equals(fLine.getAction())?fLine.clickCnt:0);
			appTrgtAdverMediaStats.addAdvrtesAmt(fLine.advrtsAmt);
			
			appTrgtAdverMediaStatsMap.put(fLine.getAppTrgtAdverMediaStatsKey() //+fLine.getAction()
				, appTrgtAdverMediaStats);
	
		}
		
		return appTrgtAdverMediaStatsMap;
	}
	
}
