/*
SQLyog Community v12.04 (64 bit)
MySQL - 10.0.34-MariaDB : Database - dreamsearch
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dreamsearch` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `dreamsearch`;

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

/*Table structure for table `media_script` */

DROP TABLE IF EXISTS `media_script`;

CREATE TABLE `media_script` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `mediasite_no` int(11) NOT NULL,
  `userid` varchar(30) NOT NULL,
  `sdate` varchar(8) NOT NULL,
  `regdate` datetime DEFAULT NULL,
  `ad_type` varchar(20) DEFAULT NULL,
  `state` varchar(1) DEFAULT 'Y',
  `h_type` varchar(20) DEFAULT 'url',
  `h_banner` text,
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
  `accept_mm` char(1) DEFAULT 'Y',
  `accept_kb` char(1) DEFAULT 'Y',
  `accept_ib` char(1) DEFAULT 'Y',
  `weight_pct` int(11) DEFAULT '100',
  `bid_price` int(11) DEFAULT '0' COMMENT '비딩단가',
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
  `cpi_gubun` varchar(2) DEFAULT NULL COMMENT 'cpi용 지면일 경우\nSN - SNS공유(List포함)\nCL - CPI광고 List\nCB - CPI 배너 광고',
  `r_gubun` varchar(2) DEFAULT 'NR' COMMENT 'CPI 리워드 여부\nNR - Non Reword\nRE - Reword',
  `ms_icover_useyn` char(1) DEFAULT 'N' COMMENT '아이커버 노출간격 사용',
  `ms_icover_time` float DEFAULT NULL COMMENT '아이커버 노출간격시간',
  `pb_weight` int(3) DEFAULT '50' COMMENT '퍼포먼스배너가중치',
  `two_yn` varchar(1) DEFAULT 'Y' COMMENT '투뎁스사용여부',
  `mcover_type` varchar(20) DEFAULT NULL COMMENT 'touch , back , direct , nofrq',
  `psb_url` text COMMENT 'passback info',
  `valid_click_yn` char(1) DEFAULT 'N' COMMENT '클릭 검증 여부 Y OR N',
  `MEDIA_ITL_TP_CODE` varchar(2) NOT NULL DEFAULT '01' COMMENT '매체연동구분코드|매체연동구분코드|MOBON_COM_CODE.MEDIA_ITL_TP_CODE}',
  `ADVRTS_STLE_TP_CODE` varchar(2) NOT NULL DEFAULT '03' COMMENT '광고형태구분코드|광고형태 구분 코드|MOBON_COM_CODE.ADVRTS_STLE_TP_CODE|',
  PRIMARY KEY (`no`),
  UNIQUE KEY `media_script_UNQ_01` (`no`,`w_type`),
  KEY `mediasite_no_idx` (`mediasite_no`),
  KEY `index` (`userid`,`state`,`del_fg`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16993 DEFAULT CHARSET=euckr COMMENT='매체 스크립트 정보';

/*Table structure for table `mobon_com_code` */

DROP TABLE IF EXISTS `mobon_com_code`;

CREATE TABLE `mobon_com_code` (
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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
