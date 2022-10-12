DELIMITER $$

-- USE `dreamsearch`$$

DROP PROCEDURE IF EXISTS `sp_banner_view`$$

CREATE PROCEDURE `sp_banner_view`(
	  IN $sdate		VARCHAR (8)	CHARACTER SET euckr	/* 등록날짜 */
	, IN $platform		VARCHAR (1)	CHARACTER SET euckr	/* 웹모바일 구분 */
	, IN $product		VARCHAR (10)	CHARACTER SET euckr	/* 상품구분 */
	, IN $ad_gubun		VARCHAR (2)	CHARACTER SET euckr	/* 광고구분 */
	, IN $site_code		VARCHAR (32)	CHARACTER SET euckr	/* 사이트코드 */
	, IN $advertiser_id	VARCHAR (20)	CHARACTER SET euckr	/* 광고주 아이디 */
	, IN $scriptuserid	VARCHAR (50)	CHARACTER SET euckr	/* 매체ID */
	, IN $media_script_no	INT (11)	/* 매체스크립트번호 */
	, IN $kno		BIGINT		/* 키워드, 성향에서 유입경로를 알수있는 no값 */
	, IN $POINT		FLOAT (11)	/* 광고단가 */ 
	, IN $mpoint		FLOAT (11)	/* 매체정산단가 */ 
	, IN $tview_cnt		INT (11)	/* 총노출 */
	, IN $sview_cnt		INT (11)	/* 광고주구좌노출 */
	, IN $viewcnt3		INT (11)	/* 매체구좌노출 */
)
BEGIN
	-- 배너 클릭 처리하는 프로시져

	IF $kno IS NULL THEN 
		SET $kno = 0;
	END IF;
	IF $media_script_no IS NULL THEN
		SET $media_script_no = 0;
	END IF;
	IF $ad_gubun='KP' THEN
		SET $ad_gubun='KL';
	END IF;

	INSERT INTO MOB_SITE_STATS
	(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, TOT_EPRS_CNT, PAR_EPRS_CNT, REG_DTTM)
	VALUES ($sdate, $platform, $product, $ad_gubun, $site_code, $tview_cnt, $sview_cnt, NOW())
	ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$tview_cnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$sview_cnt;
		
	IF $ad_gubun = 'PE' OR $ad_gubun = 'CA' THEN -- 모바일의경우 sview_cnt 를 더한다.
		INSERT INTO MOB_MEDIA_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, ADVER_ID, MEDIA_SCRIPT_NO, TOT_EPRS_CNT, PAR_EPRS_CNT, MEDIA_PYMNT_AMT, REG_DTTM)
		VALUES($sdate, $platform, $product, $ad_gubun, $advertiser_id, $media_script_no, $tview_cnt, $viewcnt3, $mpoint, NOW())
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+1, PAR_EPRS_CNT=PAR_EPRS_CNT+$sview_cnt, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	ELSE
		INSERT INTO MOB_MEDIA_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, ADVER_ID, MEDIA_SCRIPT_NO, TOT_EPRS_CNT, PAR_EPRS_CNT, MEDIA_PYMNT_AMT, REG_DTTM)
		VALUES($sdate, $platform, $product, $ad_gubun, $advertiser_id, $media_script_no, $tview_cnt, $viewcnt3, $mpoint, NOW())
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$tview_cnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$viewcnt3, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	END IF;

	IF $kno = 0 THEN
		INSERT INTO MOB_ADVER_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, MEDIA_SCRIPT_NO, TOT_EPRS_CNT, PAR_EPRS_CNT, MEDIA_PYMNT_AMT, REG_DTTM)
		VALUES($sdate, $platform, $product, $ad_gubun, $site_code, $media_script_no, $tview_cnt, $sview_cnt, $mpoint, NOW())
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$tview_cnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$sview_cnt, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	ELSE
		INSERT INTO MOB_KWRD_STATS
		(STATS_DTTM, PLTFOM_TP_CODE, ADVRTS_PRDT_CODE, ADVRTS_TP_CODE, SITE_CODE, MEDIA_SCRIPT_NO, KWRD_APPN_NO, TOT_EPRS_CNT, PAR_EPRS_CNT, MEDIA_PYMNT_AMT, REG_DTTM)
		VALUES($sdate, $platform, $product, $ad_gubun, $site_code, $media_script_no, $kno, $tview_cnt, $sview_cnt, $mpoint, NOW())
		ON DUPLICATE KEY UPDATE TOT_EPRS_CNT=TOT_EPRS_CNT+$tview_cnt, PAR_EPRS_CNT=PAR_EPRS_CNT+$sview_cnt, MEDIA_PYMNT_AMT=MEDIA_PYMNT_AMT+$mpoint;
	END IF ;

	-- IF $mpoint > 0 AND $scriptuserid != 'mkakao' AND $scriptuserid != 'kakao' THEN
	-- 	UPDATE admember SET mpoint=mpoint+$mpoint, lastupdate=NOW() WHERE userid=$scriptuserid;
	-- END IF;

END$$

DELIMITER ;

