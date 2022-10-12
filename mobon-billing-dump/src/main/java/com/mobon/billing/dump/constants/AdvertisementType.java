package com.mobon.billing.dump.constants;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @FileName : AdvertisementType.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : 모비온에서 전달되는 타겟팅 코드를 빌링용으로 전환하기 위한 Enum class
 */
public enum AdvertisementType {
    
    AL("00","전체(AL)", "N"),
    AD("01","베이스(AD)", "N"),
    CA("02","할인금액쇼콘(CA)", "N"),
    CC("03","이벤트타겟팅(CC)", "N"),
    CW("04","장바구니(CW)", "Y"),
    HU("05","헤비유저(HU)", "N"),
    KL("06","키워드(KL)", "N"),
    KP("07","키워드상품(KP)", "N"),
    PB("08","프리미엄배너(PB)", "N"),
    PE("09","할인율쇼콘(PE)", "N"),
    RC("10","리사이클(RC)", "Y"),
    RM("11","자사 타게팅(RM)", "N"),
    RR("12","RM리사이클(RR)", "N"),
    SA("13","브랜드박스(SA)", "N"),
    SH("14","브랜드박스 안 캐시백상품(SH)", "N"),
    SJ("15","쇼핑입점(SJ)", "N"),
    SP("16","추천(일반상품)(SP)", "Y"),
    SR("17","본상품(SR)", "Y"),
    ST("18","투데이베스트(ST)", "N"),
    UM("19","성향(도메인)(UM)", "N"),
    MM("20","문맥키워드(MM)", "N"),
    KB("21","키워드볼드(KB)", "N"),
    IB("22","유입키워드(IB)", "N"),
    KM("23","키워드매칭(검색광고용)", "N"),
    IK("24","유입키워드(IK)", "N"),
    PK("25","신규키워드상품(PK)", "N"),
    CM("26","카테고리매칭(CM)", "N"),
    MK("27","핵심키워드(MK)", "N"),
    AU("28","오디언스(AU)", "N"),
    MR("29","문맥리타게팅(MR)", "N"),
    GG("30","구글(GG)", "N"),
    AT("31","앱프로파일(AT)", "N"),
    GS("32","구글 쇼핑(GS)", "N"),
    HB("33","하우스배너(HB)", "N"),
    PR("34","프로모션타게팅(PR)", "Y"),
    RU("35","휴면유저타겟팅(RU)", "N"),
    MT("36","문맥매칭(MT)", "N"),
    TC("37","투데이카트매칭(TC)", "Y"),
    DP("38","덤프매칭(DP)", "N"),
    SW("39","소셜오디언스(SW)", "N"),
    WC("40","위클리카트매칭(WC)", "Y"),
    MC("41","먼슬리카트매칭(MC)", "Y"),
    TV("42","오늘본상품매칭(TV)", "Y"),
    BP("43","소비성향매칭(BP)", "N"),
    PA("44","파워베이스광고(PA)", "N"),
    PC("46","plusCall 매칭(PC)", "N"),
    WV("47","위클리본상품매칭(WV)", "Y"),
    WR("48","위클리리턴매칭(WR)", "N"),
    MV("49","먼슬리본상품(MV)","Y"),
    CR("50","커스텀리타겟팅(CR)","Y"),
    TR("51","투데이리턴(TR)","N"),
    FR("52","먼슬리리턴(FR)","N"),
    RT("53","콜리턴매칭(RT)","N"),
    CH("54","커스텀하이(CH)","Y"),
    CL("55","커스텀로우(CL)","Y"),
    PF("56", "퍼포먼스애드(PF)", "Y"),
    WP("57","위클리 보안재(WP)","Y"),
    TM("58","발신이력매칭(TM)","N"),
    NL("99","미노출(NL)", "N");

    private String advrtsTpCode;
    private String advrtsTpCodeDesc;
    private String trgtYN;
    
    private static final Map<String, AdvertisementType> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));
    
    AdvertisementType(String advrtsTpCode,String advrtsTpCodeDesc,String trgtYN) {
        this.advrtsTpCode = advrtsTpCode;
        this.advrtsTpCodeDesc = advrtsTpCodeDesc;
        this.trgtYN = trgtYN;
    }
    
    public static Optional<AdvertisementType> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }
    
    public String getAdvrtsTpCode() {
        return advrtsTpCode;
    }
    
    public String getAdvrtsTpCodeDesc() {
        return advrtsTpCodeDesc;
    }

    public static String getTrgtYN(String advrtsTpCode) {

        for(AdvertisementType advertisementType : AdvertisementType.values()){
            if(advertisementType.getAdvrtsTpCode().equals(advrtsTpCode))
                return advertisementType.trgtYN;
        }
        return "N";
    }

}
