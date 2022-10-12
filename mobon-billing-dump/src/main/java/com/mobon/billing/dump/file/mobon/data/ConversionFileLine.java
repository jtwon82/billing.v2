package com.mobon.billing.dump.file.mobon.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.utils.CommonUtils;

import lombok.Getter;

/**
 * @FileName : ConversionFileLine.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : ABTEST Conversion File Data를 구분자 별로 구분지어 분리하는 작업을 하는 클래스.
 */
@Getter
public class ConversionFileLine {
	
	private int tabSize;

	private String statsDttm;
	private int mediaScriptNo;
	private String[] abtestTypes;
	private String itlTpCode; 
	private String direct;
	private String inHour;
	private int orderAmt;
	private String freq;
	private String siteCode;
	private String pltfomTpCode;
	private String advrtsPrdtCode;
	private String advrtsTpCode;
	private String adverId;
	private String rcmmCode;
	private String frameId;
	private String parTpCode;
	private String prdtTpCode;
	private String frameCombiKey;
	private String frameType;
	
	//frame 미노출 여부  기본값 N 
	private String frameMatrExposureYN = "N";
	private String frameSendTpCode;
	
	//frameCombiTargetYN 
	private String frameCombiTargetYN  = "N";
	/**
	 * @param str
	 */
	public ConversionFileLine(String str) {
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
				case 2:	pltfomTpCode = list[idx];	break;
				case 3:	advrtsPrdtCode = list[idx];	break;
				case 4:	advrtsTpCode = list[idx];		break;
				case 5:
					if (list[idx].indexOf(GlobalConstants.ABTEST_DELIMETER) >= 0) {
						abtestTypes = CommonUtils.toArray(list[idx], GlobalConstants.ABTEST_DELIMETER);
					} else {
						abtestTypes = CommonUtils.toArray(list[idx], null);
					}
					break;
				case 6:itlTpCode = list[idx].equals("kakao")||list[idx].equals("mkakao")?"03":"01";	break;
				case 7:direct = list[idx];	break;
				case 8:inHour = list[idx];	break;
				case 10:orderAmt = Integer.parseInt(list[idx]);	break;
				case 11:freq = list[idx];	break;
				case 12:siteCode = list[idx];	break;
				case 15:adverId = list[idx];	break;
				case 16:rcmmCode = list[idx];	break;
				case 17:frameId = list[idx];	break;
				case 18:prdtTpCode = list[idx];	break;
				case 19:frameCombiKey = list[idx];	break;
				case 20:frameType = list[idx];	break;
				case 21:
					if ( ! "null".equals(list[idx])) {
						frameMatrExposureYN = list[idx];							
					};
					break;
				case 22 :
					if (! "null".equals(list[idx])) {
						frameSendTpCode = list[idx];
					}; 
					break;
				case 23 : 
					frameCombiTargetYN = list[idx];
					break;
			}
			tabSize = idx  + 1;
			parTpCode = "01"; // 값 생성되면 삭제 예정
		}
		
	}

}
