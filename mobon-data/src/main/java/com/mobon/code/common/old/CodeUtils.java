package com.mobon.code.common.old;

import com.adgather.constants.old.GlobalConstants;
import com.mobon.code.constant.old.CodeConstants;

public class CodeUtils {
    public static String getAdTypeCode(String adGubun) {
        if (adGubun.equals(GlobalConstants.CW)) return CodeConstants.ADVRTS_TP_CODE_CW;
        else if (adGubun.equals(GlobalConstants.SR)) return CodeConstants.ADVRTS_TP_CODE_SR;
        else if (adGubun.equals(GlobalConstants.RC)) return CodeConstants.ADVRTS_TP_CODE_RC;
        else if (adGubun.equals(GlobalConstants.SP)) return CodeConstants.ADVRTS_TP_CODE_SP;
        else if (adGubun.equals(GlobalConstants.RM)) return CodeConstants.ADVRTS_TP_CODE_RM;
        else if (adGubun.equals(GlobalConstants.HU)) return CodeConstants.ADVRTS_TP_CODE_HU;
        else if (adGubun.equals(GlobalConstants.RM)) return CodeConstants.ADVRTS_TP_CODE_RM;
        else if (adGubun.equals(GlobalConstants.UM)) return CodeConstants.ADVRTS_TP_CODE_UM;
        else if (adGubun.equals(GlobalConstants.KL)) return CodeConstants.ADVRTS_TP_CODE_KL;
        else if (adGubun.equals(GlobalConstants.MM)) return CodeConstants.ADVRTS_TP_CODE_MM;
        else if (adGubun.equals(GlobalConstants.AD)) return CodeConstants.ADVRTS_TP_CODE_AD;
        else if (adGubun.equals(GlobalConstants.ST)) return CodeConstants.ADVRTS_TP_CODE_ST;
        else if (adGubun.equals(GlobalConstants.CC)) return CodeConstants.ADVRTS_TP_CODE_CC;
        else if (adGubun.equals(GlobalConstants.KP)) return CodeConstants.ADVRTS_TP_CODE_KP;
        else if (adGubun.equals(GlobalConstants.PB)) return CodeConstants.ADVRTS_TP_CODE_PB;
        else if (adGubun.equals(GlobalConstants.RR)) return CodeConstants.ADVRTS_TP_CODE_RR;
        else if (adGubun.equals(GlobalConstants.SA)) return CodeConstants.ADVRTS_TP_CODE_SA;
        else if (adGubun.equals(GlobalConstants.SH)) return CodeConstants.ADVRTS_TP_CODE_SH;
        else if (adGubun.equals(GlobalConstants.SJ)) return CodeConstants.ADVRTS_TP_CODE_SJ;
        return null;
    }
}
