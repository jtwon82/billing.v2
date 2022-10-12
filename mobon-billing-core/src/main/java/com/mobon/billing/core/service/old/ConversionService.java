package com.mobon.billing.core.service.old;

import com.mobon.billing.model.v15.ConvData;
import com.mobon.conversion.domain.old.ConversionCode;
import com.mobon.conversion.domain.old.ConversionInfoFilter;

public interface ConversionService {

  public boolean isAvailableFrequency(ConversionInfoFilter filter, ConvData data);
  public boolean isAvailableFrequencyV2(ConversionInfoFilter filter, ConvData data);

  public static String convertInHourCode(String chargeCode){
    if(ConversionCode.VALID_SESSION_CODE.equals(chargeCode) || ConversionCode.VALID_DIRECT_CODE.equals(chargeCode)){
      return "24";
    } else {
      return "0";
    }
  }
  
  public static String convertDirectCode(String chargeCode){
    if(ConversionCode.VALID_INDIRECT_CODE.equals(chargeCode) || ConversionCode.VALID_DIRECT_CODE.equals(chargeCode)){
      return "0";
    } else {
      return "1";
    }
  }
}
