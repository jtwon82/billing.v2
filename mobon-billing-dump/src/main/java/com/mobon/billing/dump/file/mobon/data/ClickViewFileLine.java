package com.mobon.billing.dump.file.mobon.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.utils.CommonUtils;

import lombok.Getter;

/**
 * @FileName : ClickViewFileLine.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : ABTEST ClickView File Data를 구분자 별로 구분지어 분리하는 작업을 하는 클래스.
 */
@Getter
public class ClickViewFileLine {
	
	private int tabSize;

	private String statsDttm;
	private int mediaScriptNo;
	private String siteCode;
	private String freq;
	private String action;
	private int clickCnt;
	private String[] abtestTypes;
	private String itlTpCode;
	private int totEprsCnt;
	private int parEprsCnt;
	private BigDecimal advrtsAmt;
	private BigDecimal mediaPymntAmt; 

	
	private String pltfomTpCode;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String adverId;
	private String rcmmCode;
	private String statyn;
	private String frameId;
	private String parTpCode;
	private String prdtTpCode;
	private String frameCombiKey;
	private String frameType;
	
	//frame 미노출 여부 
	private String frameMatrExposureYN;
	private String frameSendTpCode;
	
	//frameTargetYn 
	private String frameCombiTargetYN;
	/**
	 * @param str
	 */
	public ClickViewFileLine(String str) {
		set(str);
	}

	/**
	 * @Method Name : set
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 탭을 구분자로 하는 라인 데이터를 각각 분리하여 변수에 등록.
	 * @param str
	 */
	private void set(String str) {
		String[] list = str.split("\t");
		if(list == null)
			return;
		
		for (int idx = 0; idx < list.length; idx++) {
			switch (idx) {
				case 0:	
					
				Date sendDate;
				try {
					if(!"".equals(list[idx])) {
						sendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list[idx]);
						statsDttm = new SimpleDateFormat("yyyyMMdd").format(sendDate);
					} else {
						statsDttm = "";
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				break;
				case 1:	mediaScriptNo = Integer.parseInt(list[idx]);				break;
				case 2: siteCode = list[idx];	break;
				case 3:	pltfomTpCode = list[idx];	break;
				case 4:	advrtsPrdtCode = CommonUtils.toProductType(list[idx]);	break;
				case 5:	advrtsTpCode = CommonUtils.toAdGubunType(list[idx]);		break;
				case 6: freq = list[idx];	break;
				case 7:	action = list[idx];			break;
				case 8:	clickCnt = Integer.parseInt(list[idx]);				break;
				case 10:advrtsAmt = new BigDecimal(list[idx]);			break;
				case 17:
					if (list[idx].indexOf(GlobalConstants.ABTEST_DELIMETER) >= 0) {
						abtestTypes = CommonUtils.toArray(list[idx], GlobalConstants.ABTEST_DELIMETER);
					} else {
						abtestTypes = CommonUtils.toArray(list[idx], null);
					}
					break;
				case 18:itlTpCode = list[idx];	break;
				case 19:totEprsCnt = Integer.parseInt(list[idx]);	break;
				case 20:parEprsCnt = Integer.parseInt(list[idx]);	break;
				case 21:mediaPymntAmt = new BigDecimal(list[idx]);	break;
				case 22:adverId = list[idx];	break;
				case 23:rcmmCode = list[idx];	break;
				case 24:statyn = list[idx];	break;
				case 25:frameId = list[idx];	break;
				case 26:prdtTpCode = list[idx];	break;
				case 27:frameCombiKey = list[idx];	break;
				case 28:frameType = list[idx];	break;
				case 29:frameMatrExposureYN = list[idx]; break;
				case 30:frameSendTpCode = list[idx]; break;
				case 31:frameCombiTargetYN = list[idx]; break;
			}
			tabSize = idx  + 1;
			parTpCode = "01"; // 값 생성되면 삭제 예정
		}
		
	}

}
