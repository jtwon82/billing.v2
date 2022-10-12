package com.mobon.billing.dump.domainmodel.frequency;

import com.mobon.billing.dump.domainmodel.frequency.key.FreqSdkDayStatsKey;
import com.mobon.billing.dump.domainmodel.frequency.value.FreqValues;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @FileName : FreqSdkStats.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 4. 29.
 * @Author dkchoi
 * @Comment : FREQ_SDK_DAY_STATS 도메인
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
@ToString
@Entity(name = "FREQ_SDK_DAY_STATS")
public class FreqSdkDayStats extends FreqValues {

    @Getter
    @Id
    @EmbeddedId
    private FreqSdkDayStatsKey id;

    @Builder
    private FreqSdkDayStats(FreqSdkDayStatsKey id
    		        , String totEprsCntFreq
    		        , String parEprsCntFreq
    		        , String clickCntFreq
    		        , String totOrderCntFreq
    		        , String totOrderAmtFreq
    		        , String sessOrderCntFreq
    		        , String sessOrderAmtFreq
    		        , String directOrderCntFreq
    		        , String directOrderAmtFreq
    		        , String advrtsAmtFreq
    		        , String mediaPymntAmtFreq
    		        , String regUserId
    		        , LocalDateTime regDttm
    		        , String altUserId
    		        , LocalDateTime altDttm) {
    	
        this.id = id;
        this.totEprsCntFreq = totEprsCntFreq;
        this.parEprsCntFreq = parEprsCntFreq;
        this.clickCntFreq = clickCntFreq;
        this.advrtsAmtFreq = advrtsAmtFreq;
        this.mediaPymntAmtFreq = mediaPymntAmtFreq;
        this.totOrderCntFreq = totOrderCntFreq;
        this.totOrderAmtFreq = totOrderAmtFreq;
        this.sessOrderCntFreq = sessOrderCntFreq;
        this.sessOrderAmtFreq = sessOrderAmtFreq;
        this.directOrderCntFreq = directOrderCntFreq;
        this.directOrderAmtFreq = directOrderAmtFreq;
        this.regUserId = regUserId;
        this.regDttm = regDttm;
        this.altUserId = altUserId;
        this.altDttm = altDttm;
    }

}
