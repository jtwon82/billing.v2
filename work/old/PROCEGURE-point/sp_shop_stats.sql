DELIMITER $$

-- USE `dreamsearch`$$

DROP PROCEDURE IF EXISTS `sp_shop_stats`$$

CREATE PROCEDURE `sp_shop_stats`(
  IN $yyyymmdd VARCHAR(8)
, IN $advertiserId VARCHAR(50)
, IN $pCode VARCHAR(100)
, IN $cate VARCHAR(50)
, IN $platform VARCHAR(1)
, IN $viewCnt INT
, IN $adViewCnt INT
, IN $adClickCnt INT
, IN $adConvCnt INT 
, IN $adConvPrice INT
, IN $clickCnt INT
)
BEGIN
	IF $pCode IS NULL THEN 
		SET $pCode='';
	END IF;
	
	IF $platform='w' THEN
		INSERT INTO stats_shopdata_new_TEST(sdate, userid, pcode, cate1, viewcnt, adviewcnt, adclickcnt, adconvcnt, adconvprice, clickcnt)
		VALUES($yyyymmdd, $advertiserId, $pCode, $cate, $viewCnt, $adViewCnt, $adClickCnt, $adConvCnt, $adConvPrice, $clickCnt)
		ON DUPLICATE KEY UPDATE 
			viewcnt=viewcnt+$viewCnt, adviewcnt=adviewcnt+$adViewCnt, adclickcnt=adclickcnt+$adClickCnt
			, adconvcnt=adconvcnt+$adConvCnt, adconvprice=adconvprice+$adConvPrice, clickcnt=clickcnt+$clickCnt;
			
	ELSEIF $platform = 'm' THEN
		INSERT INTO stats_shopdata_mob_TEST(sdate, userid, pcode, cate1, viewcnt, adviewcnt, adclickcnt, adconvcnt, adconvprice, clickcnt)
		VALUES($yyyymmdd, $advertiserId, $pCode, $cate, $viewCnt, $adViewCnt, $adClickCnt, $adConvCnt, $adConvPrice, $clickCnt)
		ON DUPLICATE KEY UPDATE
			viewcnt=viewcnt+$viewCnt, adviewcnt=adviewcnt+$adViewCnt, adclickcnt=adclickcnt+$adClickCnt
			, adconvcnt=adconvcnt+$adConvCnt, adconvprice=adconvprice+$adConvPrice, clickcnt=clickcnt+$clickCnt;
	END IF;
	
END$$

DELIMITER ;