package com.mobon.billing.dump.domainmodel.abtest.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

/**
 * @FileName : ABComStatsKey.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_COM_STATS 도메인 Key
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class ABComStatsKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7919859565956761152L;

	@Column(name = "STATS_DTTM")
    private int statsDttm;

    @Column(name = "PLTFOM_TP_CODE")
    private String pltfomTpCode;

    @Column(name = "ADVRTS_PRDT_CODE")
    private String advrtsPrdtCode;

    @Column(name = "ADVRTS_TP_CODE")
    private String advrtsTpCode;

    @Column(name = "AB_TEST_TY")
    private String abTestTy;
    
    @Column(name = "ITL_TP_CODE")
    private String itlTpCode;

}
