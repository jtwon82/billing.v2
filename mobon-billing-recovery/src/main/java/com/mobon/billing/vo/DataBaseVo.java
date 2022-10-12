package com.mobon.billing.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataBaseVo {
	
	/*FRME_ADVER_DAY_STATS*/
	@JsonProperty("STATS_DTTM")
	private String yyyyMMdd; 
	@JsonProperty("ADVER_ID")
	private String adverId;
	@JsonProperty("ALGM_SEQ")
	private String algmSeq;
	@JsonProperty("PRDT_TP_CODE")
	private String prdtTpCode;
	@JsonProperty("ADVRTS_TP_CODE")
	private String advrtsTpCode;
	@JsonProperty("BNR_CODE")
	private String bnrCode;
	@JsonProperty("FRME_CODE")
	private String frmeCode;
	@JsonProperty("PAR_EPRS_CNT")
	private int viewCnt;
	@JsonProperty("CLICK_CNT")
	private int clickCnt;
	@JsonProperty("FRME_CLICK_CNT")
	private int frmeClickCnt;
	@JsonProperty("ORDER_CNT")
	private int orderCnt;
	@JsonProperty("ORDER_AMT")
	private BigDecimal orderAmt;
	@JsonProperty("ADVRTS_AMT")
	private BigDecimal advrtsAmt;
	
	
	/*FRME_CYCLE_LOG*/
	@JsonProperty("MEDIA_SCRIPT_NO")
	private String scriptNo;
	@JsonProperty("CYCLE_TRN")
	private String cycleTrn;
	
}
