SELECT * FROM admember WHERE userid IN ('jtwon', 'jkseo1', 'lang') ;

SELECT * FROM MOBON_COM_CODE ;

SELECT * FROM MOB_COM_STATS_INFO WHERE STATS_DTTM IN ('20171013') ;
SELECT * FROM MOB_SITE_STATS WHERE STATS_DTTM IN ('20171026') ORDER BY TOT_EPRS_CNT DESC ;	DELETE FROM MOB_SITE_STATS WHERE STATS_DTTM IN ('20171026') ;
SELECT * FROM MOB_MEDIA_STATS WHERE STATS_DTTM IN ('20171026') ORDER BY TOT_EPRS_CNT DESC ;	DELETE FROM MOB_MEDIA_STATS WHERE STATS_DTTM IN ('20171026') ;
SELECT * FROM MOB_ADVER_STATS WHERE STATS_DTTM IN ('20171026') ORDER BY TOT_EPRS_CNT DESC ;	DELETE FROM MOB_ADVER_STATS WHERE STATS_DTTM IN ('20171026') ;
SELECT * FROM MOB_KWRD_STATS WHERE STATS_DTTM IN ('20171026') ORDER BY TOT_EPRS_CNT DESC ;	DELETE FROM MOB_KWRD_STATS WHERE STATS_DTTM IN ('20171026') ;

SELECT * FROM ACTION_LOG WHERE partdt LIKE '201710%' ORDER BY NO DESC LIMIT 10  ;
SELECT * FROM CONVACT_LOG WHERE partdt LIKE '201710%' ORDER BY NO DESC LIMIT 10  ;

SELECT a.lastupdate, a.point, a.mpoint, a.* FROM admember a WHERE a.userid IN ('midasb','dabagirl','heraldcorp') ORDER BY lastupdate DESC LIMIT 10  ;
SELECT a.usedmoney, a.* FROM adsite a LIMIT 10  ;
SELECT a.usedmoney, a.* FROM iadsite a LIMIT 10  ;


SELECT * FROM MOB_CNVRS_STATS LIMIT 10 ;
SELECT * FROM MOBON_COM_CODE a WHERE CODE_TP_ID='ADVRTS_PRDT_CODE'; 
SELECT * FROM MOBON_COM_CODE a WHERE CODE_TP_ID='ADVRTS_PRDT_CODE';	DELETE FROM MOBON_COM_CODE WHERE CODE_TP_ID='ADVRTS_PRDT_CODE';
SELECT * FROM MOBON_COM_CODE a WHERE CODE_TP_ID='ADVRTS_TP_CODE';



SELECT * FROM external_report LIMIT 10 ;


-- 확인해보자.
SELECT a.CODE_ID, a.CODE_VAL, a.CODE_DESC FROM MOBON_COM_CODE a WHERE CODE_TP_ID='ADVRTS_PRDT_CODE';


SELECT a.STATS_DTTM, COUNT(*) FROM MOB_SITE_STATS a WHERE STATS_DTTM LIKE '201709%' GROUP BY a.STATS_DTTM ORDER BY 1 DESC ;
SELECT a.STATS_DTTM, COUNT(*) FROM MOB_MEDIA_STATS a WHERE STATS_DTTM LIKE '201709%' GROUP BY a.STATS_DTTM ORDER BY 1 DESC ;



ALTER TABLE MOB_CHRG_LOG ADD PARTITION MOB_CHRG_LOG_20171016 VALUES IN (20171016) ENGINE = INNODB ;
ALTER TABLE MOB_CHRG_LOG ADD PARTITION MOB_CHRG_LOG_20171017 VALUES IN (20171017) ENGINE = INNODB ;
ALTER TABLE MOB_CHRG_LOG ADD PARTITION MOB_CHRG_LOG_20171018 VALUES IN (20171018) ENGINE = INNODB ;
ALTER TABLE MOB_CHRG_LOG ADD PARTITION MOB_CHRG_LOG_20171019 VALUES IN (20171019) ENGINE = INNODB ;
ALTER TABLE MOB_CHRG_LOG ADD PARTITION MOB_CHRG_LOG_20171020 VALUES IN (20171020) ENGINE = INNODB ;


-- 컨버전 
SELECT * FROM CONVERSION_LOG a ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM CONVACT_LOG a ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM status_conversion a ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM coupon_serial a ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM coupon_manager a ORDER BY NO DESC LIMIT 10 ;

SELECT * FROM SHOP_DATA ORDER BY NO DESC LIMIT 10 ; 
SELECT * FROM MOB_SHOP_DATA ORDER BY NO DESC LIMIT 10 ; 

SELECT * FROM stats_shopdata_new ORDER BY NO DESC LIMIT 10 ; 
SELECT * FROM stats_shopdata_mob ORDER BY NO DESC LIMIT 10 ; 
SELECT * FROM stats_shopdata_new a WHERE userid='lotteimall12' AND pcode='1262328763' ORDER BY NO DESC LIMIT 10 ; 
SELECT * FROM stats_shopdata_mob a WHERE userid='lotteimall12' AND pcode='1262328763' ORDER BY NO DESC LIMIT 10 ; 

CALL sp_shop_stats('20170928', 'lotteimall12', '1262328763','바지','m', 0, 0, 0, 0, 0, 0);



-- SHOP_DATA
SELECT * FROM SHOP_DATA a WHERE a.USERID='eranzi' AND a.PCODE='014000003785' ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM MOB_SHOP_DATA a WHERE a.USERID='eranzi' AND a.PCODE='014000003785' ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM stats_shopdata_mob ORDER BY NO DESC LIMIT 10 ;
SELECT * FROM stats_shopdata_new ORDER BY NO DESC LIMIT 10 ;

		SELECT
			a.NO NO,
			a.ADPRODUCT product,
			a.ADGUBUN adGubun,
			a.SITECODE siteCode,
			a.MEDIA_ID scriptUserId,
			a.MEDIA_CODE scriptNo,
			a.ACTGUBUN TYPE,
			a.MCGB mcgb,
			0 inHour
		FROM
			ACTION_LOG a
		WHERE
			a.IP = '39.7.54.5.30763982'
			AND a.MCODE = 'norway'
			AND a.ACTGUBUN = 'V'
			AND partdt > '20170903'
		ORDER BY NO DESC
		LIMIT 1
		;
		
		SELECT
			a.NO NO,
			a.ADPRODUCT product,
			a.ADGUBUN adGubun,
			a.SITECODE siteCode,
			a.MEDIA_ID scriptUserId,
			a.MEDIA_CODE scriptNo,
			a.ACTGUBUN TYPE,
			a.MCGB mcgb,
			CASE WHEN TIMESTAMPDIFF(HOUR, a.regdate, NOW()) <= 1 THEN 24 ELSE 0 END inHour
		FROM
			ACTION_LOG a
		WHERE
			a.IP = '118.216.48.213.78229'
			AND a.MCODE = 'norway'
			AND a.MEDIA_CODE = '11234'
			AND a.SITECODE = '352bfff3c9d69166705169ff1454cac0'
			AND a.ADGUBUN = 'CW'
			AND a.ADPRODUCT = 'mbe'
			AND a.ACTGUBUN = 'C'
			AND a.partdt >= '20170903' -- <![CDATA[ >= ]]> #yyyymmdd#
		ORDER BY a.NO DESC
		LIMIT 1
		;
		

		SELECT
			ORDCODE ordCode
		FROM
			CONVERSION_LOG
		WHERE
			ordcode = '20170928230042-35349767727'
			AND userid = 'frameSelector'
		LIMIT 1
		;
		
		DELETE FROM CONVERSION_LOG
		WHERE
			ordcode = '20170917225854-23087889750'
			AND userid = 'norway';








CALL sp_sky_click('20170719','w','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123);	-- click
CALL sp_sky_click('20170719','m','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123);	-- click
CALL sp_sky_click('20170719','w','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0);	-- click
CALL sp_sky_click('20170719','m','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0);	-- click
CALL sp_sky_view('20170719','w','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1); -- view
CALL sp_sky_view('20170719','m','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1); -- view
CALL sp_sky_view('20170719','w','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1,1); -- view
CALL sp_sky_view('20170719','m','s','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1,1); -- view
CALL sp_banner_click('20170719','w','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1);	-- click
CALL sp_banner_click('20170719','m','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1);	-- click
CALL sp_banner_click('20170719','w','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1);	-- click
CALL sp_banner_click('20170719','m','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1);	-- click
CALL sp_banner_view('20170719','w','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1,1,1);	-- view
CALL sp_banner_view('20170719','m','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1,1,1);	-- view
CALL sp_banner_view('20170719','w','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1,1,1,1);	-- view
CALL sp_banner_view('20170719','m','b','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,0, 2,1,1,1,1);	-- view
CALL sp_ico_click('20170719','w','i','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1);	-- ico
CALL sp_ico_click('20170719','m','i','SR','7a2252355e622d0148c155613f98c80d','advertiser','scriptuserid',1234,123, 2,1,1);	-- ico


-- {yyyymmdd=20170906, platform=W, product=03, adGubun=17, type=V, siteCode=, scriptNo=, advertiserId=, scriptUserId=dtnews24, kno=0, viewCnt=1, viewCnt2=1, viewCnt3=1, clickCnt=0, point=0.0, mpoint=0.0}
CALL sp_banner_view('20170906','W','03','17','cd633a6ad842a012e34b945acc6e044c','lotteshop','dtnews24',9476,123, 2,1,1,1,1);	-- view

-- ALTER TABLE ACTION_LOG ADD PARTITION (PARTITION ACTION_LOG20171011 VALUES IN(20171011));






SELECT * FROM ACTION_LOG ORDER BY NO DESC LIMIT 10  ;
SELECT * FROM CONVERSION_LOG ORDER BY NO DESC LIMIT 10  ;
SELECT * FROM CONVACT_LOG ORDER BY NO DESC LIMIT 10  ;
SELECT * FROM MOB_IP_INTRCP_MNG ORDER BY reg_dttm DESC LIMIT 10  ;

SELECT DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 HOUR), '%y-%m-%m %H');

SELECT * FROM SHOP_DATA ORDER BY NO DESC LIMIT 10 ;		DELETE FROM SHOP_DATA WHERE userid='mnate' ;
SELECT * FROM MOB_SHOP_DATA ORDER BY NO DESC LIMIT 10 ;		DELETE FROM MOB_SHOP_DATA WHERE userid='mnate' ;

SELECT * FROM stats_shopdata_new ORDER BY NO DESC LIMIT 10 ;	DELETE FROM stats_shopdata_new WHERE userid='mnate' ;
SELECT * FROM stats_shopdata_mob ORDER BY NO DESC LIMIT 10 ;	DELETE FROM stats_shopdata_mob WHERE userid='mnate' ;

SELECT * FROM chksite_new ORDER BY NO DESC LIMIT 10 ;



SELECT a.lastupdate, a.point, a.mpoint, a.userid FROM admember a WHERE a.userid IN ('oliveyoungda', 'swing') ORDER BY a.lastupdate DESC LIMIT 10 ;
SELECT a.usedmoney, a.site_code, a.userid FROM adsite a WHERE a.site_code IN ('58cec72d6d438ad64f2b80c79cd02b30', '') LIMIT 10 ;
SELECT * FROM ACTION_LOG ORDER BY NO DESC LIMIT 10  ;

-- update admember set point = 10000, mpoint = 0; 
-- UPDATE adsite SET usemoney=0, usedmoney = 0; 
-- UPDATE iadsite SET usemoney=0, usedmoney = 0; 

SELECT a.lastupdate, a.point, a.mpoint, a.userid FROM admember a ORDER BY POINT DESC LIMIT 10 ;
SELECT a.site_code, a.userid, a.usedmoney, a.usemoney FROM adsite a ORDER BY usedmoney DESC LIMIT 10 ; 
SELECT a.site_code, a.userid, a.usedmoney, a.usemoney FROM iadsite a ORDER BY usedmoney DESC LIMIT 10 ; 

UPDATE admember a, admember_bak1 b SET a.point = b.point WHERE a.userid=b.userid ;
UPDATE adsite a, adsite_bak1 b SET a.usemoney = b.usemoney WHERE a.site_code=b.site_code ;
UPDATE iadsite a, iadsite_bak1 b SET a.usemoney = b.usemoney WHERE a.site_code=b.site_code ;
-- insert into admember_bak1 select * from admember;
-- INSERT INTO adsite_bak1 SELECT * FROM adsite;
-- INSERT INTO iadsite_bak1 SELECT * FROM iadsite;


-- TRUNCATE TABLE stats_adsite;
-- TRUNCATE TABLE stats_media_script;
-- TRUNCATE TABLE stats_adlink;
-- TRUNCATE TABLE stats_adlink_kno;

SELECT * FROM status_sitecode WHERE sdate LIKE '201708%' AND site_code='3f0a3acf988c70208f6559dd3bb31090' ;
SELECT * FROM stats_media_script;
SELECT * FROM stats_adlink;
SELECT * FROM stats_adlink_kno;


SELECT * FROM status_sitecode LIMIT 10 ;
SELECT * FROM status_day_media LIMIT 10 ;
SELECT * FROM status_adlink LIMIT 10 ;
SELECT * FROM status_urlmatch LIMIT 10 ;
SELECT * FROM status_keywordlink LIMIT 10 ;


SELECT * FROM admember WHERE userid = 'dgrid' ;
SELECT * FROM media_script WHERE NO = 2062;


SELECT * FROM adsite WHERE site_code = 'cee9b294880632830c20d14335b98ab7' ;



SELECT * FROM external_report ORDER BY sdate DESC LIMIT 10 ;

SELECT * FROM KEYWORD_WEB_DATA;
SELECT * FROM status_shopdata_brand;
SELECT * FROM chksite_new ORDER BY NO DESC LIMIT 100 ;

-- DELETE FROM chksite_new WHERE ukey IS NULL ;


SELECT cdid, cddetailid, cdkey FROM db_config_dtl a WHERE a.description = 'DEFAULT_FRAME_WEB_SIZE' ; 




-- ALTER TABLE stats_shopdata_new ADD PARTITION (PARTITION stats_shopdata_new20171011 VALUES IN(20171011));



		SELECT a.r_gubun,
			a.userid USERID,
			e.corpname,
			b.sitename,
			a.no NO,
			ad_type AD_TYPE,
			imgcode,
			h_type H_TYPE,
			h_banner H_BANNER,
			accept_sr,
			accept_um,
			accept_kl,
			accept_ad,
			accept_st,
			accept_sp,
			accept_rm,
			accept_rc,
			accept_cw,
			accept_pb,
			accept_hu,
			accept_pm,
			accept_sj,
			(SELECT COUNT(seq) c FROM media_icover_limit b WHERE a.no=b.script_no AND state='Y' ) limit_pop,
			a.bid_price,
			a.price_type,
			weight_pct,
			weight_type,
			a.mediasite_no,
			a.frameRtb_YN,
			a.w_type,
			a.pb_weight,
			b.siteurl,
			b.scate,
			IFNULL(d.actype,0) actype,
			IFNULL(d.acprice,0) acprice,
			IFNULL(d.acper,0) acper,
			IFNULL(d.deduct,0) deduct,
			e.icover_useyn,
			IFNULL(e.icover_time,0) icover_time,
			two_yn,
			mcover_type,
      c.imgCode imgCodeTemp,
			psb_url
		FROM
			media_script a
			JOIN media_site b ON a.mediasite_no = b.no
			JOIN dcode c ON a.ad_type = c.dcodeno
			JOIN admember e ON b.userid = e.userid
			LEFT JOIN acount_media_new d ON d.msno = b.no AND d.media_id IS NOT NULL AND b.userid = d.media_id
		WHERE
			b.state='Y'
			AND a.state='Y'
			AND a.del_fg != 'Y'
			AND a.no=2592
			;

		SELECT
			a.userid,
			IFNULL(b.iad ,0) iad,
			IFNULL(b.ikl ,0) ikl,
			IFNULL(b.isr ,0) isr,
			IFNULL(b.irc ,0) irc,
			IFNULL(b.ium ,0) ium,
			IFNULL(b.ist ,0) ist,
			IFNULL(b.isp ,0) isp,
			IFNULL(b.irm ,0) irm,
	  		IFNULL(b.icw ,0) icw,
	  		IFNULL(b.ihu ,0) ihu,
			IFNULL(b.ad ,0) ad,
			IFNULL(b.kl ,0) kl,
			IFNULL(b.sr ,0) sr,
			IFNULL(b.rc ,0) rc,
			IFNULL(b.um ,0) um,
			IFNULL(b.st ,0) st,
			IFNULL(b.sp ,0) sp,
			IFNULL(b.rm ,0) rm,
 			IFNULL(b.cw ,0) cw,
 			IFNULL(b.pb ,0) pb,
			IFNULL(b.hu ,0) hu,
			IFNULL(b.sj ,0) sj,
			IFNULL(b.sho ,0) sa,
			IFNULL(b.sad ,0) sad,
			IFNULL(b.skl ,0) skl,
			IFNULL(b.ssr ,0) ssr,
			IFNULL(b.src ,0) src,
			IFNULL(b.sum ,0) SUM,
			IFNULL(b.sst ,0) sst,
			IFNULL(b.ssp ,0) ssp,
			IFNULL(b.srm ,0) srm,
 			IFNULL(b.scw ,0) scw,
 			IFNULL(b.shu ,0) shu,
			IFNULL(a.cookieday ,0) cookieday,
			IFNULL(a.cookie_dirsales ,0) cookie_dirsales,
			IFNULL(a.cookie_indirsales ,0) cookie_indirsales,
			IFNULL(a.point ,0) POINT,
			a.icover_useyn,
			IFNULL(a.icover_time,0) icover_time
		FROM
			admember a,admember_price b
		WHERE
			a.userid = b.userid;