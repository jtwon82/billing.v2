package com.mobon.billing.model.v15;

import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketData implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonAlias({"statsDttm"})       private String statsDttm = "";
    @JsonAlias({"auId"})            private String auid = "";
    @JsonAlias({"siteCode"})        private String siteCode = "";
    @JsonAlias({"pcode"})           private String pcode = "";
    @JsonAlias({"platfromTpCode"})  private String platfromTpCode = "";
    @JsonAlias({"cartTpCode"})      private String cartTpCode = "";
    @JsonAlias({"ip"})              private String ip = "";
    @JsonAlias({"adGubun"})         private String adGubun = "";
    @JsonAlias({"clickDttm"})       private String clickDttm = "";
    @JsonAlias({"adverId"})         private String adverId = "";
    @JsonAlias({"mediaId"})         private String mediaId = "";
    @JsonAlias({"sessionSelng2Yn"}) private String sessionSelng2Yn = "";
    @JsonAlias({"pnm"})             private String pnm = "";
    @JsonAlias({"parNo"})           private String parNo = "";
    @JsonAlias({"advrtsPrdtCode"})  private String advrtsPrdtCode = "";
    @JsonAlias({"sessionSelngYn"})  private String sessionSelngYn = "";
    @JsonAlias({"product"})         private String product = "";
    @JsonAlias({"direct"})          private String direct = "";
    private String mediaTpCode = "";


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public String getStatsDttm() {
        return statsDttm;
    }

    public void setStatsDttm(String statsDttm) {
        this.statsDttm = statsDttm;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public String getMediaTpCode() {
        return mediaTpCode;
    }

    public void setMediaTpCode(String mediaTpCode) {
        this.mediaTpCode = mediaTpCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPlatfromTpCode() {
        return platfromTpCode;
    }

    public void setPlatfromTpCode(String platfromTpCode) {
        this.platfromTpCode = platfromTpCode;
    }

    public String getCartTpCode() {
        return cartTpCode;
    }

    public void setCartTpCode(String cartTpCode) {
        this.cartTpCode = cartTpCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAdGubun() {
        return adGubun;
    }

    public void setAdGubun(String adGubun) {
        this.adGubun = adGubun;
    }

    public String getClickDttm() {
        return clickDttm;
    }

    public void setClickDttm(String clickDttm) {
        this.clickDttm = clickDttm;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getSessionSelng2Yn() {
        return sessionSelng2Yn;
    }

    public void setSessionSelng2Yn(String sessionSelng2Yn) {
        this.sessionSelng2Yn = sessionSelng2Yn;
    }

    public String getPnm() {
        return pnm;
    }

    public void setPnm(String pnm) {
        /* 우선 이모지를 String 에서 제외하고
         String 의 길이가 200 (DB 컬럼 사이즈) 이 넘으면
         우선 URL 디코드 수행하고 그래도 길이가 200이 넘거나
         혹은 디코딩 Exception 발생하면 길이를 200 까지 제한해서 세트 */

        String tempPnm = StringUtils.removeEmoji(pnm);

        if (tempPnm.length() >= 200) {
            try {
                String decodePnm = URLDecoder.decode(tempPnm, "UTF-8");

                tempPnm = (decodePnm.length() >= 200) ? decodePnm.substring(0,200) : decodePnm;
            } catch (UnsupportedEncodingException e) {
                tempPnm = tempPnm.substring(0,200);
            }
        }

        this.pnm = tempPnm;
    }

    public String getParNo() {
        return parNo;
    }

    public void setParNo(String parNo) {
        this.parNo = parNo;
    }

    public String getAdvrtsPrdtCode() {
        return advrtsPrdtCode;
    }

    public void setAdvrtsPrdtCode(String advrtsPrdtCode) {
        this.advrtsPrdtCode = advrtsPrdtCode;
    }

    public String getSessionSelngYn() {
        return sessionSelngYn;
    }

    public void setSessionSelngYn(String sessionSelngYn) {
        this.sessionSelngYn = sessionSelngYn;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getAdverId() {
        return adverId;
    }

    public void setAdverId(String adverId) {
        this.adverId = adverId;
    }
}
