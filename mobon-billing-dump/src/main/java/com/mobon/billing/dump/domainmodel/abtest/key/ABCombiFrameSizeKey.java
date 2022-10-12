package com.mobon.billing.dump.domainmodel.abtest.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

/**
 * @FileName : ABCombiFrameSizeKey.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 7. 2. 
 * @Author dkchoi
 * @Comment : AB_COMBI_FRAME_SIZE 도메인 Key
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class ABCombiFrameSizeKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2709836319193470848L;

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
    
    @Column(name = "BNR_CODE")
    private String bnrCode;
    
    @Column(name = "PAR_TP_CODE")
    private String parTpCode;
    
    @Column(name = "FRME_TYPE")
    private String frmeType;

}
