package com.mobon.billing.dump.constants;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @FileName : AdvertisementProductType.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : 모비온에서 전달되는 상품 코드를 빌링용으로 전환하기 위한 Enum class
 */
public enum AdvertisementProductType {
    
    b("01","배너(b)"),
    s("02","브랜드링크(s)"),
    i("03","아이커버(i)"),
    c("04","쇼콘(c)"),
    m("05","문맥(m)"),
    p("06","플레이링크(p)"),
    t("07","네이티브(t)"),
    w("08","플러스콜(w)"),
    f("09","퍼포먼스광고(f)");

    private String advrtsPrdtCode;
    private String advrtsPrdtCodeDesc;
    private static final Map<String, AdvertisementProductType> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));
    
    AdvertisementProductType(String advrtsPrdtCode, String advrtsPrdtCodeDesc){
        
        this.advrtsPrdtCode = advrtsPrdtCode;
        this.advrtsPrdtCodeDesc = advrtsPrdtCodeDesc;
        
    }
    
    public static Optional<AdvertisementProductType> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }
    
    public String getAdvrtsPrdtCode() {
        return advrtsPrdtCode;
    }
    
    public String getAdvrtsPrdtCodeDesc() {
        return advrtsPrdtCodeDesc;
    }


}
