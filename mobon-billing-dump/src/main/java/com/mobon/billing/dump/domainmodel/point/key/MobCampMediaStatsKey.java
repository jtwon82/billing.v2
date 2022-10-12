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
public class MobCampMediaStatsKey implements Serializable{

	private static final long serialVersionUID = 3501784304377984915L;
	
	@Column(name = "STATS_DTTM")
	private int statsDttm;
	
	@Column(name = "SITE_CODE")
	private String siteCode;
	
	@Column(name = "MEDIA_ID")
	private String mediaId;
	
	@Builder
	private MobCampMediaStatsKey(int statsDttm , String siteCode, String mediaId) {
		this.statsDttm = statsDttm;
		this.siteCode = siteCode;
		this.mediaId = mediaId;
	}
}
