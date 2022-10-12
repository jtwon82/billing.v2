package com.mobon.billing.dump.domainmodel.point;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.mobon.billing.dump.domainmodel.point.key.MobCampMediaStatsKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@Entity(name = "MOB_CAMP_MEDIA_STATS")
public class MobCampMediaStandardStats {

	@Id
	@EmbeddedId
	private MobCampMediaStatsKey id;
	
	@Column(name = "PLTFOM_TP_CODE")
	private String pltfomTpCode;
	
	@Column(name = "ADVRTS_PRDT_CODE")
	private String advrtsPrdtCode;
	
	@Column(name = "ADVRTS_TP_CODE")
	private String advrtsTpCode;
	
	@Column(name = "MEDIA_SCRIPT_NO")
	private int scriptNo;
	
	@Column(name = "ADVER_ID")
	private String adverId;

	@Column(name = "CLICK_CNT")
	private int clickCnt;
	
	@Column(name = "ADVRTS_AMT")
	private BigDecimal advrtsAmt;

	public String getStandardStatsKey(BigDecimal diffPoint) {
		StringBuffer sb = new StringBuffer()
				.append(id.getStatsDttm()).append("_")
				.append(id.getSiteCode()).append("_")
				.append(id.getMediaId()).append("_")
				.append(this.adverId).append("_")
				.append(scriptNo).append("_")
				.append(diffPoint);
		return sb.toString();
	}
	
	
	
	
}
