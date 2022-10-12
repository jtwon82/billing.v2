package com.mobon.billing.dump.domainmodel.apptrgt;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mobon.billing.dump.domainmodel.apptrgt.key.AppTrgtAdverMediaStatsKey;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @FileName : 
 * @Project : mobon-billing-dump
 * @Date :  
 * @Author 
 * @Comment : APP_TRGT_ADVER_MEDIA_STATS 도메인
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@Entity(name = "APP_TRGT_ADVER_MEDIA_STATS")
public class AppTrgtAdverMediaStats {

    @Id
    @EmbeddedId
    private AppTrgtAdverMediaStatsKey id;
    
    @Column(name = "TOT_EPRS_CNT")
    @ColumnTransformer(write="TOT_EPRS_CNT + ?")
    private int totEprsCnt;

    @Column(name = "PAR_EPRS_CNT")
    @ColumnTransformer(write="PAR_EPRS_CNT + ?")
    private int parEprsCnt;

    @Column(name = "CLICK_CNT")
    @ColumnTransformer(write="CLICK_CNT + ?")
    private int clickCnt;

    @Column(name = "ADVRTS_AMT")
    @ColumnTransformer(write="ADVRTS_AMT + ?")
    private BigDecimal advrtesAmt;
    
    @Column(name = "DPLK_AMT")
    @ColumnTransformer(write="DPLK_AMT + ?")
    private BigDecimal dplkAmt;
    
    @Column(name = "REG_USER_ID")
    private String regUserId;

    @CreationTimestamp
    @Column(name = "REG_DTTM", updatable = false)
    private LocalDateTime regDttm;

    @Column(name = "ALT_USER_ID")
    private String altUserId;

    @UpdateTimestamp
    @Column(name = "ALT_DTTM", updatable = true)
    private LocalDateTime altDttm;
    
    @Builder
    private AppTrgtAdverMediaStats(AppTrgtAdverMediaStatsKey id
    		        , int totEprsCnt
    		        , int parEprsCnt
    		        , int clickCnt
    		        , BigDecimal advrtesAmt
    		        , BigDecimal mediaPymntAmt
    		        , BigDecimal dplkAmt
    		        , String regUserId
    		        , LocalDateTime regDttm
    		        , String altUserId
    		        , LocalDateTime altDttm) {
    	
        this.id = id;
        this.totEprsCnt = totEprsCnt;
        this.parEprsCnt = parEprsCnt;
        this.clickCnt = clickCnt;
        this.advrtesAmt = advrtesAmt;
        this.dplkAmt = dplkAmt;
        this.regUserId = regUserId;
        this.regDttm = regDttm;
        this.altUserId = altUserId;
        this.altDttm = altDttm;
    }
    
    public void addAdvrtesAmt(BigDecimal advrtesAmt) {
        this.advrtesAmt = this.advrtesAmt.add(advrtesAmt);
    }
    
    public void addDplkAmt(BigDecimal dplkAmt) {
        this.dplkAmt = this.dplkAmt.add(dplkAmt);
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
}
