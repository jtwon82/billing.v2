-- MOBON.ADVRTS_PRDT_ADVRTS_SETUP definition

CREATE TABLE `ADVRTS_PRDT_ADVRTS_SETUP` (
  `ADVRTS_STLE_TP_CODE` varchar(2) NOT NULL COMMENT '광고형태구분코드|광고 형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVRTS_SORT_NO` int(11) NOT NULL COMMENT '광고정렬번호|광고정렬번호||',
  `ADVRTS_PRDT_SORT_NO` int(11) NOT NULL COMMENT '광고상품정렬번호|광고상품정렬번호||',
  `ADVRTS_STLE_NM` varchar(30) DEFAULT NULL COMMENT '광고형태이름|광고형태이름||',
  `ADVRTS_PRDT_NM` varchar(30) DEFAULT NULL COMMENT '광고상품이름|광고상품이름||',
  `ADVRTS_TP_NM` varchar(30) DEFAULT NULL COMMENT '광고구분이름|광고구분이름||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADVRTS_STLE_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='광고상품구분설정|광고상품구분설정||';


-- MOBON.MOBON_ADM_FLT_INFO definition

CREATE TABLE `MOBON_ADM_FLT_INFO` (
  `ADM_FLT_TP_SEQ` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '모비온관리자필터순서|모비온 관리자 필터 순서||',
  `ADM_FLT_NM` varchar(50) NOT NULL COMMENT '관리자필터명|관리자 필터 명||',
  `ADM_FLT_MEMO` varchar(100) NOT NULL COMMENT '관리자필터메모|관리자 필터 메모||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADM_FLT_TP_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='모비온관리자필터정보|모비온 관리자 필터 정보||';


-- MOBON.MOBON_ADM_FLT_SETUP definition

CREATE TABLE `MOBON_ADM_FLT_SETUP` (
  `ADM_FLT_SETUP_SEQ` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '모비온관리자필터셋팅순서|모비온 관리자 필터 셋팅 순서||',
  `ADM_FLT_TP_SEQ` bigint(20) NOT NULL COMMENT '모비온관리자필터순서|모비온 관리자 필터 순서|MOBON_ADM_FLT_INFO.ADM_FLT_TP_SEQ|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVRTS_PRDT_SORT_NO` int(11) NOT NULL COMMENT '광고상품정렬번호|광고 상품 정렬 번호||',
  `ADVRTS_TP_SORT_NO` int(11) NOT NULL COMMENT '광고타겟팅정렬번호|광고 타겟팅 정렬 번호||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADM_FLT_SETUP_SEQ`),
  UNIQUE KEY `MOBON_ADM_FLT_SETUP_UK` (`ADM_FLT_TP_SEQ`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=390 DEFAULT CHARSET=utf8 COMMENT='모비온관리자필터설정|모비온 관리자 필터 설정||';


-- MOBON.MOBON_ADVER_NOTICE_MPG definition

CREATE TABLE `MOBON_ADVER_NOTICE_MPG` (
  `ADVER_NOTICE_NO` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '웰컴페이지 노출 항목 설정 번호|웰컴페이지 노출 항목 설정 번호, 자동증가||',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '사용자ID | admember.userid|',
  `MPG_TP_CODE` varchar(30) DEFAULT NULL COMMENT '노출항목설정타입 ||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADVER_NOTICE_NO`),
  KEY `MOBON_ADVER_NOTICE_MPG_IDX_01` (`ADVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=590 DEFAULT CHARSET=utf8 COMMENT='웰컴페이지 노출 항목 설정 테이블';


-- MOBON.MOBON_ADVER_TMPLT_IMAGE_INFO definition

CREATE TABLE `MOBON_ADVER_TMPLT_IMAGE_INFO` (
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `TMPLT_NO` int(11) NOT NULL COMMENT '템플릿번호|광고주 템플릿의 번호|MOBON_ADVER_TMPLT_INFO.TMPLT_NO|',
  `IMAGE_TP_CODE` varchar(2) NOT NULL COMMENT '이미지구분코드|이미지구분코드|MOBON_COM_CODE.IMAGE_TP_CODE|',
  `IMAGE_INFO` varchar(100) NOT NULL COMMENT '이미지정보|이미지 정보 저장||',
  `IMAGE_SE` varchar(2) NOT NULL DEFAULT '01' COMMENT '이미지구분|이미지 사용 처에 대한 구분|01 : 공통 이미지, 02 : 웹 이미지, 03 : 모바일 이미지|',
  `IMAGE_BKG` varchar(7) DEFAULT NULL COMMENT '이미지배경|이미지에 대한 배경 설정 CSS 타입||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADVER_ID`,`TMPLT_NO`,`IMAGE_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온광고주템플릿이미지정보|모비온광고주템플릿이미지정보||';


-- MOBON.MOBON_ADVER_TMPLT_INFO definition

CREATE TABLE `MOBON_ADVER_TMPLT_INFO` (
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `TMPLT_NO` int(11) NOT NULL COMMENT '템플릿번호|광고주 템플릿의 번호, 광고주별 고유, 기본값 1|MAX(TMPLT_NO) + 1|',
  `TMPLT_NM` varchar(50) NOT NULL COMMENT '템플릿명|템플릿 이름, 광고주별 고유||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADVER_ID`,`TMPLT_NO`),
  UNIQUE KEY `MOBON_ADVER_TMPLT_INFO_UNQ01` (`ADVER_ID`,`TMPLT_NM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온광고주템플릿정보|모비온광고주템플릿정보||';


-- MOBON.MOBON_AUTH_CHG_HIST definition

CREATE TABLE `MOBON_AUTH_CHG_HIST` (
  `HIST_SEQ` bigint(20) unsigned NOT NULL COMMENT '이력순서',
  `TGET_USER_ID` varchar(30) NOT NULL COMMENT '대상사용자ID',
  `AUTH_CHG_TP_CODE` varchar(2) NOT NULL COMMENT '권한변경구분코드',
  `HIST_MEMO` varchar(200) DEFAULT NULL COMMENT '이력메모',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`HIST_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온권한변경이력|모비온권한변경이력||';


-- MOBON.MOBON_MENU_INFO definition

CREATE TABLE `MOBON_MENU_INFO` (
  `MENU_SEQ` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '메뉴순서|메뉴의 고유번호, 자동증가||',
  `MENU_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '메뉴구분코드|메뉴구분코드|MOBON_COM_CODE.MENU_TP_CODE|',
  `MENU_NM` varchar(50) NOT NULL COMMENT '메뉴이름|메뉴의 이름||',
  `MENU_LV` int(11) NOT NULL COMMENT '메뉴레벨|메뉴의 계층 깊||',
  `MENU_HIRNK_SEQ` int(10) unsigned DEFAULT NULL COMMENT '메뉴상위순서|메뉴의 상위 번호|MOBON_MENU_INFO.MENU_SEQ|',
  `MENU_URL` varchar(200) DEFAULT NULL COMMENT '메뉴URL|메뉴의 URL 정보||',
  `MENU_SORT_NO` int(11) DEFAULT NULL COMMENT '메뉴정렬번호|메뉴의 정렬 번호||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `ADM_FLT_TP_SEQ` bigint(20) NOT NULL DEFAULT 1 COMMENT '모비온관리자필터순서|모비온 관리자 필터 순서|MOBON_ADM_FLT_INFO.ADM_FLT_TP_SEQ|',
  PRIMARY KEY (`MENU_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=769 DEFAULT CHARSET=utf8 COMMENT='모비온메뉴정보|모비온메뉴정보||';


-- MOBON.MOBON_MENU_INFO_20220810_bak definition

CREATE TABLE `MOBON_MENU_INFO_20220810_bak` (
  `MENU_SEQ` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '메뉴순서|메뉴의 고유번호, 자동증가||',
  `MENU_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '메뉴구분코드|메뉴구분코드|MOBON_COM_CODE.MENU_TP_CODE|',
  `MENU_NM` varchar(50) NOT NULL COMMENT '메뉴이름|메뉴의 이름||',
  `MENU_LV` int(11) NOT NULL COMMENT '메뉴레벨|메뉴의 계층 깊||',
  `MENU_HIRNK_SEQ` int(10) unsigned DEFAULT NULL COMMENT '메뉴상위순서|메뉴의 상위 번호|MOBON_MENU_INFO.MENU_SEQ|',
  `MENU_URL` varchar(200) DEFAULT NULL COMMENT '메뉴URL|메뉴의 URL 정보||',
  `MENU_SORT_NO` int(11) DEFAULT NULL COMMENT '메뉴정렬번호|메뉴의 정렬 번호||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `ADM_FLT_TP_SEQ` bigint(20) NOT NULL DEFAULT 1 COMMENT '모비온관리자필터순서|모비온 관리자 필터 순서|MOBON_ADM_FLT_INFO.ADM_FLT_TP_SEQ|'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- MOBON.MOBON_MENU_INFO_TMP definition

CREATE TABLE `MOBON_MENU_INFO_TMP` (
  `MENU_SEQ` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '메뉴순서|메뉴의 고유번호, 자동증가||',
  `MENU_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '메뉴구분코드|메뉴구분코드|MOBON_COM_CODE.MENU_TP_CODE|',
  `MENU_NM` varchar(50) NOT NULL COMMENT '메뉴이름|메뉴의 이름||',
  `MENU_LV` int(11) NOT NULL COMMENT '메뉴레벨|메뉴의 계층 깊||',
  `MENU_HIRNK_SEQ` int(10) unsigned DEFAULT NULL COMMENT '메뉴상위순서|메뉴의 상위 번호|MOBON_MENU_INFO.MENU_SEQ|',
  `MENU_URL` varchar(200) DEFAULT NULL COMMENT '메뉴URL|메뉴의 URL 정보||',
  `MENU_SORT_NO` int(11) DEFAULT NULL COMMENT '메뉴정렬번호|메뉴의 정렬 번호||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `ADM_FLT_TP_SEQ` bigint(20) NOT NULL DEFAULT 1 COMMENT '모비온관리자필터순서|모비온 관리자 필터 순서|MOBON_ADM_FLT_INFO.ADM_FLT_TP_SEQ|',
  PRIMARY KEY (`MENU_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=761 DEFAULT CHARSET=utf8 COMMENT='모비온메뉴정보|모비온메뉴정보||';


-- MOBON.MOBON_PJT_DEPT_MPG definition

CREATE TABLE `MOBON_PJT_DEPT_MPG` (
  `PJT_NO` bigint(20) NOT NULL COMMENT '프로젝트번호|매핑시킬 프로젝트번호|MOBON_PJT_INFO.NO',
  `DEPT_NM` varchar(100) NOT NULL COMMENT '프로젝트진행부서|프로젝트진행부서명||',
  `DEPT1_NO` int(11) NOT NULL COMMENT '프로젝트진행부서 번호|프로젝트진행부서 번호||',
  `REG_USER_ID` varchar(30) NOT NULL COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`PJT_NO`,`DEPT_NM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='프로젝트 진행 부서 매핑테이블';


-- MOBON.MOBON_PJT_INFO definition

CREATE TABLE `MOBON_PJT_INFO` (
  `NO` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '일련번호|순차 증가값||',
  `PJT_NM` varchar(100) NOT NULL COMMENT '프로젝트이름|프로젝트의 이름||',
  `ST_DTTM` int(11) NOT NULL COMMENT '시작일자|프로젝트의 시작일자||',
  `EN_DTTM` int(11) NOT NULL COMMENT '종료일자|프로젝트의 종료일자||',
  `PLN_ST_DTTM` int(11) NOT NULL COMMENT '기획시작일자|프로젝트의 기획 시작일자||',
  `PLN_EN_DTTM` int(11) NOT NULL COMMENT '기획종료일자|프로젝트의 기획 종료일자||',
  `DEV_ST_DTTM` int(11) NOT NULL COMMENT '개발시작일자|프로젝트의 개발 시작일자||',
  `DEV_EN_DTTM` int(11) NOT NULL COMMENT '개발종료일자|프로젝트의 개발 종료일자||',
  `AB_YN` varchar(1) NOT NULL COMMENT 'AB테스트여부|프로젝트의 A/B테스트 여부||',
  `MEMO` text NOT NULL COMMENT '비고|프로젝트비고||',
  `STATUS` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '프로젝트상태|프로젝트상태, Y:진행중,N:종료,W:보류,S:중지||',
  `URL` text NOT NULL COMMENT '프로젝트 url|프로젝트 url||',
  `EMGC_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '긴급여부|해당프로젝트의 긴급여부||',
  `REG_USER_ID` varchar(30) NOT NULL COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`NO`),
  KEY `MOBON_PJT_INFO_IDX_01` (`PJT_NM`)
) ENGINE=InnoDB AUTO_INCREMENT=431 DEFAULT CHARSET=utf8 COMMENT='프로젝트 정보 테이블';


-- MOBON.MOBON_PJT_MPG definition

CREATE TABLE `MOBON_PJT_MPG` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '프로젝트매핑 키||',
  `PJT_NO` bigint(20) NOT NULL COMMENT '매핑프로젝트번호|매핑될 프로젝트의 번호|MOBON_PJT_INFO.NO|',
  `MPG_PJT_NO` bigint(20) NOT NULL COMMENT '매핑프로젝트번호|매핑시킬 프로젝트의 번호|MOBON_PJT_INFO.NO|',
  `REG_USER_ID` varchar(30) DEFAULT NULL COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime DEFAULT NULL COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`NO`),
  UNIQUE KEY `PJT_NO` (`PJT_NO`,`MPG_PJT_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 COMMENT='프로젝트 매핑 테이블';


-- MOBON.MOBON_PJT_USER_MPG definition

CREATE TABLE `MOBON_PJT_USER_MPG` (
  `PJT_NO` bigint(20) NOT NULL COMMENT '프로젝트번호|매핑시킬 프로젝트번호|MOBON_PJT_INFO.NO',
  `USER_ID` varchar(30) NOT NULL COMMENT '프로젝트진행자ID|프로젝트진행자의 고유ID|admember.userid|',
  `DEPT_NM` varchar(100) NOT NULL COMMENT '프로젝트진행부서|프로젝트진행부서명||',
  `PJT_USER_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '프로젝트진행자타입|MOBON_COM_CODE.PJT_USER_TP_CODE|',
  `REG_USER_ID` varchar(30) NOT NULL COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`PJT_NO`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='프로젝트 진행 인원 매핑테이블';


-- MOBON.MOBON_SYS_CHG_CODE definition

CREATE TABLE `MOBON_SYS_CHG_CODE` (
  `CHG_NO` int(11) NOT NULL COMMENT '변경번호|시스템변경코드의 고유값|MAX(CHG_NO)+1|',
  `CHG_INFO_NM` varchar(100) NOT NULL COMMENT '변경정보명|변경정보의 이름||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`CHG_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온시스템변경코드|모비온시스템변경코드||';


-- MOBON.MOBON_SYS_CHG_LOG definition

CREATE TABLE `MOBON_SYS_CHG_LOG` (
  `CHG_SEQ` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '변경순서|시스템변경로그의 고유값, 자동증가||',
  `LOG_DTTM` int(11) NOT NULL COMMENT '입력일시(YYYYMMDD)',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `CHG_NO` int(11) NOT NULL COMMENT '변경번호|시스템 변경 코드의 고유값|MOBON_SYS_CHG_CODE.CHG_NO|',
  `CHG_INFO` varchar(100) DEFAULT NULL COMMENT '변경정보|변경정보의 조회값, 100자만 저장||',
  `CHG_DTL_INFO` varchar(5000) DEFAULT NULL COMMENT '변경상세정보|변경정보의 상세 정보값, 실제 값 저장||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`CHG_SEQ`,`LOG_DTTM`),
  KEY `MOBON_SYS_CHG_LOG_IDX_01` (`ADVER_ID`,`CHG_NO`),
  KEY `MOBON_SYS_CHG_LOG_IDX_02` (`REG_DTTM`)
) ENGINE=InnoDB AUTO_INCREMENT=45742542 DEFAULT CHARSET=utf8 COMMENT='모비온시스템변경로그|모비온시스템변경로그||'
 PARTITION BY RANGE (`LOG_DTTM`)
(PARTITION `MOBON_SYS_CHG_LOG_20180101` VALUES LESS THAN (20190101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20190101` VALUES LESS THAN (20200101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20200501` VALUES LESS THAN (20200601) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20200601` VALUES LESS THAN (20200701) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20200701` VALUES LESS THAN (20200801) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20200801` VALUES LESS THAN (20200901) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20200901` VALUES LESS THAN (20201001) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20201001` VALUES LESS THAN (20201101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20201101` VALUES LESS THAN (20201201) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20201201` VALUES LESS THAN (20210101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210101` VALUES LESS THAN (20210201) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210201` VALUES LESS THAN (20210301) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210301` VALUES LESS THAN (20210401) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210401` VALUES LESS THAN (20210501) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210501` VALUES LESS THAN (20210601) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210601` VALUES LESS THAN (20210701) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210701` VALUES LESS THAN (20210801) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210801` VALUES LESS THAN (20210901) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20210901` VALUES LESS THAN (20211001) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20211001` VALUES LESS THAN (20211101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20211101` VALUES LESS THAN (20211201) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20211201` VALUES LESS THAN (20220101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220101` VALUES LESS THAN (20220201) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220201` VALUES LESS THAN (20220301) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220301` VALUES LESS THAN (20220401) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220401` VALUES LESS THAN (20220501) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220501` VALUES LESS THAN (20220601) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220701` VALUES LESS THAN (20220801) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220801` VALUES LESS THAN (20220901) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20220901` VALUES LESS THAN (20221001) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20221001` VALUES LESS THAN (20221101) ENGINE = InnoDB,
 PARTITION `MOBON_SYS_CHG_LOG_20221101` VALUES LESS THAN (20221201) ENGINE = InnoDB);


-- MOBON.MOBON_USER_ACTION_STATE definition

CREATE TABLE `MOBON_USER_ACTION_STATE` (
  `ADVER_ID` varchar(30) NOT NULL COMMENT '사용자ID | admember.userid|',
  `KPI_NO` bigint(20) NOT NULL DEFAULT 0 COMMENT 'KPI 고유값 | MOBON_AD_KPI_SETUP.KPI_NO|',
  `STATE_DTTM` datetime DEFAULT NULL COMMENT '수정일자',
  PRIMARY KEY (`ADVER_ID`,`KPI_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온 측정 대기 광고주 날짜 정보 | 광고 설정 변경일 정보';


-- MOBON.MOBON_USER_AUTH definition

CREATE TABLE `MOBON_USER_AUTH` (
  `AUTH_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '권한번호',
  `AUTH_NM` varchar(50) NOT NULL COMMENT '권한명|직급명',
  `P_AUTH_NO` varchar(100) NOT NULL COMMENT '상위 권한번호|상위 직급번호',
  `DEPTH` int(11) NOT NULL COMMENT '권한깊이|권한의 레벨||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|사용여부|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`AUTH_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='광고주센터 사용자 권한직급 테이블';


-- MOBON.MOBON_USER_AUTH_MPG definition

CREATE TABLE `MOBON_USER_AUTH_MPG` (
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `AUTH_NO` int(10) unsigned NOT NULL COMMENT '사용자직급번호',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='광고주센터 사용자권한직급 매핑 테이블';


-- MOBON.MOBON_USER_DEPT definition

CREATE TABLE `MOBON_USER_DEPT` (
  `DEPT_NO` int(11) NOT NULL AUTO_INCREMENT COMMENT '소속번호',
  `DEPT_NM` varchar(50) NOT NULL COMMENT '소속명',
  `P_DEPT_NO` varchar(100) NOT NULL COMMENT '상위 소속번호',
  `DEPTH` int(11) DEFAULT NULL COMMENT '부서깊이|부서의 레벨||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|사용여부|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`DEPT_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='광고주센터사용자소속 테이블';


-- MOBON.MOBON_USER_DEPT_MPG definition

CREATE TABLE `MOBON_USER_DEPT_MPG` (
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `DEPT_NO` int(10) unsigned NOT NULL COMMENT '사용자소속번호',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='광고주센터 사용자소속매핑 테이블';


-- MOBON.MOBON_USER_GRP_INFO definition

CREATE TABLE `MOBON_USER_GRP_INFO` (
  `USER_GRP_NO` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '사용자그룹번호|사용자 그룹번호, 자동증가||',
  `USER_GRP_NM` varchar(50) NOT NULL COMMENT '사용자그룹명|사용자 그룹명||',
  `GRP_SORT_NO` int(11) DEFAULT NULL COMMENT '그룹정렬번호|그룹의 정렬 번호||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_GRP_NO`),
  UNIQUE KEY `MOBON_USER_GRP_INFO_UNQ01` (`USER_GRP_NM`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8 COMMENT='모비온사용자그룹정보|모비온사용자그룹정보||';


-- MOBON.MOBON_USER_GRP_MENU_AUTH definition

CREATE TABLE `MOBON_USER_GRP_MENU_AUTH` (
  `USER_GRP_NO` int(10) unsigned NOT NULL COMMENT '사용자그룹번호|사용자그룹번호|MOBON_USER_GRP_INFO.GRP_NO|',
  `MENU_SEQ` int(10) unsigned NOT NULL COMMENT '메뉴순서|메뉴의 고유번호|MOBON_MENU_INFO.MENU_SEQ|',
  `USER_AUTH_TP_CODE` varchar(2) NOT NULL COMMENT '사용자권한구분코드|사용자권한구분코드|MOBON_COM_CODE.USER_AUTH_TP_CODE|',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_GRP_NO`,`MENU_SEQ`,`USER_AUTH_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온사용자그룹메뉴권한|모비온사용자그룹메뉴권한||';


-- MOBON.MOBON_USER_GRP_MENU_INFO definition

CREATE TABLE `MOBON_USER_GRP_MENU_INFO` (
  `USER_GRP_NO` int(10) unsigned NOT NULL COMMENT '사용자그룹번호|사용자그룹번호|MOBON_USER_GRP_INFO.GRP_NO|',
  `MENU_SEQ` int(10) unsigned NOT NULL COMMENT '메뉴순서|메뉴의 고유번호|MOBON_MENU_INFO.MENU_SEQ|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_GRP_NO`,`MENU_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온사용자그룹메뉴정보|모비온사용자그룹메뉴정보||';


-- MOBON.MOBON_USER_GRP_MPG definition

CREATE TABLE `MOBON_USER_GRP_MPG` (
  `USER_GRP_NO` int(10) unsigned NOT NULL COMMENT '사용자그룹번호',
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_GRP_NO`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온사용자그룹매핑|모비온사용자그룹매핑||';


-- MOBON.MOBON_USER_INFO definition

CREATE TABLE `MOBON_USER_INFO` (
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `ERP_USER_NO` varchar(255) NOT NULL COMMENT 'ERP 사원코드',
  `HIWK_ID` varchar(30) NOT NULL COMMENT '하이웍스 아이디',
  `ACS_TP_CODE` varchar(2) NOT NULL COMMENT '접근구분코드|접근구분코드|MOBON_COM_CODE.ACS_TP_CODE|',
  `AUTH_LV_TP_CODE` varchar(2) NOT NULL COMMENT '권한레벨구분코드|권한레벨구분코드|MOBON_COM_CODE.AUTH_LV_TP_CODE|',
  `USER_STATE_TP_CODE` varchar(2) NOT NULL COMMENT '사용자상태구분코드|사용자상태구분코드|MOBON_COM_CODE.USER_STATE_TP_CODE|',
  `PAGE_AUTH_TP_CODE` varchar(2) NOT NULL COMMENT '한페이지권한구분코드|페이지권구분코드|MOBON_COM_CODE.PAGE_AUTH_TP_CODE|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `TRELLO_ID` varchar(255) DEFAULT NULL COMMENT '트렐로 아이디 KEY',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `MOBON_USER_INFO_UNQ01` (`USER_ID`),
  UNIQUE KEY `MOBON_USER_INFO_UNQ02` (`HIWK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온사용자정보|모비온사용자정보||';


-- MOBON.MOBON_USER_MAPPING_MPG definition

CREATE TABLE `MOBON_USER_MAPPING_MPG` (
  `USER_MAPPING_NO` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '사용자매핑넘버',
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `MAPPING_ID` varchar(30) NOT NULL COMMENT '영업자,어드민 매핑 사용자ID|사용자의 고유ID|admember.userid|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_MAPPING_NO`),
  UNIQUE KEY `MOBON_USER_MAPPING_MPG_UK_01` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='영업자, 어드민 매핑';


-- MOBON.MOBON_USER_MENU_AUTH definition

CREATE TABLE `MOBON_USER_MENU_AUTH` (
  `MENU_SEQ` int(10) unsigned NOT NULL COMMENT '메뉴순서|메뉴의 고유번호|MOBON_MENU_INFO.MENU_SEQ|',
  `USER_AUTH_TP_CODE` varchar(2) NOT NULL COMMENT '사용자권한구분코드|사용자권한구분코드|MOBON_COM_CODE.USER_AUTH_TP_CODE|',
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자의 고유ID|admember.userid|',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|메뉴의 사용여부, 기본값:Y|Y:사용, N:미사용|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`MENU_SEQ`,`USER_AUTH_TP_CODE`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온사용자메뉴권한|모비온사용자메뉴권한||';


-- MOBON.QUEUE_MONITOR definition

CREATE TABLE `QUEUE_MONITOR` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `job_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `queue` varchar(255) NOT NULL,
  `started_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `started_at_exact` varchar(255) NOT NULL,
  `finished_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `finished_at_exact` varchar(255) NOT NULL,
  `time_elapsed` float NOT NULL,
  `failed` tinyint(1) NOT NULL DEFAULT 0,
  `attempt` int(10) DEFAULT 0,
  `progress` int(10) NOT NULL,
  `exception` longtext NOT NULL,
  `exception_message` text NOT NULL,
  `exception_class` text NOT NULL,
  `data` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `job_id` (`job_id`),
  KEY `started_at` (`started_at`),
  KEY `failed` (`failed`),
  KEY `time_elapsed` (`time_elapsed`)
) ENGINE=InnoDB AUTO_INCREMENT=245849 DEFAULT CHARSET=utf8 COMMENT='큐배치 모니터링 테이블';


-- MOBON.SCRIPT_CARD definition

CREATE TABLE `SCRIPT_CARD` (
  `SEQ` int(11) NOT NULL AUTO_INCREMENT COMMENT '카드 고유번호',
  `CREATE_DATE` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일',
  `CREATE_USER` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '생성 유저',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '수정일',
  `UPDATE_USER` varchar(30) DEFAULT NULL COMMENT '수정 유저',
  `STATE` varchar(3) NOT NULL DEFAULT '00' COMMENT '상태',
  `TITLE` varchar(100) NOT NULL DEFAULT '' COMMENT '카드 제목',
  `ADVER_ID` varchar(30) NOT NULL DEFAULT '' COMMENT '광고주ID',
  `SOLUTION` varchar(3) NOT NULL DEFAULT '' COMMENT '솔루션 타입',
  `MANAGER` varchar(30) NOT NULL DEFAULT '' COMMENT '담당자',
  `TYPE` varchar(3) NOT NULL DEFAULT '' COMMENT '스크립트 삽입 종류',
  `TRELLO_LINK` varchar(50) NOT NULL DEFAULT '' COMMENT '트렐로 카드 링크',
  `EXTRA_POINTS` int(11) DEFAULT 0 COMMENT '추가 점수',
  PRIMARY KEY (`SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=5227 DEFAULT CHARSET=utf8 COMMENT='스크립트 진행 현황 파악을 위한 정보';