package com.mobon.billing.dump.domainmodel.point;

import java.math.BigDecimal;


import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
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
public class MobCampMediaStats {
	
	@Id
	@EmbeddedId
	private MobCampMediaStatsKey id;
	
	@Column(name = "ADVRTS_AMT")
	private BigDecimal point;
	
	
	
	public String getSiteKey() {
		StringBuffer key = new StringBuffer().append(id.getStatsDttm()).append("_").append(id.getSiteCode());
		return key.toString();
	}

	public void addPoint(BigDecimal point2) {
		this.point = this.point.add(point2);
		
	}

	public String getMediaKey() {
		StringBuffer key = new StringBuffer()
				.append(id.getStatsDttm()).append("_")
				.append(id.getSiteCode()).append("_")
				.append(id.getMediaId());
		
		return key.toString();
	}	
	
	
}
