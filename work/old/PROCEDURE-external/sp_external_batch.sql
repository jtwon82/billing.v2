DELIMITER $$

-- USE `billing`$$

DROP PROCEDURE IF EXISTS `sp_external_batch`$$

CREATE PROCEDURE `sp_external_batch`(
	  IN $sdate		VARCHAR (8)	CHARACTER SET euckr	/* 등록날짜 */
	, IN $platform		VARCHAR (1)	CHARACTER SET euckr	/* 웹모바일 구분 */
	, IN $product		VARCHAR (10)	CHARACTER SET euckr	/* 상품구분 */
	, IN $ad_gubun		VARCHAR (2)	CHARACTER SET euckr	/* 광고구분 */
	, IN $site_code		VARCHAR (32)	CHARACTER SET euckr	/* 사이트코드 */
	, IN $advertiser_id	VARCHAR (20)	CHARACTER SET euckr	/* 광고주 아이디 */
	, IN $scriptuserid	VARCHAR (50)	CHARACTER SET euckr	/* 매체ID */
	, IN $media_script_no	INT (11)	/* 매체스크립트번호 */
	, IN $kno			BIGINT		/* 키워드, 성향에서 유입경로를 알수있는 no값 */
	, IN $viewcnt		INT (11)	/* 총노출 */
	, IN $clickcnt		INT (11)	/* 총클릭 */
	, IN $POINT			FLOAT (11)	/* 광고단가 */ 
	, IN $mpoint		FLOAT (11)	/* 매체정산단가 */ 
)
BEGIN
	
	INSERT INTO MOB_SITE_STATS
	(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, CLICK_CNT, ADVRTS_AMT) VALUES ($sdate, $platform, $product, $ad_gubun, $site_code, 1, $POINT)
	ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$viewcnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$viewcnt
		, CLICK_CNT=CLICK_CNT+$clickcnt, ADVRTS_AMT=ADVRTS_AMT+$POINT;
		
	INSERT INTO MOB_MEDIA_STATS
	(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, ADVER_ID, MEDIA_SCRIPT_NO, CLICK_CNT, ADVRTS_AMT, MEDIA_PYMNT_AMT) VALUES($sdate, $platform, $product, $ad_gubun, $advertiser_id, $media_script_no, 1, $POINT, $mpoint)
	ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$viewcnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$viewcnt
		, CLICK_CNT=CLICK_CNT+$clickcnt, ADVRTS_AMT=ADVRTS_AMT+$POINT, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
		
	IF $kno = 0 THEN
		INSERT INTO MOB_ADVER_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, MEDIA_SCRIPT_NO, CLICK_CNT, ADVRTS_AMT, MEDIA_PYMNT_AMT) VALUES($sdate, $platform, $product, $ad_gubun, $site_code, $media_script_no, 1, $POINT, $mpoint)
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$viewcnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$viewcnt
			, CLICK_CNT=CLICK_CNT+$clickcnt, ADVRTS_AMT=ADVRTS_AMT+$POINT, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	ELSE
		INSERT INTO MOB_KWRD_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, MEDIA_SCRIPT_NO, KWRD_APPN_NO, CLICK_CNT, ADVRTS_AMT, MEDIA_PYMNT_AMT) VALUES($sdate, $platform, $product, $ad_gubun, $site_code, $media_script_no, $kno, 1, $POINT, $mpoint)
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$viewcnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$viewcnt
			, CLICK_CNT=CLICK_CNT+$clickcnt, ADVRTS_AMT=ADVRTS_AMT+$POINT, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	END IF ;
	
END$$

DELIMITER ;