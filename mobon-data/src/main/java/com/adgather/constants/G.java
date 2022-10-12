package com.adgather.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adgather.constants.old.GlobalConstants;

public class G extends GlobalConstants{

	public static final String ContributeConvData = "ContributeConvData";
	public static List<Map<String, String>> ADVRTS_TP_CODE = null;
	
	// 2020-12-15 공통코드 적용 되어 넘어오면 해당 로직 삭제가능
	// 삭제시 ifnull = 01
	// 삭제시 ifnull = 01
	// 삭제시 ifnull = 01
	// 삭제시 ifnull = 01
	public static String convertDEVICE_TP_CODE(String CODE_VAL){
		String result = "";
		if(CODE_VAL.startsWith("M") || "02".equals(CODE_VAL)) {
			result = "02";
		} else {
			result = "01";
		}
		return result;
	}
	
	// 2020-12-15 공통코드 적용 되어 넘어오면 해당 로직 삭제가능.
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	public static String convertOS_TP_CODE(String CODE_VAL){
		String result = "";
		try {
			if(CODE_VAL.toLowerCase().contains("android") || "01".equals(CODE_VAL)) {
				result = "01";
//			} else if(CODE_VAL.toLowerCase().contains("ios")) {
//				result = "02";
			} else if(CODE_VAL.toLowerCase().contains("windows") || "03".equals(CODE_VAL)) {
				result = "03";
			// ios 는 맥os에 포함시켜달라는 채수삼부장 요청.
			} else if(CODE_VAL.toLowerCase().contains("macos") || CODE_VAL.toLowerCase().contains("ios") || "02".equals(CODE_VAL) || "04".equals(CODE_VAL)) {
				result = "04";
			} else if(CODE_VAL.toLowerCase().contains("linux") || "05".equals(CODE_VAL)) {
				result = "05";
			} else if(CODE_VAL.toLowerCase().contains("chromeos") || "06".equals(CODE_VAL)) {
				result = "06";
			} else {
				result = "99";
			}
		}catch(Exception e) {
		}
		
		return result;
	}
	
	public static String convertBROWSER_VERSION(String CODE_VAL){
		String result = "";
		
		try {
			String[] codeVal = CODE_VAL.split("\\.");
			if("error".equals(CODE_VAL)){
				return result;
			} else if(codeVal.length > 0) {
				result = codeVal[0];
			} else {
				if(CODE_VAL.length() > 10) {
					result = CODE_VAL.substring(0, 10);
				} else {
					result = CODE_VAL;
				}
			}
		}catch(Exception e) {
		}
		
		return result;
	}
	
	// 2020-12-15 공통코드 적용 되어 넘어오면 해당 로직 삭제가능.
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	// 삭제시 ifnull = 99
	public static String convertBROWSER_TP_CODE(String CODE_VAL){
		String result = "";
		
		try {
			if(CODE_VAL.toLowerCase().contains("chrome") || "01".equals(CODE_VAL)) {
				result = "01";
			} else if(CODE_VAL.toLowerCase().contains("ie") || "02".equals(CODE_VAL)) {
				result = "02";
			} else if(CODE_VAL.toLowerCase().contains("safari") || "03".equals(CODE_VAL)) {
				result = "03";
			} else if(CODE_VAL.toLowerCase().contains("samsung") || "04".equals(CODE_VAL)) {
				result = "04";
			} else if(CODE_VAL.toLowerCase().contains("naver")  || "05".equals(CODE_VAL)) {
				result = "05";
			} else if(CODE_VAL.toLowerCase().contains("facebook") || "06".equals(CODE_VAL)) {
				result = "06";
			} else if(CODE_VAL.toLowerCase().contains("daum") || "07".equals(CODE_VAL)) {
				result = "07";
			} else if(CODE_VAL.toLowerCase().contains("firefox") || "08".equals(CODE_VAL)) {
				result = "08";
			} else if (CODE_VAL.toLowerCase().contains("edge") || "09".equals(CODE_VAL)) {
				result = "09";
			} else if (CODE_VAL.toLowerCase().contains("whale") || "10".equals(CODE_VAL)) {
				result = "10";
			} else if (CODE_VAL.toLowerCase().contains("opera") || "11".equals(CODE_VAL)) {
				result = "11";
			} else if (CODE_VAL.toLowerCase().contains("swing") || "12".equals(CODE_VAL)) {
				result = "12";
			}else {
				result = "99";
			}
		}catch(Exception e) {
		}
		
		return result;
	}
	
	public static String convertPLATFORM_CODE(String CODE_VAL){
		String result = "";
		switch(CODE_VAL.toLowerCase()){ 
			case "01": case "w": case "W":	result = "01";  break;
			case "02": case "m": case "M": result = "02";  break; 
			case "03": case "d": case "D": result = "03";  break; 
			default: result = "01";  break;
		}
		return result;
	}
	public static String convertPLATFORM_CODE_BACK(String CODE_VAL){
		String result = "";
		switch(CODE_VAL.toLowerCase()){ 
			case "01": case "":	result = "W";  break;
			case "02": result = "M";  break; 
			case "03": result = "D";  break; 
			default: result = CODE_VAL;  break;
		}
		return result;
	}
	
	public static String convertPRDT_CODE(String CODE_VAL){
		String result = "";
		switch(CODE_VAL){
			case "01": result = "01";  break;
			case "02": result = "02";  break;
			case "03": result = "03";  break;
			case "04": result = "04";  break;
			case "05": result = "05";  break;
			case "06": result = "06";  break;
			case "07": result = "07";  break;
			case "08": result = "08";  break;
			case "09": result = "09";  break;
			
			case "b": result = "01";  break; 
			case "s": result = "02";  break; 
			case "i": result = "03";  break;
			case "c": result = "01";  break;
			case "scn": result = "01";  break; 
			case "m": case "nct": case "mct": result = "05";  break; 
			case "p": case "pl": case "mpl": result = "06";  break; 
			case "t": case "pnt": case "mnt": case "ntimg":  case "mnw": result = "07";  break;
			
			case "e": result = "03";  break; 
			case "mbb": result = "02";  break; 
			case "mba": result = "01";  break; 
			case "mbw": result = "01";  break; 
			case "mbe": result = "03";  break; 
			case "mbs": result = "01";  break; 
			case "video": result = "01";  break; 
			case "nor": result = "01";  break; 
			case "floating": result = "01";  break; 
			case "ico": result = "03";  break; 
			case "ico_m": result = "03";  break; 
			case "sky": result = "02";  break; 
			case "sky_m": result = "02";  break; 
			case "banner": result = "01";  break;
			case "mpw":	result = "08";	break;				// 플러스콜(pluscall(w)) 추가
			case "pf": case "pfw": case "pfm": result = "09"; break;	// 퍼포먼스에드 추가
			default:		result = "99";		break;
		}
		return result;
	}
	
	public static String convertTP_CODE(String CODE_VAL){
		String result = "";
		
		for(Map<String , String> advrtsTpCode : ADVRTS_TP_CODE) {
			if(advrtsTpCode.get("CODE_ID").equals(CODE_VAL)||advrtsTpCode.get("CODE_VAL").equals(CODE_VAL)) {
				result = advrtsTpCode.get("CODE_ID"); 
				break;
			}
		}
		
		if("".equals(result)) {
			result = "99";
		}

		return result;
	}

	public static Boolean checkTargetYN(String CODE_VAL) {
		boolean result = true;

		for (Map<String, String> advrtsTpCode : ADVRTS_TP_CODE) {
			if(advrtsTpCode.get("CODE_ID").equals(CODE_VAL)||advrtsTpCode.get("CODE_VAL").equals(CODE_VAL)) {
				if (advrtsTpCode.get("TARGET_YN") != null){
					if ( "Y".equals(advrtsTpCode.get("TARGET_YN"))) {
						return result;
					}
				}
			}
		}
		return false;
	}

	public static String convertSUBADGUBUN_CODE(String CODE_VAL){
		String result="";
		switch(CODE_VAL){
			case "01":	case "CW":		result = "01";		break;
			case "02":	case "RC":		result = "02";		break;
			case "03":	case "SR":		result = "03";		break;
			case "04":	case "CR":		result = "04";		break;
			case "05":	case "LS":		result = "05";		break;
			case "06":	case "CT":		result = "06";		break;
			case "07":	case "RP":		result = "07";		break;
			case "08":	case "SC":		result = "08";		break;
			case "09":	case "HS":		result = "09";		break;
			case "10":	case "SM":		result = "10";		break;
			case "11":	case "SS":		result = "11";		break;
			case "12":	case "SP":		result = "12";		break;
			case "13":	case "TC":		result = "13";		break;
			case "20":  case "ST":      result = "20";      break;
			case "33":  case "AR": 		result = "33";      break;
			case "34":  case "VT":      result = "34";      break;
			case "35":  case "CC":		result = "35";		break;
			case "36":  case "GA":		result = "36";		break;
			default:		result = "99";		break;
		}
		return result;
	}
	public static String convertTP_CODE_BACK(String CODE_VAL){
		String result = CODE_VAL;
		
		
		for(Map<String , String> advrtsTpCode : ADVRTS_TP_CODE) {
			if(advrtsTpCode.get("CODE_ID").equals(CODE_VAL)) {
				result = advrtsTpCode.get("CODE_VAL"); 
				break;
			}
		}

//		switch(CODE_VAL){
//			case "01":	result = "AD";	break;
//			case "02":	result = "CA";	break;
//			case "03":	result = "CC";	break;
//			case "04":	result = "CW";	break;
//			case "05":	result = "HU";	break;
//			case "06":	result = "KL";	break;
//			case "07":	result = "KP";	break;
//			case "08":	result = "PB";	break;
//			case "09":	result = "PE";	break;
//			case "10":	result = "RC";	break;
//			case "11":	result = "RM";	break;
//			case "12":	result = "RR";	break;
//			case "13":	result = "SA";	break;
//			case "14":	result = "SH";	break;
//			case "15":	result = "SJ";	break;
//			case "16":	result = "SP";	break;
//			case "17":	result = "SR";	break;
//			case "18":	result = "ST";	break;
//			case "19":	result = "UM";	break;
//			case "20":	result = "MM";	break;
//			case "21":	result = "KB";	break;
//			case "22":	result = "IB";	break;
//			case "23":	result = "KM";	break;
//			case "24":	result = "HT";	break;
//			case "25":	result = "PK";	break;
//			case "26":	result = "CM";	break;
//			case "27":	result = "MK";	break;
//			case "28":	result = "AU";	break;
//			case "29":	result = "MR";	break;
//			case "30":	result = "GG";	break;
//			case "31":	result = "AT";	break;
//			case "32":	result = "GS";	break;
//			default:		result = CODE_VAL;		break;
//		}
		
		return result;
	}

	// InterLock 값을 세팅 합니다.
	public static String convertITL_TP_CODE(String CODE_VAL) {
		String itlTpCode = "";

		if ("daisy".equals(CODE_VAL) || "mdaisy".equals(CODE_VAL)) {
			itlTpCode = "02";

		} else if ("kakao".equals(CODE_VAL.toLowerCase()) || "mkakao".equals(CODE_VAL.toLowerCase())) {
			itlTpCode = "03";

		} else if (("googlemedia".equals(CODE_VAL.toLowerCase())) || ("mgooglemedia".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "04";

		} else if (("padfit".equals(CODE_VAL.toLowerCase())) || ("madfit".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "05";

		} else if (("taboola".equals(CODE_VAL.toLowerCase())) || ("mtaboola".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "06";

		} else if (("exelbid".equals(CODE_VAL.toLowerCase())) || ("mexelbid".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "07";

		} else if (("igaworksp".equals(CODE_VAL.toLowerCase())) || ("igaworksm".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "08";

		} else if (("rtbmobon".equals(CODE_VAL.toLowerCase())) || ("mrtbmobon".equals(CODE_VAL.toLowerCase()))) {
			itlTpCode = "10";

		} else {
			itlTpCode = "01";
		}

		return itlTpCode;
	}

	// viewclick
	public static String	ViewClickVo			= "ViewClickVo";
	public static String	ConversionVo		= "ConversionVo";

	public static String	kafka_dbname		= "billing";
	public static String	click_view			= "click_view";
	public static String	click_view_adblock			= "click_view_adblock";
	public static String	click_view_crossAUID	= "click_view_crossAUID";
	public static String	click_view_openrtb	= "click_view_openrtb";
	public static String	click_view_pcode	= "click_view_pcode";
	public static String 	view_pcode 			= "view_pcode";
	public static String	Phone_info			= "Phone_info";
	public static String	client_environment	= "client_environment";
	public static String	adver_client_environment	= "adver_client_environment";
	public static String	camp_media_retrn_aval	= "camp_media_retrn_aval";
	public static String	client_age_gender	= "client_age_gender";
	public static String	click_view_point	= "click_view_point";
	public static String	click_view_point2	= "click_view_point2";
	public static String	short_cut			= "short_cut";
	public static String	conv_info			= "conv_info";
	public static String	convAbusing_info	= "convAbusing_info";
	public static String	convAll_info		= "convAll_info";
	public static String	IntgCntrconv_info	= "IntgCntrconv_info";
	public static String	shop_info			= "shop_info";
	public static String	shopMdPcode_info	= "shopMdPcode_info";
	public static String	shop_stats			= "shop_stats";
	public static String	external_info		= "external_info";
	public static String	skip_info			= "skip_info";
	public static String	near_info			= "near_info";
	public static String	rfdata_info			= "rfdata_info";
	public static String	framertb_info		= "framertb_info";
	public static String 	framertb_ab_info	= "framertb_ab_info";
	public static String	AppTarget_info		= "AppTarget_info";
	public static String	NativeNonAdReport	= "NativeNonAdReport";
	public static String	MediaChrgData		= "MediaChrgData";
	public static String	ParGatrData			= "ParGatrData";
	public static String	KnoUMSiteCodeData	= "KnoUMSiteCodeData";
	public static String	KnoUMScriptNoData	= "KnoUMScriptNoData";
	public static String	KnoKpiData			= "KnoKpiData";
	public static String	IntgCntrData		= "IntgCntrData";
	public static String	IntgCntrKgrData		= "IntgCntrKgrData";
	public static String	IntgCntrUMData		= "IntgCntrUMData";
	public static String	IntgCntrTtimeData	= "IntgCntrTtimeData";
	public static String	AdverProductHHData	= "AdverProductHHData";
	public static String	IntgCntrConv_info	= "IntgCntrConv_info";
	public static String	ConvPcode_info		= "ConvPcode_info";
	public static String    ABPcodeRecomData    = "ABPcodeRecom";
	public static String    ABPcodeRecomConvData    = "ABPcodeRecomConvData";
	public static String 	ActionABPcodeData 	= 	"ActionABPcodeData";
	public static String 	ActionRenewLogData  = "ActionRenewLogData";
	public static String	HHtoDD				= "HHtoDD";
	
	public static String	offset				= "offset";
	public static String	seq_date			= "seq_date";
	public static String	consumer_thread		= "consumer_thread";
	public static String	action_data			= "action_data";
	public static String	action_pcode_data	= "action_pcode_data";
	public static String	IntgCntrAction_data	= "IntgCntrAction_data";
	
	public static String	ClickViewData		= "ClickViewData";
	public static String	OpenRTBClickData	= "OpenRTBClickData";
	public static String	OpenRTBViewData		= "OpenRTBViewData";
	public static String	ClickViewPointData	= "ClickViewPointData";
	public static String	ConversionData		= "ConversionData";
	public static String	ConversionSuccData	= "ConversionSuccData";
	public static String	ExternalData		= "ExternalData";
	public static String	ShopInfoData		= "ShopInfoData";
	public static String	ShopStatsInfoData	= "ShopStatsInfoData";
	public static String	RfData				= "RfData";
	public static String	reqLog				= "REQ-LOG";
	//소재 카피 topic 
	public static String 	SubjectCopyClickView = "subjectCopyClickView";
	public static String    SuccConversion = "SuccConversion";
	
	public static String	AdChargeData		= "AdChargeData"; 
	public static String	DrcData				= "DrcData"; 
	public static String	ShortCutData		= "ShortCutData"; 
	public static String	RTBReportData		= "RTBReportData"; 
	public static String	RTBDrcData			= "RTBDrcData"; 
	public static String    AlgoViewData 		= "AlgoViewData";
	public static String 	BasketData 			= "BasketData";
	public static String 	InsiteClickView		= "InsiteClickView";
	public static String 	InsiteConversion	= "InsiteConversion";
	public static String 	YmdMapData			= "YmdMapData";
	
	public static String	BounceRateData		= "BounceRateData";
	public static String	EC	=	EXTERNALCHARGE; 
	public static String	EB	=	EXTERNALBATCH; 

    //AdFit
//    public static String OpenRTBBid_Adfit = "OpenRTBBid_Adfit";
    public static String OpenRTBClick_Adfit = "OpenRTBClick_Adfit";
    public static String OpenRTBView_Adfit = "OpenRTBView_Adfit";
    public static String OpenRTBConversion_Adfit = "OpenRTBConversion_Adfit";
//    public static String OpenRTBPoint_Adfit = "OpenRTBPoint_Adfit";
    //#Google
//    public static String OpenRTBBid_Google = "OpenRTBBid_Google";
    public static String OpenRTBClick_Google = "OpenRTBClick_Google";
    public static String OpenRTBView_Google = "OpenRTBView_Google";
    public static String OpenRTBConversion_Google = "OpenRTBConversion_Google";
//    public static String OpenRTBPoint_Google = "OpenRTBPoint_Google";
    //#kakao
//    public static String OpenRTBBid_Kakao = "OpenRTBBid_Kakao";
    public static String OpenRTBClick_Kakao = "OpenRTBClick_Kakao";
    public static String OpenRTBView_Kakao = "OpenRTBView_Kakao";
    public static String OpenRTBConversion_Kakao = "OpenRTBConversion_Kakao";
//    public static String OpenRTBPoint_Kakao = "OpenRTBPoint_Kakao";

    //타블라
//    public static String OpenRTBBid_Taboola = "OpenRTBBid_Taboola";
    public static String OpenRTBClick_Taboola = "OpenRTBClick_Taboola";
    public static String OpenRTBView_Taboola = "OpenRTBView_Taboola";
    public static String OpenRTBConversion_Taboola = "OpenRTBConversion_Taboola";
//    public static String OpenRTBPoint_Taboola = "OpenRTBPoint_Taboola";

    //igaworks
//    public static String OpenRTBBid_Igaworks = "OpenRTBBid_Igaworks";
    public static String OpenRTBClick_Igaworks = "OpenRTBClick_Igaworks";
    public static String OpenRTBView_Igaworks = "OpenRTBView_Igaworks";
    public static String OpenRTBConversion_Igaworks = "OpenRTBConversion_Igaworks";
//    public static String OpenRTBPoint_Igaworks = "OpenRTBPoint_Igaworks";

    //Mixer
//    public static String OpenRTBBid_Mixer = "OpenRTBBid_Mixer";
    public static String OpenRTBClick_Mixer = "OpenRTBClick_Mixer";
    public static String OpenRTBView_Mixer = "OpenRTBView_Mixer";
    public static String OpenRTBConversion_Mixer = "OpenRTBConversion_Mixer";
//    public static String OpenRTBPoint_Mixer = "OpenRTBPoint_Mixer";
    
	// 타게팅 구분코드
	public static List<String> adgubunFilter= Arrays.asList("CW","RC","SP","SR","PR","TC");
	public static List<String> adgubunCodeFilter= Arrays.asList("04","10","16","17","34","37");
	
	//FrameRtb 특정 광고주와 사이즈 수집
	public static Map<String, String> frameAdverSize = new HashMap<String, String>() {
	    {
	        // put("kolonmall", "M05");
	        put("jull", "M05");
	        put("hfashionmall", "W12");
	    }
	};
	public static Boolean retryYN = false;
	public static Boolean topicYN = false;
	
	//유니크 
	public static String Unique_Click = "Unique_Click";
	
	//AI 블럭 
	public static String AiBlockClickView = "AiBlockClickView";
	
	//클릭프리컨시
	public static String ChrgLogData = "ChrgLogData";
	
	// AI 캠페인
	public static String AiData = "AiData";

	// 플러스콜
	public static String PluscallLogData = "PluscallLogData";

}
