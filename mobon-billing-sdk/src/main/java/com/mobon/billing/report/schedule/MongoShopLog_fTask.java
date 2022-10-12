package com.mobon.billing.report.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.mobon.billing.report.AppReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.service.sdk.gather.cat.shoplog.CatToShopLogBatch;
import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.disk.ReadADID;
import com.mobon.billing.report.service.dao.PackageDao;
import com.mobon.dao.mongo.MongoManager;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Configuration
@Service
public class MongoShopLog_fTask extends MongoShopLogTask {
	
	@Autowired
	private PackageDao packageDao;
	
	@Autowired
	private MongoClient mongoClient;
	
	CatToShopLogBatch batch = new CatToShopLogBatch();
	
	ReadADID read = new ReadADID();
	
	private static final Logger logger = LoggerFactory.getLogger(AppReport.class);
	
	public static String PARTITION = "../packaging/data";
	
	public static String[] DIR = new String[] {"f"};
//	public static String[] DIR = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
	
//	@Scheduled(cron="0 0 04 * * ?") 
	public void execute() {
		
		try {
			while(PackageKeyTask.START_SHOP_LOG) {
//			while(true) {
//				System.out.println("test");
				Thread.sleep(1000);
//				PackageKeyTask.START_SHOP_LOG = true;
			}
			logger.info("disk scan start.....");
			System.out.println(Thread.currentThread().getName());
			for(int i=0; i<DIR.length; i++) {
				read.read(MongoShopLog_fTask.PARTITION+"/"+DIR[i], new ReadADID.IADID() {
					@Override
					public void execute(JSONObject adid) {
						// TODO Auto-generated method stub
						try {
							addShopLog(adid);
							Thread.sleep(200);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error(e.getMessage(), e);
						}
					}
				});
			}
			logger.info(Thread.currentThread().getName()+"-disk scan end.....");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
	
	
	
	public static void main(String args[]) {
		try {
			
			String[] s = "xx_gg".split("_");
			System.out.println(s.length);
			
			CacheConfig.getCachePackageKey().put("com.gameinsight.gobandroid", "1");
//			JSONArray r = JSONArray.fromObject("[{\"com.gameinsight.gobandroid\":20160205},{\"com.android.chrome\":20160205},{\"com.android.settings\":20160205},{\"com.android.vending\":20160205},{\"com.google.android.apps.docs\":20160205},{\"com.google.android.apps.maps\":20160205},{\"com.google.android.apps.photos\":20160205},{\"com.google.android.gm\":20160205},{\"com.google.android.music\":20160205},{\"com.google.android.talk\":20160205},{\"com.google.android.videos\":20160205},{\"com.google.android.youtube\":20160205},{\"com.samsung.android.calendar\":20170219},{\"com.samsung.android.contacts\":20170219},{\"com.samsung.android.contacts\":20170219},{\"com.samsung.android.email.provider\":20160205},{\"com.samsung.android.messaging\":20170219},{\"com.samsung.android.oneconnect\":20170219},{\"com.sec.android.app.camera\":20160205},{\"com.sec.android.app.sbrowser\":20160205},{\"com.iloen.melon\":20160908},{\"com.kakao.talk\":20160419},{\"com.lgcns.mpost\":20160419},{\"com.sec.android.app.shealth\":20160205},{\"com.google.android.googlequicksearchbox\":20160205},{\"com.lguplus.appstore\":20160205},{\"com.lguplus.mobile.cs\":20160205},{\"com.samsung.android.spay\":20160205},{\"com.samsung.voiceserviceplatform\":20180601},{\"com.sec.android.app.clockpackage\":20160205},{\"com.sec.android.app.dmb\":20160205},{\"com.sec.android.app.myfiles\":20160205},{\"com.sec.android.app.samsungapps\":20160205},{\"com.uplus.onphone\":20160205},{\"lg.uplusbox\":20160205},{\"a.kakao.iconnect.dotmingo\":20170601},{\"a.kakao.iconnect.lovecoffee\":20170703},{\"a.kakao.iconnect.supericecream\":20170601},{\"cheehoon.ha.particulateforecaster\":20170503},{\"com.adobe.reader\":20170214},{\"com.ahnlab.v3mobileplus\":20160420},{\"com.akbobada.mobile\":20180725},{\"com.bccard.bcsmartapp\":20160611},{\"com.cgv.android.movieapp\":20160603},{\"com.choroc.mgreen.app\":20170715},{\"com.coupang.mobile\":20180620},{\"com.dencreak.spbook\":20160419},{\"com.dnt7.threeW\":20160420},{\"com.doodledoodle.kakao.rosestory\":20170703},{\"com.doodledoodle.kakao.theme.greenleaf\":20170703},{\"com.estsoft.alyac\":20160205},{\"com.google.android.play.games\":20160612},{\"com.havit.android\":20170409},{\"com.homeplus.myhomeplus\":20180401},{\"com.hpapp\":20161224},{\"com.iconnect.app.pts.a\":20160420},{\"com.kakao.story\":20160506},{\"com.kakao.taxi\":20180331},{\"com.kbcard.cxh.appcard\":20180705},{\"com.kbstar.kbbank\":20160419},{\"com.kbstar.smartotp\":20161217},{\"com.kia.kr.qfriends\":20170215},{\"com.korail.talk\":20180126},{\"com.ksncho.hospitalinfo\":20180310},{\"com.ktcs.whowho\":20170224},{\"com.kyobo.ebook.common.b2c\":20180222},{\"com.lgu.app.appbundle\":20160205},{\"com.lguplus.homeiot\":20160205},{\"com.lguplus.ltealive\":20160205},{\"com.lguplus.navi\":20160503},{\"com.lguplus.paynow\":20160205},{\"com.lguplus.usimsvcm\":20160205},{\"com.lotte\":20160428},{\"com.makeshop.powerapp.ibori\":20170308},{\"com.megabox.mop\":20160802},{\"com.microsoft.office.excel\":20160205},{\"com.microsoft.office.onenote\":20160205},{\"com.microsoft.office.powerpoint\":20160205},{\"com.microsoft.office.word\":20160205},{\"com.microsoft.skydrive\":20160205},{\"com.mnet.app\":20160205},{\"com.mpod.stopbook\":20170817},{\"com.musinsa.store\":20180411},{\"com.mw.Android_KidsLove2\":20180529},{\"com.naver.labs.translator\":20170129},{\"com.nhn.android.band\":20180314},{\"com.nhn.android.naverplayer\":20160814},{\"com.nhn.android.navertv\":20180518},{\"com.nhn.android.ndrive\":20160814},{\"com.nhn.android.search\":20160419},{\"com.samsung.android.app.memo\":20160205},{\"com.samsung.android.app.watchmanager\":20160205},{\"com.samsung.android.voc\":20160205},{\"com.sec.android.app.popupcalculator\":20160205},{\"com.sec.android.app.voicenote\":20160205},{\"com.snaps.mobile.kr\":20170904},{\"com.ssfshop.app\":20180622},{\"com.thenextc.moabebe\":20180508},{\"com.uplus.ipagent\":20160205},{\"com.uplus.movielte\":20160205},{\"com.vaultmicro.kidsnote\":20180601},{\"com.wemakeprice\":20180806},{\"com.wisdomhouse.redbookstore\":20161107},{\"con.example.flash.flashplayer\":20170214},{\"jp.sugitom.android.memoneko\":20160419},{\"kr.co.company.hwahae\":20160907},{\"kr.co.lottecinema.lcm\":20160802},{\"kr.co.srail.app\":20180126},{\"kr.co.vcnc.android.couple\":20160419},{\"kr.go.cdc.nip.android\":20160913},{\"kr.tenbyten.shopping\":20160419},{\"kvp.jjy.MispAndroid320\":20160420},{\"lgt.call\":20160205},{\"mok.android\":20160424},{\"net.btworks.phishingguard\":20160420},{\"net.cj.cjhv.gs.tving\":20180715},{\"net.donxu.reading_diary\":20160515},{\"nh.smart\":20160419},{\"nh.smart.nhcok\":20160730},{\"nh.smart.signone\":20161217},{\"pr.baby.myBabyWidget\":20170503},{\"uplus.membership\":20161111}]");
			JSONArray r = JSONArray.fromObject("[{\"com.gameinsight.gobandroid\":20160205}]");
			JSONObject o = new JSONObject();
			o.put("PACKAGE", r);
			
			new MongoShopLog_fTask().getPackage(o);
			System.out.println(diffOfDate("20180828", "", ""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		File f = new File("data/1");
//		System.out.println(f.listFiles().length);
//		System.out.println(f.listFiles()[0].getName());
	}
}
