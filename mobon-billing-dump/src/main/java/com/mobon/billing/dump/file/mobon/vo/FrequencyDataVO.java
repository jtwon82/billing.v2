package com.mobon.billing.dump.file.mobon.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @FileName : ABTestFileDataVO.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : MOBON 파일 라인 데이터가 전달 되면 해당 파일에 맞게 데이터를 정제하여 결과를 읽는 VO.
 */
@Getter
@ToString
@Builder
public class FrequencyDataVO {

	private String statsDttm;
	private int mediaScriptNo;
	private String siteCode;
	private String pltfomTpCode;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String itlTpCode;
	private Map<String, Integer> totOrderCntMap=null;
	private Map<String, Integer> totOrderAmtMap=null;
	private Map<String, Integer> sessOrderCntMap=null;
	private Map<String, Integer> sessOrderAmtMap=null;
	private Map<String, Integer> directOrderCntMap=null;
	private Map<String, Integer> directOrderAmtMap=null;
	private Map<String, Integer> clickCntMap=null;
	private Map<String, Integer> totEprsCntMap=null;
	private Map<String, Integer> parEprsCntMap=null;
	private Map<String, BigDecimal> advrtsAmtMap=null;
	private Map<String, BigDecimal> mediaPymntAmtMap=null;

	public void addAdvrtsAmt(String freq, BigDecimal advrtsAmt) {
		if(BigDecimal.ZERO.equals(advrtsAmt))
			return;

		if(this.advrtsAmtMap.containsKey(freq)) {
			this.advrtsAmtMap.put(freq, this.advrtsAmtMap.get(freq).add(advrtsAmt));
		} else {
			this.advrtsAmtMap.put(freq, advrtsAmt);
		}
	}
	public void addMediaPymntAmt(String freq, BigDecimal mediaPymntAmt) {
		if(BigDecimal.ZERO.equals(mediaPymntAmt))
			return;

		if(this.mediaPymntAmtMap.containsKey(freq)) {
			this.mediaPymntAmtMap.put(freq, this.mediaPymntAmtMap.get(freq).add(mediaPymntAmt));
		} else {
			this.mediaPymntAmtMap.put(freq, mediaPymntAmt);
		}
	}

	public void addClickCnt(String freq, int clickCnt) {
		if(clickCnt == 0 )
			return;

		if(this.clickCntMap.containsKey(freq)) {
			this.clickCntMap.put(freq, this.clickCntMap.get(freq) + clickCnt);
		} else {
			this.clickCntMap.put(freq, clickCnt);
		}
	}

	public void addTotEprsCnt(String freq, int totEprsCnt) {
		if(totEprsCnt == 0 )
			return;

		if(this.totEprsCntMap.containsKey(freq)) {
			this.totEprsCntMap.put(freq, this.totEprsCntMap.get(freq) + totEprsCnt);
		} else {
			this.totEprsCntMap.put(freq, totEprsCnt);
		}
	}

	public void addParEprsCnt(String freq, int parEprsCnt) {
		if(parEprsCnt == 0 )
			return;

		if(this.parEprsCntMap.containsKey(freq)) {
			this.parEprsCntMap.put(freq, this.parEprsCntMap.get(freq) + parEprsCnt);
		} else {
			this.parEprsCntMap.put(freq, parEprsCnt);
		}
	}

	public void addTotOrderCnt(String freq, int totOrderCnt) {
		if(totOrderCnt == 0 )
			return;

		if(this.totOrderCntMap.containsKey(freq)) {
			this.totOrderCntMap.put(freq, this.totOrderCntMap.get(freq) + totOrderCnt);
		} else {
			this.totOrderCntMap.put(freq, totOrderCnt);
		}
	}

	public void addTotOrderAmt(String freq, int totOrderAmt) {
		if(totOrderAmt == 0 )
			return;

		if(this.totOrderAmtMap.containsKey(freq)) {
			this.totOrderAmtMap.put(freq, this.totOrderAmtMap.get(freq) + totOrderAmt);
		} else {
			this.totOrderAmtMap.put(freq, totOrderAmt);
		}
	}

	public void addSessOrderCnt(String freq, int sessOrderCnt) {
		if(sessOrderCnt == 0 )
			return;

		if(this.sessOrderCntMap.containsKey(freq)) {
			this.sessOrderCntMap.put(freq, this.sessOrderCntMap.get(freq) + sessOrderCnt);
		} else {
			this.sessOrderCntMap.put(freq, sessOrderCnt);
		}
	}

	public void addSessOrderAmt(String freq, int sessOrderAmt) {
		if(sessOrderAmt == 0 )
			return;

		if(this.sessOrderAmtMap.containsKey(freq)) {
			this.sessOrderAmtMap.put(freq, this.sessOrderAmtMap.get(freq) + sessOrderAmt);
		} else {
			this.sessOrderAmtMap.put(freq, sessOrderAmt);
		}
	}

	public void addDirectOrderCnt(String freq, int directOrderCnt) {
		if(directOrderCnt == 0 )
			return;

		if(this.directOrderCntMap.containsKey(freq)) {
			this.directOrderCntMap.put(freq, this.directOrderCntMap.get(freq) + directOrderCnt);
		} else {
			this.directOrderCntMap.put(freq, directOrderCnt);
		}
	}

	public void addDirectOrderAmt(String freq, int directOrderAmt) {
		if(directOrderAmt == 0 )
			return;

		if(this.directOrderAmtMap.containsKey(freq)) {
			this.directOrderAmtMap.put(freq, this.directOrderAmtMap.get(freq) + directOrderAmt);
		} else {
			this.directOrderAmtMap.put(freq, directOrderAmt);
		}
	}

	/**
	 * @Method Name : getFreqDayStatsKey
	 * @Date : 2021. 3. 26.
	 * @Author : dkchoi
	 * @Comment : FREQ_DAY_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getFreqDayStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode;
	}

	/**
	 * @Method Name : getFreqSdkDayStatsKey
	 * @Date : 2021. 4. 29.
	 * @Author : dkchoi
	 * @Comment : FREQ_SDK_DAY_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getFreqSdkDayStatsKey() {
		return this.statsDttm + this.advrtsPrdtCode + this.advrtsTpCode;
	}

	/**
	 * @Method Name : getFreqCampDayStatsKey
	 * @Date : 2021. 3. 26.
	 * @Author : dkchoi
	 * @Comment : FREQ_CAMP_DAY_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getFreqCampDayStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.siteCode;
	}

	/**
	 * @Method Name : getFreqMediaScriptDayStatsKey
	 * @Date : 2021. 3. 26.
	 * @Author : dkchoi
	 * @Comment : FREQ_MEDIA_SCRIPT_DAY_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getFreqMediaScriptDayStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.mediaScriptNo ;
	}
}
