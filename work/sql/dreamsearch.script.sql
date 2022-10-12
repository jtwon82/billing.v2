-- dreamsearch.action_ab_pcode_recom_log definition

CREATE TABLE `action_ab_pcode_recom_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `STATS_DTTM` int(11) NOT NULL COMMENT '날짜|',
  `IP` varchar(20) NOT NULL COMMENT '클라이언트 아이피|',
  `AU_ID` varchar(50) NOT NULL COMMENT '클라이언트 AU_ID|',
  `PRODUCT` varchar(5) NOT NULL COMMENT '상품구분코드|mnt, mbe, ico, mbw, nor, mct, mbb, pnt, sky, nct ... |',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVRSB_TP_CODE` varchar(2) NOT NULL COMMENT '서브분코드 코드|MOBON_COM_CODE.ADVRSB_TP_CODE|',
  `RECOM_TP_CODE` varchar(2) NOT NULL COMMENT '추천구분 코드|MOBON_COM_CODE.RECOM_TP_CODE|',
  `RECOM_ALGO_CODE` varchar(2) NOT NULL COMMENT '알고리즘코드|MOBON_COM_CODE.RECOM_ALGO_CODE |',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '캠페인코드|',
  `PAR_NO` int(11) NOT NULL COMMENT '매체 지면번호|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주 아이디|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체 아이디|',
  `AB_TEST_TY` varchar(10) NOT NULL COMMENT 'AB테스트 타입|',
  `FROM_APP` varchar(1) NOT NULL COMMENT '앱여부|',
  `FREQ_CNT` int(11) DEFAULT 0 COMMENT '프리컨시|',
  `POINT` decimal(13,2) DEFAULT 0.00 COMMENT '소진금액|',
  `REG_DTTM` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|',
  PRIMARY KEY (`NO`,`STATS_DTTM`),
  KEY `ACTION_AB_PCODE_RECOM_LOG_IDX_01` (`IP`),
  KEY `ACTION_AB_PCODE_RECOM_LOG_IDX_02` (`AU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=110608327 DEFAULT CHARSET=utf8 COMMENT='타겟 추천상품 ABTEST액션로그| 45일';


-- dreamsearch.action_log definition

CREATE TABLE `action_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `PARTDT` int(8) NOT NULL,
  `IP` varchar(20) NOT NULL,
  `PCODE` varchar(100) DEFAULT NULL,
  `SHOPLOG_NO` mediumtext DEFAULT NULL,
  `SITECODE` varchar(40) DEFAULT NULL,
  `MCODE` varchar(20) DEFAULT NULL,
  `MEDIA_CODE` varchar(13) DEFAULT NULL,
  `MEDIA_ID` varchar(13) DEFAULT NULL,
  `K_NO` varchar(13) DEFAULT NULL,
  `VCNT` int(11) DEFAULT NULL,
  `VCNT2` int(11) DEFAULT NULL,
  `CCNT` int(11) DEFAULT NULL,
  `point` decimal(13,2) DEFAULT 0.00,
  `ACTGUBUN` varchar(1) DEFAULT NULL,
  `ADGUBUN` varchar(2) DEFAULT NULL,
  `ADPRODUCT` varchar(5) DEFAULT NULL,
  `REGDATE` timestamp NOT NULL DEFAULT current_timestamp(),
  `MCGB` varchar(10) DEFAULT '',
  PRIMARY KEY (`NO`,`PARTDT`),
  KEY `IP` (`IP`)
) ENGINE=InnoDB AUTO_INCREMENT=430755 DEFAULT CHARSET=utf8;


-- dreamsearch.action_pcode_recom_log definition

CREATE TABLE `action_pcode_recom_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `STATS_DTTM` int(11) NOT NULL COMMENT '날짜|',
  `IP` varchar(20) NOT NULL COMMENT '클라이언트 아이피|',
  `AU_ID` varchar(50) NOT NULL COMMENT '클라이언트 AU_ID|',
  `PRODUCT` varchar(5) NOT NULL COMMENT '상품구분코드|mnt, mbe, ico, mbw, nor, mct, mbb, pnt, sky, nct ... |',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVRSB_TP_CODE` varchar(2) NOT NULL COMMENT '서브분코드 코드|MOBON_COM_CODE.ADVRSB_TP_CODE|',
  `RECOM_TP_CODE` varchar(2) NOT NULL COMMENT '추천구분 코드|MOBON_COM_CODE.RECOM_TP_CODE|',
  `RECOM_ALGO_CODE` varchar(2) NOT NULL COMMENT '알고리즘코드|MOBON_COM_CODE.RECOM_ALGO_CODE |',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '캠페인코드|',
  `PAR_NO` int(11) NOT NULL COMMENT '매체 지면번호|',
  `MAIN_PCODE` varchar(100) DEFAULT NULL COMMENT '노출 상품코드|SHOP_DATA.PCODE|',
  `CLICK_PCODE` varchar(100) DEFAULT NULL COMMENT '상품코드|SHOP_DATA.PCODE|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주 아이디|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체 아이디|',
  `FROM_APP` varchar(1) NOT NULL COMMENT '앱여부|',
  `FREQ_CNT` int(11) DEFAULT 0 COMMENT '프리컨시|',
  `POINT` decimal(13,2) DEFAULT 0.00 COMMENT '소진금액|',
  `REG_DTTM` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|',
  PRIMARY KEY (`NO`,`STATS_DTTM`),
  KEY `ACTION_PCODE_RECOM2_LOG_IDX_01` (`IP`),
  KEY `ACTION_PCODE_RECOM2_LOG_IDX_02` (`AU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=722400 DEFAULT CHARSET=utf8 COMMENT='타겟 추천상품 액션로그| 2개월';


-- dreamsearch.admember definition

CREATE TABLE `admember` (
  `seq` varchar(13) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL COMMENT '아이디',
  `email` varchar(100) NOT NULL COMMENT '이메일',
  `corpname` varchar(100) DEFAULT NULL COMMENT '회사명',
  `uname` varchar(20) NOT NULL COMMENT '사용자명',
  `upasswd` varchar(100) DEFAULT NULL COMMENT '패스워드',
  `grade` varchar(1) DEFAULT NULL COMMENT '권한, 빈값:일반, 2:대행사, 3:직매체, 4:신디, 7:관리자',
  `hp` varchar(20) DEFAULT NULL COMMENT '휴대폰번호',
  `tel` varchar(30) DEFAULT NULL COMMENT '전화번호',
  `bankname` varchar(30) DEFAULT NULL COMMENT '은행명',
  `homepi` varchar(100) DEFAULT NULL COMMENT '홈페이지',
  `banknum` varchar(50) DEFAULT NULL COMMENT '은행계좌번호',
  `bname` varchar(100) DEFAULT NULL COMMENT '예금주',
  `gubun` varchar(2) DEFAULT NULL COMMENT '11:광고주, 12:파트너, 13:매체',
  `point` decimal(13,2) DEFAULT 0.00 COMMENT '±Ý¾×(¿ø)',
  `mpoint` decimal(13,2) DEFAULT 0.00 COMMENT '웹매체피',
  `plist` text DEFAULT NULL COMMENT '파트너 대행사 리스트(콤마로 구분)',
  `regdate` datetime DEFAULT NULL,
  `delflag` varchar(1) DEFAULT 'N' COMMENT '삭제플래그, Y:삭제',
  `danga` int(11) DEFAULT 0,
  `cate` varchar(3) DEFAULT '001',
  `lastupdate` datetime DEFAULT NULL,
  `MALLNM` varchar(50) DEFAULT NULL,
  `SHOPLOADURL` varchar(200) DEFAULT NULL,
  `cookieday` int(11) DEFAULT 31,
  `dispo_sex` varchar(30) DEFAULT 'M,W' COMMENT '성별',
  `dispo_age` varchar(30) DEFAULT 'p,q,r,s,t,u,v,w,x' COMMENT '연령대 : p(~13),q(14~19),r(20~24),s(25~27),t(28~35),u(36~44),v(45~54),w(55~64),x(64~)',
  `new_dispo_age` varchar(30) DEFAULT 'h,i,j,k,l,m,n,o' COMMENT '나이대:h(~13),i(14~19),j(20~26),k(27~32),l(33~40),m(41~46),n(47~56),o(57~)',
  `cookie_dirsales` int(11) DEFAULT 48,
  `cookie_indirsales` int(11) DEFAULT 15,
  `imgname_logo` varchar(100) DEFAULT NULL,
  `site_name` varchar(50) DEFAULT NULL,
  `site_desc` varchar(50) DEFAULT NULL COMMENT '사이트공통설명(1)',
  `site_desc1` varchar(1000) DEFAULT NULL COMMENT '사이트공통설명(2)',
  `site_desc2` varchar(1000) DEFAULT NULL COMMENT '사이트공통설명(3)',
  `site_desc3` varchar(200) DEFAULT '' COMMENT '사이트공통설명(4)',
  `site_desc4` varchar(50) DEFAULT NULL,
  `site_title` varchar(50) DEFAULT NULL,
  `desc_wt_1` varchar(20) DEFAULT NULL,
  `desc_wt_2` varchar(20) DEFAULT NULL,
  `baseadweight` float(3,1) DEFAULT 1.0,
  `kwadweight` float(3,1) DEFAULT 1.0,
  `urladweight` float(3,1) DEFAULT 1.0,
  `brbaseadweight` float(3,1) DEFAULT 1.0,
  `brkwadweight` float(3,1) DEFAULT 1.0,
  `brurladweight` float(3,1) DEFAULT 1.0,
  `icbaseadweight` float(3,1) DEFAULT 1.0,
  `ickwadweight` float(3,1) DEFAULT 1.0,
  `icurladweight` float(3,1) DEFAULT 1.0,
  `imgname_schonlogo` varchar(200) DEFAULT NULL,
  `contact` varchar(30) DEFAULT NULL COMMENT '담당자',
  `contact_userid` varchar(50) DEFAULT '' COMMENT '담당자아이디',
  `media_rs` tinyint(3) unsigned DEFAULT 40 COMMENT '매체피 설정',
  `shop_web` varchar(1) DEFAULT 'N',
  `shop_mobile` varchar(1) DEFAULT 'N',
  `shop_tag` varchar(100) DEFAULT NULL COMMENT '상점태그',
  `shop_weight` int(10) unsigned DEFAULT NULL,
  `co_gubun` varchar(1) DEFAULT 'C' COMMENT '개인:P, 사업자:C',
  `co_info` varchar(100) DEFAULT NULL COMMENT '사업장정보(법인명,사업자등록번호,대표자명,사업장소재지,업태/업종)',
  `ap_packege` varchar(255) DEFAULT NULL COMMENT '앱패키지',
  `ap_url` varchar(255) DEFAULT NULL COMMENT '앱 설치 URL',
  `mobile_url_select` char(5) DEFAULT NULL COMMENT '모바일경유URL사용체크,웹인지 앱인지',
  `mobile_url` varchar(255) DEFAULT NULL COMMENT '모바일웹&앱 경유 URL',
  `imgname_fvconlogo` varchar(200) DEFAULT NULL,
  `s_gubun` varchar(1) DEFAULT NULL COMMENT '광고주 구분값 (1.비광고주 0.실광고주) 기본값 0',
  `advertiser_tel` varchar(30) DEFAULT NULL COMMENT '광고주연락처',
  `advertiser_email` varchar(100) DEFAULT NULL COMMENT '광고주이메일',
  `acount_file` varchar(200) DEFAULT '' COMMENT '정산관련서류파일',
  `acount_file2` varchar(200) DEFAULT '' COMMENT '정산관련서류파일2',
  `corp_name` varchar(200) DEFAULT '' COMMENT '정산관련사업자명',
  `corp_number` varchar(200) DEFAULT '' COMMENT '정산관련사업자번호',
  `cwday` tinyint(3) unsigned DEFAULT 30 COMMENT '장바구니보관기간',
  `icover_useyn` char(1) DEFAULT 'N' COMMENT '아이커버 노출간격 사용',
  `icover_time` float DEFAULT NULL COMMENT '아이커버 노출간격시간',
  `appicon_noline` varchar(200) DEFAULT NULL,
  `login_fail` int(1) unsigned NOT NULL DEFAULT 0 COMMENT '�α��ν���Ƚ��',
  `lastlogin` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '�ֱٷα��γ�¥',
  `PWD_CHG_DT` varchar(8) NOT NULL DEFAULT '0',
  `ADMIN_MNGR` varchar(30) DEFAULT NULL COMMENT '어드민관리자|어드민관리자 설정, admember.userid||',
  `USER_STATE_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '사용자상태구분코드|사용자 상태 구분 코드|MOBON_COM_CODE.USER_STATE_TP_CODE|',
  UNIQUE KEY `userid` (`userid`),
  KEY `seq` (`seq`),
  KEY `admember_idx_01` (`ADMIN_MNGR`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='회원관리';


-- dreamsearch.adsite definition

CREATE TABLE `adsite` (
  `site_code` varchar(32) NOT NULL,
  `userid` varchar(20) NOT NULL,
  `KPI_NO` bigint(20) NOT NULL DEFAULT 0 COMMENT 'KPI순서|KPI 고유값|MOBON_AD_KPI_SETUP.KPI_NO|',
  `site_name` varchar(40) DEFAULT NULL,
  `site_url` varchar(2083) DEFAULT NULL,
  `site_title` varchar(200) DEFAULT NULL,
  `site_desc` varchar(300) DEFAULT NULL,
  `site_etc` varchar(500) DEFAULT '',
  `view_cnt` int(10) unsigned DEFAULT 0,
  `click_cnt` int(10) unsigned DEFAULT 0,
  `state` char(1) DEFAULT 'N',
  `cate` int(10) unsigned DEFAULT NULL COMMENT '광고주카테고리 MOB_CTGR_INFO.CTGR_SEQ',
  `cate2` varchar(3) DEFAULT NULL,
  `del_fg` char(1) NOT NULL,
  `gubun` varchar(2) NOT NULL DEFAULT '',
  `usemoney` int(11) DEFAULT 0,
  `use_f_day` varchar(8) DEFAULT NULL,
  `use_t_day` varchar(8) DEFAULT NULL,
  `sun` varchar(1) DEFAULT 'N',
  `mon` varchar(1) DEFAULT 'N',
  `tue` varchar(1) DEFAULT 'N',
  `wed` varchar(1) DEFAULT 'N',
  `thu` varchar(1) DEFAULT 'N',
  `fri` varchar(1) DEFAULT 'N',
  `sat` varchar(1) DEFAULT 'N',
  `reg_date` datetime DEFAULT NULL,
  `modi_date` datetime DEFAULT NULL,
  `mediacnt` int(11) DEFAULT 0,
  `popgb` varchar(1) DEFAULT 'E',
  `upseq` varchar(20) DEFAULT NULL,
  `imgname` varchar(100) DEFAULT NULL,
  `imgname2` varchar(100) DEFAULT NULL,
  `imgname3` varchar(100) DEFAULT NULL,
  `imgname4` varchar(100) DEFAULT NULL,
  `imgname5` varchar(100) DEFAULT NULL,
  `imgname6` varchar(100) DEFAULT NULL,
  `imgname7` varchar(100) DEFAULT NULL,
  `imgname8` varchar(100) DEFAULT NULL,
  `imgname9` varchar(100) DEFAULT NULL,
  `imgname10` varchar(100) DEFAULT NULL,
  `imgname11` varchar(100) DEFAULT NULL,
  `imgname12` varchar(100) DEFAULT NULL,
  `imgname13` varchar(100) DEFAULT NULL,
  `imgname14` varchar(100) DEFAULT NULL,
  `imgname15` varchar(100) DEFAULT NULL COMMENT '970 * 100',
  `imgname16` varchar(100) DEFAULT NULL COMMENT '720 * 120',
  `imgname17` varchar(100) DEFAULT NULL COMMENT '300 * 250',
  `imgname18` varchar(100) DEFAULT NULL COMMENT '200*200',
  `usedmoney` int(11) DEFAULT 0,
  `site_code_s` varchar(32) DEFAULT NULL,
  `url_style` varchar(1) DEFAULT '1',
  `shoplink` varchar(200) DEFAULT NULL,
  `sales_url` varchar(200) DEFAULT '',
  `sales_prdno` varchar(20) DEFAULT '',
  `recom_ad` char(1) DEFAULT 'Y',
  `svc_type` varchar(20) DEFAULT 'banner',
  `dispo_sex` varchar(30) DEFAULT 'M,W',
  `dispo_age` varchar(30) DEFAULT 'p,q,r,s,t,u,v,w,x' COMMENT '연령대 : p(~13),q(14~19),r(20~24),s(25~27),t(28~35),u(36~44),v(45~54),w(55~64),x(64~)',
  `new_dispo_age` varchar(30) DEFAULT 'h,i,j,k,l,m,n,o' COMMENT '나이대:h(~13),i(14~19),j(20~26),k(27~32),l(33~40),m(41~46),n(47~56),o(57~)',
  `ad_rhour` varchar(50) DEFAULT 'a,b,c,d,e,f,g,h,i,j',
  `adweight` float(3,1) DEFAULT 1.0,
  `cate_select` varchar(30) DEFAULT 'N,N,N,N,N|N,N,N,N,N,N,N' COMMENT '카테고리,부분선택 저장부분',
  `freq_weight` int(11) DEFAULT 50 COMMENT '캠페인의 광고소비자별 송출횟수 가중치',
  `freq_coverage` int(11) DEFAULT 50 COMMENT '캠페인 프리퀀시 보장률',
  `c_price` int(11) DEFAULT 0 COMMENT '캠페인별 단가',
  `pb_gubun` varchar(2) DEFAULT NULL COMMENT '퍼포먼스배너 구분 : QA(퀴즈형)',
  `important_yn` char(1) DEFAULT 'N' COMMENT '중요캠페인',
  `premium_yn` char(1) DEFAULT 'N' COMMENT '프리미엄배너사용여부',
  `premium_no` bigint(20) DEFAULT NULL COMMENT '프리미엄배너번호',
  `ADVRTS_STLE_TP_CODE` varchar(2) DEFAULT NULL COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `ALL_USE_YN` varchar(1) NOT NULL DEFAULT 'N',
  `KWRD_ADIT_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '키워드추가여부|키워드 자동저장 여부, 기본값:N|Y:자동추가, N:수동 추가|',
  `DC_FRME_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '할인프레임여부|할인 프레임 여부, 기본값:Y|Y:할인프레임 적용, N:할인프레임 미적용|',
  `ad_phone_num` varchar(20) DEFAULT NULL COMMENT '전화번호연결광고-전화번호',
  `adid_trgt_tp_code` varchar(2) DEFAULT NULL COMMENT '타게팅여부|타게팅,디타게팅구분|MOBON_COM_CODE.ADID_TRGT_TP_CODE|',
  `use_st_day` datetime DEFAULT NULL COMMENT '캠페인운영기간시작일자|캠페인 운영 기간 시작 일자||',
  `use_end_day` datetime DEFAULT NULL COMMENT '캠페인운영기간종료일자|캠페인 운영 기간 종료 일자||',
  PRIMARY KEY (`site_code`),
  UNIQUE KEY `adsite_UNQ01` (`userid`,`site_code`),
  KEY `adsite_X01` (`gubun`,`state`,`use_st_day`,`use_end_day`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='배너 캠페인 테이블';


-- dreamsearch.adsite_rtb_info definition

CREATE TABLE `adsite_rtb_info` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `site_code` varchar(32) NOT NULL COMMENT '사이트 코드',
  `userid` varchar(13) NOT NULL COMMENT '광고주 아이디',
  `svc_type` varchar(20) DEFAULT 'banner' COMMENT '서비스구분 : banner, ico, sky',
  `w_state` char(1) NOT NULL DEFAULT 'N' COMMENT '웹노출여부',
  `m_state` char(1) NOT NULL DEFAULT 'N' COMMENT '모바일노출여부',
  `rtb_price_yn` char(1) DEFAULT 'N' COMMENT 'RTB 광고용 cpc 단가 사용 여부',
  `price` int(11) DEFAULT 0 COMMENT 'cpc단가',
  `price_m` int(11) DEFAULT 0 COMMENT 'cpc 모바일 단가',
  `rtb_usemoney_yn` char(1) DEFAULT 'N' COMMENT 'RTB 일허용예산 사용 여부',
  `w_usemoney` int(11) DEFAULT 0 COMMENT '웹 일허용예산',
  `m_usemoney` int(11) DEFAULT 0 COMMENT '모바일 일허용예산',
  `w_usedmoney` int(11) DEFAULT 0 COMMENT '웹 일소진비용',
  `m_usedmoney` int(11) DEFAULT 0 COMMENT '모바일 일소진비용',
  PRIMARY KEY (`no`),
  UNIQUE KEY `idx_site_code_svc_type` (`site_code`,`svc_type`),
  KEY `idx_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='캠페인별 RTB 정보';


-- dreamsearch.convact_log definition

CREATE TABLE `convact_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `ADACTLOG_NO` bigint(20) NOT NULL,
  `PARTDT` int(8) NOT NULL,
  `IP` varchar(20) NOT NULL,
  `PCODE` varchar(100) DEFAULT NULL,
  `SHOPLOG_NO` bigint(20) DEFAULT NULL,
  `SITECODE` varchar(32) DEFAULT NULL,
  `MCODE` varchar(20) DEFAULT NULL,
  `MEDIA_CODE` varchar(13) DEFAULT NULL,
  `MEDIA_ID` varchar(13) DEFAULT NULL,
  `K_NO` varchar(13) DEFAULT NULL,
  `VCNT` int(11) DEFAULT NULL,
  `VCNT2` int(11) DEFAULT NULL,
  `CCNT` int(11) DEFAULT NULL,
  `point` decimal(13,2) DEFAULT 0.00 COMMENT '±Ý¾×(¿ø)',
  `ACTGUBUN` varchar(1) DEFAULT NULL,
  `ADGUBUN` varchar(2) DEFAULT NULL,
  `ADPRODUCT` varchar(5) DEFAULT NULL,
  `REGDATE` timestamp NOT NULL DEFAULT current_timestamp(),
  `MCGB` varchar(10) DEFAULT '',
  `CONVACTCNT` int(11) DEFAULT 1,
  PRIMARY KEY (`NO`),
  UNIQUE KEY `ADACTLOG_NO` (`ADACTLOG_NO`),
  KEY `MCODE` (`MCODE`,`PARTDT`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


-- dreamsearch.conversion_log definition

CREATE TABLE `conversion_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `PARTDT` int(8) NOT NULL,
  `IP` varchar(20) NOT NULL,
  `USERID` varchar(20) NOT NULL,
  `ORDCODE` varchar(32) DEFAULT NULL,
  `ORDRFURL` varchar(200) DEFAULT NULL,
  `ORDPCODE` varchar(100) DEFAULT NULL,
  `ORDQTY` int(5) DEFAULT 1,
  `ORDPRICE` int(11) DEFAULT 0,
  `ACTION_LOG_NO` mediumtext DEFAULT NULL,
  `REGDATE` timestamp NOT NULL DEFAULT current_timestamp(),
  `PNM` varchar(200) DEFAULT NULL,
  `UNAME` varchar(200) DEFAULT NULL,
  `USEX` char(1) DEFAULT NULL,
  `UPNO` varchar(20) DEFAULT NULL,
  `IN_HOUR` int(11) DEFAULT 0,
  `DIRECT` int(11) DEFAULT 0,
  `LAST_CLICK_TIME` varchar(14) DEFAULT NULL COMMENT '최종 광고 클릭 시간',
  `MOBON_YN` varchar(1) DEFAULT NULL COMMENT 'Y:모비온,N:타사',
  `INFLOW_ROUTE` varchar(4000) DEFAULT NULL COMMENT '유입경로',
  `RGN_IP` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`NO`,`PARTDT`),
  KEY `IP` (`IP`),
  KEY `IDX_USERID` (`USERID`,`ORDCODE`),
  KEY `CONVERSION_LOG_X01` (`PARTDT`,`RGN_IP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dreamsearch.exl_itl_info definition

CREATE TABLE `exl_itl_info` (
  `EXL_ITL_SEQ` bigint(20) unsigned DEFAULT NULL,
  `PAR_NO` int(11) DEFAULT NULL,
  `EXL_ITL_KEY` varchar(100) DEFAULT NULL,
  `MEDIA_ID` varchar(30) DEFAULT NULL,
  `EXL_ITL_ID` varchar(30) DEFAULT NULL,
  `EXL_ITL_MEDIA_NM` varchar(50) DEFAULT NULL,
  `SEND_TP_CODE` varchar(2) DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `EXL_ITL_MEMO` varchar(200) DEFAULT NULL,
  `EXL_ITL_ADDITION` varchar(2000) DEFAULT NULL,
  `LIVE_DTTM` datetime DEFAULT NULL,
  `STOP_DTTM` datetime DEFAULT NULL,
  `REG_USER_ID` varchar(30) DEFAULT NULL,
  `REG_DTTM` datetime NOT NULL,
  `ALT_USER_ID` varchar(30) DEFAULT NULL,
  `ALT_DTTM` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dreamsearch.exl_itl_unused_info definition

CREATE TABLE `exl_itl_unused_info` (
  `UNUSED_SEQ` bigint(20) unsigned DEFAULT NULL,
  `PAR_NO` int(11) DEFAULT NULL,
  `AD_EXL_TP_CODE` varchar(3) DEFAULT NULL,
  `EXL_ITL_MEMO` varchar(200) DEFAULT NULL,
  `REG_USER_ID` varchar(30) DEFAULT NULL,
  `REG_DTTM` datetime NOT NULL,
  `ALT_USER_ID` varchar(30) DEFAULT NULL,
  `ALT_DTTM` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dreamsearch.external_match definition

CREATE TABLE `external_match` (
  `no` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `zoneid` varchar(100) NOT NULL DEFAULT 'none' COMMENT '외부연동 ID',
  `media_site` int(11) NOT NULL COMMENT '연동할 매체의 ID',
  `media_code` int(11) NOT NULL COMMENT '연동할 매체의 스크립트ID',
  `userid` varchar(30) NOT NULL COMMENT '매체ID',
  `regdate` datetime DEFAULT NULL COMMENT '등록날짜',
  `state` varchar(1) DEFAULT 'Y' COMMENT '상태코드 Y,N',
  `ad_type` varchar(20) DEFAULT NULL COMMENT '사이즈형태',
  `external_id` varchar(30) DEFAULT '' COMMENT '외부연동ID',
  `external_name` varchar(45) DEFAULT NULL COMMENT '연동매체이름',
  `etc1` varchar(200) DEFAULT NULL COMMENT '기타 1 (상황에 따라 사용할 임시 컬럼)',
  `etc2` varchar(30) DEFAULT NULL COMMENT '기타 2 (상황에 따라 사용할 임시 컬럼)',
  `del_fg` char(1) DEFAULT 'N' COMMENT '삭제여부',
  `transmit` varchar(1) NOT NULL DEFAULT 'R' COMMENT 'S: 송출 / R:수신',
  `unit_name` varchar(200) DEFAULT NULL COMMENT '애드오피 API 전용 키값',
  PRIMARY KEY (`no`),
  UNIQUE KEY `index2` (`zoneid`,`media_site`,`media_code`,`userid`),
  KEY `external_match_X01` (`media_code`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dreamsearch.frme_media_info definition

CREATE TABLE `frme_media_info` (
  `MEDIA_SCRIPT_NO` int(11) NOT NULL DEFAULT 0 COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `MEDIA_SITE_NO` int(11) NOT NULL COMMENT '매체사이트번호|매체 사이트 번호|media_site.no|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `BNR_SCRIPT` varchar(1000) NOT NULL COMMENT '배너스크립트|배너 스크립트||',
  `BNR_CODE` varchar(20) NOT NULL COMMENT '배너구분코드|배너 구분 코드|dcode.dcodeno|',
  `PAR_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '지면구분코드|지면면구분코드|MOBON_COM_CODE.PAR_TP_CODE|',
  `RGN_ADVRTS_PMS_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '지역광고허용여부|지역광고허용여부, 기본값:Y|Y:지역광고허용, N:지역광고비허용|',
  `PAR_LIVE_DTTM` datetime DEFAULT NULL COMMENT '지면라이브일자|지면별 라이브 일자||',
  `EPRS_GRADE` int(11) DEFAULT NULL COMMENT '노출 등급 분류',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `PAR_GRADE` int(11) DEFAULT NULL COMMENT '등급분류',
  PRIMARY KEY (`MEDIA_SCRIPT_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='프레임매체정보|프레임매체정보||';


-- dreamsearch.iadsite definition

CREATE TABLE `iadsite` (
  `site_code` varchar(32) NOT NULL,
  `userid` varchar(20) NOT NULL,
  `KPI_NO` bigint(20) NOT NULL DEFAULT 0 COMMENT 'KPI순서|KPI 고유값|MOBON_AD_KPI_SETUP.KPI_NO|',
  `site_name` varchar(40) DEFAULT NULL,
  `site_url` varchar(2083) DEFAULT NULL,
  `site_title` varchar(200) DEFAULT NULL,
  `site_desc` varchar(300) DEFAULT NULL,
  `site_desc1` varchar(1000) DEFAULT NULL COMMENT '브랜드박스 사이트 문구',
  `site_etc` varchar(500) DEFAULT NULL,
  `view_cnt` int(10) unsigned DEFAULT 0,
  `click_cnt` int(10) unsigned DEFAULT 0,
  `state` char(1) DEFAULT 'N',
  `cate` int(10) unsigned DEFAULT NULL COMMENT '광고주카테고리 MOB_CTGR_INFO.CTGR_SEQ',
  `cate2` varchar(3) DEFAULT NULL,
  `del_fg` char(1) NOT NULL,
  `gubun` varchar(2) NOT NULL DEFAULT '',
  `usemoney` int(11) DEFAULT 0,
  `use_f_day` varchar(8) DEFAULT NULL,
  `use_t_day` varchar(8) DEFAULT NULL,
  `sun` varchar(1) DEFAULT 'N',
  `mon` varchar(1) DEFAULT 'N',
  `tue` varchar(1) DEFAULT 'N',
  `wed` varchar(1) DEFAULT 'N',
  `thu` varchar(1) DEFAULT 'N',
  `fri` varchar(1) DEFAULT 'N',
  `sat` varchar(1) DEFAULT 'N',
  `reg_date` datetime DEFAULT NULL,
  `modi_date` datetime DEFAULT NULL,
  `mediacnt` int(11) DEFAULT 0,
  `popgb` varchar(1) DEFAULT 'E',
  `upseq` varchar(20) DEFAULT NULL,
  `imgname` varchar(100) DEFAULT NULL,
  `imgname2` varchar(100) DEFAULT NULL,
  `imgname3` varchar(100) DEFAULT NULL,
  `imgname4` varchar(100) DEFAULT NULL,
  `imgname5` varchar(100) DEFAULT NULL,
  `usedmoney` int(11) DEFAULT 0,
  `site_code_s` varchar(32) DEFAULT NULL,
  `pop_style` char(255) DEFAULT '0',
  `url_style` varchar(1) DEFAULT '1',
  `shoplink` varchar(200) DEFAULT NULL,
  `sales_url` varchar(200) DEFAULT '',
  `sales_prdno` varchar(20) DEFAULT '',
  `svc_type` varchar(20) DEFAULT '',
  `recom_ad` char(1) DEFAULT 'Y',
  `dispo_sex` varchar(30) DEFAULT 'M,W',
  `dispo_age` varchar(30) DEFAULT 'p,q,r,s,t,u,v,w,x' COMMENT '연령대 : p(~13),q(14~19),r(20~24),s(25~27),t(28~35),u(36~44),v(45~54),w(55~64),x(64~)',
  `new_dispo_age` varchar(30) DEFAULT 'h,i,j,k,l,m,n,o' COMMENT '나이대:h(~13),i(14~19),j(20~26),k(27~32),l(33~40),m(41~46),n(47~56),o(57~)',
  `ad_rhour` varchar(50) DEFAULT 'a,b,c,d,e,f,g,h,i,j',
  `adweight` float(3,1) DEFAULT 1.0,
  `cate_select` varchar(30) DEFAULT 'N,N,N,N,N|N,N,N,N,N,N,N' COMMENT '카테고리,부분선택 저장부분',
  `freq_weight` int(11) DEFAULT 50 COMMENT '캠페인의 광고소비자별 송출횟수 가중치',
  `freq_coverage` int(11) DEFAULT 50 COMMENT '캠페인 프리퀀시 보장률',
  `c_price` int(11) DEFAULT 0 COMMENT '캠페인별 단가',
  `important_yn` char(1) DEFAULT 'N' COMMENT '중요캠페인',
  `ADVRTS_STLE_TP_CODE` varchar(2) DEFAULT NULL COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `ALL_USE_YN` varchar(1) NOT NULL DEFAULT 'N',
  `KWRD_ADIT_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '키워드추가여부|키워드 자동저장 여부, 기본값:N|Y:자동추가, N:수동 추가|',
  `adid_trgt_tp_code` varchar(2) DEFAULT NULL COMMENT '타게팅여부|타게팅,디타게팅구분|MOBON_COM_CODE.ADID_TRGT_TP_CODE|',
  `use_st_day` datetime DEFAULT NULL COMMENT '캠페인운영기간시작일자|캠페인 운영 기간 시작 일자||',
  `use_end_day` datetime DEFAULT NULL COMMENT '캠페인운영기간종료일자|캠페인 운영 기간 종료 일자||',
  PRIMARY KEY (`site_code`),
  UNIQUE KEY `iadsite_UNQ01` (`userid`,`site_code`),
  KEY `iadsite_X01` (`site_code_s`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='아이커버, 브랜드링크 캠페인 테이블';


-- dreamsearch.media_script definition

CREATE TABLE `media_script` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `mediasite_no` int(11) NOT NULL,
  `userid` varchar(30) NOT NULL,
  `sdate` varchar(8) NOT NULL,
  `regdate` datetime DEFAULT NULL,
  `ad_type` varchar(20) DEFAULT NULL,
  `state` varchar(1) DEFAULT 'Y',
  `h_type` varchar(20) DEFAULT 'url',
  `h_banner` text DEFAULT NULL,
  `accept_sr` char(1) DEFAULT 'Y',
  `accept_rc` char(1) DEFAULT 'Y',
  `accept_rm` char(1) DEFAULT 'Y',
  `accept_um` char(1) DEFAULT 'Y',
  `accept_kl` char(1) DEFAULT 'Y',
  `accept_ad` char(1) DEFAULT 'Y',
  `accept_st` char(1) DEFAULT 'Y',
  `accept_sp` char(1) DEFAULT 'Y',
  `accept_cc` char(1) DEFAULT 'Y',
  `accept_sa` char(1) DEFAULT 'Y',
  `accept_cw` char(1) DEFAULT 'Y' COMMENT '장바구니 광고승인여부',
  `accept_hu` char(1) DEFAULT 'Y' COMMENT '헤비유저 광고승인여부',
  `accept_pm` char(1) DEFAULT 'N' COMMENT '프리미엄배너 광고승인여부',
  `accept_pb` char(1) DEFAULT 'Y' COMMENT '퍼포먼스배너 광고승인여부',
  `accept_sj` char(1) DEFAULT 'Y' COMMENT '쇼핑입점 광고승인여부',
  `accept_mm` char(1) DEFAULT 'Y' COMMENT '검색광고 > 문맥',
  `accept_kb` char(1) DEFAULT 'Y' COMMENT '네이티브광고 > 키워드볼드',
  `accept_ib` char(1) DEFAULT 'Y' COMMENT '검색광고 > 유입',
  `accept_km` char(1) DEFAULT 'Y',
  `accept_ik` char(1) DEFAULT 'Y',
  `accept_mk` char(1) DEFAULT 'Y',
  `accept_cm` char(1) DEFAULT 'Y',
  `accept_mr` char(1) DEFAULT 'Y' COMMENT '문맥리타겟팅 광고 승인여부',
  `accept_au` char(1) DEFAULT 'Y' COMMENT '오디언스 광고 승인여부',
  `accept_gg` char(1) NOT NULL DEFAULT 'Y' COMMENT '구글 광고 승인여부',
  `accept_at` char(1) NOT NULL DEFAULT 'Y' COMMENT '앱프로파일 광고 승인여부',
  `accept_gs` char(1) NOT NULL DEFAULT 'Y' COMMENT '구글 쇼핑 광고 승인여부',
  `accept_pr` char(1) NOT NULL DEFAULT 'Y' COMMENT '프로모션 타게팅',
  `weight_pct` int(11) DEFAULT 100,
  `bid_price` int(11) DEFAULT 0 COMMENT '비딩단가',
  `price_type` varchar(3) DEFAULT NULL COMMENT '과금타입',
  `product_type` char(2) DEFAULT 'a' COMMENT 'a: 모바일띠배너320, b:모바일플로팅250, c:인트로250, d:인트로800, e:엔딩250, f:엔딩800, g:종료후800',
  `w_type` char(2) DEFAULT 'w' COMMENT '웹,모바일 구분',
  `s_type` char(2) DEFAULT 'b' COMMENT '스크립트 타입 b:배너 f:플로팅 t:토스트',
  `i_gubun` varchar(3) DEFAULT '' COMMENT 'end:엔딩팝업',
  `del_fg` char(1) DEFAULT 'N',
  `weight_type` varchar(100) DEFAULT '100,100,100,100,100,100,100,100,100,100,100,100,100,100,100' COMMENT '상품별가중치:SR,SP,UM,KL,AD,ST,RM,SA,RC,CW,PB,HU,SJ,MM,KB',
  `frameRtb_YN` varchar(1) DEFAULT 'N' COMMENT '프레임RTB사용여부',
  `m_bacon_yn` char(1) DEFAULT 'N' COMMENT '모바일 바콘 사용여부',
  `cpi_yn` char(1) DEFAULT 'N',
  `cpi_gubun` varchar(2) DEFAULT NULL COMMENT 'cpi용 지면일 경우 SN - SNS공유(List포함) CL - CPI광고 List CB - CPI 배너 광고',
  `r_gubun` varchar(2) DEFAULT 'NR' COMMENT 'CPI 리워드 여부 NR - Non Reword RE - Reword',
  `ms_icover_useyn` char(1) DEFAULT 'N' COMMENT '아이커버 노출간격 사용',
  `ms_icover_time` float DEFAULT NULL COMMENT '아이커버 노출간격시간',
  `pb_weight` int(3) DEFAULT 50 COMMENT '퍼포먼스배너가중치',
  `two_yn` varchar(1) DEFAULT 'Y' COMMENT '투뎁스사용여부',
  `mcover_type` varchar(20) DEFAULT NULL COMMENT 'touch , back , direct , nofrq',
  `psb_url` text DEFAULT NULL COMMENT 'passback info',
  `valid_click_yn` char(1) DEFAULT 'N' COMMENT '클릭 검증 여부 Y OR N',
  `MEDIA_ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '매체연동구분코드|매체연동구분코드|MOBON_COM_CODE.MEDIA_ITL_TP_CODE}',
  `ADVRTS_STLE_TP_CODE` varchar(2) NOT NULL DEFAULT '03' COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `PASS_YN` varchar(1) NOT NULL DEFAULT 'N',
  `CHRG_FOM_TP_CODE` varchar(2) DEFAULT '01' COMMENT '과금형식구분코드|과금 형식 구분코드|MOBON_COM_CODE.CHRG_SECT_TP_CODE|',
  `FQ_SETUP_MM` int(11) NOT NULL DEFAULT -1,
  `CHRG_SECT_TP_CODE` varchar(2) DEFAULT NULL COMMENT '과금구간구분코드|과구간 구분코드|MOBON_COM_CODE.CHRG_SECT_TP_CODE|',
  `CHRG_IP_AVAL_CNT` int(11) DEFAULT -1 COMMENT '과금IP유효횟수|과금금IP유효횟수|-1일경우 제한없음|',
  `EPRS_REST_RATE` int(11) DEFAULT 100 COMMENT '노출제한비율|노출제한 비율||',
  `PRTC_USE_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '프로토콜사용여부|HTTPS 프로토콜 사용여부, 기본값:N|Y:프로토콜 사용, N:프로토콜 미사용|',
  `PAR_KND_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '지면종류구분코드|지면 종류 구분코드|MOBON_COM_CODE.PAR_KND_TP_CODE|',
  `PAR_SORT_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '지면정렬구분코드|지면정렬 구분코드|MOBON_COM_CODE.PAR_SORT_TP_CODE|',
  `CR_INTRCP_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '크롭차단여부|크롭 차단 여부, 기본값:N|Y:크롬 차단, N:크롬 미차단|',
  PRIMARY KEY (`no`),
  UNIQUE KEY `media_script_UNQ_01` (`no`,`w_type`),
  KEY `mediasite_no_idx` (`mediasite_no`),
  KEY `media_script_X01` (`userid`),
  KEY `media_script_idx_01` (`PAR_KND_TP_CODE`,`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=644553 DEFAULT CHARSET=euckr COMMENT='매체 스크립트 정보';


-- dreamsearch.media_site definition

CREATE TABLE `media_site` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(30) NOT NULL,
  `sdate` varchar(8) NOT NULL,
  `regdate` datetime DEFAULT NULL,
  `sitecate` varchar(20) DEFAULT NULL,
  `sitename` varchar(150) DEFAULT NULL,
  `siteurl` varchar(250) DEFAULT NULL,
  `sitedesc` varchar(200) DEFAULT NULL,
  `sitetype` varchar(10) DEFAULT NULL,
  `state` varchar(1) DEFAULT 'Y',
  `scate` varchar(20) DEFAULT NULL,
  `cpi_sitetype` varchar(2) DEFAULT '0' COMMENT 'cpi광고 사이트 타입\n0 - API연동 사이트\n1 - SNS공유 직접 광고',
  PRIMARY KEY (`no`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=645058 DEFAULT CHARSET=euckr COMMENT='매체 사이트 관리';


-- dreamsearch.mob_adver_chrg_log definition

CREATE TABLE `mob_adver_chrg_log` (
  `SEQ` int(11) NOT NULL AUTO_INCREMENT COMMENT '순서|어뷰징로그 순서, 자동증가||',
  `YYYYMMDD` int(11) NOT NULL COMMENT '년월일|어뷰징로그 기록일||',
  `SVC_TP_CODE` varchar(2) NOT NULL COMMENT '서비스구분코드|서비스 구분 코드|MOBON_COM_CODE.SVC_TP_CODE|',
  `CHRG_TP_CODE` varchar(2) NOT NULL COMMENT '과금구분코드|과금 구분 코드|MOBON_COM_CODE.CHRG_TP_CODE|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL DEFAULT 0 COMMENT '미디어스크립트번호|미디어스크립트 번호|media_script.no|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `ORDER_NO` varchar(32) NOT NULL COMMENT '주문번호',
  `ORDER_PRC` decimal(13,2) NOT NULL COMMENT '주문가격',
  `pcode` varchar(100) NOT NULL COMMENT 'pcode|pcode|shop_data.pcode|',
  `ACL_NO` bigint(20) NOT NULL DEFAULT 0 COMMENT '액션로그|액션로그의 키|ACTION_LOG.NO|',
  `ETC` varchar(100) DEFAULT NULL COMMENT '기타|기타||',
  `USER_IDFY_VAL` varchar(100) NOT NULL COMMENT '사용자구분값|사용자 구분 값||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`SEQ`,`YYYYMMDD`),
  UNIQUE KEY `MOB_ADVER_CHRG_LOG_UNQ01` (`YYYYMMDD`,`SVC_TP_CODE`,`MEDIA_SCRIPT_NO`,`ADVER_ID`,`ORDER_NO`,`USER_IDFY_VAL`),
  KEY `MOB_ADVER_CHRG_LOG_X01` (`YYYYMMDD`,`SVC_TP_CODE`,`USER_IDFY_VAL`,`MEDIA_SCRIPT_NO`,`pcode`,`CHRG_TP_CODE`),
  KEY `MOB_ADVER_CHRG_LOG_X02` (`ADVER_ID`,`CHRG_TP_CODE`,`SVC_TP_CODE`,`YYYYMMDD`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


-- dreamsearch.mob_chrg_log definition

CREATE TABLE `mob_chrg_log` (
  `SEQ` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '순서|어뷰징로그 순서, 자동증가||',
  `YYYYMMDD` int(11) NOT NULL COMMENT '년월일|어뷰징로그 기록일||',
  `SVC_TP_CODE` varchar(2) NOT NULL COMMENT '서비스구분코드|서비스 구분 코드|MOBON_COM_CODE.SVC_TP_CODE|',
  `CHRG_TP_CODE` varchar(2) NOT NULL COMMENT '과금구분코드|과금 구분 코드|MOBON_COM_CODE.CHRG_TP_CODE|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL DEFAULT 0 COMMENT '미디어스크립트번호|미디어스크립트 번호|media_script.no|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `pcode` varchar(100) NOT NULL COMMENT 'pcode|pcode|shop_data.pcode|',
  `ETC` varchar(100) DEFAULT NULL COMMENT '기타',
  `USER_IDFY_VAL` varchar(100) NOT NULL COMMENT '사용자구분값|사용자 구분 값||',
  `POINT` decimal(13,2) DEFAULT NULL,
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`SEQ`,`YYYYMMDD`),
  KEY `MOB_CHRG_LOG_X01` (`YYYYMMDD`,`SVC_TP_CODE`,`USER_IDFY_VAL`,`MEDIA_SCRIPT_NO`,`SITE_CODE`,`pcode`,`CHRG_TP_CODE`),
  KEY `MOB_CHRG_LOG_X02` (`YYYYMMDD`,`SVC_TP_CODE`,`CHRG_TP_CODE`,`SITE_CODE`),
  KEY `MOB_CHRG_LOG_X03` (`USER_IDFY_VAL`,`YYYYMMDD`)
) ENGINE=InnoDB AUTO_INCREMENT=3005 DEFAULT CHARSET=utf8;


-- dreamsearch.mob_ctgr_info definition

CREATE TABLE `mob_ctgr_info` (
  `CTGR_SEQ` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '카테고리순서|카테고리의 고유번호, 자동증가||',
  `CTGR_SEQ_NEW` int(11) DEFAULT NULL COMMENT '카테고리코드대체컬럼',
  `USER_TP_CODE` varchar(2) NOT NULL COMMENT '사용자구분코드|사용자 구분 코드|MOBON_COM_CODE.USER_TP_CODE|',
  `CTGR_NM` varchar(50) NOT NULL COMMENT '카테고리명|카테고리 명||',
  `HIRNK_CTGR_SEQ` int(10) unsigned DEFAULT NULL COMMENT '상위카테고리순서|상위 카테고리의 번호|MOB_CTGR_INFO.CTGR_SEQ|',
  `CTGR_DEPT` int(11) NOT NULL COMMENT '카테고리깊이|카테고리의 레벨||',
  `CTGR_USE_YN` varchar(1) NOT NULL COMMENT '카테고리사용여부|카테고리 사용여부, 기본값:Y|Y:사용, N:미사용||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  `SORT` int(11) DEFAULT 0 COMMENT '정렬순서',
  PRIMARY KEY (`CTGR_SEQ`),
  KEY `MOB_CTGR_INFO_X01` (`USER_TP_CODE`,`CTGR_NM`),
  KEY `MOB_CTGR_INFO_IDX_01` (`CTGR_SEQ_NEW`,`CTGR_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=717 DEFAULT CHARSET=utf8 COMMENT='모비온카테고리정보|모비온카테고리정보||';


-- dreamsearch.mob_ctgr_user_info definition

CREATE TABLE `mob_ctgr_user_info` (
  `USER_ID` varchar(30) NOT NULL COMMENT '사용자ID|사용자 ID|admember.userid|',
  `CTGR_SEQ` int(10) unsigned NOT NULL COMMENT '카테고리순서|카테고리의 번호|MOB_CTGR_INFO.CTGR_SEQ|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`USER_ID`,`CTGR_SEQ`),
  KEY `MOB_CTGR_ADVER_INFO_X01` (`CTGR_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온카테고리사용자정보|모비온카테고리사용자정보||';


-- dreamsearch.mobon_ad_kpi_setup definition

CREATE TABLE `mobon_ad_kpi_setup` (
  `KPI_NO` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'KPI순서|KPI순서, 고유값 자동증가||',
  `KPI_NM` varchar(50) NOT NULL COMMENT 'KPI그룹명|KPI 그룹 명||',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `PLTFOM_TP_CODE` varchar(2) DEFAULT NULL COMMENT ' 웹모바일 구분 코드 ||00전체,01웹,02모바일',
  `KPI_SETUP_PRDT_CODE` varchar(2) NOT NULL COMMENT '핵심성과지표설정코드|핵심성과지표설정코드|MOBON_COM_CODE.KPI_SETUP_PRDT_CODE|',
  `KPI_SETUP_TP_CODE_01` varchar(2) NOT NULL COMMENT '핵심성과지표설정구분코드_01|핵심성과지표설정구분코드_01|MOBON_COM_CODE.KPI_SETUP_TP_CODE_01|',
  `KPI_SETUP_TP_CODE_02` varchar(2) NOT NULL COMMENT '핵심성과지표설정구분코드_02|핵심성과지표설정구분코드_02|MOBON_COM_CODE.KPI_SETUP_TP_CODE_02|',
  `KPI_SETUP_TP_CODE_03` varchar(2) DEFAULT NULL COMMENT '핵심성과지표설정구분코드_03|핵심성과지표설정구분코드_03|MOBON_COM_CODE.KPI_SETUP_TP_CODE_03|',
  `KPI_SETUP_TP_CODE_04` varchar(2) DEFAULT NULL COMMENT '핵심성과지표설정구분코드_04|핵심성과지표설정구분코드_04|MOBON_COM_CODE.KPI_SETUP_TP_CODE_04|',
  `KPI_SETUP_VAL` int(11) DEFAULT NULL COMMENT '핵심성과지표설정값|핵심 성과 지표의 값||',
  `KPI_ADMIN_MEMO` varchar(150) DEFAULT NULL COMMENT '핵심성과지표운영메모|핵심성과지표 운영 방안||',
  `KPI_MEMO` varchar(500) DEFAULT NULL COMMENT '핵심성과지표메모|핵심 성과지표 비고||',
  `CYCLE_TP_CODE` char(2) NOT NULL COMMENT ',핵심성과지표측정주기타입|핵심 성과지표 측정 주기 타입||',
  `STAND_DTTM` datetime DEFAULT NULL COMMENT '핵심성과지표측정기준일|핵심 성과지표 측정 기준일||',
  `MAIN_KPI_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '메인핵심성과지표여부|메인 핵심 성과지표 여부, 기본값:Y|Y:메인 핵심성과지표, N:서브 핵심성과지표|',
  `DD_EXHS_AMT` int(11) NOT NULL COMMENT '일소진금액|일 최대 소진 금액||',
  `MTH_BDGT_AMT` int(11) NOT NULL COMMENT '월예산금액|월 예산||',
  `START_DATE` datetime DEFAULT NULL COMMENT '광고 시작기간',
  `END_DATE` datetime DEFAULT NULL COMMENT '광고 마지막 기간',
  `DATE_UNLIMIT_YN` varchar(1) DEFAULT NULL COMMENT '기간 무제한 유무',
  `BUDGET_LIMIT_TYPE` varchar(2) DEFAULT NULL COMMENT '예산 제한 설정',
  `BUDGET_USE_TYPE` varchar(2) DEFAULT NULL COMMENT '예산 사용 방식',
  `AMOUNT_USED_PER_WEEK` int(11) NOT NULL DEFAULT 0 COMMENT '일주일 사용 금액',
  `SETTING_TYPE` varchar(2) NOT NULL DEFAULT '01' COMMENT '셋팅 유형 | 01:직접세팅, 02:AI세팅',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`KPI_NO`),
  KEY `idx_MOBON_AD_KPI_SETUP_01` (`ADVER_ID`,`KPI_SETUP_TP_CODE_01`,`KPI_SETUP_TP_CODE_02`,`KPI_SETUP_TP_CODE_03`,`KPI_SETUP_TP_CODE_04`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='광고주KPI설정|광고주KPI설정||';


-- dreamsearch.mobon_advrts_prdt_mpg_info definition

CREATE TABLE `mobon_advrts_prdt_mpg_info` (
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_MPG_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`ADVRTS_PRDT_CODE`,`ADVRTS_MPG_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='광고상품매핑정보|광고상품매핑정보||';


-- dreamsearch.mobon_com_code definition

CREATE TABLE `mobon_com_code` (
  `CODE_TP_ID` varchar(30) DEFAULT NULL,
  `CODE_ID` varchar(3) DEFAULT NULL,
  `CODE_VAL` varchar(50) DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `CODE_DESC` varchar(500) DEFAULT NULL,
  `REG_USER_ID` varchar(30) DEFAULT NULL,
  `REG_DTTM` datetime NOT NULL,
  `ALT_USER_ID` varchar(30) DEFAULT NULL,
  `ALT_DTTM` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- dreamsearch.pointlog definition

CREATE TABLE `pointlog` (
  `no` int(11) NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `userid` varchar(50) DEFAULT NULL,
  `order_number` varchar(30) DEFAULT NULL,
  `point` int(11) DEFAULT 0 COMMENT '포인트',
  `rpoint` int(11) DEFAULT NULL COMMENT '부과세제외포인트',
  `bpoint` decimal(13,2) DEFAULT NULL COMMENT 'rpoint가 충전되기 직전, 광고주의 전체 point',
  `gubun` varchar(2) DEFAULT NULL COMMENT '01:포인트충전,02:포인트정산',
  `regdate` datetime DEFAULT NULL COMMENT '등록일시',
  `pay_type` varchar(5) DEFAULT NULL COMMENT '결재타입',
  `memo` varchar(100) DEFAULT NULL COMMENT '메모',
  `corpname` varchar(100) DEFAULT NULL COMMENT '회사명',
  `uname` varchar(20) DEFAULT NULL COMMENT '사용자명',
  `YYYYMMDD` varchar(8) DEFAULT NULL,
  `REST_RSN_CODE` varchar(2) NOT NULL COMMENT '충전사유코드|충전사유 코드. 공통코드 검색|MOBON_COM_CODE.CODE_ID|',
  `SETLE_USER_ID` varchar(30) NOT NULL COMMENT '결제사용자ID|결제 사용자 ID|admember.userid|',
  PRIMARY KEY (`no`),
  KEY `pointlog_X01` (`YYYYMMDD`,`rpoint`),
  KEY `pointlog_X02` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=290859 DEFAULT CHARSET=euckr COMMENT='광고주 포인트 충전 정보';


-- dreamsearch.status_conversion definition

CREATE TABLE `status_conversion` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `sdate` varchar(8) NOT NULL,
  `ordcode` varchar(32) DEFAULT NULL,
  `userid` varchar(20) NOT NULL DEFAULT '',
  `product` varchar(10) DEFAULT NULL,
  `gubun` varchar(2) NOT NULL,
  `sitecode` varchar(40) DEFAULT NULL,
  `media_id` varchar(13) DEFAULT NULL,
  `media_code` int(11) DEFAULT NULL,
  `convtype` varchar(5) DEFAULT NULL,
  `in_hour` int(11) DEFAULT 0,
  `ordcnt` int(11) DEFAULT NULL,
  `ordpcnt` int(11) DEFAULT NULL,
  `ordprice` int(11) DEFAULT NULL,
  `REGDATE` timestamp NOT NULL DEFAULT current_timestamp(),
  `MCGB` varchar(100) DEFAULT '',
  `direct` int(11) DEFAULT 0,
  `sdate_time` varchar(2) DEFAULT NULL,
  `MOBON_YN` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`no`),
  UNIQUE KEY `unique_idx` (`ordcode`,`userid`),
  KEY `status_conversion_X02` (`media_code`,`sdate`),
  KEY `status_conversion_X01` (`userid`,`sdate`),
  KEY `status_conversion_X03` (`sdate`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='컨버전(전환) 통계';


-- dreamsearch.unexposure_action_log definition

CREATE TABLE `unexposure_action_log` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '일련번호',
  `PARTDT` int(8) NOT NULL COMMENT '기준일',
  `IP` varchar(20) NOT NULL COMMENT '사용자IP',
  `PCODE` varchar(100) DEFAULT NULL COMMENT '상품코드',
  `SHOPLOG_NO` mediumtext DEFAULT NULL COMMENT ' ',
  `SITECODE` varchar(40) DEFAULT NULL COMMENT '캠페인코드',
  `MCODE` varchar(20) DEFAULT NULL COMMENT '광고주아이디',
  `MEDIA_CODE` varchar(13) DEFAULT NULL COMMENT '지면번호',
  `MEDIA_ID` varchar(13) DEFAULT NULL COMMENT '매체사ID',
  `K_NO` varchar(13) DEFAULT NULL COMMENT '기준일',
  `VCNT` int(11) DEFAULT NULL COMMENT '노출수',
  `VCNT2` int(11) DEFAULT NULL COMMENT '노출수2',
  `CCNT` int(11) DEFAULT NULL COMMENT '클릭수',
  `point` decimal(13,2) DEFAULT 0.00 COMMENT '소진금액',
  `ACTGUBUN` varchar(1) DEFAULT NULL COMMENT '노출 클릭구분',
  `ADGUBUN` varchar(2) DEFAULT NULL COMMENT '타게팅',
  `ADPRODUCT` varchar(5) DEFAULT NULL COMMENT '광고상품',
  `REGDATE` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '등록일시',
  `MCGB` varchar(10) DEFAULT '' COMMENT '기준일',
  PRIMARY KEY (`NO`,`PARTDT`),
  KEY `UNEXPOSURE_ACTION_LOG_IDX_01` (`IP`,`PARTDT`)
) ENGINE=InnoDB AUTO_INCREMENT=5735 DEFAULT CHARSET=utf8;