package com.mobon.billing.model.v20;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.adgather.constants.G;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewClickVo extends CommonVo implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ViewClickVo.class);

    // value 관련 데이터
    private int viewcnt1 = 0;			// 총노출
    private int viewcnt2 = 0;			//
    private int viewcnt3 = 0;			// 지면노출
    private int clickcnt = 0;			// 클릭
    private float point = 0;			// 포인트
    private int avalCallTime = 0;		// 유효콜 콜타임
    private int dbCnvrsCnt = 0;			// DB 전환수

    /**
     * PollingData를 이용해서
     * 1. 객체변환
     * 2. 유효성검증
     * 3. 데이터가공
     * 을 수행해서 해당 객체 반환 혹은 생성 안될경우 null 반환
     */
    public static ViewClickVo create(PollingData from) {
        PollingData pollingData = from.copy();
        ViewClickVo viewClickVo = null;

        // 객체변환
        viewClickVo = ViewClickVo.fromPollingData(pollingData);

        if (!validate(viewClickVo)) {
            // 유효성 검증
            viewClickVo = null;
        } else if (!convert(viewClickVo)) {
            // className, dumpType 에 대한 데이터 가공
            viewClickVo = null;
        }

        return viewClickVo;
    }

    /**
     * PollingData 를 ViewClickVo 로 처리할 수 있는지에 대한 유효성 검사
     * @return true : 유효, false : 무효
     */
    private static boolean validate(ViewClickVo from) {
        boolean result = true;

        try {
            // 테이블 파티션 체크 - 2주
            LocalDate objDate = LocalDate.parse(from.getYyyymmdd(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (objDate.isBefore(LocalDate.now().minusWeeks(2))) {
                logger.error("ViewClickVo over partition - {}", from);
                result = false;
            }

            // 필수갑 체크
            if (StringUtils.isBlank(from.getYyyymmdd())
                    || StringUtils.isBlank(from.getProduct())
                    || StringUtils.isBlank(from.getAdGubun())
                    || StringUtils.isBlank(from.getSiteCode())
                    || StringUtils.isBlank(from.getScriptNo())
            ) {
                logger.debug("ViewClickVo no required values - {}", from);
                result = false;
            }

            // 무효 데이터 처리 안함
            if ("91".equals(from.getChrgTpCode())) {
                logger.debug("ViewClickVo chrgTpCode is '91'");
                result = false;
            }
        } catch (Exception e) {
            result = false;
            logger.error("ViewClickVo validate err - {} / object - {}", e ,from);
        }

        return result;
    }

    /**
     * PollingData를 ViewClickVo에 맞게 데이터를 가공
     */
    private static boolean convert(ViewClickVo from) {
        boolean result = true;

        try {
            // className 에 따른 벨류처리
            if (G.RTBReportData.equals(from.getClassName())) {
                from.setType("V");
            } else if (G.RTBDrcData.equals(from.getClassName())) {
                from.setType("C");
                from.setClickcnt(1);
            } else if (G.ShortCutData.equals(from.getClassName())) {
                if (StringUtils.isBlank(from.getType())) {
                    result = false;
                } else {
                    from.setClickcnt(1);
                }
            } else {
                // dumpType 에 따른 벨류처리
                if (G.DRCCHARGE.equals(from.getDumpType()) || G.SHOPCONCHARGE.equals(from.getDumpType())) {
                    from.setType("C");
                    from.setClickcnt(1);
                } else if (G.NORMALCHARGE.equals(from.getDumpType())) {
                    from.setType("V");
                    from.setViewcnt1(Math.max(from.getViewcnt1(), 1));
                } else if (G.MOBILECHARGE.equals(from.getDumpType())) {
                    from.setType("V");
                } else if (G.SKYCHARGE.equals(from.getDumpType())) {
                    if (G.VIEW.equals(from.getType())) {
                        from.setType("V");
                    } else {
                        from.setType("C");
                        from.setClickcnt(1);
                    }
                } else if (G.ICOCHARGE.equals(from.getDumpType())) {
                    from.setType("V");
                } else if (G.PLAY_LINK_CHARGE.equals(from.getDumpType())) {
                    if (G.VIEW.equals(from.getType())) {
                        from.setType("V");
                    } else {
                        from.setType("C");
                    }
                } else if (G.ACTIONCHARGE.equals(from.getDumpType())) {
                    from.setType("A");
                    from.setViewcnt1(0);
                    from.setViewcnt3(0);
                    from.setClickcnt(0);
                    result = false;     // ACTION_LOG 적재 로직 부분
                } else if (G.CONVERSIONCHARGE.equals(from.getDumpType())) {
                    from.setProduct(from.getType());
                    from.setType("CONV");
                }
            }

            // 아이커버 인경우 노출 = 클릭이므로 해당 타입을 click 으로 변경
            if ("03".equals(from.getProduct())
                    || from.getProduct().toLowerCase().indexOf("ico") > -1) {
                from.setViewcnt3(from.getViewcnt1());
                from.setClickcnt(from.getViewcnt1());
            }

            // 플러스콜 인경우 클릭카운트 제어 - 유효콜 및 DB전환이 ConversionData 로 이동되면 삭제 가능
            if (G.DrcData.equals(from.getClassName())
                    && "08".equals(G.convertPRDT_CODE(from.getProduct()))
                    && (from.getAvalCallTime() >= 1 || from.getDbCnvrsCnt() >= 1)) {
                from.setClickcnt(0);
            }

            // OpenRTB 데이터 이외에는 부모지면이 있을경우 부모지면에 데이터 적재
            if (!StringUtils.isBlank(from.getScriptHirnkNo()) &&
                    !(G.RTBReportData.equals(from.getClassName()) || G.RTBDrcData.equals(from.getClassName()))) {
                from.setScriptNo(from.getScriptHirnkNo());
            }

            // 플랫폼 코드 형식 변경
            if (!StringUtils.isBlank(from.getPlatform())) {
                from.setPlatform(from.getPlatform().substring(0, 1).toUpperCase());
            }

            logger.debug("PollingData to ViewClickVo convert succ");

        } catch (Exception e) {
            result = false;
            logger.error("PollingData to ViewClickVo convert err - {} / object - {}", e, from);
        }

        return result;
    }

    /**
     * 객체 고유갑 생성 메소드
     */
    public String getKey() {
        return String.format("%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s",
                this.getYyyymmdd(), this.getHh() , this.getPlatform(), this.getProduct(), this.getAdGubun(),
                this.getSiteCode(), this.getScriptNo(), this.getInterlock(), this.getKpiNo(), this.getAdvertiserId(),
                this.getScriptUserId(), this.getNoExposureYN(), this.getIp(), this.getAuId(), this.getType());
    }

    /**
     * 데이터 누적처리 메소드
     */
    public void sumGethering(Object _from) {
        ViewClickVo from = (ViewClickVo) _from;

        if ("03".equals(from.getProductCode())) {
            this.setViewcnt1(this.getViewcnt1()+from.getViewcnt1());
            this.setViewcnt2(this.getViewcnt2()+from.getViewcnt2());
            this.setViewcnt3(this.getViewcnt3()+from.getViewcnt3());
            this.setClickcnt(this.getClickcnt()+from.getClickcnt());
        } else {
            if ("V".equals(from.getType())) {
                this.setViewcnt1(this.getViewcnt1() + from.getViewcnt1());
                this.setViewcnt2(this.getViewcnt2() + from.getViewcnt2());
                this.setViewcnt3(this.getViewcnt3() + from.getViewcnt3());

            } else if ("C".equals(from.getType())) {
                this.setClickcnt(this.getClickcnt() + from.getClickcnt());
            }
        }

        this.setPoint(this.getPoint() + from.getPoint());
    }

    /**
     * 현재 소속그룹 반환 메소드
     */
    public String getGrouping() {
        return String.format("[%s]", Math.abs(Integer.parseInt(this.getScriptNo()) % 20) + "");
    }

    /**
     * PollingData -> 해당 객체 생성 메소드
     * (변수 타입이 안맞는 경우에만 해당 메소드에서 별도 로직으로 set 을 하고 그 이외에는 convert 에서 다 맞춰서 넘어온다)
     */
    private static ViewClickVo fromPollingData(PollingData from) {
        ViewClickVo result = new ViewClickVo();

        try {
            result.setClassName(from.getClassName());
            result.setDumpType(from.getDumpType());
            result.setSendDate(from.getSendDate());
            result.setYyyymmdd(from.getYyyymmdd());
            result.setHh(from.getHh());
            result.setIp(from.getIp());
            result.setAuId(from.getAuId());
            result.setPlatform(from.getPlatform());
            result.setProduct(from.getProduct());
            result.setAdGubun(from.getAdGubun());
            result.setAdvertiserId(from.getAdvertiserId());
            result.setKpiNo(from.getKpiNo());
            result.setSiteCode(from.getSiteCode());
            result.setScriptUserId(from.getScriptUserId());
            result.setScriptNo(from.getMediaCode());
            result.setScriptHirnkNo(from.getScriptHirnkNo());
            result.setType(from.getType());
            result.setSvcTpCode(from.getSvcTpCode());
            result.setChrgTpCode(from.getChrgTpCode());
            result.setNoExposureYN(from.getNoExposureYN().startsWith("true") ? "Y" : "N");
            result.setTargetYn(G.checkTargetYN(from.getAdGubun()));
            result.setViewcnt1(from.getViewcnt1());
            result.setViewcnt2(from.getViewcnt2());
            result.setViewcnt3(from.getViewcnt3());
            result.setClickcnt(from.getClickcnt());
            result.setPoint(from.getPoint());
            result.setAvalCallTime(from.getAvalCallTime());
            result.setDbCnvrsCnt(from.getDbCnvrsCnt());

            logger.debug("ViewClickVo fromPollingData succ");
        } catch (Exception e) {
            result = null;
            logger.error("ViewClickVo fromPollingData err - {} / object - {}", e, from);
        }

        return result;
    }

    /**
     * 생성자를 사용해서 객체 깊은 복사하는 메소드
     */
    public ViewClickVo copy() {
        return new ViewClickVo(this);
    }

    /**
     * 갹채 갚은 복사 생성자 (필드 추가되면 여기도 추가 필요, 밖에서 사용못하게 private)
     */
    private ViewClickVo(ViewClickVo obj) {
        super(obj);
        this.viewcnt1 = obj.viewcnt1;
        this.viewcnt2 = obj.viewcnt2;
        this.viewcnt3 = obj.viewcnt3;
        this.clickcnt = obj.clickcnt;
        this.point = obj.point;
        this.avalCallTime = obj.avalCallTime;
        this.dbCnvrsCnt = obj.dbCnvrsCnt;
    }

    /**
     * Default Constructor (밖에서 사용못하게 private)
     */
    private ViewClickVo() {
        super();
    }

    /**
     * Getter, Setter
     */
    public int getViewcnt1() {
        return viewcnt1;
    }

    public void setViewcnt1(int viewcnt1) {
        this.viewcnt1 = viewcnt1;
    }

    public int getViewcnt2() {
        return viewcnt2;
    }

    public void setViewcnt2(int viewcnt2) {
        this.viewcnt2 = viewcnt2;
    }

    public int getViewcnt3() {
        return viewcnt3;
    }

    public void setViewcnt3(int viewcnt3) {
        this.viewcnt3 = viewcnt3;
    }

    public int getClickcnt() {
        return clickcnt;
    }

    public void setClickcnt(int clickcnt) {
        this.clickcnt = clickcnt;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public int getAvalCallTime() {
        return avalCallTime;
    }

    public void setAvalCallTime(int avalCallTime) {
        this.avalCallTime = avalCallTime;
    }

    public int getDbCnvrsCnt() {
        return dbCnvrsCnt;
    }

    public void setDbCnvrsCnt(int dbCnvrsCnt) {
        this.dbCnvrsCnt = dbCnvrsCnt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
