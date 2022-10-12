package com.mobon.billing.dump.constants;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static com.mobon.billing.dump.constants.AdvertisementProductType.*;

/**
 * @FileName : AdvertisementMpgType.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : 모비온에서 전달되는 상품 코드를 빌링용으로 전환하기 위한 Enum class
 */
public enum AdvertisementMpgType {

    mba("01","배너모바일", b),
    mbw("02","배너웹", b),
    mbs("03","쇼콘모바일", b),
    video("04","배너비디오(구형)", b),
    nor("05","배너", b),
    floating("06","배너플로팅", b),
    banner("07","배너구형", b),
    con("08","쇼콘", b),
    mbb("09","브랜드링크모바일", s),
    sky("10","브랜드링크", s),
    sky_m("11","브랜드링크모바일", s),
    mbe("12","아이커버모바일(엔딩커버)", i),
    ico("13","아이커버", i),
    ico_m("14","아이커버모바일", i),
    nct("15","문맥웹", m),
    mct("16","문맥모바일", m),
    pl("17","플레이링크웹", p),
    mpl("18","플레이링크모바일", p),
    pnt("19","네이티브웹", t),
    mnt("20","네이티브모바일", t),
    ntimg("21","네이티브이미지", t),
    mpw("22","플러스콜", w),
    pfw("23","퍼포먼스애드웹", f),
    pfm("24","퍼포먼스애드모바일", f);

    private AdvertisementProductType advrtsPrdt;
    private String advrtsMpgTpCode;
    private String advrtsMpgTpCodeDesc;

    private static final Map<String, AdvertisementMpgType> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));

    AdvertisementMpgType(String advrtsMpgTpCode , String advrtsMpgTpCodeDesc, AdvertisementProductType advrtsPrdt){
        this.advrtsMpgTpCode = advrtsMpgTpCode;
        this.advrtsMpgTpCodeDesc = advrtsMpgTpCodeDesc;
        this.advrtsPrdt = advrtsPrdt;
    }


    public static Optional<AdvertisementMpgType> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }


    public String getAdvrtsPrdtCode() {
        return advrtsPrdt.getAdvrtsPrdtCode();
    }

    public String getAdvrtsMpgTpCode() {
        return advrtsMpgTpCode;
    }

    public String getAdvrtsMpgTpCodeDesc() {
        return advrtsMpgTpCodeDesc;
    }
}