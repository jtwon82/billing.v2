package com.adgather.user.inclinations.status;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;

/**
 * 변환 여부 상태 관리(request 마다 개별 관리)
 * @author yhlim
 *
 */
public class RefactStatus {
	/** 테스트 설정 상태**/
	private Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
	
	/** 현 소비자 테스트 설정된 타입 (테스트 케이스, 케스트 타입)**/
	private Map<String, String> testTypes = new HashMap<String, String>();
	
	private String device;
	private String bChangedDomain;

	/** 기본설정 **********************************************************************************/
	public boolean isRefacting(String refactName) {
		if(StringUtils.isEmpty(refactName))		return false;
		
		return BooleanUtils.isTrue(statusMap.get(refactName));
	}
	
	public void setRefacting(String auId) {
		statusMap.put(CookieDefRepository.SHOP_LOG, CIFunctionController.isRefactingShops());
		statusMap.put(CookieDefRepository.IC_KL, CIFunctionController.isRefactingInctKl());
		statusMap.put(CookieDefRepository.IC_HU, CIFunctionController.isRefactingInctHu());
		statusMap.put(CookieDefRepository.IC_UM, CIFunctionController.isRefactingInctUm());
		statusMap.put(CookieDefRepository.AGE, CIFunctionController.isRefactingAge());
		statusMap.put(CookieDefRepository.GENDER, CIFunctionController.isRefactingGender());
		//기터 설정 추가
	}

	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getbChangedDomain() {
		return bChangedDomain;
	}
	public void setbChangedDomain(String bChangedDomain) {
		this.bChangedDomain = bChangedDomain;
	}

	/** 개별설정 **********************************************************************************/
	/** AB테스트 상태 적용 **/
	public String getTestType(String testCase) {
		if(testCase == null)	return null;
		
		return testTypes.get(testCase);
	}
	
	private boolean checkRefactingKl(String auId) {
		return CIFunctionController.isRefactingInctKl();
		/*
		boolean bRefacting = false;
		
		if(CIFunctionController.isRefactingInctKl()) {
			
			if(InctKlABTest.isActive()) {			//AB테스트 인 경우
				String tempTestTeyp = InctKlABTest.selectType(auId);	// 현재의 AB 테스트 적용 타입 설정
				if(InctKlABTest.TYPE_A.equals(tempTestTeyp)) {
					bRefacting = false;					//AB테스트 기존
					testTypes.put(InctKlABTest.TEST_NAME, InctKlABTest.TYPE_A);
				} else if (InctKlABTest.TYPE_B.equals(tempTestTeyp)) {
					bRefacting = true;					//AB테스트 신규
					testTypes.put(InctKlABTest.TEST_NAME, InctKlABTest.TYPE_B);
				} else {
					bRefacting = false;					//AB테스트 그외(변환하지 않음)
				} 
				
			} else {									//AB테스트가 아닌 경우
				bRefacting = true;
			}
		} else {
			bRefacting = false;
		}
		return bRefacting;
		*/
	}
	
	private boolean checkRefactingHu(String auId) {
		return CIFunctionController.isRefactingInctHu();
		
		/*HU 변환 ab테스트 제외(개수 ab테스트로 이용)
		boolean bRefacting = false;
		
		if(CIFunctionController.isRefactingInctHu()) {
			
			if(InctHuABTest.isActive()) {			//AB테스트 인 경우
				String tempTestTeyp = InctHuABTest.selectType(auId);	// 현재의 AB 테스트 적용 타입 설정
				if(InctHuABTest.TYPE_A.equals(tempTestTeyp)) {
					bRefacting = false;					//AB테스트 기존
					testTypes.put(InctHuABTest.TEST_NAME, InctHuABTest.TYPE_A);
				} else if (InctHuABTest.TYPE_B.equals(tempTestTeyp)) {
					bRefacting = true;					//AB테스트 신규
					testTypes.put(InctHuABTest.TEST_NAME, InctHuABTest.TYPE_B);
				} else {
					bRefacting = false;					//AB테스트 그외(변환하지 않음)
				} 
				
			} else {									//AB테스트가 아닌 경우
				bRefacting = true;
			}
		} else {
			bRefacting = false;
		}
		return bRefacting;
		*/
	}

	private boolean checkRefactingUm(String auId) {
		return CIFunctionController.isRefactingInctUm();
		/*UM 변환 ab테스트 제외(개수 ab테스트로 이용)
		boolean bRefacting = false;
		
		if(CIFunctionController.isRefactingInctUm()) {
			
			if(InctUmABTest.isActive()) {			//AB테스트 인 경우
				String tempTestTeyp = InctUmABTest.selectType(auId);	// 현재의 AB 테스트 적용 타입 설정
				if(InctUmABTest.TYPE_A.equals(tempTestTeyp)) {
					bRefacting = false;					//AB테스트 기존
					testTypes.put(InctUmABTest.TEST_NAME, InctUmABTest.TYPE_A);
				} else if (InctUmABTest.TYPE_B.equals(tempTestTeyp)) {
					bRefacting = true;					//AB테스트 신규
					testTypes.put(InctUmABTest.TEST_NAME, InctUmABTest.TYPE_B);
				} else {
					bRefacting = false;					//AB테스트 그외(변환하지 않음)
				} 
				
			} else {									//AB테스트가 아닌 경우
				bRefacting = true;
			}
		} else {
			bRefacting = false;
		}
		return bRefacting;
		*/
	}
	
}
