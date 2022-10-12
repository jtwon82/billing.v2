package com.mobon.billing.dump.domainmodel.frequency.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @FileName : FreqSdkDayStatsKey.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 4. 29.
 * @Author dkchoi
 * @Comment : FREQ_DAY_STATS 도메인 Key
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class FreqSdkDayStatsKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7919859565956761152L;

	@Column(name = "STATS_DTTM")
    private int statsDttm;

    @Column(name = "ADVRTS_PRDT_CODE")
    private String advrtsPrdtCode;

    @Column(name = "ADVRTS_TP_CODE")
    private String advrtsTpCode;

}
