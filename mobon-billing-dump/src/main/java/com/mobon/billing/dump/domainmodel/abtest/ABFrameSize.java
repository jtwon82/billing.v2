package com.mobon.billing.dump.domainmodel.abtest;

import com.mobon.billing.dump.domainmodel.abtest.key.ABFrameSizeKey;
import com.mobon.billing.dump.domainmodel.abtest.values.ABTestValues;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @FileName : ABFrameSize.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 1. 4.
 * @Author dkchoi
 * @Comment : AB_FRAME_SIZE 도메인
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@ToString
@Entity(name = "AB_FRAME_SIZE")
public class ABFrameSize extends ABTestValues {

    @Getter
    @Id
    @EmbeddedId
    private ABFrameSizeKey id;

    @Builder
    private ABFrameSize(ABFrameSizeKey id
    		        , int totEprsCnt
    		        , int parEprsCnt
    		        , int clickCnt
    		        , BigDecimal advrtsAmt
    		        , BigDecimal mediaPymntAmt
    		        , int totOrderCnt
    		        , int totOrderAmt
    		        , int sessOrderCnt
    		        , int sessOrderAmt
    		        , int directOrderCnt
    		        , int directOrderAmt
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
