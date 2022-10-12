package com.mobon.billing.dump.domainmodel.point;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnTransformer;

import com.mobon.billing.dump.domainmodel.point.key.PointDataStatsKey;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor(access=AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@Entity(name = "MOB_POINT_DATA_STATS")
public class PointDataStats {

	@EmbeddedId
	private PointDataStatsKey id;

	@Column(name="ADVRTS_AMT")
	private BigDecimal point;
	

	@Builder
	private PointDataStats(PointDataStatsKey id,
			BigDecimal point) {
		this.id = id;
		this.point = point;

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
