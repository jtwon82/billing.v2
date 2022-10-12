package com.mobon.billing.dump.file.mobon.vo;

import java.math.BigDecimal;

import com.mobon.billing.dump.file.mobon.data.ClickViewFileLine;
import com.mobon.billing.dump.file.mobon.data.ConversionFileLine;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import com.mobon.billing.dump.constants.GlobalConstants;

import lombok.Getter;
import lombok.ToString;

/**
 * @FileName : ABTestFileDataVO.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : MOBON 파일 라인 데이터가 전달 되면 해당 파일에 맞게 데이터를 정제하여 결과를 읽는 VO.
 */
@Getter
@ToString
public class MobonFileDataVO {

	private String statsDttm;
	private int mediaScriptNo;
	private String siteCode;
	private String action;
	private String abtestTypes[];
	private String itlTpCode;
	private String direct;
	private String inHour;
	private String pltfomTpCode;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String freq;
	private String adverId;
	private String rcmmYn;
	private String statyn;
	private String frameId;
	private String prdtTpCode;
	private String parTpCode;
	private String frameCombiKey;
	private String frameType;

	@Setter
	private int orderCnt=0;
	@Setter
	private int orderAmt=0;
	@Setter
	private int clickCnt=0;
	@Setter
	private int totEprsCnt=0;
	@Setter
	private int parEprsCnt=0;
	@Setter
	private BigDecimal advrtsAmt=BigDecimal.ZERO;
	@Setter
	private BigDecimal mediaPymntAmt=BigDecimal.ZERO;
	
	//frame 미노출 여부 
	private String frameMatrExposureYN;
	private String frameSendTpCode;
	
	//frameCombiTargetYN 
	private String frameCombiTargetYN;
	/**
	 * @param str
	 * @param fileName
	 */
	public MobonFileDataVO(String str, String fileName) {

		if(fileName.startsWith(GlobalConstants.MOBON_CLICKVIEW_FILE_NAME_PREFIX)) {

			ClickViewFileLine clickViewFileLine = new ClickViewFileLine(str);

			this.statsDttm = clickViewFileLine.getStatsDttm();
			this.mediaScriptNo = clickViewFileLine.getMediaScriptNo();
			this.siteCode = clickViewFileLine.getSiteCode();
			this.action = clickViewFileLine.getAction();
			this.abtestTypes = clickViewFileLine.getAbtestTypes();
			this.itlTpCode = clickViewFileLine.getItlTpCode();
			this.pltfomTpCode = clickViewFileLine.getPltfomTpCode();
			this.advrtsPrdtCode = clickViewFileLine.getAdvrtsPrdtCode();
			this.advrtsTpCode = clickViewFileLine.getAdvrtsTpCode();
			this.freq = StringUtils.isNotEmpty(clickViewFileLine.getFreq()) && Integer.parseInt(clickViewFileLine.getFreq()) > 3000 ? "3001" : clickViewFileLine.getFreq();
			this.adverId = clickViewFileLine.getAdverId();
			this.rcmmYn = StringUtils.isEmpty(clickViewFileLine.getRcmmCode()) ? "N" : "Y";
			this.statyn = clickViewFileLine.getStatyn();
			this.frameId = clickViewFileLine.getFrameId();
			this.frameCombiKey = clickViewFileLine.getFrameCombiKey();
			this.frameType = !StringUtils.isEmpty(clickViewFileLine.getFrameType()) && clickViewFileLine.getFrameType().length() != 2 ? null : clickViewFileLine.getFrameType();
			this.parTpCode = clickViewFileLine.getParTpCode();
			this.prdtTpCode = StringUtils.isEmpty(clickViewFileLine.getPrdtTpCode()) ? "Y".equals(clickViewFileLine.getFrameId()) ? "01": "02" : clickViewFileLine.getPrdtTpCode();
			this.clickCnt = "N".equals(clickViewFileLine.getStatyn()) ? 0 : clickViewFileLine.getClickCnt();
			this.totEprsCnt = "N".equals(clickViewFileLine.getStatyn()) ? 0 :clickViewFileLine.getTotEprsCnt();
			this.parEprsCnt = "N".equals(clickViewFileLine.getStatyn()) ? 0 :clickViewFileLine.getParEprsCnt();
			this.advrtsAmt = clickViewFileLine.getAdvrtsAmt();
			this.mediaPymntAmt = clickViewFileLine.getMediaPymntAmt();
			this.frameMatrExposureYN = clickViewFileLine.getFrameMatrExposureYN();
			this.frameSendTpCode = StringUtils.isEmpty(clickViewFileLine.getFrameSendTpCode())? "" : clickViewFileLine.getFrameSendTpCode();
			this.frameCombiTargetYN = StringUtils.isEmpty(clickViewFileLine.getFrameCombiTargetYN())? "N" : clickViewFileLine.getFrameCombiTargetYN();
					
					

		} else if(fileName.startsWith(GlobalConstants.MOBON_CONVERSION_FILE_NAME_PREFIX)) {

			ConversionFileLine conversionFileLine = new ConversionFileLine(str);

			this.statsDttm = conversionFileLine.getStatsDttm();
			this.mediaScriptNo = conversionFileLine.getMediaScriptNo();
			this.abtestTypes = conversionFileLine.getAbtestTypes();
			this.itlTpCode = conversionFileLine.getItlTpCode();
			this.pltfomTpCode = conversionFileLine.getPltfomTpCode();
			this.advrtsPrdtCode = conversionFileLine.getAdvrtsPrdtCode();
			this.advrtsTpCode = conversionFileLine.getAdvrtsTpCode();
			this.adverId = conversionFileLine.getAdverId();
			this.rcmmYn = StringUtils.isEmpty(conversionFileLine.getRcmmCode()) ? "N" : "Y";
			this.frameId = conversionFileLine.getFrameId();
			this.frameCombiKey = conversionFileLine.getFrameCombiKey();
			this.frameType = !StringUtils.isEmpty(conversionFileLine.getFrameType()) && conversionFileLine.getFrameType().length() != 2 ? null : conversionFileLine.getFrameType();
			this.parTpCode = conversionFileLine.getParTpCode();
			this.prdtTpCode = StringUtils.isEmpty(conversionFileLine.getPrdtTpCode()) ? "Y".equals(conversionFileLine.getFrameId()) ? "01": "02" : conversionFileLine.getPrdtTpCode();
			this.direct = conversionFileLine.getDirect();
			this.inHour = conversionFileLine.getInHour();
			this.orderCnt = 1;
			this.orderAmt = conversionFileLine.getOrderAmt();
			this.freq = StringUtils.isNotEmpty(conversionFileLine.getFreq()) && Integer.parseInt(conversionFileLine.getFreq()) > 3000 ? "3001" : conversionFileLine.getFreq();
			this.siteCode = conversionFileLine.getSiteCode();
			this.frameMatrExposureYN = conversionFileLine.getFrameMatrExposureYN();
			this.frameSendTpCode = StringUtils.isEmpty(conversionFileLine.getFrameSendTpCode()) ?  "" : conversionFileLine.getFrameSendTpCode();
			this.frameCombiTargetYN = StringUtils.isEmpty(conversionFileLine.getFrameCombiTargetYN())? "N" : conversionFileLine.getFrameCombiTargetYN();
		}

	}

	/**
	 * @Method Name : getABComStatsKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COM_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABComStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.itlTpCode;
	}

	/**
	 * @Method Name : getABParStatsKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_PAR_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABParStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.mediaScriptNo + this.itlTpCode;
	}

	/**
	 * @Method Name : getABAdverStatsWebKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_ADVER_STATS_WEB 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABAdverStatsWebKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.adverId + this.itlTpCode + this.rcmmYn;
	}

	/**
	 * @Method Name : getABAdverStatsMobileKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_ADVER_STATS_MOBILE 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABAdverStatsMobileKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.adverId + this.itlTpCode + this.rcmmYn;
	}

	/**
	 * @Method Name : getABComFrameSizeKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COM_FRAME_SIZE 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABComFrameSizeKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.itlTpCode + this.frameId.substring(10, 13) + this.parTpCode + this.frameType;
	}

	/**
	 * @Method Name : getABFrameSizeKey
	 * @Date : 2021. 1. 4.
	 * @Author : dkchoi
	 * @Comment : AB_FRAME_SIZE 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABFrameSizeKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.itlTpCode + this.frameId.substring(10, 13) + this.parTpCode + this.frameType + this.prdtTpCode;
	}

	/**
	 * @Method Name : getABCombiFrameSizeKey
	 * @Date : 2020. 7. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COMBI_FRAME_SIZE 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.
	 * @return
	 */
	public String getABCombiFrameSizeKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.itlTpCode + this.frameCombiKey.substring(3, 6) + this.parTpCode + this.frameType;
	}

	/**
	 * @Method Name : getFrequencyPreProDaySumKey
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : JSON 객체로 변환 하기전 사전 Summary를 위한 키 생성 메소드 (Day).
	 * @return
	 */
	public String getFrequencyPreProDaySumKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode;
	}

	/**
	 * @Method Name : getFrequencyPreProSdkDaySumKey
	 * @Date : 2021. 4. 29.
	 * @Author : dkchoi
	 * @Comment : JSON 객체로 변환 하기전 사전 Summary를 위한 키 생성 메소드 (SDK).
	 * @return
	 */
	public String getFrequencyPreProSdkDaySumKey() {
		return this.statsDttm + this.advrtsPrdtCode + this.advrtsTpCode;
	}
	/**
	 * @Method Name : getFrequencyPreProCampSumKey
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : JSON 객체로 변환 하기전 사전 Summary를 위한 키 생성 메소드 (Camp).
	 * @return
	 */
	public String getFrequencyPreProCampSumKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.siteCode;
	}

	/**
	 * @Method Name : getFrequencyPreProSumMediaScriptKey
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : JSON 객체로 변환 하기전 사전 Summary를 위한 키 생성 메소드 (MediaScript).
	 * @return
	 */
	public String getFrequencyPreProSumMediaScriptKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.mediaScriptNo;
	}
}
