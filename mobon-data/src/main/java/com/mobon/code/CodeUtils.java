package com.mobon.code;

import java.util.Arrays;
import java.util.List;

import com.mobon.billing.model.v15.BaseCVData;

public class CodeUtils {

	public static String getRecomTpCode(String adGubun, String subAdGubun) {
		String result="";
		
		if (subAdGubun.equals(adGubun)) {
			result= "02";	//타게팅
			
		}
		else if (!subAdGubun.equals(adGubun)) {
			result= "01";	//추천
			
			// 타게팅으려 변경
			List<String> filter= Arrays.asList("CW","RC","SP","SR","TC","WC","MC","TV","WV","MV","CR");
			if (filter.contains(subAdGubun)) {
				result= "02";
			}
			
			// 기타로 변경
			if ("".equals(subAdGubun) 
					// 값은 넘어오는데 추천으로 잡히지 않게 하기위해 (불필요) 기타로
					|| Arrays.asList("RP","SC").contains(subAdGubun)
				) {
				result= "03";	//기타
			}
		}
		
		return result;
	}
	public static String getRecomAlgoCode(String subAdgubun, String ergDetail, String algoTpCode) {
		String result="";
		
		if(subAdgubun==null || subAdgubun.equals("")) {
			result= RECOM_ALGO_CODE.ETC.getValue();
		}
		else {
//			if(ergDetail.equals("")) {
//				result= RECOM_ALGO_CODE.ETC.getValue();
//			}
//			else
			if( "01".equals(subAdgubun) || "CW".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.CW_trgt.getValue();
			}
			else if( "02".equals(subAdgubun) || "RC".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.RC_trgt.getValue();
			}
			else if( "03".equals(subAdgubun) || "SR".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.SR_trgt.getValue();
			}
			else if( "04".equals(subAdgubun) || "CR".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.CR_3or2click_top30.getValue();
			}
			else if( "05".equals(subAdgubun) || "LS".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.LS_1click_top30.getValue();
			}
			else if( "06".equals(subAdgubun) || "CT".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.CT_3or2click2_top30.getValue();
			}
			else if( "07".equals(subAdgubun) || "RP".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.RP_pastLogic_nonXbox.getValue();
			}
			else if( "08".equals(subAdgubun) || "SC".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.SC_other_shopRandom.getValue();
			}			
			else if( "09".equals(subAdgubun) || "HS".equals(subAdgubun.toUpperCase()) ) {
				if( ergDetail.startsWith("crsc|dummy|")) {
					result= RECOM_ALGO_CODE.HS_crsc_dummy.getValue();
				}
				else if( ergDetail.startsWith("crsc|")) {
					result= RECOM_ALGO_CODE.HS_crsc.getValue();
				}
				else if( ergDetail.startsWith("croc|")) {
					result= RECOM_ALGO_CODE.HS_croc.getValue();
				}
				else if( ergDetail.startsWith("pnks|")) {
					result= RECOM_ALGO_CODE.HS_pnks.getValue();
				}
				// 필요없는것
//				else if( ergDetail.startsWith("st|")) {
//					result= RECOM_ALGO_CODE.SSnSM_st.getValue();
//				}
				
				else if( ergDetail.startsWith("crsc_ai|")) {
					result= RECOM_ALGO_CODE.HS_crsc_ai.getValue();
				}
				else if( ergDetail.startsWith("croc_ai|")) {
					result= RECOM_ALGO_CODE.HS_croc_ai.getValue();
				}
				else if( ergDetail.startsWith("sssc|")) {
					result= RECOM_ALGO_CODE.HS_sssc.getValue();
				}
				else if( ergDetail.startsWith("sssc_ai|")) {
					result= RECOM_ALGO_CODE.HS_sssc_ai.getValue();
				}
				else if( ergDetail.startsWith("ssoc|")) {
					result= RECOM_ALGO_CODE.HS_ssoc.getValue();
				}
				else if( ergDetail.startsWith("ssoc_ai|")) {
					result= RECOM_ALGO_CODE.HS_ssoc_ai.getValue();
				}
				else {
					result= RECOM_ALGO_CODE.ETC.getValue();
				}
			}
			else if ( "10".equals(subAdgubun) || "SM".equals(subAdgubun.toUpperCase())) {
				if( ergDetail.startsWith("crsc|dummy|")) {
					result= RECOM_ALGO_CODE.SM_crsc_dummy.getValue();
				}
				else if( ergDetail.startsWith("crsc|")) {
					result= RECOM_ALGO_CODE.SM_crsc.getValue();
				}
				else if( ergDetail.startsWith("croc|")) {
					result= RECOM_ALGO_CODE.SM_croc.getValue();
				}
				else if( ergDetail.startsWith("pnks|vtDummy|")) {
					result= RECOM_ALGO_CODE.SM_vtDummy.getValue();
				}
				else if( ergDetail.startsWith("pnks|")) {
					result= RECOM_ALGO_CODE.SM_pnks.getValue();
				}
				else if( ergDetail.startsWith("ar|")) {
					result= RECOM_ALGO_CODE.SM_ar.getValue();
				}
				else if( ergDetail.startsWith("st|")) {
					result= RECOM_ALGO_CODE.SSnSM_st.getValue();
				}
				
				else if( ergDetail.startsWith("crsc_ai|")) {
					result= RECOM_ALGO_CODE.SM_crsc_ai.getValue();
				}
				else if( ergDetail.startsWith("croc_ai|")) {
					result= RECOM_ALGO_CODE.SM_croc_ai.getValue();
				}
				else if( ergDetail.startsWith("sssc|")) {
					result= RECOM_ALGO_CODE.SM_sssc.getValue();
				}
				else if( ergDetail.startsWith("sssc_ai|")) {
					result= RECOM_ALGO_CODE.SM_sssc_ai.getValue();
				}
				else if( ergDetail.startsWith("ssoc|")) {
					result= RECOM_ALGO_CODE.SM_ssoc.getValue();
				}
				else if( ergDetail.startsWith("ssoc_ai|")) {
					result= RECOM_ALGO_CODE.SM_ssoc_ai.getValue();
				}
				else {
					result= RECOM_ALGO_CODE.ETC.getValue();
				}
			}
			else if( "11".equals(subAdgubun) || "SS".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.SS_sm.getValue();
			}
			else if( "33".equals(subAdgubun) || "AR".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.AR.getValue();
			}
			else if( "34".equals(subAdgubun) || "VT".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.VT.getValue();
			}
			else if( "35".equals(subAdgubun) || "CC".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.CC.getValue();
			}
			else if( "36".equals(subAdgubun) || "GA".equals(subAdgubun.toUpperCase()) ) {
				result= RECOM_ALGO_CODE.GA.getValue();
			}
			else {
				result= RECOM_ALGO_CODE.ETC.getValue();
			}
		}
		
		return result;
	}
	
	
	
	

    // InterLock 값을 세팅 합니다.
    public static BaseCVData setInterLock(BaseCVData result) {
        if ("daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId())) {
            result.setInterlock("02");
            
        } else if ("kakao".equals(result.getScriptUserId().toLowerCase()) || "mkakao".equals(result.getScriptUserId().toLowerCase())) {
            result.setInterlock("03");
            
        } else if (("googlemedia".equals(result.getScriptUserId().toLowerCase())) || ("mgooglemedia".equals(result.getScriptUserId().toLowerCase()))) {
            result.setInterlock("04");
            
        } else if (("padfit".equals(result.getScriptUserId().toLowerCase())) || ("madfit".equals(result.getScriptUserId().toLowerCase()))) {
            result.setInterlock("05");
            
        } else if (("taboola".equals(result.getScriptUserId().toLowerCase())) || ("mtaboola".equals(result.getScriptUserId().toLowerCase()))) {
            result.setInterlock("06");
            
        } else if (("exelbid".equals(result.getScriptUserId().toLowerCase())) || ("mexelbid".equals(result.getScriptUserId().toLowerCase()))) {
            result.setInterlock("07");
            
        } else if (("igaworksp".equals(result.getScriptUserId().toLowerCase())) || ("igaworksm".equals(result.getScriptUserId().toLowerCase()))) {
            result.setInterlock("08");
            
        } else if (("rtbmobon".equals(result.getScriptUserId().toLowerCase())) || ("mrtbmobon".equals(result.getScriptUserId().toLowerCase()))) {
          result.setInterlock("10");
          
        } else {
        	result.setInterlock("01");
        }

        return result;
    }
    
    public static String openrtbTopic(String topic) {
    	
    	if( topic.startsWith("OpenRTBClick")
    	    	|| topic.startsWith("OpenRTBClickData")
    			) {
    		return "OpenRTBClickData";
    	}
    	else if(topic.startsWith("OpenRTBView")
    	    	|| topic.startsWith("OpenRTBViewData")
    			){
    		return "OpenRTBViewData";
    	}
		return topic;
    }
}
