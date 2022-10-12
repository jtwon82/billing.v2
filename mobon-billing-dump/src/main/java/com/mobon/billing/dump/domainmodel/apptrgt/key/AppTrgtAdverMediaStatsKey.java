package com.mobon.billing.dump.domainmodel.apptrgt.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @FileName : 
 * @Project : mobon-billing-dump
 * @Date :  
 * @Author 
 * @Comment : APP_TRGT_ADVER_MEDIA_STATS 도메인 Key
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class AppTrgtAdverMediaStatsKey implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3501784304377984915L;

	@Column(name = "STATS_DTTM")
    private int statsDttm;

    @Column(name = "PLTFOM_TP_CODE")
    private String pltfomTpCode;

    @Column(name = "ADVRTS_PRDT_CODE")
    private String advrtsPrdtCode;

    @Column(name = "ADVRTS_TP_CODE")
    private String advrtsTpCode;

    
    @Column(name = "ADVER_ID")
    private String adverId;

    @Column(name = "MEDIA_SCRIPT_NO")
    private int mediaScriptNo;

    @Builder
    private AppTrgtAdverMediaStatsKey(int statsDttm, String pltfomTpCode, String advrtsPrdtCode
    		, String advrtsTpCode, String adverId, int mediaScriptNo) {
        this.statsDttm = statsDttm;
        this.pltfomTpCode = pltfomTpCode;
        this.advrtsPrdtCode = advrtsPrdtCode;
        this.advrtsTpCode = advrtsTpCode;
        this.adverId = adverId;
        this.mediaScriptNo = mediaScriptNo;
    }
}
