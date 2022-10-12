package com.mobon.billing.dump.file.mobon;

import com.mobon.billing.dump.constants.AdvertisementType;
import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.file.mobon.vo.MobonFileDataVO;

public abstract class DataFilter {

    /**
     * @Method Name : adverIDSizeCheck
     * @Date : 2020. 08. 31.
     * @Author : dkchoi
     * @Comment : 광고주아이디 크기 Check 로직.
     * @param fLine
     * @return
     */
    protected boolean adverIDLengthCheck(MobonFileDataVO fLine) {
        return fLine.getAdverId().length() > GlobalConstants.MAX_ADVERID_FIELD ? false : true;
    }

    /**
     * @Method Name : noCountFrequency
     * @Date : 2021. 04. 27.
     * @Author : dkchoi
     * @Comment : 비상품이면서 구좌노출이 0인 데이터 프리퀀시 카운트 하지 않기 위한 로직. (채우기 로직 카운트 제외)
     * @param fLine
     * @return
     */
    protected boolean noCountFrequency(MobonFileDataVO fLine) {
        return "N".equals(AdvertisementType.getTrgtYN(fLine.getAdvrtsTpCode())) &&
                fLine.getParEprsCnt() == 0 && "V".equals(fLine.getAction());
    }

}
