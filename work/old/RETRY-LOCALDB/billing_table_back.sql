/*
SQLyog Community v12.04 (64 bit)
MySQL - 10.0.34-MariaDB : Database - billing
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`billing` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `billing`;

/*Table structure for table `action_log_test_rec` */

DROP TABLE IF EXISTS `action_log_test_rec`;

CREATE TABLE `action_log_test_rec` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `PARTDT` int(8) NOT NULL,
  `IP` varchar(20) NOT NULL,
  `PCODE` varchar(100) DEFAULT NULL,
  `SHOPLOG_NO` mediumtext,
  `SITECODE` varchar(40) DEFAULT NULL,
  `MCODE` varchar(20) DEFAULT NULL,
  `MEDIA_CODE` varchar(13) DEFAULT NULL,
  `MEDIA_ID` varchar(13) DEFAULT NULL,
  `K_NO` varchar(13) DEFAULT NULL,
  `VCNT` int(11) DEFAULT NULL,
  `VCNT2` int(11) DEFAULT NULL,
  `CCNT` int(11) DEFAULT NULL,
  `point` decimal(13,2) DEFAULT '0.00' COMMENT '±Y¾×(¿ø)',
  `ACTGUBUN` varchar(1) DEFAULT NULL,
  `ADGUBUN` varchar(2) DEFAULT NULL,
  `ADPRODUCT` varchar(5) DEFAULT NULL,
  `REGDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MCGB` varchar(10) DEFAULT '',
  PRIMARY KEY (`NO`,`PARTDT`),
  KEY `IP` (`IP`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='액션로그(JAVA에서 사용)';

/*Table structure for table `admember_test_rec` */

DROP TABLE IF EXISTS `admember_test_rec`;

CREATE TABLE `admember_test_rec` (
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
  `point` decimal(13,2) DEFAULT '0.00' COMMENT '±?¾×(¿ø)',
  `mpoint` decimal(13,2) DEFAULT '0.00' COMMENT '웹매체피',
  `plist` text COMMENT '파트너 대행사 리스트(콤마로 구분)',
  `regdate` datetime DEFAULT NULL,
  `delflag` varchar(1) DEFAULT 'N' COMMENT '삭제플래그, Y:삭제',
  `danga` int(11) DEFAULT '0',
  `cate` varchar(3) DEFAULT '001',
  `lastupdate` datetime DEFAULT NULL,
  `MALLNM` varchar(50) DEFAULT NULL,
  `SHOPLOADURL` varchar(200) DEFAULT NULL,
  `cookieday` int(11) DEFAULT '31',
  `dispo_sex` varchar(30) DEFAULT 'M,W' COMMENT '성별',
  `dispo_age` varchar(30) DEFAULT 'a,b,c,d,e,f,g' COMMENT '나이대, a:10세이하\\nb:10세~20세\\nc:20세~30세\\nd:30세~40세\\ne:40세~50세\\nf:50세~60세\\ng:60세~70세',
  `cookie_dirsales` int(11) DEFAULT '48',
  `cookie_indirsales` int(11) DEFAULT '15',
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
  `baseadweight` float(3,1) DEFAULT '1.0',
  `kwadweight` float(3,1) DEFAULT '1.0',
  `urladweight` float(3,1) DEFAULT '1.0',
  `brbaseadweight` float(3,1) DEFAULT '1.0',
  `brkwadweight` float(3,1) DEFAULT '1.0',
  `brurladweight` float(3,1) DEFAULT '1.0',
  `icbaseadweight` float(3,1) DEFAULT '1.0',
  `ickwadweight` float(3,1) DEFAULT '1.0',
  `icurladweight` float(3,1) DEFAULT '1.0',
  `imgname_schonlogo` varchar(200) DEFAULT NULL,
  `contact` varchar(30) DEFAULT NULL COMMENT '담당자',
  `contact_userid` varchar(50) DEFAULT '' COMMENT '담당자아이디',
  `media_rs` tinyint(3) unsigned DEFAULT '40' COMMENT '매체피 설정',
  `shop_web` enum('Y','N') DEFAULT 'N',
  `shop_mobile` enum('Y','N') DEFAULT 'N',
  `shop_tag` varchar(100) DEFAULT NULL COMMENT '상점태그',
  `shop_weight` int(10) unsigned DEFAULT NULL,
  `co_gubun` enum('C','P') DEFAULT 'C' COMMENT '개인:P, 사업자:C',
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
  `cwday` tinyint(3) unsigned DEFAULT '30' COMMENT '장바구니보관기간',
  `icover_useyn` char(1) DEFAULT 'N' COMMENT '아이커버 노출간격 사용',
  `icover_time` float DEFAULT NULL COMMENT '아이커버 노출간격시간',
  `appicon_noline` varchar(200) DEFAULT NULL,
  `login_fail` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '??N????',
  `lastlogin` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '????N??',
  `PWD_CHG_DT` varchar(8) NOT NULL DEFAULT '0',
  `ADMIN_MNGR` varchar(30) DEFAULT NULL COMMENT '어드민관리자|어드민관리자 설정, admember.userid||',
  `USER_STATE_TP_CODE` varchar(2) DEFAULT '01',
  UNIQUE KEY `userid` (`userid`),
  KEY `seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='회원관리';

/*Table structure for table `adsite_rtb_info_test_rec` */

DROP TABLE IF EXISTS `adsite_rtb_info_test_rec`;

CREATE TABLE `adsite_rtb_info_test_rec` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `site_code` varchar(32) NOT NULL COMMENT '사이트 코드',
  `userid` varchar(13) NOT NULL COMMENT '광고주 아이디',
  `svc_type` varchar(20) DEFAULT 'banner' COMMENT '서비스구분 : banner, ico, sky',
  `w_state` char(1) NOT NULL DEFAULT 'N' COMMENT '웹노출여부',
  `m_state` char(1) NOT NULL DEFAULT 'N' COMMENT '모바일노출여부',
  `rtb_price_yn` char(1) DEFAULT 'N' COMMENT 'RTB 광고용 cpc 단가 사용 여부',
  `price` int(11) DEFAULT '0' COMMENT 'cpc단가',
  `price_m` int(11) DEFAULT '0' COMMENT 'cpc 모바일 단가',
  `rtb_usemoney_yn` char(1) DEFAULT 'N' COMMENT 'RTB 일허용예산 사용 여부',
  `w_usemoney` int(11) DEFAULT '0' COMMENT '웹 일허용예산',
  `m_usemoney` int(11) DEFAULT '0' COMMENT '모바일 일허용예산',
  `w_usedmoney` int(11) DEFAULT '0' COMMENT '웹 일소진비용',
  `m_usedmoney` int(11) DEFAULT '0' COMMENT '모바일 일소진비용',
  PRIMARY KEY (`no`),
  UNIQUE KEY `idx_site_code_svc_type` (`site_code`,`svc_type`),
  KEY `idx_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='캠페인별 RTB 정보';

/*Table structure for table `adsite_test_rec` */

DROP TABLE IF EXISTS `adsite_test_rec`;

CREATE TABLE `adsite_test_rec` (
  `site_code` varchar(32) NOT NULL,
  `userid` varchar(20) NOT NULL,
  `site_name` varchar(40) DEFAULT NULL,
  `site_url` varchar(2083) DEFAULT NULL,
  `site_title` varchar(200) DEFAULT NULL,
  `site_desc` varchar(300) DEFAULT NULL,
  `site_etc` varchar(500) DEFAULT '',
  `view_cnt` int(10) unsigned DEFAULT '0',
  `click_cnt` int(10) unsigned DEFAULT '0',
  `state` char(1) DEFAULT 'N',
  `cate` varchar(20) DEFAULT NULL,
  `cate2` varchar(3) DEFAULT NULL,
  `del_fg` char(1) NOT NULL,
  `gubun` varchar(2) NOT NULL DEFAULT '',
  `usemoney` int(11) DEFAULT '0',
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
  `mediacnt` int(11) DEFAULT '0',
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
  `usedmoney` int(11) DEFAULT '0',
  `site_code_s` varchar(32) DEFAULT NULL,
  `url_style` varchar(1) DEFAULT '1',
  `shoplink` varchar(200) DEFAULT NULL,
  `sales_url` varchar(200) DEFAULT '',
  `sales_prdno` varchar(20) DEFAULT '',
  `recom_ad` char(1) DEFAULT 'Y',
  `svc_type` varchar(20) DEFAULT 'banner',
  `dispo_sex` varchar(30) DEFAULT 'M,W',
  `dispo_age` varchar(30) DEFAULT 'a,b,c,d,e,f,g',
  `ad_rhour` varchar(50) DEFAULT 'a,b,c,d,e,f,g,h,i,j',
  `adweight` float(3,1) DEFAULT '1.0',
  `cate_select` varchar(30) DEFAULT 'N,N,N,N,N|N,N,N,N,N,N,N' COMMENT '카테고리,부분선택 저장부분',
  `freq_weight` int(11) DEFAULT '50' COMMENT '캠페인의 광고소비자별 송출횟수 가중치',
  `freq_coverage` int(11) DEFAULT '50' COMMENT '캠페인 프리퀀시 보장률',
  `c_price` int(11) DEFAULT '0' COMMENT '캠페인별 단가',
  `pb_gubun` varchar(2) DEFAULT NULL COMMENT '퍼포먼스배너 구분 : QA(퀴즈형)',
  `important_yn` char(1) DEFAULT 'N' COMMENT '중요캠페인',
  `premium_yn` char(1) DEFAULT 'N' COMMENT '프리미엄배너사용여부',
  `premium_no` bigint(20) DEFAULT NULL COMMENT '프리미엄배너번호',
  `ADVRTS_STLE_TP_CODE` varchar(2) DEFAULT NULL COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `ALL_USE_YN` varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`site_code`),
  KEY `userinfo` (`userid`),
  KEY `gubun` (`gubun`),
  KEY `adsite_X01` (`gubun`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='배너 캠페인 테이블';

/*Table structure for table `external_report_test_rec` */

DROP TABLE IF EXISTS `external_report_test_rec`;

CREATE TABLE `external_report_test_rec` (
  `sdate` varchar(8) NOT NULL COMMENT '날짜',
  `zoneid` varchar(100) NOT NULL DEFAULT 'none' COMMENT '외부연동 키값',
  `userid` varchar(30) NOT NULL COMMENT '외부연동 아이디',
  `media_site` int(11) NOT NULL COMMENT '사용자(매체)ID',
  `media_code` int(11) NOT NULL COMMENT '사용자(매체) 스크립트',
  `media_id` varchar(30) NOT NULL COMMENT '매체ID (사용자ID)',
  `ad_type` varchar(20) DEFAULT NULL COMMENT '사이즈형태',
  `totalcall` int(11) DEFAULT '0' COMMENT '''광고 총 노출 수''',
  `viewcnt` int(11) DEFAULT '0' COMMENT '외부연동의 노출값',
  `viewcnt_mobon` int(11) DEFAULT '0' COMMENT '인라이플의 노출수',
  `clickcnt` int(11) DEFAULT '0' COMMENT '외부연동의 클릭수',
  `clickcnt_mobon` int(11) DEFAULT '0' COMMENT '인라이플의 클릭수',
  `imv` float DEFAULT '0' COMMENT '노출값',
  `point` decimal(13,2) DEFAULT '0.00' COMMENT '±?¾×(¿ø)',
  `ppoint` float DEFAULT '0' COMMENT '특수한 처리를 위한 임시값 (가중치 처리 등등)',
  `regdate` datetime DEFAULT NULL COMMENT '등록날짜',
  `gubun` varchar(2) DEFAULT '0' COMMENT '광고구분자(AD:베이스광고, SR: 본상품)',
  `passback` int(11) DEFAULT '0' COMMENT '패스백 수',
  `transmit` varchar(1) DEFAULT 'R' COMMENT 'S: 송출 / R:수신',
  PRIMARY KEY (`sdate`,`zoneid`,`userid`,`media_site`,`media_code`,`media_id`),
  KEY `external_report_X01` (`sdate`,`transmit`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='외부연동 통계';

/*Table structure for table `iadsite_test_rec` */

DROP TABLE IF EXISTS `iadsite_test_rec`;

CREATE TABLE `iadsite_test_rec` (
  `site_code` varchar(32) NOT NULL,
  `userid` varchar(20) NOT NULL,
  `site_name` varchar(40) DEFAULT NULL,
  `site_url` varchar(2083) DEFAULT NULL,
  `site_title` varchar(200) DEFAULT NULL,
  `site_desc` varchar(300) DEFAULT NULL,
  `site_desc1` varchar(1000) DEFAULT NULL COMMENT '브랜드박스 사이트 문구',
  `site_etc` varchar(500) DEFAULT NULL,
  `view_cnt` int(10) unsigned DEFAULT '0',
  `click_cnt` int(10) unsigned DEFAULT '0',
  `state` char(1) DEFAULT 'N',
  `cate` varchar(20) DEFAULT NULL,
  `cate2` varchar(3) DEFAULT NULL,
  `del_fg` char(1) NOT NULL,
  `gubun` varchar(2) NOT NULL DEFAULT '',
  `usemoney` int(11) DEFAULT '0',
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
  `mediacnt` int(11) DEFAULT '0',
  `popgb` varchar(1) DEFAULT 'E',
  `upseq` varchar(20) DEFAULT NULL,
  `imgname` varchar(100) DEFAULT NULL,
  `imgname2` varchar(100) DEFAULT NULL,
  `imgname3` varchar(100) DEFAULT NULL,
  `imgname4` varchar(100) DEFAULT NULL,
  `imgname5` varchar(100) DEFAULT NULL,
  `usedmoney` int(11) DEFAULT '0',
  `site_code_s` varchar(32) DEFAULT NULL,
  `pop_style` char(255) DEFAULT '0',
  `url_style` varchar(1) DEFAULT '1',
  `shoplink` varchar(200) DEFAULT NULL,
  `sales_url` varchar(200) DEFAULT '',
  `sales_prdno` varchar(20) DEFAULT '',
  `svc_type` varchar(20) DEFAULT '',
  `recom_ad` char(1) DEFAULT 'Y',
  `dispo_sex` varchar(30) DEFAULT 'M,W',
  `dispo_age` varchar(30) DEFAULT 'a,b,c,d,e,f,g',
  `ad_rhour` varchar(50) DEFAULT 'a,b,c,d,e,f,g,h,i,j',
  `adweight` float(3,1) DEFAULT '1.0',
  `cate_select` varchar(30) DEFAULT 'N,N,N,N,N|N,N,N,N,N,N,N' COMMENT '카테고리,부분선택 저장부분',
  `freq_weight` int(11) DEFAULT '50' COMMENT '캠페인의 광고소비자별 송출횟수 가중치',
  `freq_coverage` int(11) DEFAULT '50' COMMENT '캠페인 프리퀀시 보장률',
  `c_price` int(11) DEFAULT '0' COMMENT '캠페인별 단가',
  `important_yn` char(1) DEFAULT 'N' COMMENT '중요캠페인',
  `ADVRTS_STLE_TP_CODE` varchar(2) DEFAULT NULL COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  `ALL_USE_YN` varchar(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`site_code`),
  KEY `userinfo` (`userid`),
  KEY `idx_site_code_s` (`site_code_s`),
  KEY `gubun` (`gubun`)
) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='아이커버, 브랜드링크 캠페인 테이블';

/*Table structure for table `mob_adver_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_adver_hh_stats_rec`;

CREATE TABLE `mob_adver_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ADVER_ID`,`ITL_TP_CODE`),
  KEY `MOB_ADVER_HH_STATS_REC_X01` (`ADVER_ID`,`STATS_DTTM`,`STATS_HH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온광고주별시간통계|모비온광고주별시간통계||';

/*Table structure for table `mob_adver_media_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_adver_media_hh_stats_rec`;

CREATE TABLE `mob_adver_media_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ADVER_ID`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_ADVER_MEDIA_STATS_X01` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`),
  KEY `MOB_ADVER_MEDIA_STATS_X02` (`ADVER_ID`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온광고주매체시간통계|모비온광고주매체시간통계||';

/*Table structure for table `mob_adver_media_stats_rec` */

DROP TABLE IF EXISTS `mob_adver_media_stats_rec`;

CREATE TABLE `mob_adver_media_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분  코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ADVER_ID`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_ADVER_MEDIA_STATS_REC_X01` (`MEDIA_SCRIPT_NO`),
  KEY `MOB_ADVER_MEDIA_STATS_REC_X02` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`),
  KEY `MOB_ADVER_MEDIA_STATS_REC_X03` (`ADVER_ID`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온광고주매체통계|모비온광고주매체통계||';

/*Table structure for table `mob_adver_stats_rec` */

DROP TABLE IF EXISTS `mob_adver_stats_rec`;

CREATE TABLE `mob_adver_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ADVER_ID` varchar(30) NOT NULL COMMENT '광고주ID|광고주 ID|admember.userid|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분  코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ADVER_ID`,`ITL_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온매체별통계|모비온매체별통계||';

/*Table structure for table `mob_camp_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_camp_hh_stats_rec`;

CREATE TABLE `mob_camp_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`ITL_TP_CODE`),
  KEY `MOB_CAMP_STATS_X01` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온캠페인시간통계|모비온캠페인시간통계||';

/*Table structure for table `mob_camp_media_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_camp_media_hh_stats_rec`;

CREATE TABLE `mob_camp_media_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_CAMP_MEDIA_STATS_X01` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온캠페인매체시간통계|모비온캠페인매체시간통계||';

/*Table structure for table `mob_camp_media_stats_rec` */

DROP TABLE IF EXISTS `mob_camp_media_stats_rec`;

CREATE TABLE `mob_camp_media_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_CAMP_MEDIA_STATS_REC_X01` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온캠페인매체통계|모비온캠페인매체통계||';

/*Table structure for table `mob_camp_stats_rec` */

DROP TABLE IF EXISTS `mob_camp_stats_rec`;

CREATE TABLE `mob_camp_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`ITL_TP_CODE`),
  KEY `MOB_CAMP_STATS_REC_X01` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온캠페인통계|모비온캠페인통계||';

/*Table structure for table `mob_cnvrs_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_cnvrs_hh_stats_rec`;

CREATE TABLE `mob_cnvrs_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `SESION_SELNG_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '세션매출여부|세션 매출여부, 기본값 N|Y:세션매출, N:세션아님|',
  `DIRECT_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '직접여부|직간접 여부, 기본값 N|Y:직접, N:간접|',
  `CLICK_TP` varchar(1) NOT NULL DEFAULT 'V' COMMENT '클릭부분|클릭 구분, 기본값 노출|V:노출, C:클릭|',
  `CNVRS_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '전환구분코드|전환구분코드, 기본값 01|MOBON_COM_CODE.CNVRS_TP_CODE|\r\n	',
  `MOB_ORDER_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '모비온주문여부|모비온 주문여부, 기본값 N|Y:모비온 주문, N:모비온 주문아님|',
  `ORDER_AMT` int(11) NOT NULL DEFAULT '0' COMMENT '주문금액|주문금액||',
  `ORDER_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '주문횟수|주문횟수||',
  `ORDER_QY` int(11) NOT NULL DEFAULT '0' COMMENT '주문수량|주문수량||',
  `TRGT_ORDER_AMT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문금액|타겟팅 주문금액||',
  `TRGT_ORDER_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문횟수|타겟팅 주문횟수||',
  `TRGT_ORDER_QY` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문수량|타겟팅 주문수량||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`,`SESION_SELNG_YN`,`DIRECT_YN`,`CLICK_TP`,`CNVRS_TP_CODE`,`MOB_ORDER_YN`),
  KEY `MOB_CNVRS_STATS_X01` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`),
  KEY `MOB_CNVRS_STATS_X02` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온전환통계|모비온전환통계||';

/*Table structure for table `mob_cnvrs_stats_rec` */

DROP TABLE IF EXISTS `mob_cnvrs_stats_rec`;

CREATE TABLE `mob_cnvrs_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `SESION_SELNG_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '세션매출여부|세션 매출여부, 기본값 N|Y:세션매출, N:세션아님|',
  `DIRECT_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '직접여부|직간접 여부, 기본값 N|Y:직접, N:간접|',
  `CLICK_TP` varchar(1) NOT NULL DEFAULT 'V' COMMENT '클릭부분|클릭 구분, 기본값 노출|V:노출, C:클릭|',
  `CNVRS_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '전환구분코드|전환구분코드, 기본값 01|MOBON_COM_CODE.CNVRS_TP_CODE|\r\n	',
  `MOB_ORDER_YN` varchar(1) NOT NULL DEFAULT 'N' COMMENT '모비온주문여부|모비온 주문여부, 기본값 N|Y:모비온 주문, N:모비온 주문아님|',
  `ORDER_AMT` int(11) NOT NULL DEFAULT '0' COMMENT '주문금액|주문금액||',
  `ORDER_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '주문횟수|주문횟수||',
  `ORDER_QY` int(11) NOT NULL DEFAULT '0' COMMENT '주문수량|주문수량||',
  `TRGT_ORDER_AMT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문금액|타겟팅 주문금액||',
  `TRGT_ORDER_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문횟수|타겟팅 주문횟수||',
  `TRGT_ORDER_QY` int(11) NOT NULL DEFAULT '0' COMMENT '타겟주문수량|타겟팅 주문수량||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`,`SESION_SELNG_YN`,`DIRECT_YN`,`CLICK_TP`,`CNVRS_TP_CODE`,`MOB_ORDER_YN`),
  KEY `MOB_CNVRS_STATS_REC_X01` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`),
  KEY `MOB_CNVRS_STATS_REC_X02` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온전환통계|모비온전환통계||';

/*Table structure for table `mob_com_flag_info` */

DROP TABLE IF EXISTS `mob_com_flag_info`;

CREATE TABLE `mob_com_flag_info` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온공통플래그정보|모비온공통플래그정보|시간별 통계테이블에 적제된 데이터를 시간단위로 일자테이블에 적제할 플래그테이블|';

/*Table structure for table `mob_com_hh_stats_info_rec` */

DROP TABLE IF EXISTS `mob_com_hh_stats_info_rec`;

CREATE TABLE `mob_com_hh_stats_info_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ITL_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온공통시간통계|모비온 시간 통계 공통 정보|통계 기본 데이터 통계에 사용할 데이터는 미리 저장되어야 함.|';

/*Table structure for table `mob_com_stats_info_rec` */

DROP TABLE IF EXISTS `mob_com_stats_info_rec`;

CREATE TABLE `mob_com_stats_info_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`ITL_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온통계공통|모비온 통계 공통 정보|통계 기본 데이터 통계에 사용할 데이터는 미리 저장되어야 함.|';

/*Table structure for table `mob_kwrd_stats_rec` */

DROP TABLE IF EXISTS `mob_kwrd_stats_rec`;

CREATE TABLE `mob_kwrd_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `SITE_CODE` varchar(40) NOT NULL COMMENT '사이트코드|사이트 코드|adsite.site_code|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `KWRD_APPN_NO` bigint(20) unsigned NOT NULL COMMENT '키워드지정번호|키워드지정 고유번호||',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`SITE_CODE`,`MEDIA_SCRIPT_NO`,`KWRD_APPN_NO`,`ITL_TP_CODE`),
  KEY `MOB_KWRD_STATS_REC_X01` (`SITE_CODE`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온키워드통계|모비온키워드통계||';

/*Table structure for table `mob_media_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_media_hh_stats_rec`;

CREATE TABLE `mob_media_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체아이디|매체 아이디admember.userid|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`MEDIA_ID`,`ITL_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온매체사시간통계|모비온매체사시간통계||';

/*Table structure for table `mob_media_script_hh_stats_rec` */

DROP TABLE IF EXISTS `mob_media_script_hh_stats_rec`;

CREATE TABLE `mob_media_script_hh_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `STATS_HH` varchar(2) NOT NULL COMMENT '통계시간|통계의 시간을 기록|00 ~ 23|',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `MEDIA_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '매체구분코드|매체구분코드, 기본값 01||MOBON_COM_CODE.MEDIA_TP_CODE|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체아이디|매체 아이디admember.userid|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`STATS_HH`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_MEDIA_SCRIPT_STATS_X01` (`MEDIA_ID`,`STATS_DTTM`),
  KEY `MOB_MEDIA_SCRIPT_STATS_X02` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온매체스크립트시간통계|모비온매체스크립트시간통계||';

/*Table structure for table `mob_media_script_stats_rec` */

DROP TABLE IF EXISTS `mob_media_script_stats_rec`;

CREATE TABLE `mob_media_script_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `MEDIA_SCRIPT_NO` int(11) NOT NULL COMMENT '매체스크립트번호|매체스크립트 번호|media_script.no|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `MEDIA_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '매체구분코드|매체구분코드, 기본값 01||MOBON_COM_CODE.MEDIA_TP_CODE|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체아이디|매체 아이디admember.userid|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`MEDIA_SCRIPT_NO`,`ITL_TP_CODE`),
  KEY `MOB_MEDIA_SCRIPT_STATS_REC_X01` (`MEDIA_ID`,`STATS_DTTM`),
  KEY `MOB_MEDIA_SCRIPT_STATS_REC_X02` (`MEDIA_SCRIPT_NO`,`STATS_DTTM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온매체스크립트통계|모비온매체스크립트통계||';

/*Table structure for table `mob_media_stats_rec` */

DROP TABLE IF EXISTS `mob_media_stats_rec`;

CREATE TABLE `mob_media_stats_rec` (
  `STATS_DTTM` int(11) NOT NULL COMMENT '통계일자|통계의 일자를 기록||',
  `PLTFOM_TP_CODE` varchar(2) NOT NULL COMMENT '플랫폼구분코드|플랫폼 구분 코드|MOBON_COM_CODE.PLTFOM_TP_CODE|',
  `ADVRTS_PRDT_CODE` varchar(2) NOT NULL COMMENT '광고상품코드|광고 상품 코드|MOBON_COM_CODE.ADVRTS_PRDT_CODE|',
  `ADVRTS_TP_CODE` varchar(2) NOT NULL COMMENT '광고구분코드|광고 구분 코드|MOBON_COM_CODE.ADVRTS_TP_CODE|',
  `MEDIA_ID` varchar(30) NOT NULL COMMENT '매체아이디|매체 아이디admember.userid|',
  `ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '연동구분코드|연동 구분 코드, 기본값 ''01''|MOBON_COM_CODE.ITL_TP_CODE|',
  `TOT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '총노출횟수|총 노출 횟수||',
  `PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '지면노출횟수|지면 노출 횟수||',
  `CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '클릭횟수|클릭 횟수||',
  `ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '광고금액|광고 금액||',
  `MEDIA_PYMNT_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '매체지급금액|매체지급금액||',
  `TRGT_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟노출횟수|타겟 노출 횟수||',
  `TRGT_PAR_EPRS_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타겟지면노출횟수|타겟지면 노출 횟수||',
  `TRGT_CLICK_CNT` int(11) NOT NULL DEFAULT '0' COMMENT '타켓클릭횟수|타겟 클릭 횟수||',
  `TRGT_ADVRTS_AMT` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '타겟광고금액|타겟 광고금액||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록||',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`STATS_DTTM`,`PLTFOM_TP_CODE`,`ADVRTS_PRDT_CODE`,`ADVRTS_TP_CODE`,`MEDIA_ID`,`ITL_TP_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='모비온매체별통계|모비온매체별통계||';

/*Table structure for table `mob_shop_data_test_rec` */

DROP TABLE IF EXISTS `mob_shop_data_test_rec`;

CREATE TABLE `mob_shop_data_test_rec` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERID` varchar(50) CHARACTER SET euckr NOT NULL,
  `PCODE` varchar(100) CHARACTER SET euckr DEFAULT NULL,
  `croChk` char(1) CHARACTER SET euckr DEFAULT 'N' COMMENT '????',
  `url` varchar(2000) DEFAULT NULL,
  `PNM` varchar(100) DEFAULT NULL COMMENT '?????',
  `PRICE` int(11) DEFAULT '0',
  `IMGPATH` varchar(2000) CHARACTER SET euckr DEFAULT NULL,
  `purl` varchar(2000) DEFAULT NULL,
  `GB` varchar(2) CHARACTER SET euckr DEFAULT '02',
  `RDATE` varchar(8) CHARACTER SET euckr DEFAULT NULL,
  `RTIME` varchar(4) CHARACTER SET euckr DEFAULT NULL,
  `STATUS` char(1) CHARACTER SET euckr DEFAULT 'Y',
  `CATE1` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CATE2` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CATE3` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CATE4` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CAID1` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CAID2` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CAID3` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `CAID4` varchar(50) CHARACTER SET euckr DEFAULT NULL,
  `LASTUPDATE` datetime DEFAULT NULL,
  `regdate` datetime DEFAULT NULL,
  `LOADINFO` varchar(1) CHARACTER SET euckr DEFAULT NULL,
  `down_ok` varchar(1) CHARACTER SET euckr DEFAULT 'N',
  `width` varchar(4) CHARACTER SET euckr DEFAULT NULL,
  `height` varchar(4) CHARACTER SET euckr DEFAULT NULL,
  `liveChk` varchar(1) CHARACTER SET euckr DEFAULT 'Y',
  `kakao_status` char(1) NOT NULL DEFAULT 'W' COMMENT '??? ?? A:??, W:??, R:??',
  `PBLICT_STOP_DTTM` datetime DEFAULT NULL,
  PRIMARY KEY (`NO`),
  UNIQUE KEY `match2` (`USERID`,`PCODE`),
  KEY `search_index` (`USERID`,`RDATE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mobon_com_code_bak` */

DROP TABLE IF EXISTS `mobon_com_code_bak`;

CREATE TABLE `mobon_com_code_bak` (
  `CODE_TP_ID` varchar(30) NOT NULL COMMENT '코드유형|코드 유형을 관리 각 컬럼의 값을 사용||',
  `CODE_ID` varchar(3) NOT NULL COMMENT '코드ID|코드의 ID를 사용. 기본은 01 부터 사용 확장은 001 로 사용||',
  `CODE_VAL` varchar(50) NOT NULL COMMENT '코드값|코드의 설정된 값을 사용||',
  `USE_YN` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '사용여부|사용여부|Y:차단사용, N:차단 미사용|',
  `CODE_DESC` varchar(500) DEFAULT NULL COMMENT '코드설명|코드의 설명||',
  `REG_USER_ID` varchar(30) NOT NULL DEFAULT 'ADMIN' COMMENT '등록사용자ID|등록사용자ID를 기록|admember.userid|',
  `REG_DTTM` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자|등록시간을 기록|DB버전 올리면 변경. 기본값 없음.|',
  `ALT_USER_ID` varchar(30) DEFAULT NULL COMMENT '수정사용자ID|수정사용자ID를 기록|admember.userid|',
  `ALT_DTTM` datetime DEFAULT NULL COMMENT '수정일자|수정시간을 기록||',
  PRIMARY KEY (`CODE_TP_ID`,`CODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='공통코드|모비온 사용 공통 코드||';

/*Table structure for table `shop_data_test_rec` */

DROP TABLE IF EXISTS `shop_data_test_rec`;

CREATE TABLE `shop_data_test_rec` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERID` varchar(50) NOT NULL,
  `PCODE` varchar(100) DEFAULT NULL,
  `croChk` char(1) DEFAULT 'N' COMMENT '????',
  `url` varchar(2000) DEFAULT NULL,
  `PNM` varchar(100) DEFAULT NULL COMMENT '?????',
  `PRICE` int(11) DEFAULT '0',
  `IMGPATH` varchar(2000) DEFAULT NULL,
  `purl` varchar(2000) DEFAULT NULL,
  `GB` varchar(2) DEFAULT '02',
  `RDATE` varchar(8) DEFAULT NULL,
  `RTIME` varchar(4) DEFAULT NULL,
  `STATUS` char(1) DEFAULT 'Y',
  `CATE1` varchar(50) DEFAULT NULL,
  `CATE2` varchar(50) DEFAULT NULL,
  `CATE3` varchar(50) DEFAULT NULL,
  `CATE4` varchar(50) DEFAULT NULL,
  `CAID1` varchar(50) DEFAULT NULL,
  `CAID2` varchar(50) DEFAULT NULL,
  `CAID3` varchar(50) DEFAULT NULL,
  `CAID4` varchar(50) DEFAULT NULL,
  `LASTUPDATE` datetime DEFAULT NULL,
  `regdate` datetime DEFAULT NULL,
  `LOADINFO` varchar(1) DEFAULT NULL,
  `down_ok` varchar(1) DEFAULT 'N',
  `width` varchar(4) DEFAULT NULL,
  `height` varchar(4) DEFAULT NULL,
  `liveChk` varchar(1) DEFAULT 'Y',
  `kakao_status` char(1) NOT NULL DEFAULT 'W' COMMENT '??? ?? A:??, W:??, R:??',
  `PBLICT_STOP_DTTM` datetime DEFAULT NULL,
  PRIMARY KEY (`NO`),
  UNIQUE KEY `match2` (`USERID`,`PCODE`),
  KEY `search_index` (`USERID`,`RDATE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stats_shopdata_mob_test_rec` */

DROP TABLE IF EXISTS `stats_shopdata_mob_test_rec`;

CREATE TABLE `stats_shopdata_mob_test_rec` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `sdate` varchar(8) NOT NULL,
  `userid` varchar(50) NOT NULL,
  `pcode` varchar(100) NOT NULL,
  `cate1` varchar(50) DEFAULT NULL,
  `viewcnt` int(11) DEFAULT '0',
  `adviewcnt` int(11) DEFAULT '0',
  `adclickcnt` int(11) DEFAULT '0',
  `adconvcnt` int(11) DEFAULT '0',
  `adconvprice` int(11) DEFAULT '0',
  `regdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clickcnt` int(11) DEFAULT '0' COMMENT '광고주상품 클릭 카운트',
  PRIMARY KEY (`NO`,`sdate`),
  UNIQUE KEY `sucp` (`sdate`,`userid`,`cate1`,`pcode`),
  KEY `sucp2` (`userid`,`cate1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `stats_shopdata_new_test_rec` */

DROP TABLE IF EXISTS `stats_shopdata_new_test_rec`;

CREATE TABLE `stats_shopdata_new_test_rec` (
  `NO` bigint(20) NOT NULL AUTO_INCREMENT,
  `sdate` varchar(9) NOT NULL,
  `userid` varchar(50) NOT NULL,
  `pcode` varchar(100) NOT NULL,
  `cate1` varchar(50) DEFAULT NULL,
  `viewcnt` int(11) DEFAULT '0',
  `adviewcnt` int(11) DEFAULT '0',
  `adclickcnt` int(11) DEFAULT '0',
  `adconvcnt` int(11) DEFAULT '0',
  `adconvprice` int(11) DEFAULT '0',
  `regdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `clickcnt` int(11) DEFAULT '0' COMMENT '광고주상품 클릭 카운트',
  PRIMARY KEY (`NO`,`sdate`),
  UNIQUE KEY `sucp` (`sdate`,`userid`,`cate1`,`pcode`),
  KEY `sucp2` (`userid`,`cate1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `total_status_time` */

DROP TABLE IF EXISTS `total_status_time`;

CREATE TABLE `total_status_time` (
  `no` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '자동증가값',
  `sdate` varchar(20) NOT NULL COMMENT '날짜',
  `time` varchar(20) NOT NULL COMMENT '아이디',
  `l_gubun` char(3) DEFAULT NULL,
  `gubun` char(2) DEFAULT NULL,
  `w_vcnt` int(10) DEFAULT NULL,
  `m_vcnt` int(10) DEFAULT NULL,
  `w_ccnt` int(10) DEFAULT NULL,
  `m_ccnt` int(10) DEFAULT NULL,
  `w_point` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT 'À¥ ÁöÃâ±Ý¾×',
  `m_point` decimal(13,2) NOT NULL DEFAULT '0.00' COMMENT '¸ð¹ÙÀÏ ÁöÃâ±Ý¾×',
  `ordpricev` int(10) DEFAULT NULL,
  `ordcntc` int(10) DEFAULT NULL,
  `ordpricec` int(10) DEFAULT NULL,
  `ordprice24` int(10) DEFAULT NULL,
  `direct` int(10) DEFAULT NULL,
  `mordpricev` int(10) DEFAULT NULL,
  `mordcntc` int(10) DEFAULT NULL,
  `mordpricec` int(10) DEFAULT NULL,
  `mordprice24` int(10) DEFAULT NULL,
  `mdirect` int(10) DEFAULT NULL,
  `w_vcnt2` int(10) DEFAULT NULL,
  `m_vcnt2` int(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `search` (`sdate`,`time`,`gubun`,`l_gubun`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=702732 DEFAULT CHARSET=utf8 COMMENT='광고주용 보고서홈 통계';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
