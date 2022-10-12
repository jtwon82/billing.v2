package com.mobon.billing.dump.domainmodel.point.key;

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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class PointDataStatsKey implements Serializable{

	private static final long serialVersionUID = 3501784304377984915L;
		
	@Column(name ="STATS_DTTM")
	private Integer statsDttm;
	
	@Column(name ="SITE_CODE", unique = true)
	private String siteCode;

	@Column(name ="MEDIA_ID", unique = true)
	private String mediaId;

	@Builder
	private PointDataStatsKey(int statsDttm, String siteCode, String mediaId) {
		this.statsDttm = statsDttm;
		this.siteCode = siteCode;
		this.mediaId = mediaId;		
	}
}
