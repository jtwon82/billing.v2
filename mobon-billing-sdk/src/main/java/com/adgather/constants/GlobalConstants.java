package com.adgather.constants;

import com.adgather.util.PropertyHandler;

public class GlobalConstants {
	public final static String GUBUN = "_";
	public final static String LOWER_M = "m";		// 모바일의 M 을 의미 (소문자)
	public final static String UPPER_M = "M";		// 모바일의 M 을 의미 (대문자)

	// ********** DB SETTING 시작 ****************
	public final static String DREAMDB = "sqlmap_54.xml";
	public final static String DREAMLOG = "sqlmap_58.xml";
	public final static String BILLING = "sqlmap_80.xml";
	// ********** DB SETTING 시작 ****************

	// ********** 도메인 시작 ****************
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	public final static String WWW = "www.";
	public final static String DOMAIN = PropertyHandler.getProperty("DOMAIN");
	public final static String DREAM_DOMAIN = "dreamsearch.or.kr";
	public final static String MEDIACATEGORY_DOMAIN = "mediacategory.com";
	public final static String WWW_DOMAIN = WWW + DOMAIN;
	public final static String HTML_DIRECTORY = PropertyHandler.getProperty("HTML_DIRECTORY");
	public final static String IMG_DOMAIN = "img.mobon.net";			// 이미지 처리 경로
	public final static String HTTPS_PROXY_DOMAIN ="https://img.en-mobon.com/?src="; // HTTPS 경유 PROXY 도메인 정보
	// ********** 도메인  끝 ****************

	// ********** 외부연동 시작 ****************
	public final static String RECEIVE = "R";					// receive 약어
	public final static String SEND = "S";					// send 약어
	// ********** 외부연동 끝 ****************

	// ********** 파일 경로 시작 ****************
	public final static String IMG_PATH = "/ad/imgfile/";				// 이미지 경로
	public final static String MOBON_LOG_IMG_PATH = "/rtb/images/mobon.jpg";				// 이미지 경로
	// ********** 파일 경로 끝 ****************

	// ********** 광고 서브 구분자 시작 ****************

	// 해당 광고 카테고리 추천 상품으로 채우기 시작
	public final static String SUB_GUBUN_CATE = "CT";

	//부족하면 해당 광고주의 랜덤 카테고리로 가져옴
	public final static String SUB_GUBUN_CATE_RANDOM = "CR";

	// 그래도 부족하면 해당 광고주의 Shop데이터 기준 랜덤 카테고리로 가져옴
	public final static String SUB_GUBUN_SHOP_CATE = "SC";

	// 그래도 부족하면 해당 광고주의 오늘포함 3일 1클릭 이상 데이터 가져옴
	public final static String SUB_GUBUN_LAST_SHOP = "LS";

	// 기존 모듈에서 돌려서 삽입. 엑박이나 부족하게 안나가기 위해
	public final static String SUB_GUBUN_REPEAT = "RP";

	// ********** 광고 서브 구분자 끝 ****************

	// ********** 광고 구분자 시작 ****************
	public final static String SR = "SR";				// 본상품
	public final static String SP = "SP";				// 추천 (일반 추천상품)
	public final static String AD = "AD";				// 베이스
	public final static String UM = "UM";				// 성향 (도메인)
	public final static String ST = "ST";				// 투데이베스트 (어제자 추천상품)
	public final static String CC = "CC";				// 이벤트타겟팅(프로모션)
	public final static String HU = "HU";				// 헤비유저 타겟팅
	public final static String SJ = "SJ";				// 쇼핑입점
	public final static String RM = "RM";				// 본도메인 (자사 타겟팅)
	public final static String RR = "RR";				// RM 리사이클
	public final static String KL = "KL";				// 키워드
	public final static String KM = "KM";				// 키워드매칭_20180607 추가
	public final static String SH = "SH";				// 브랜드박스안 캐시백상품
	public final static String SA = "SA";				// 브랜드박스
	public final static String HB = "HB";				// 하우스배너 (패스백해주는 타사 주소)
	public final static String RC = "RC";				// 리사이클
	public final static String KP = "KP";				// 키워드 상품광고
	public final static String PK = "PK";				// 키워드매칭상품
	public final static String CW = "CW";				// 장바구니 상품광고
	public final static String QA = "QA";				// 퀴즈형배너
	public final static String PB = "PB";				// 프리미엄배너
	public final static String REAL = "REAL";				// 퀴즈형배너
	public final static String CP = "CP";				// 유료쿠폰
	public final static String CB = "CB";				// CPI 배너광고
	public final static String MM = "MM";				// 문맥 타기팅 광고
	public final static String MR = "MR";				// 문맥 리타기팅 광고
	public final static String IB = "IB";				// 유입키워드 매칭
	public final static String IK = "IK";				// 네이티브광고 > 유입키워드
	public final static String MK = "MK";				// 네이티브광고 > 핵심키워드
	public final static String CM = "CM";				// 네이티브광고 > 카테고리 매칭
	public final static String PS = "PS";				// 패스백광고
	public final static String CA = "CA";				// 할인금액쇼콘
	public final static String PE = "PE";				// 할인율쇼콘
	public final static String AU = "AU";				// 오디언스 by sunys
	// ********** 광고 구분자 끝 ****************

	// ********** 타입 구분자 시작 ****************
	public final static String WEB = "web";
	public final static String MOBILE = "mobile";
	public final static String ALL = "all";

	public final static String BANNER = "banner";		// 일반배너를 의미
	public final static String ICO = "ico";				// 아이커버를 의미
	public final static String ICO_BLINK = "";			// 아이커버를 의미 (아이커버의 의 svc_type 컬럼값은 "", 브랜드링크는 "sky")
	public final static String SKY = "sky";				// 브랜드링크를 의미
	public final static String BRAND_BOX = "bb";		// 브랜드 박스를 의미
	public final static String PLAY_LINK_OLD = "pl_old";		// 플레이 링크(구)
	public final static String PLAY_LINK = "pl";		// 플레이 링크
	public final static String PLAY_LINK_MOBILE = "mpl";		// 모바일 플레이 링크

	public final static String NATIVE = "nt";			// 네이티브광고

	public final static String PUSH = "push";				// 상품수집 및 성향 타겟팅 수집을 1분동안 수집하지 않도록함.

	public final static String VIEW = "V";					// 노출
	public final static String CLICK = "C";					// 클릭
	// ********** 타입 구분자 끝 ****************

	public final static String NORMALCHARGE = "normalCharge";	// 웹 노출
	public final static String MOBILECHARGE = "mobileCharge";	// 모바일 노출
	public final static String DRCCHARGE = "drcCharge";			// 클릭
	public final static String SKYCHARGE = "skyCharge";				// 브랜드링크 노출(클릭)
	public final static String ICOCHARGE = "icoCharge";				// 아이커버의 노출(클릭)
	public final static String PLAY_LINK_CHARGE = "plCharge";				// 플레이링크의 노출(클릭)
	public final static String EXTERNALCHARGE = "externalCharge";			// 외부연동
	public final static String EXTERNALBATCH = "externalBatch";				// 외부연동 from batch
	public final static String SHORTCUTCHARGE = "shortcutCharge";			// SHORTCUT
	public final static String SHOPCONCHARGE = "shopconCharge";				// shopcon
	public final static String SHOPLOGCHARGE = "shoplogCharge";				// shoplog
	public final static String SHOPSTATSCHARGE = "shopstatsCharge";			// shopstats
	public final static String CONVERSIONCHARGE = "conversionCharge";		// conversion
	public final static String ACTIONCHARGE = "actionCharge";				// actionCharge


	// *********** 모바일 구분자 시작 *************
	public final static String MBB = "mbb";			// 모바일 브랜드 링크 약자
	public final static String MBA = "mba";			// 모바일 앱 약자
	public final static String MBW = "mbw";			// 모바일 웹 약자
	public final static String MBE = "mbe";			// 모바일 엔딩커버의 약자
	public final static String MCT = "mct";			// 모바일 문맥타기팅의 약자
	public final static String MNT = "mnt";			// 모바일 네이티브의 약자

	public final static String APP = "app";											// adbnApp : 모네드 전용 (사용하는 곳 적음
	public final static String MOBILE_BRANDLINK = "mobile_sky";			// 모바일용 브랜드링크 (사용하는 곳 적음
	// *********** 모바일 구분자 끝 **************

	public final static String NOR = "nor";										// 웹(일반배너)를 의미
	public final static String NCT = "nct";										// 웹(문맥타기팅 일반배너)를 의미
	public final static String PNT = "pnt";										// 웹(문맥타기팅 일반배너)를 의미

	// *********** 쿠키에 대한 제어값 시작 *********

	public final static String BANNER_A = "A";									// 일반배너를 의미

	public final static String BANNER_RC_D = "D";								// 일반배너의 RC를 의미
	public final static String BRAND_RC_F = "F";								// 브랜드링크의 RC를 의미

	public final static String ENDINGPOPUP_M = "M";								// 엔딩팝업을 의미
	public final static String MOBILE_M = "M";									// 모바일을 의미
	public final static String WEB_W = "W";								// 웹을 의미

	public final static String MAN = "M";										// 남자를 의미
	public final static String WOMAN = "W";										// 여자를 의미
	public final static String CW_TARGET = "GHI";								// 장바구니 타겟팅이 되었다는 의미



	/**
	 * 이미지 코드 정리 - 이미지코드 및 igb 가 추가되면 꼭 해당값 넣도록!!!! (igb 에 대한 테이블 정보가 없어서 꼭 필요함)
	 * 플래폼타입_가로길이_세로길이 = "00" 값						// 컬럼명 (Dcode 목록), (igb 목록)
	 * 12, 15, 16 은 추후에 추가하면 해당 기능을 주석 풀어둘 것(디폴트 이미지 작업도 꼭 해야함)
	 */
	public final static String MOBILEIMG_360_50 = "00";			// banner_path1 컬럼명 사용
	public final static String IMGNAME1_250_250 = "01";
	public final static String IMGNAME2_120_600 = "02";
	public final static String IMGNAME3_728_90 = "03";
	public final static String IMGNAME4_300_180 = "04";
	public final static String IMGNAME5_800_1500 = "05";
	public final static String IMGNAME6_EDGE = "06";
	public final static String IMGNAME7_160_300 = "07";
	public final static String IMGNAME8_300_65 = "08";
	public final static String IMGNAME9_850_800 = "09";
	public final static String IMGNAME10_960_100 = "10";
	public final static String IMGNAME11_720_1230 = "11";
	public final static String IMGNAME12_160_600 = "12";
	public final static String IMGNAME13_640_350 = "13";
	public final static String IMGNAME14_250_250 = "14";
	public final static String IMGNAME17_300_250 = "17";
	public final static String IMGNAME22_320_100 = "22";
	public final static String IMGNAME24_300_150 = "24";
	public final static String IMGNAME26_970_250 = "26";
	public final static String IMGNAME28_300_300 = "28"; // 누끼이미지
	public final static String IMG_NON = "99";

	// 상품관련 파라미터
	public final static String NOCATE = "nocate";	// 상품에서 카테고리가 없을 경우 사용하는 값

	// shop log 쿠키명
	public final static String SHOP_LOG = "shop_log";	// 상품에서 카테고리가 없을 경우 사용하는 값

	public final static String MONGODB = "MONGODB";
	public final static String COOKIE = "COOKIE";
	public final static String SEND_CHK = "send_chk";	// 아이커버에서 다음 번 창을 띄우기 전까지 걸리는 시간을 제어하는 값

	public final static String TRUE = "TRUE";	//

	public final static String NOEXPOSURE = "shoppul123";	// 미노출 수치를 쌓는 계정ID, 아예없는 계정에다 쌓을 수 없어, 임의의 계정에 미노출수치를 쌓음

	public static final String AU_ID 		= "au_id";

	public static final String MCOVERDIRECT 		= "direct"; //엠커버 다이렉트 임영현과장님이 만든 구분값
	public static final String MCOVERNOFRQ 		= "noFrq";      //엠커버 프리퀀시 걸려있을때 패스백 보내주는 광고구분값

	public static final String ADTRUTH 		= "_at";      //ADTruth 키 발급 여부
	public final static String ADTRUTH_DOMAIN = "adtruth."+PropertyHandler.getProperty("DOMAIN");		// ADTruth 도메인
	public final static String DRC_DOMAIN = "drc." + PropertyHandler.getProperty("DOMAIN");		// drc 도메인
	public final static String DRC_MEDIACATEGORY = "drc." + MEDIACATEGORY_DOMAIN;				// 미디어카테고리 drc 도메인
	// device info
	public static final String DEVICE_PC = "pc";
	public static final String DEVICE_MOBILE_ETC = "etc";
	public static final String DEVICE_MOBILE_IOS = "ios";
	public static final String DEVICE_MOBILE_ANDROID = "android";
	public static final String DEVICE_MOBILE_SAMSUNG = "samsung";

	// MM url redirection domain
	public final static String SAD = "02"; //검색광고
	public final static String SEN = "03"; //검색엔진
	

	// device browser info
	public static final String DEVICE_BROWSER_MSIE = "MSIE";
	public static final String DEVICE_BROWSER_CHROME = "Chrome";
	public static final String DEVICE_BROWSER_OPERA = "Opera";
	public static final String DEVICE_BROWSER_FIREFOX = "Firefox";
	public static final String DEVICE_BROWSER_SAFARI = "Safari";

	public final static String EPRS_TP_CODE_VISIT = "02"; // 방문자 2회 이상인 상품만 노출
	public final static String EPRS_TP_CODE_VISIT_FIRST = "03"; // 방문자 2회 이상인 상품먼저 노출

}
