package com.mobon.billing.dump.domainmodel.abtest;

import com.mobon.billing.dump.domainmodel.abtest.key.ABComStatsKey;
import com.mobon.billing.dump.domainmodel.abtest.values.ABTestValues;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @FileName : ABComStats.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_COM_STATS 도메인
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@ToString
@Entity(name = "AB_COM_STATS")
public class ABComStats extends ABTestValues {

    @Getter
    @Id
    @EmbeddedId
    private ABComStatsKey id;

    @Builder
    private ABComStats(ABComStatsKey id
    		        , int totEprsCnt
    		        , int parEprsCnt
    		        , int clickCnt
    		        , int totOrderCnt
    		        , int totOrderAmt
    		        , int sessOrderCnt
    		        , int sessOrderAmt
    		        , int directOrderCnt
    		        , int directOrderAmt
    		        , BigDecimal advrtsAmt
    		        , BigDecimal mediaPymntAmt
    		        , String regUserId
    		        , LocalDateTime regDttm
    		        , String altUserId
    		        , LocalDateTime altDttm) {

        this.id = id;
        this.totEprsCnt = totEprsCnt;
        this.parEprsCnt = parEprsCnt;
        this.clickCnt = clickCnt;
        this.advrtsAmt = advrtsAmt;
        this.mediaPymntAmt = mediaPymntAmt;
        this.totOrderCnt = totOrderCnt;
        this.totOrderAmt = totOrderAmt;
        this.sessOrderCnt = sessOrderCnt;
        this.sessOrderAmt = sessOrderAmt;
        this.directOrderCnt = directOrderCnt;
        this.directOrderAmt = directOrderAmt;
        this.regUserId = regUserId;
        this.regDttm = regDttm;
        this.altUserId = altUserId;
        this.altDttm = altDttm;
    }

}
