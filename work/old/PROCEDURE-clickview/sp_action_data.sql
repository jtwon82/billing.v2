DELIMITER $$

-- USE `dreamsearch`$$

DROP PROCEDURE IF EXISTS `sp_action_data`$$

CREATE PROCEDURE `sp_action_data`(
  `PARTDT` INT(8)
  , `IP` VARCHAR(20) CHARACTER SET euckr
  , `PCODE` VARCHAR(100)
  , `SHOPLOG_NO` LONG
  , `SITECODE` VARCHAR(32)
  , `MCODE` VARCHAR(20)
  , `MEDIA_CODE` VARCHAR(13)
  , `MEDIA_ID` VARCHAR(13)
  , `K_NO` VARCHAR(13)
  , `VCNT` INT(11)
  , `VCNT2` INT(11)
  , `CCNT` INT(11)
  , `POINT` INT(11)
  , `ACTGUBUN` VARCHAR(1)
  , `ADGUBUN` VARCHAR(2)
  , `$adproduct` VARCHAR(10)
  , `MCGB` VARCHAR(10)
)
BEGIN
	DECLARE $actgubun VARCHAR(10);
	SET $actgubun=ACTGUBUN;

	IF $adproduct='ico' THEN
		SET $actgubun='C';
	END IF;

	IF $adproduct='mbe' THEN
		SET $actgubun='C';
	END IF;

	IF $adproduct='floating' THEN
		SET $adproduct='nor';
	END IF;

	INSERT INTO ACTION_LOG_TEST(PARTDT,IP,PCODE,SHOPLOG_NO,SITECODE,MCODE,MEDIA_CODE,MEDIA_ID,K_NO,VCNT,VCNT2,CCNT,POINT,ACTGUBUN,ADGUBUN,ADPRODUCT,MCGB)
		VALUES(PARTDT,IP,PCODE,SHOPLOG_NO,SITECODE,MCODE,MEDIA_CODE,MEDIA_ID,K_NO,VCNT,VCNT2,CCNT,POINT,$actgubun,ADGUBUN,$adproduct,MCGB);
END$$

DELIMITER ;