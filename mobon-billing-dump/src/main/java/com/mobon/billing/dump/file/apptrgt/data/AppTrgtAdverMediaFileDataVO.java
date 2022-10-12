package com.mobon.billing.dump.file.apptrgt.data;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.dump.utils.CommonUtils;

import lombok.Getter;
import lombok.ToString;

/**
 * @FileName : 
 * @Project : 
 * @Date :  
 * @Author 
 * @Comment : 
 */
@Getter
@ToString
public class AppTrgtAdverMediaFileDataVO{
	
	private String statsDttm;
	private int mediaScriptNo;
	private String action;
	private String abtestTypes[];
	private String itlTpCode; 
	private String direct;
	private String inHour;
	private String pltfomTpCode;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String adverId;
	private String rcmmYn;
	private String statyn;
	private String frameId;
	private String parTpCode;
	
	public int orderCnt=0;
	public int orderAmt=0;
	public int clickCnt=0;
	public int totEprsCnt=0;
	public int parEprsCnt=0;
	public BigDecimal advrtsAmt=BigDecimal.ZERO;
	public BigDecimal mediaPymntAmt=BigDecimal.ZERO;
	
	/**
	 * @param str
	 * @param fileName
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public AppTrgtAdverMediaFileDataVO(String str) {

		ObjectMapper mapper = new ObjectMapper();

		PollingData fileLine = null;
		try {
			fileLine = mapper.readValue(str, PollingData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.statsDttm = fileLine.getYyyymmdd();
			this.pltfomTpCode = "m".equals(fileLine.getPlatform().toLowerCase().substring(0,1))?"02":"01";
			this.advrtsPrdtCode = CommonUtils.toProductType(fileLine.getProduct());
			this.advrtsTpCode = CommonUtils.toAdGubunType(fileLine.getAdGubun());
			this.adverId = fileLine.getAdvertiserId();
			this.mediaScriptNo = Integer.parseInt(fileLine.getMediaCode());
			this.action = fileLine.getType();
			this.totEprsCnt = fileLine.getViewcnt1();
			this.parEprsCnt = fileLine.getViewcnt3();
			this.clickCnt = fileLine.getClickcnt();
			this.advrtsAmt = new BigDecimal(fileLine.getPoint());
			this.mediaPymntAmt = new BigDecimal(fileLine.getMpoint());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Method Name : getABComStatsKey
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COM_STATS 도메인 Key를 기준으로 데이터를 Summary 하기 위한 키 생성 메소드.  
	 * @return
	 */
	public String getAppTrgtAdverMediaStatsKey() {
		return this.statsDttm + this.pltfomTpCode + this.advrtsPrdtCode + this.advrtsTpCode + this.adverId + this.mediaScriptNo;
	}
	
	
}
