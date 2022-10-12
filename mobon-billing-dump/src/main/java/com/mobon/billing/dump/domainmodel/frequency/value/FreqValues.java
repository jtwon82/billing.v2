package com.mobon.billing.dump.domainmodel.frequency.value;

import com.mobon.billing.dump.utils.AddableJSONObeject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @FileName : FreqValues.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 22.
 * @Author dkchoi
 * @Comment : FREQ 도메인 Values
 */

@MappedSuperclass
@Getter
@Setter
public abstract class FreqValues {

    @Column(name = "TOT_EPRS_CNT_FREQ", columnDefinition = "MEDIUMTEXT")
    protected String totEprsCntFreq;

    @Column(name = "PAR_EPRS_CNT_FREQ", columnDefinition = "MEDIUMTEXT")
    protected String parEprsCntFreq;

    @Column(name = "CLICK_CNT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String clickCntFreq;

    @Column(name = "ADVRTS_AMT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String advrtsAmtFreq;

    @Column(name = "MEDIA_PYMNT_AMT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String mediaPymntAmtFreq;

    @Column(name = "TOT_ORDER_CNT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String totOrderCntFreq;

    @Column(name = "TOT_ORDER_AMT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String totOrderAmtFreq;

    @Column(name = "SESS_ORDER_CNT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String sessOrderCntFreq;

    @Column(name = "SESS_ORDER_AMT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String sessOrderAmtFreq;

    @Column(name = "DIRECT_ORDER_CNT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String directOrderCntFreq;

    @Column(name = "DIRECT_ORDER_AMT_FREQ", columnDefinition  = "MEDIUMTEXT")
    protected String directOrderAmtFreq;

    @Setter
    @Column(name = "REG_USER_ID")
    protected String regUserId;

    @CreationTimestamp
    @Column(name = "REG_DTTM", updatable = false)
    protected LocalDateTime regDttm;

    @Column(name = "ALT_USER_ID")
    protected String altUserId;

    @UpdateTimestamp
    @Column(name = "ALT_DTTM", updatable = true)
    protected LocalDateTime altDttm;

    public void addAdvrtesAmtFreq(String key, float val) {
        advrtsAmtFreq = AddableJSONObeject.addFloat(key,val,advrtsAmtFreq);
    }

    public void addAdvrtesAmtFreq(String json) {
        advrtsAmtFreq = AddableJSONObeject.addAllFloat(advrtsAmtFreq,json);
    }

    public void addMediaPymntAmtFreq(String key, float val) {
        mediaPymntAmtFreq = AddableJSONObeject.addFloat(key,val,mediaPymntAmtFreq);
    }

    public void addMediaPymntAmtFreq(String json) {
        mediaPymntAmtFreq = AddableJSONObeject.addAllFloat(mediaPymntAmtFreq,json);
    }

    public void addClickCntFreq(String key, int val) {
        clickCntFreq = AddableJSONObeject.addInt(key,val,clickCntFreq);
    }

    public void addClickCntFreq(String json) {
        clickCntFreq = AddableJSONObeject.addAllInt(clickCntFreq,json);
    }

    public void addTotEprsCntFreq(String key, int val) {
        totEprsCntFreq = AddableJSONObeject.addInt(key,val,totEprsCntFreq);
    }

    public void addTotEprsCntFreq(String json) {
        totEprsCntFreq = AddableJSONObeject.addAllInt(totEprsCntFreq,json);
    }

    public void addParEprsCntFreq(String key, int val) {
        parEprsCntFreq = AddableJSONObeject.addInt(key,val,parEprsCntFreq);
    }

    public void addParEprsCntFreq(String json) {
        parEprsCntFreq = AddableJSONObeject.addAllInt(parEprsCntFreq,json);
    }

    public void addTotOrderCntFreq(String key, int val) {
        totOrderCntFreq = AddableJSONObeject.addInt(key,val,totOrderCntFreq);
    }

    public void addTotOrderCntFreq(String json) {
        totOrderCntFreq = AddableJSONObeject.addAllInt(totOrderCntFreq,json);
    }

    public void addTotOrderAmtFreq(String key, int val) {
        totOrderAmtFreq = AddableJSONObeject.addInt(key,val,totOrderAmtFreq);
    }

    public void addTotOrderAmtFreq(String json) {
        totOrderAmtFreq = AddableJSONObeject.addAllInt(totOrderAmtFreq,json);
    }

    public void addSessOrderCntFreq(String key, int val) {
        sessOrderCntFreq = AddableJSONObeject.addInt(key,val,sessOrderCntFreq);
    }

    public void addSessOrderCntFreq(String json) {
        sessOrderCntFreq = AddableJSONObeject.addAllInt(sessOrderCntFreq,json);
    }

    public void addSessOrderAmtFreq(String key, int val) {
        sessOrderAmtFreq = AddableJSONObeject.addInt(key,val,sessOrderAmtFreq);
    }

    public void addSessOrderAmtFreq(String json) {
        sessOrderAmtFreq = AddableJSONObeject.addAllInt(sessOrderAmtFreq,json);
    }

    public void addDirectOrderCntFreq(String json) {
        directOrderCntFreq = AddableJSONObeject.addAllInt(directOrderCntFreq,json);
    }

    public void addDirectOrderAmtFreq(String key, int val) {
        directOrderAmtFreq = AddableJSONObeject.addInt(key,val,directOrderAmtFreq);
    }

    public void addDirectOrderAmtFreq(String json) {
        directOrderAmtFreq = AddableJSONObeject.addAllInt(directOrderAmtFreq,json);
    }

}
