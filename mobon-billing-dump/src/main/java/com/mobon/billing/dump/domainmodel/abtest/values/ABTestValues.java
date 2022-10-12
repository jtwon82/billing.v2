package com.mobon.billing.dump.domainmodel.abtest.values;

import lombok.Getter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @FileName : ABTestValues.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 22.
 * @Author dkchoi
 * @Comment : ABTEST 도메인 Values
 */
@MappedSuperclass
@Getter
public abstract class ABTestValues {

    @Column(name = "TOT_EPRS_CNT")
    @ColumnTransformer(write="TOT_EPRS_CNT + ?")
    protected int totEprsCnt;

    @Column(name = "PAR_EPRS_CNT")
    @ColumnTransformer(write="PAR_EPRS_CNT + ?")
    protected int parEprsCnt;

    @Column(name = "CLICK_CNT")
    @ColumnTransformer(write="CLICK_CNT + ?")
    protected int clickCnt;

    @Column(name = "ADVRTS_AMT")
    @ColumnTransformer(write="ADVRTS_AMT + ?")
    protected BigDecimal advrtsAmt;

    @Column(name = "MEDIA_PYMNT_AMT")
    @ColumnTransformer(write="MEDIA_PYMNT_AMT + ?")
    protected BigDecimal mediaPymntAmt;

    @Column(name = "TOT_ORDER_CNT")
    @ColumnTransformer(write="TOT_ORDER_CNT + ?")
    protected int totOrderCnt;

    @Column(name = "TOT_ORDER_AMT")
    @ColumnTransformer(write="TOT_ORDER_AMT + ?")
    protected int totOrderAmt;

    @Column(name = "SESS_ORDER_CNT")
    @ColumnTransformer(write="SESS_ORDER_CNT + ?")
    protected int sessOrderCnt;

    @Column(name = "SESS_ORDER_AMT")
    @ColumnTransformer(write="SESS_ORDER_AMT + ?")
    protected int sessOrderAmt;

    @Column(name = "DIRECT_ORDER_CNT")
    @ColumnTransformer(write="DIRECT_ORDER_CNT + ?")
    protected int directOrderCnt;

    @Column(name = "DIRECT_ORDER_AMT")
    @ColumnTransformer(write="DIRECT_ORDER_AMT + ?")
    protected int directOrderAmt;

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

    public void addAdvrtsAmt(BigDecimal advrtsAmt) {
        this.advrtsAmt = this.advrtsAmt.add(advrtsAmt);
    }
    public void addMediaPymntAmt(BigDecimal mediaPymntAmt) {
        this.mediaPymntAmt = this.mediaPymntAmt.add(mediaPymntAmt);
    }

    public void addClickCnt(int clickCnt) {
        this.clickCnt += clickCnt;
    }

    public void addOneClickCnt() {
        this.clickCnt += 1;
    }

    public void addTotEprsCnt(int totEprsCnt) {
        this.totEprsCnt += totEprsCnt;
    }

    public void addParEprsCnt(int parEprsCnt) {
        this.parEprsCnt += parEprsCnt;
    }

    public void addTotOrderCnt(int totOrderCnt) {
        this.totOrderCnt += totOrderCnt;
    }

    public void addTotOrderAmt(int totOrderAmt) {
        this.totOrderAmt += totOrderAmt;
    }

    public void addSessOrderCnt(int sessOrderCnt) {
        this.sessOrderCnt += sessOrderCnt;
    }

    public void addSessOrderAmt(int sessOrderAmt) {
        this.sessOrderAmt += sessOrderAmt;
    }

    public void addDirectOrderCnt(int directOrderCnt) {
        this.directOrderCnt += directOrderCnt;
    }

    public void addDirectOrderAmt(int directOrderAmt) {
        this.directOrderAmt += directOrderAmt;
    }
    
}
