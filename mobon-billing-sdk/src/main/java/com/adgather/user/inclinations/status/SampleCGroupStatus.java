package com.adgather.user.inclinations.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
//import com.adgather.abtest.SampleCGroupTest;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.abtest.InfoAbTest;

/**
 * 변환 여부 상태 관리(request 마다 개별 관리)
 * @author yhlim
 *
 */
public class SampleCGroupStatus {
	/** 테스트 설정 상태**/
	//private Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
	
	/** 현 소비자 테스트 설정된 타입 (테스트 케이스, 케스트 타입)**/
	private Map<String, String> testTypes = new HashMap<String, String>();
	
	/** 램덤 생성자 **/
	private static Random random = new Random();
	

	/** 기본설정 **********************************************************************************/
	public void setSampleCGroupABTest(ConsumerInclinations inclination) {
//		InfoAbTest sampleCGroupABTest = checkType(inclination, SampleCGroupTest.TEST_NAME, SampleCGroupTest.SAMPLE_C, SampleCGroupTest.SAMPLE_C[0]);
//		if(sampleCGroupABTest != null) {
//			testTypes.put(sampleCGroupABTest.getTesting(), sampleCGroupABTest.getType());
//		}
	}
		

	/** 개별설정 **********************************************************************************/
	/** AB테스트 상태 적용 **/
	public String getTestType(String testCase) {
		if(testCase == null)	return null;
		
		return testTypes.get(testCase);
	}
	
	private static InfoAbTest checkType(ConsumerInclinations inclination, String testing, String[] types, String defaultType) {
		InfoAbTest test = null;
		
//		if(SampleCGroupTest.isActive()) {			//AB테스트 인 경우
//			List<?> abtestTypeList = inclination.getCookie(CookieDefRepository.ABTEST_TYPE);
//			
//			if(abtestTypeList != null) {
//				for(Object obj : abtestTypeList) {
//					if(!(obj instanceof InfoAbTest))	continue;
//					
//					InfoAbTest temp = (InfoAbTest)obj;
//					if(testing.equals(temp.getTesting())) {
//						for(String abtest : types) {
//							if(abtest.equals(temp.getType())) {
//								test = temp;
//								break;
//							}
//						}
//					}
//				}
//			}
//			
//			if(test == null) {
//				String type = getRandomType(types, defaultType);
//				
//				InfoAbTest temp = new InfoAbTest();
//				temp.setTesting(testing);
//				temp.setType(type);
//				test = temp;
//				
//				inclination.addCookie(CookieDefRepository.ABTEST_TYPE, temp, false);
//			}
//		}
		
		return test;
	}
	private static String getRandomType(String[] types, String defaultType) {
		if(types == null || types.length == 0)
			return defaultType;
		
		int idx = random.nextInt(types.length);
		return types[idx];
	}
	
}