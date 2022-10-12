package com.adgather.user.auth;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * AuId 생성 결과 값
 * @date 2017. 7. 12.
 * @param 
 * @exception
 * @see
*/
public class UserAuthIDResult {
	private String nowAuId;		// 기존 auid (쿠키나 파라미터 값이 된다. 초기에 없을 경우 null이 된다.)
	private String newAuId;		// 신규 auid
	private List<Map<String, String>> abTest = null;
	
	public UserAuthIDResult() {}
	public UserAuthIDResult(String auId) {
		this.nowAuId = auId;
		this.newAuId = auId;
	}
	public UserAuthIDResult(String nowAuId, String newAuId) {
		this.nowAuId = nowAuId;
		this.newAuId = newAuId;
	}

	public String getNowAuId() {
		return nowAuId;
	}
	public void setNowAuId(String nowAuId) {
		this.nowAuId = nowAuId;
	}
	public String getNewAuId() {
		return newAuId;
	}
	public void setNewAuId(String newAuId) {
		this.newAuId = newAuId;
	}
	
	// 현재 요청에 대한 auid(newAuId이용)
	public String getAuId() {
		return this.newAuId;
	}
	
	public void setABTest(List abTest) {
		this.abTest = abTest;
	}
	
	public List<Map<String, String>> getABTest() {
		return this.abTest;
	}
	
	public boolean isReissued() {
		return StringUtils.isNotEmpty(newAuId) && !newAuId.equals(nowAuId);
	}
	
	
	public static UserAuthIDResult valueOf(Object obj) {
		if(obj == null)		return null;
		if(!(obj instanceof UserAuthIDResult))		return null;
		
		return (UserAuthIDResult)obj;
	}
}
