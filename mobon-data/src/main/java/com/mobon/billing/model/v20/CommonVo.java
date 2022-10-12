package com.mobon.billing.model.v20;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.adgather.constants.G;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonVo implements Serializable {

    // key 관련 데이터
    private String dumpType = "";           // 처리타입
    private String className = "";          // 객체타입
    private String sendDate = "";           // 전송일
    private String yyyymmdd = "";           // 통계일
    private String hh = "";                 // 통계시간
    private String auId = "";               // 사용자 고유값
    private String ip = "";                 // 사용자 IP
    private String platform = "";           // 플랫폼
    private String product = "";            // 광고 노출형태
    private String adGubun = "";            // 광고 타게팅기법
    private String subadgubun = "";         // sub 타게팅기법
    private String advertiserId = "";       // 광고주 ID
    private String siteCode = "";           // 사이트코드
    private String ctgrSeq = "";            // 카테고리 번호 (= MOB_CTGR_INFO.CTGR_SEQ)
    private String kpiNo = "";              // kpiNo
    private String scriptUserId = "";       // 매체 ID
    private String mediaTpCode = "";        // 매체타입
    private String scriptNo = "";           // 매체코드
    private String scriptHirnkNo = "";      // 부모지면 매체코드
    private String type = "";               // 노출(V), 클릭(C)
    private String interlock = "01";		// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
    private String svcTpCode = "";          // 서비스타입
    private String chrgTpCode = "";         // 과금타입
    private String noExposureYN = "";       // 미노출여부 (Y:미노출, N:정상노출)
    private String osCode = "";             // 운영체제코드 (= OS_TP_CODE)
    private String browserCode = "";        // 브라우저코드 (= BROWSER_TP_CODE)
    private String deviceCode = "";         // 디바이스코드 (= DEVICE_TP_CODE)
    private String browserCodeVersion = ""; // 브라우저버전
    private int retryCnt = 0;               // 재처리 횟수
    private boolean targetYn = false;       // 상품 타겟팅 여부

    public int increaseRetryCnt() {
        return ++retryCnt;
    }

    public String getPlatformCode() {
        try {
            return G.convertPLATFORM_CODE(this.getPlatform());
        } catch (Exception e) {
            return "";
        }
    }

    public String getProductCode() {
        try {
            return G.convertPRDT_CODE(this.getProduct());
        } catch (Exception e) {
            return "";
        }
    }

    public String getAdGubunCode() {
        try {
            return G.convertTP_CODE(this.getAdGubun());
        } catch (Exception e) {
            return "";
        }
    }

    public String getSubAdGubunCode() {
        try {
            return G.convertSUBADGUBUN_CODE(this.getSubadgubun());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 객체 깊은 복사 생성자
     */
    public CommonVo(CommonVo obj) {
        this.dumpType = obj.dumpType;
        this.className = obj.className;
        this.sendDate = obj.sendDate;
        this.yyyymmdd = obj.yyyymmdd;
        this.hh = obj.hh;
        this.auId = obj.auId;
        this.ip = obj.ip;
        this.platform = obj.platform;
        this.product = obj.product;
        this.adGubun = obj.adGubun;
        this.subadgubun = obj.subadgubun;
        this.advertiserId = obj.advertiserId;
        this.siteCode = obj.siteCode;
        this.ctgrSeq = obj.ctgrSeq;
        this.kpiNo = obj.kpiNo;
        this.scriptUserId = obj.scriptUserId;
        this.mediaTpCode = obj.mediaTpCode;
        this.scriptNo = obj.scriptNo;
        this.scriptHirnkNo = obj.scriptHirnkNo;
        this.type = obj.type;
        this.interlock = obj.interlock;
        this.svcTpCode = obj.svcTpCode;
        this.chrgTpCode = obj.chrgTpCode;
        this.noExposureYN = obj.noExposureYN;
        this.osCode = obj.osCode;
        this.browserCode = obj.browserCode;
        this.deviceCode = obj.deviceCode;
        this.browserCodeVersion = obj.browserCodeVersion;
        this.retryCnt = obj.retryCnt;
        this.targetYn = obj.targetYn;
    }

    /**
     * Default Constructor
     */
    public CommonVo() {
    }

    /**
     * Getter, Setter
     */

    public String getDumpType() {
        return dumpType;
    }

    public void setDumpType(String dumpType) {
        this.dumpType = dumpType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAdGubun() {
        return adGubun;
    }

    public void setAdGubun(String adGubun) {
        this.adGubun = adGubun;
    }

    public String getSubadgubun() {
        return subadgubun;
    }

    public void setSubadgubun(String subadgubun) {
        this.subadgubun = subadgubun;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCtgrSeq() {
        return ctgrSeq;
    }

    public void setCtgrSeq(String ctgrSeq) {
        this.ctgrSeq = ctgrSeq;
    }

    public String getKpiNo() {
        return kpiNo;
    }

    public void setKpiNo(String kpiNo) {
        this.kpiNo = kpiNo;
    }

    public String getScriptUserId() {
        return scriptUserId;
    }

    public void setScriptUserId(String scriptUserId) {
        this.scriptUserId = scriptUserId;
    }

    public String getMediaTpCode() {
        return mediaTpCode;
    }

    public void setMediaTpCode(String mediaTpCode) {
        this.mediaTpCode = mediaTpCode;
    }

    public String getScriptNo() {
        return scriptNo;
    }

    public void setScriptNo(String scriptNo) {
        this.scriptNo = scriptNo;
    }

    public String getScriptHirnkNo() {
        return scriptHirnkNo;
    }

    public void setScriptHirnkNo(String scriptHirnkNo) {
        this.scriptHirnkNo = scriptHirnkNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterlock() {
        return interlock;
    }

    public void setInterlock(String interlock) {
        this.interlock = interlock;
    }

    public String getSvcTpCode() {
        return svcTpCode;
    }

    public void setSvcTpCode(String svcTpCode) {
        this.svcTpCode = svcTpCode;
    }

    public String getChrgTpCode() {
        return chrgTpCode;
    }

    public void setChrgTpCode(String chrgTpCode) {
        this.chrgTpCode = chrgTpCode;
    }

    public String getNoExposureYN() {
        return noExposureYN;
    }

    public void setNoExposureYN(String noExposureYN) {
        this.noExposureYN = noExposureYN;
    }

    public String getOsCode() {
        return osCode;
    }

    public void setOsCode(String osCode) {
        this.osCode = osCode;
    }

    public String getBrowserCode() {
        return browserCode;
    }

    public void setBrowserCode(String browserCode) {
        this.browserCode = browserCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBrowserCodeVersion() {
        return browserCodeVersion;
    }

    public void setBrowserCodeVersion(String browserCodeVersion) {
        this.browserCodeVersion = browserCodeVersion;
    }

    public int getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
    }

    public boolean isTargetYn() {
        return targetYn;
    }

    public void setTargetYn(boolean targetYn) {
        this.targetYn = targetYn;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
