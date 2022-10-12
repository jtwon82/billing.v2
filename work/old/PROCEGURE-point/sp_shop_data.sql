DELIMITER $$

-- USE `dreamsearch`$$

DROP PROCEDURE IF EXISTS `sp_shop_data`$$

CREATE PROCEDURE `sp_shop_data`(
  IN $userid VARCHAR (50) 
, IN $pcode VARCHAR (100)
, IN $cate VARCHAR (50)
, IN $platform CHAR (2)
, IN $STATUS CHAR (1)
, IN $url VARCHAR (2000)
, IN $imgpath VARCHAR (2000)
, IN $pnm VARCHAR (50)
, IN $price INT
, IN $purl VARCHAR (1000)
, IN $width VARCHAR (4)
, IN $height VARCHAR (4)
, IN $kakaostatus CHAR (1)
)
BEGIN
	IF $kakaostatus IS NULL THEN
		SET $kakaostatus = '';
	END IF;
	
	IF $platform = '01' THEN
		INSERT INTO SHOP_DATA_TEST(userid, url, pnm, pcode, price, imgpath, purl, gb, rdate, rtime
		, cate1, cate2, cate3, cate4, caid1, caid2, caid3, caid4
		, LOADINFO, REGDATE, LASTUPDATE, width, height, STATUS, kakao_status ) 
		VALUES ($userid, $url, $pnm, $pcode, $price, $imgpath, $purl, '', REPLACE(LEFT(NOW(),10),'-',''), '0101'
		, $cate, '', '', '', '', '', '', ''
		, 'U', NOW(), NOW(), $width, $height, $STATUS, $kakaostatus )
		ON DUPLICATE KEY UPDATE
			pnm=$pnm, price=$price, imgpath=$imgpath, purl=$purl, width=$width, height=$height
			, cate1=$cate, STATUS=$STATUS, LOADINFO='U', LASTUPDATE=NOW();
			
	ELSEIF $platform = '02' THEN
		INSERT INTO MOB_SHOP_DATA_TEST(userid, url, pnm, pcode, price, imgpath, purl, gb, rdate, rtime
		, cate1, cate2, cate3, cate4, caid1, caid2, caid3, caid4
		, LOADINFO, REGDATE, LASTUPDATE, width, height, STATUS, kakao_status ) 
		VALUES ($userid, $url, $pnm, $pcode, $price, $imgpath, $purl, '', REPLACE(LEFT(NOW(),10),'-',''), '0101'
		, $cate, '', '', '', '', '', '', ''
		, 'U', NOW(), NOW(), $width, $height, $STATUS, $kakaostatus )
		ON DUPLICATE KEY UPDATE
			pnm=$pnm, price=$price, imgpath=$imgpath, purl=$purl, width=$width, height=$height
			, cate1=$cate, STATUS=$STATUS, LOADINFO='U', LASTUPDATE=NOW();
			
	END IF;
END$$

DELIMITER ;