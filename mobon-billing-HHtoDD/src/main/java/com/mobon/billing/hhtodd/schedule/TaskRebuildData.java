package com.mobon.billing.hhtodd.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.adgather.util.old.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mobon.billing.hhtodd.service.HHtoDDMariaDB;
import com.mobon.billing.util.FileUtils;
import com.mobon.billing.util.TimeToLiveCollection;

import net.sf.json.JSONObject;

@Component
public class TaskRebuildData {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskRebuildData.class);

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;

	@Value("${log.path}")					private String	logPath;

	
	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	@Resource(name="sqlSessionTemplateDream")
	private SqlSessionTemplate sqlSessionTemplateDreamSearch;
	
	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private HHtoDDMariaDB			hHtoDDMariaDB;
	
	
	public static void setThreadCnt(int threadCnt) {
		TaskRebuildData.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskRebuildData.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskRebuildData.threadCnt--;
	}
	

//	@Scheduled(fixedDelay = 1000)
	public void updateKpiMediaStats() {
		String [] sqlidList= new String[] {"updateUpdate0816kpimedia"};
		
		for (String sqlid: sqlidList)
			logger.info("START sqlid {}", sqlid);
			transactionTemplate.execute(new TransactionCallback<Object>() {
				boolean res = false;
		
				public Object doInTransaction(TransactionStatus status) {
					boolean hisYN = true;
					long startTime = System.currentTimeMillis();
					long endTime = System.currentTimeMillis();
					long resutTime = 0;
		
					try {
						Map param= new HashMap();
//						sqlSessionTemplateBilling.update(String.format("%s.%s"
//								, "hHtoDDAdverMTHhhMapper", sqlid), param);
//						sqlSessionTemplateBilling.flushStatements();
						
						res = true;
					} catch (Exception e) {
						hisYN = false;
						status.setRollbackOnly();
						res = false;
						logger.error("updateKpiMediaStats Exception ==>", e);
		
					} finally {
						if (hisYN) {
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("updateKpiMediaStats 종료  : (TBRT) :{}, {}, {}", resutTime +"(ms)");
						}
					}
					return res;
				}
			});
		
		System.exit(0);
	}
	
//	@Scheduled(fixedDelay = 1000)
	public void updateTrgtFld() {
		
		String [] sqlidList= new String[] {"updateTrgtStats","updateTrgtStats2","updateTrgtStats3"
				,"updateTrgtStats4","updateTrgtStats5","updateTrgtStats6"};
		
		for (String sqlid: sqlidList)
			transactionTemplate.execute(new TransactionCallback<Object>() {
				boolean res = false;
		
				public Object doInTransaction(TransactionStatus status) {
					boolean hisYN = true;
					long startTime = System.currentTimeMillis();
					long endTime = System.currentTimeMillis();
					long resutTime = 0;
		
					try {
						Map param= new HashMap();
						sqlSessionTemplateBilling.update(String.format("%s.%s", "hHtoDDAdverMTHhhMapper", sqlid), param);
						sqlSessionTemplateBilling.flushStatements();
						
						res = true;
					} catch (Exception e) {
						hisYN = false;
						status.setRollbackOnly();
						res = false;
						logger.error("updateTrgtFld Exception ==>", e);
		
					} finally {
						if (hisYN) {
							endTime = System.currentTimeMillis();
							resutTime = endTime - startTime;
							logger.info("updateTrgtFld 종료  : (TBRT) :{}, {}, {}", sqlid, resutTime +"(ms)");
						}
					}
					return res;
				}
			});
		
		System.exit(0);
	}
	
	public void deleteFRME_CTGR_DAY_STATS() {
		String []ADVRTS_TP_CODES= new String[] { "01","04","05","06","10","11","16","17","18","19","26","28","31","35","36","37","40","41"};
		for (String tpcode : ADVRTS_TP_CODES) {
			for(String algmseq: new String[] { "8","5","1","11"}) {
				trFRME_CTGR_DAY_STATS(algmseq, tpcode);
			}
		}
		System.exit(0);
	}
	public boolean trFRME_CTGR_DAY_STATS(String ALGM_SEQ, String tpcode) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;
	
			public Object doInTransaction(TransactionStatus status) {
				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;
	
				try {
					Map param= new HashMap();
					param.put("ALGM_SEQ", ALGM_SEQ);
					param.put("ADVRTS_TP_CODE", tpcode);
					
					sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteFRME_CTGR_DAY_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					status.setRollbackOnly();
					res = false;
					logger.error("trFRME_CTGR_DAY_STATS Exception ==>", e);
	
				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("trFRME_CTGR_DAY_STATS 종료  : (TBRT) :{}, {}, {}", ALGM_SEQ, tpcode, resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}

	
	public void updateTrgtField() {
		try {
			String[] SITECODES = new String[] { "8527056eaf7d48aba89077210a1c37f6", "d137072af31d4358937e72d8a4eec7ea",
					"d6d5bb230af64ca6a28d7fe774ff97d4", "b9929742c04f42a68dbadc6727d73d7b",
					"016b0fbc7c89dbe97f68eefcd2f9d40c", "018b02e1dddcf31b26ac386a9cabacb7",
					"0231bdf522dab6eb852ff511a024a972", "029c3dc9981c7a4697f45c6d1974b14f",
					"02abcb48a36dbee3f90db586908abfbb", "0309ad074671ae551e71f3604cf84c3c",
					"03596583f8bb062f29b9ac4d99a7ad1b", "035e42727b4f3f85aa54d4b11b6841a0",
					"03e707b917da603b4a23ab88919fdab1", "047c145afbaf5374d8117ee6e9a2049c",
					"0514e5aeeb8b24326f79d5b5890ebb90", "057ab08dcc5efd8e547809c50724dacf",
					"060b137bfd2d9e0ed2794389b288d1f8", "06100c0ca53b99730daf670787f8e252",
					"073e74bd8ba0c9f63bb4c4fd5451cafa", "07433bbe42d43c6e1be0c7208b78ef9b",
					"0746f714effdae0bfa975d1e28186137", "0764f9dcf7cfbd92137518dedff6ac13",
					"0794ae63dcabca83822e52c539051df4", "08015f0e623a0c2ef14025bf11b31846",
					"08cc426873c19f4b1a3876a6018d29eb", "0bda952a650b97ed3ddb49f599f247c4",
					"0be5eb7dc68996a1c4bc953b6fefecb0", "0c5b2621878f2fafe95915f0e6824b3d",
					"0d7b9a212fed75614d385c890b9492f1", "0dac3bc3c2afbc2774d039e369c1bbb5",
					"0e074e784b6141b8f043a13dd7974ee8", "0e92536a39ae0566ae90fb295d8e0e91",
					"0f183c7eadadcc1b2c2312d81a6ad3d1", "1076a689ad74d805941e95d92b303f19",
					"10a7c4b9bd94142ea98293ddeefa0841", "10da149d6674de3312eed509ba3b400f",
					"10eb8ad7fb862151d8fe2fe808f26950", "12de0ad96713826c3cfcedcfcf26dfe7",
					"139834bc31e66c9b6acce54fc798e546", "13f05f8b50ce4a5757ef163b27acf4be",
					"14ab36bd469ba425f89e83c839e8f4f5", "15cfcc233fad0dd0cceb5268bdb57a2d",
					"162c3dc5a0c229c9a0ef06bc793e69b0", "164849883608872496b23422f0ac05d7",
					"166c2b968fe3f38b6e06bc929d439598", "166e222fbc188f242e70d977ff23883c",
					"1693a8f4ba28afc4163482ff1e3cedee", "171589facbefdee813c49da788a172dc",
					"173d68844d0dc71a572422a83712e3e5", "19b6cf9264b18bd02bae21e5374f0fd6",
					"19e3d18c7515bb8fe5792fd686b0693d", "1a00190fa907225359c3fc57770e4c0e",
					"1ba0ed6c799d7169a64387ff2908c545", "1cf21b57c55b5b6c3ace154d3d14ce03",
					"1d7bbf30c9a382c5aa406f711af894b0", "1f4ca6bdc68e68c0d0c09d537e5b7614",
					"1f7b2c3d558d88966b0e26aebb018910", "200623e3ab61e1d9e9f3f235931c0d5f",
					"20187c2545378e7f47138e73706ab12e", "202d062252adafbba39eab69cfaa38af",
					"20335c48d7356889a2969369cde284c0", "208d098956dc3b2cc45371c59059621d",
					"22cc9f70f0b81348977e5c747a750189", "22e5d3f4aada2a1feb997451c773d9ea",
					"233de11958df67e64b88b43a5e9ec6b4", "237e0fa6a797ce2931eba634c55bba7c",
					"237e5d1f0aaa6cd54a322b8861ae12e4", "23b1d0562dee0f708178f8141e6080af",
					"242566dacd85b7f530b67f8e908e03f7", "252d739bf382f081c01aa13d8a5b5a9c",
					"268ba8ccb8af7469c8c42fa386b7aff8", "26c919b2ec39fdafc0cc0d22d17ada81",
					"26e4ccc782c0bfaa3bd0116563be716f", "28655de06196bd0014e1b1e8db4c3db9",
					"288909c39967c4d055696c7344a980ca", "297c16b6f7f04aff9a5589c1d7d3186f",
					"29d35187cc74bbeda597869e5f22249d", "2a3aa9aef4fda2f25faede2abfc91814",
					"2a620c49917939bed48c847dc8dfb025", "2aad04f4e18bc4bd6eefbbe7020b3457",
					"2acb7e0024723a3cc5d75b7663a4ef72", "2c389a588e55e71cde13ff8b3a8e6b0a",
					"2c925183db2ac722b4d0a6c1c0bfae5c", "2cd2f20dc2cb2c7d73be348485fd79c0",
					"2cdcc70c2f42f172e01761c0e575555e", "2d6c3c12560e91ab844b97596839f309",
					"2ddc42583234621abe3c93821d03f23f", "2e294de73f0249d4d98654cc383c3a27",
					"2e388c650bbebdbdc514cc5977fbf97f", "2e4baba028f428a73b4b0e064ad91b2d",
					"2e5d9c841f5778f998d085bc5c2e402e", "2eeee27ac008c1394e90aaecae5a8012",
					"2f6fd717315d8859e85815e2b7b6c29e", "2f925e06c52c4328c698715acfc56533",
					"2f99dbb3e150323a3ac3c8d5ccc6c12b", "303fdb6ef26403e8c1a5fe01c3763c78",
					"3058d71db7b197d4bf0b06ef04a4d959", "311b0b6af243cbe5d76fd4295918b1bd",
					"314b5fff5ab3af74edd1a717cb0da460", "3158319737ab34b4e41ed7ae5caca277",
					"339f152474f23493a190142d182898c9", "343413c36cf3022549aab791c6a53734",
					"3467cf7239ae3157168ee2c7f8444481", "3590c19187a5a6ac85d702dcb7a7b237",
					"35b4a94f3b3e64ed58c3adc57feee447", "3641abc2df3dd8f73a8935d930c82668",
					"378bf6c783212b7f61b7cc84b352874c", "37b3693193fb05b242169a85b478c7d3",
					"37bf9aca2169efa69e2f0ea870a538eb", "380b29e6721e727d751b1de0606a1ac9",
					"384070b4d6d0cad06fcb40c555fc5ffe", "3abb7963bb58f24100014da652f3627d",
					"3cdaff4dc260938f7e051e9c7f97ec7c", "3d6e4cd897fed98e5bc5262d1a2027f2",
					"3daf3821036e724701d78fc2dbfa5385", "3de9fe1dd46699ce3a9a782d99722520",
					"3e31c07fc6c02042c8286865178f7067", "3f2e7d81b09223ec38c48a0b26b1addc",
					"3f841ce40df9ca2ffa5f11bf8ae3e1cb", "40655657667b42045bfa0717e6d9a7e0",
					"40db78d93c6785d3b396015e6fde8583", "41d15f89d25f1279f254d98881ae64de",
					"4271ec76713343fdde363f84e2be1291", "434ccfb2457c7f4d6ceed5042fc145fa",
					"43907ca41e302ad12a319843cecf1cbf", "448c0195a8e66ae41795e19b86742837",
					"45cc74b84a39a825602a0f73930484a1", "46d3314f94595ce809d64d45fc86bfa8",
					"46eb24668ce936e3cc6178199a68dad5", "47078282de7a8b187b454af56f68edc9",
					"477dfac6229e77adf25751ab1d7695e3", "478dab3fe6a8ef31be884cadde5bfccf",
					"47cc3e0dbfddf2c2185a85db4d39ff82", "48600a3f8f90032afa0195fa29da91c1",
					"48c3e496a828afdc4b28d538178a8aa7", "496f29f4022a500f3f6149adf9d48d46",
					"49e447d61210ff35a802177a65e31835", "4a93a08e19461cb32d64e298e6e99d8a",
					"4a9f4dcfbe6a737ae2c0259913f45cf5", "4b45fbeb4dafe4b582ac7cb04e68c2f5",
					"4b49be7da296e5c2d3391ee47c075e5c", "4ba4acc4a3439ff3a29652f947160c78",
					"4bbcb8eb3edcb2ca596fd3415b0a2f15", "4bd92873de3706e00ca6c5cab6e36250",
					"4be42413571dad849eb4c6bcc3b84385", "4c26405ead074cc17336a957883705b5",
					"4c58d25d7f158bad03109f9eab0ade5f", "4db7cf8773634fc359d77be340fb0812",
					"4dbafde09de606e23ba2f96cf1fa75a4", "4dda1246892365135f7af5670fdeee77",
					"4f27151d4a9c7f2b8c9f2ec34077d15b", "4f4a4d775de3fb7ec5de1660f97f7548",
					"4f669159e8097bda4781a1512c565347", "4fe0c6f585c3e543697473ff3662ec89",
					"5069cf488bc5e6c81d04cbdbfc537f11", "509e7828583ab05c3a52ddc77dba8ff0",
					"50bf133b8abf677563dfc333e2617bab", "510c1907d85f026c3ba170e472caa7da",
					"510c7e360e43db1db773ccd0e8cf23ec", "511187e2b777b7e9f3ce53cd4eced0ac",
					"530a24fafd5b3aeac2958ce71720271a", "5333b514c8c7591cbe0d6a9ae3d1c11f",
					"539a358950896b91b5fbb555ba713d3b", "5432332d3c8155f1e07b8dfa518c6eb7",
					"547a13213d4bcc92b8f59d251f7de7d7", "54d6c328e5fa916d5e28dbaa90ddc7fc",
					"55849313303e8efddc65891537913caf", "55855beeea07c1e79c93459decedb66e",
					"55c46dfd7306dd3353c9341bcc7f64a7", "56d552f00a5a679f4e120b898cb5a051",
					"56d6bd149e4d89c1423bbf785869e3d9", "56f06f75d097b366420e15823ab37787",
					"5709f21ab96d506f6a052e6f7495471d", "572d9001d3298c3501b5e7ccc010f445",
					"57d11c7ca28dac9b859c42890a11f611", "57f471b5a0a25ddfe5530dc81066dba9",
					"5a2133fe6eca7ed7284d67b1d99a42b0", "5a6ce052cc66a492b248bd2f55923cab",
					"5bdea3f5237c42c3d7b7866072a43cff", "5cf34e6567c5e8bbd63e9761e4495403",
					"5d1a0d28ebf2a3c8ce0abece4bd77add", "5e00eaddd67fb2091c876f71ecdf242f",
					"5e0a0c3f7040a6a7297c7135e3bc955e", "5e95a4869f5d9554892c53c88fca6812",
					"5eb328479171c5d0c30e7e9688ef0e76", "5eda12296a5987b616df812f5b404b94",
					"5f6abfb5fe141992b38125564393c93a", "5fc02ca93ece2dfcbff8698daf1417c0",
					"60efbdd067565aeaca4203f37e7957eb", "613fd3049fadaa70bcd42ccc7c445cd4",
					"61b47c08bc12e96ca932d95778203998", "61f1809da35b94c2f67e32cc34ac2355",
					"620b0bd5734b1a589eb91baa30283a55", "622026a7b594030ce03bd266a2b58151",
					"62a9582b11c37fdc293c298615eb9d9b", "635c345c9f23911d16d6f5bb4d144d91",
					"63ad101fe7ee4391f333124ffd488628", "646e279fb4bbb96b4ed0d607d3a5891f",
					"6482c70c6ead02e39d559f059fc22562", "6483a3642e8eb3cc4530290bfba007d4",
					"65bd15371573a157916ef7309388fc85", "67099a5e4d0ee1dfbf957d134291473e",
					"6769f4ebc76f6cf9c134c14bc54df030", "67feb6e334fcea9ee9c6979470b3e729",
					"6893838bfcf5f5a2bd52b93680336188", "691f0e951c5d60bb7a52032041aac99b",
					"697a6cfd2e7d868df0295130e1ed05d5", "69c8b438fe211c08f45c4ca6ab4f5713",
					"69cf5abbfcdd961dbb94473937298d7b", "6a47139c4fb4c02fdce22e6a5056bf6d",
					"6a9d2207794e3ca9f19b50ad0479212a", "6ad4f06ea99283deba68f8a344c15395",
					"6b54349a1fd72267d931a53fee7d1f0e", "6c2bd0bd17f4780eeaf1613ce77ac3c2",
					"6c4287e32c46e25a297b758b0f5bc440", "6d3c998c8bb2f4dd8df866c9d7268d5f",
					"6df709c3c27b61094cc951629b4331a3", "6f452819985fdf38fe0f49fec108f830",
					"6f64fbfdc932a82f1bfaaaa294d03134", "6fadc1144c3d2310c28a888e00eaa640",
					"6ff7c72b1fd17ad21cc46a099244cc2e", "70288ec812e31abab07e5cc4b4193133",
					"711082f329ffaf11574d1463628e9278", "715e1836a5cd7a9f7b2bba5c834fa35e",
					"71c2ca22d876fe5d3d7789d222051bfb", "725c4012506bee9c003b85b5257fbcff",
					"72b472fc678307cbbb30834611fe0761", "72d4ae05153afe98d797765eaacabb70",
					"731d8dc5c69b4e3e99ff274131f8dc8f", "74a9a7c948d2986d6ff0ee561518c8ea",
					"75f918674f27766a2df224dcea704e9a", "76b94df20513d55e90843b5d7396639d",
					"77273a69765b2545ec35a8d2e79daeef", "7766ebace507a67b4226fecd1b5b5877",
					"77ef8e2738d565a32dd858d67e7a3b27", "79b7484b6c5c5501986d3c7eee8d3a59",
					"7a3e70402e848e8bcea8d3c040206ca0", "7c71886b02392bb8ff3295321bd4d217",
					"7d2318143497bbba97fa0d9ef6f8d85a", "7d7d85ed0891d62987d4955d5d48b879",
					"7dc944ddc8f7b4b41425e0b5fd3ab017", "7f100d0a7df9da4bdb10784c72c76c26",
					"7f52cdd45cc1a7215d4f616a97f42398", "7f7c89f4fc1884ef6c9bbf8a0cd7a7d3",
					"805d1ae7d70e20af9fb3ec008d53ab6e", "80aef850962596256d3ecc694e0508d8",
					"80b211087dfbd92fc8176aecda8a1a6a", "810259abfad64b12929f481836881847",
					"816be0be1651fcef4ac9b1e72ae4667a", "817d845862bb0590c74f9fd192a4b122",
					"8187403d73a3ad93649882bb2b9acdf1", "81924620903e691073275cacb77dcf1c",
					"8250870c617995d924fa70a23e781e56", "8317649a954a6cad0005f984d42742e5",
					"836ded63351663f9e5846a8f2f0a1cc3", "841fd25f45c120cd4834ce225f6de709",
					"8434dccf9ba97fb5756e07ba2ecbf146", "84e58f7f3b1efac679b6de7ec422a25f",
					"88552b596667e1fc872914748343b906", "88c8218d24547f2ce3d55737af982008",
					"895818b923384c68dbc0471cf633aebf", "896dd6ff9dfe43d1a07d16cd41d97b30",
					"89e6538a09381c79dcbd2f1c45b4ed2e", "89eb379c6e0632b3ae18da18bbfb3aa5",
					"8a43660aa27d730f3ecadce6e6ea4c41", "8a532ccd0f59f1e03e3d12fb2190d450",
					"8ae3d5883af6fa14c7a3d9d0f8c4d867", "8be4964880fd44cae79a0071b3c48ab9",
					"8c27f61f14928752945021fc8785b681", "8c485fd6414b45b01c0c9da0357f5ac0",
					"8c8f0f5dee025e3c1eccbd5caf7defdf", "8d604ffd34739f0d05c98008b5bb69f7",
					"8d6650009cc502742ea4bcc11c820400", "8da08d5b0b8c0a1c5308221fce851456",
					"8daa9c0d08328c3b33b9f86e482d93a6", "8dc42d2cfcdee6dd55f0eb4e5190b461",
					"8e05126f2aedb09b4f832e3d9e95ad93", "8e16d04c62fecff34406aa6703e64f60",
					"8ea794c91d9f3f6c408f46343628de27", "90d45881260624ca5d29fff94877453b",
					"9116729ba15a3323868a42930b7766af", "91450d311ec7425c4ce027189528e33a",
					"91be96596697a0f744b29da1ab7f2c3a", "923bfe98fd80d2056feb37a4834e4be9",
					"925576c305e905214d985486c6cc3094", "931931b38273bbe8aa7bb5fb6df5b1e4",
					"933cf402ea6dddfae795dd63fc396223", "93441b932883dc34e5749ad8434ecd2d",
					"9388925b8c6e10d090efe2db728d1e14", "93e7e7ac896c36d8f3f90653ce46bca7",
					"941999a2b45ee3f7cf9ba826ddf3fe64", "94ab6fd59b87130cfdc300da36b4e064",
					"94b288b501aa4c5cd9291668d00f10ce", "95e1d36ab97c0940e86ccf8ee4a5bce9",
					"962518ab7cc8c6b4cd486ed543763547", "96cc848ecbe0cf4ebde14ca0fcb02777",
					"971767344f0654365f4be1cc20f373c2", "972b8fb0dc3ba8afb9c47503508ae721",
					"97815938283767c0dd14741d3a500af9", "979acd2d86d2a2aa130c07fc3474147b",
					"980b8154be8396aa12be51be33bf026a", "9819084b6deeed76e99af77046214265",
					"98a6d31e39777c5ba32fbbadb874a3b7", "98f77ea59ab4c8ea3729b36148692168",
					"990e179ca18033b017bb65a004d05abf", "99b86101efd55169050a98168e6eed59",
					"9a78fa171bd5fabd4f20cffa333c506e", "9a89d0f48c71d2cba9fddb4e9dbe83a3",
					"9b3b166c4046fbdfe7fb4a959e049b02", "9b974bf43139add441d6e977837727ea",
					"9d8450cabcd4b6ed9cfe77ab8b1717e9", "9ec448ec51cd4fd188009e8f0c379109",
					"9efd70270e21d74cf1fe99a27715e2f7", "a02d091c2900bc0b0b73ba7d2f1dd649",
					"a081283b934b3247d0b6105bd2541615", "a1755f74ddaaefc2965285d3813faa47",
					"a199f5fae8ff31a4572a6f73e793cdf9", "a1c14614733ab6d43caf941762658f33",
					"a23563ec9e6e0c586f308d84af93bff1", "a2c88a653be5b7ef833e6c61f2a441cf",
					"a3100f11f83b7d9fcbc566803521c87d", "a32af2ad6ad41e4bfa6026e33dd5b57b",
					"a39f9f0df984ff071ad97b5d6a14b2a4", "a3baf45da9b96e0436fbdae3e400c820",
					"a45a3916d08d743d35b9a6410b01dfcd", "a484838fc3dd4348f3ca9147f2f8288c",
					"a49baf229395becf4e08eced1f93b16d", "a4acbb7aa5927192e80336066d5cfb45",
					"a4c39054ed2978f558868f11c710263c", "a4e935c9f75301f74166c5aebfcd0917",
					"a50a89fb48c0af6fc62582a093ac38c1", "a757895af8da89dd8eb2a7ed1ecc501b",
					"a7f52c3a4162944e65186d3091795de4", "a887442924f9a9028f8783a8c97d635f",
					"a98d9bb92f983c936597364134f82512", "a9e07af1ac300d572c3f9b5d21242635",
					"aa60e9ccb883cd16b0822ec61d4d8689", "aabeb663cce907cae94dccce5a40f6b2",
					"aaf75bcf95249b3c75dcd89295e37471", "ac236e30eeeae75b344ffd45316915a4",
					"ac63f3f3c73f0a8092bf7817643b09b3", "accdc8bc9bb3ebcf0f43df13e97139dd",
					"accdfd863558bd3b17295938dc38de51", "ad3718f67c184772e6a8ecfc8ed9358d",
					"ad3b45a30c9bfae6c5e701d3ff918253", "ad47b4b0c1c880064cfd47b846bf3bb4",
					"ad8bf03716dfb907ee74b5c317378807", "adc578c830113a73bfe6cd893b774e0f",
					"adf465ad74d70bee27c86b9053dd2ef9", "aebd8fd2b73054574cd4069a443ea86c",
					"af78f2d0dd09111ed88c7757bf980bc9", "afb88f6dfb3bf01d266da1c1a1c5c715",
					"b0287fc31be53100c8d358661142afd4", "b0890016f2c096389285021c0d29c973",
					"b0ad6196c177871fbdf880bfe7a4e5a0", "b1fd4ba063d94b7a73f21f9a1a00b5a4",
					"b2229d6dbb017bcf9b77f5781d654a8e", "b26af9b314d72dbb5f21f8a10859b27a",
					"b2c2f83f61b214a343113ed987ff7af6", "b2fb7e4647b938d6dc29cf6a17635ed1",
					"b3196df869400394b62ca6d84c5d19b0", "b392c3509260c73119d055c0fe74d5af",
					"b3939eb67e22a9618e3f622340f04578", "b543a0bdadb691f49a2c600cde026749",
					"b688ee78aa2cae8e81ae4756cfd60521", "b689d98ed8fd7758a2885cca42413727",
					"b6ab6e133ae102bdb8fdab3d02d65b10", "b7ab71259c709e1437084d7548b9db78",
					"b7c1fb2f60a4ee0fe52f6b7e9af2854c", "b7e66e32389e6db4b76bf7a5968237fc",
					"b85748cccad740a3ddcf04a71edc58c3", "b9248cee2e93b43579e9fddebdd213b5",
					"b983e1553446f060c9c4946d6fca841c", "b9a2f4d1a602fe6599aea48aeed6e5e2",
					"b9c8dff3097bf3f49c7fc8d3d4d04705", "baa6c3a111594801e2891d2867a085bb",
					"bb40a97abef4d506c4439214c48cbd8b", "bb9764d4847aaec64071e931d0793ccf",
					"bbbae1a42eef83f40380b419c530f945", "bbf2ec6d302904f3c914663ad37ef35a",
					"bc4935c8aaefaf86a7438be9f06140ec", "bc99a5b643a15f462e1346542bf46a68",
					"bca82a5cb102609dccf03ce02acc5b21", "bd7530e6ca566a55930f195778b45603",
					"bd9e1885a9e1c8765f70fddda21b70b3", "bebb618eb9129a13ccd8c50efef43a75",
					"bf293421b32727cf0745e8d563ac0460", "bf636975c32960f9ff0c1462a16ca01e",
					"bfc652bbc48b23cc467fe175db0e24a0", "c0abec9a4993d25ac1178a49388ff0b0",
					"c0c371572c812573ee6473d2ea1194f8", "c13ab30e9b510702ebf4f4e16d397c52",
					"c1558d40e4f6c7b2aa2d6c3fc7fd8d8d", "c1a08b1d2692a25c403976143366008e",
					"c2114de62e4fb02446f0ec35a35898a2", "c228aa78ddc488b22882489737070986",
					"c274ad1a451d5b7cd44bc0e4becf9542", "c36e1123fa4991ae5fb7a2c8db2767d5",
					"c4de7cf2c8491f9566a66a0d3cd24612", "c558ff3da87ab39cda55dd5bb448fce8",
					"c620751c29cfd1a9322d8cfe0bfd6579", "c648d01fc5fb7302f4579a7ab2a67c17",
					"c651ad40eaa27cf54279747f88c27a43", "c7f85423301ad2e2fa08dd62abf99c9e",
					"c7fcdc0085a2b5ed81b42374d75d3cc2", "c87131a039faf2f9003eef39f7c8597f",
					"c87568d4f7fb40daa30e0e618ff44ffc", "c8f45bf6e098ae056ca200348a3495b3",
					"c9fa8d6337f2eab2ce04ee39a56796c7", "c9fc47cbce0aa912bc2945b9d76be2c0",
					"ca1f8f660072444dd415ef256173751b", "ca3cd865bcaf3ee034102a27e2787b51",
					"ca538c50e5607ec931d68c86af64831f", "caa14df261a04e1c8e8fcc60c0532249",
					"cac759c12205a630e0d9db7580666820", "cb2db4a7486c37127f9634ffd92f67b0",
					"cbf8d10b0b8d6c2e5ff6a9c94fe3acae", "cc5a553506d6ac29390fcb0b16fb06c8",
					"cd32a83da0034b50b59b3995bd16fa56", "ce1d6e6b71642cf649a8badba417775b",
					"ce58f003060b498461e5c5d96f132d5c", "ce86f40b93c019432522b691032d4110",
					"ce98bf66f56b0c002c4885e0efb48a78", "cf9da54a9f20391a2850e8bd488ea2a8",
					"d06a707c6643ef9fa04d911fd3f3178a", "d095577a9ba1994fad1b22370c6a8410",
					"d0f2bca940f38a10c5bcc3f0255e25da", "d1420716983d473ec7d5e0e3c39a5a7b",
					"d1ab8d4c5c2368dec4dbf5d18fdce67e", "d1ea22751760c4eace54b192461b9c02",
					"d218447ffed5b53228d8840382eb713f", "d231c776cbf7dff19b60fd4b4df08af3",
					"d23691a8936fe400d59638ff18bc2f4d", "d23f55704439e92591c7af80fbfbf8b8",
					"d266975d92f25063836829eac5c17ec2", "d2fdbaae79536ebae1fe0524eccccee9",
					"d441349150240f7dbe169e2719af42bd", "d45de11213a33e18a38d5db816e606f0",
					"d4b73dae7614e849d4692238dd7c1db0", "d565af174bff93b7b1597be9a00301d0",
					"d5c3dd7cc4a53fe20006e84ce9bd0d8e", "d64a5f0893dc70d29cde928a5ebda998",
					"d65cc79d6c4b29d58ec359a2457821e4", "d6e18752564f03a615f5e256ee48b890",
					"d6e535f5c97dace2dcc8a1e34cc479ae", "d72f078d21e897a151e850111e69ccc8",
					"d774fe6146abe98181335c4100601d31", "d7f28d7b95210a5f59ab28e1092b6f7a",
					"d83e09fb4490dccbb44fd48dcffffd93", "d95f82b5cb6372acdc27f523c0a5859e",
					"da135df9a73b1cd9ed5ba5d2ba84b81e", "db1bda988523f82b3eaa6195b3881786",
					"db44dbee7712c52eea053cf368f73829", "db5e7cb3df671e63dd58d8f576575806",
					"db8d1aaa4a162254e12e8a0c3b72b2ef", "dbb6e63210c8ffcf5b62105ef93cb1ed",
					"dbc70386bd56802152e646be61788142", "dbd32e670d450473a319d7e519d1ba6e",
					"dc15caaec58919b682edb56878af4e93", "dc5f7e970209d5df0a9ca3be56c295ec",
					"dd1b7c18dba92470a09ee67f2225be25", "dd55b20768036db04a1afc109220c6c3",
					"dd81acaf36983a5ce30b25d0e6f07057", "dea565f258224adfbae0cb14fab6639b",
					"df45bd452887693680af89d392c45d3d", "df669c7f14df3f9c5e7727d61bb4dae9",
					"dfbbd356a9f9b250f5e42e30cdf1049b", "dfbe796830faae2168befa4600482b9e",
					"e074b655f82ab75e70ebf5f62194628c", "e07fb4d9462faf68d86d4bee58cb8ab6",
					"e0f867992e0b9863a02b8472cd42005a", "e12e9edded90069b5e6c9080af0603f9",
					"e1acad36108c29b3a1599eebe4971fe8", "e2d638d003a2080f160a6046478632c7",
					"e33f9c71cf3befbeed4daf249cd2f5e5", "e3503074959920b21f47a8e2f944546d",
					"e3b24e49ab17708d59efba6691cd10b9", "e3c26d655fe82706a54b17b69c823f50",
					"e3d40e26b8d876b43e09909d3c91f3bb", "e47912992ec1cc00499e47d6def65623",
					"e52b77cc41961c37394d619c2c8aefb2", "e53aa87c71833919af79c41c894eea51",
					"e5498657dff893fc67ae7dd1e170d9ee", "e5ec86b0edbca890ee49225872c836cc",
					"e6576004771d487629d8023699bb0dcc", "e728d1d93573ec5bce12b2de9300d83d",
					"e7d88cd761d2495710a6dfbb73b951fa", "e819e429e9d5198f2a43a49a7b02578a",
					"e8429a36f457c18db6cca1e024207ae8", "e91c1860b5bac683c9c661db9f670887",
					"ea11ac56f88455984072f480926259da", "ea4b38255a9c924b475fb734cdf73a60",
					"eab3cc68bb9827a74ad6418b38b421ab", "eb641f823a0a017cde26d695a796f1df",
					"eba58e0d5ca16d77b7c521640ce42e6b", "ec2acb86427713784e4448d434ab885f",
					"ece2192ab7cc6174b842153db3c4a392", "ee88b2e638b8a9c8e501db90613af0e3",
					"ee9c962b7efb3306eaa87feb08e8545e", "f0184bbc9870bfabdbc191b233fee20d",
					"f0303bdb1bd243aa7d09a3b473379528", "f044ac6650f60305608bf5c408d6cfdf",
					"f1e4b2ef1cbf3da76744148194ec11ac", "f24402508c48a9472faacf240d4ab57e",
					"f2465c89c48343529cf8dff054f15d22", "f2a948d2d10d04e3980a6a17f6711932",
					"f3333d3cf15eeada16deccaf458ad262", "f3370ee06393b33a2e54b64ab590c8e6",
					"f342755ed92faad45a024090707722ec", "f383adb04f1fc74e27dd94d8c5df9be3",
					"f3f78205f8bf2203dc3926e0940ca5e9", "f40dd26bcbef2e570b9a9123c68e1326",
					"f4c6d56105e1dd3990fa9048ec2ec953", "f4dec6cdae1de6f674c6f57fb69ccd75",
					"f55389d8a9945a3b8020a4bf6c4d9015", "f604bf0edbd90c44838916f90b9069d5",
					"f6a2423dc15c413b4ed820b3954c542f", "f787ba73c282cbc3de098e658a198aad",
					"f78f8b2b49104d65a35beab8b0a05419", "f7d95c6799ac2406949a9239b813bd65",
					"f7e7a285424cf09b2c97c0cff4acb1cf", "f885ec4bc3faa0d91452426e1e9fc097",
					"f9354e499edf63d109d28b4b445cf443", "fa08184f1ca7b65a5ec45779d86e3b89",
					"fb44801dc0250b1aa48335706f37e068", "fc343489bc61c12fe3f26929ea06a2a6",
					"fc59406017b2d0467addf45e513632e7", "fcc9e8ce30c41e9b77297263c9f379d8",
					"fd45d26f95f37d6c54d6c19b03c005e8", "fe8c85c717e47c54079e3b646d50fe26",
					"fefd88086add4de5a53ab2b8bcc2b61a", "ffa9239b017760682df0e7bfffaab6ee",
					"ffb8ba401aebd77d7bfcc47968eb65e1", "011edd65eb5f64224d0e4ffa4f5cf2d0",
					"012d0f67573340521ccff5815df213f9", "01ff7ebb358c5f4019412d783b9b0436",
					"02f784fe7a78184993070b64c3ac0d2e", "0372bd550e61a9df487ab3d07716dd82",
					"05146c2a493800cae78130e812690f74", "0521d62ba62f9fd3d8d5f82e50cb9ae8",
					"06c6b8e59efbd363683f00661242f36f", "075758a88598f651e0366b82285cdbdd",
					"07e08956bac6eb0b27269eef62a6a4f4", "07e2f5366e03ba9511887506a443e344",
					"08bae3f8b6749dff2e1ee04d857fc6e1", "091c9d6de915dbde6c7182ec4c85f9f9",
					"0b886196719a9b4e4ccd84d0303da198", "0b8dd277204c0ab073b9936f2048ccce",
					"0be99581101c56317d7af4aa6b20edae", "0cdd955be25f74d680d43a656da2bd34",
					"0d2bcde47bc6c6ae8960815826da7471", "0d5430f1696146285190348597bb6331",
					"0da25a854e10b027bc90618e5d01124c", "0e0af915d53a0cea75f25217bae2fe38",
					"0ea277133a658c883f5e06a7843f16f4", "0f9bc7a05d58ca9c081dcfce5ddfb05a",
					"0ffdecbb7acffbfeff466a8fb595a628", "10cf9d28fea61aad2d7c0bca3556e6c3",
					"10d6cd5de273805a1f3b699724fa974b", "1137660ed11af9ee9a5a8823941ee024",
					"1238327f280232137e3326acca97770d", "129425e4c9bd722cf14e7eee0ab77669",
					"13530f3a11e92ef6bc6d74afdf97ba6b", "13a8daed840667b2b2cfe88577096a01",
					"144f5a74d023d3f39c3763b7a94894a8", "17abf2ee88996f02b188341c5be76948",
					"17b83e89d489c4df494f1dd38cf5a58f", "18062bc8f05f3d96073f1818db050b2a",
					"18824945b43c1dcebb774afe58ea40c4", "18f315fcedc2330c4c633d8f02124360",
					"18ffaa8c9b844183aad5d48e057bccff", "1a6ee30c30f54ad119a2cf5084217041",
					"1acd6d24bb1056fcde00f6f221837ed1", "1ae57cd1c1a820094275232af01753bc",
					"1bcb791f9ef72ea574320d9d07bfba04", "1bf77686e6890f31747e2674668f2930",
					"1edeec2d26bf4bc1499021f441c2aff4", "1f3e6e6dbd22e29ed41b0c83fa162980",
					"209f130b9a3c06bf66d82da65065ca39", "20bebfd6ba897363decb272f69c045e0",
					"20d60aa120c51997a6b901a5709aaf58", "20f9aff5be17e9da55090d194a0e76de",
					"2177addb9a898f166479ef5ba1b2b7de", "21924438b0d78c52158c48e1809f5c01",
					"21c1a96bcf646faf6b7ae68f5b683cb9", "22af930331693b8b311fb52c00c2d0e6",
					"238051672411f7f416e32dd664cbf454", "242bd826dbf6aaf175f7b278c95d22d4",
					"25e403084786f570e878ad19297b4a19", "261143de364acadfde9a1dacd09e0014",
					"26f1156a9e875943ec485f3ab116ccdf", "2724f5553e06a3284461cf8d227fa7f5",
					"273fab6f13c26576e83a5f296e467b50", "29a242ccc943f793aff6e70320d6f067",
					"2b9d2249f1104a3c7f5ba4414695050d", "2d3f09556fc4c458ae56564f30ce5fe7",
					"2d4c52b852ec05cc6809b525833ad49a", "2d67eac9b30982cc5cc9f1d17e05f354",
					"2e7a96ad92db6066c18e07571451b6ed", "2e87dde29c831e046cfb794eb067f96c",
					"2ed585a7e34e93fad94315908c9465a1", "2f17ffa8b96b0e98dee72297e0a9ab4d",
					"303a170e83443839776b93d6dcfdb983", "3194e08b82466877b7fa66b9f15ea3a2",
					"322ab3348be00fefb7947246312238fa", "3273e1e00f5582b6baa8f608127e56af",
					"32a2a8a8d8fe4217f74a3cd2c0a82bc8", "32c8e2da3a8b9639514ad09d7137b7e2",
					"35374e869185a8a2651d97a69ec8776c", "359e3f04e512444a83b9e5b4b59d1e1d",
					"35a48e7b41444a8735d74f7feba698e6", "35e29e237fec4a76241fac0c8c290245",
					"3685421e91217f3affbc668abceabbd7", "3693f39db46672c5abe767fe71f4131c",
					"37ecf73805833678d93ef086052ab59a", "3a0a1eb3a79d00620c1648e66c4fa408",
					"3a1b9d50a8a89af633d7de55ebba2606", "3a6762321603998a9eb7917416760a28",
					"3adb64616ec2323c21db8c1b629275e0", "3c0078d51c9e188e34055db2ff389ed3",
					"3c72b270eabf1d3d570edd576eae9ebb", "3ceec90dc821f2f68130dc2746f55863",
					"3d73797e0a3534d86684fb6334642399", "3d84ab7eee1ef5c8547e3230a407a593",
					"3db78a6eb5bc46012f9bd740fca9c797", "3f4e8e0d168f7a796a107d8547f443d0",
					"3f7c5e7c2c2142ab9e36eabd9c4ed5c0", "40efb6cb293a69b8ec0b663bb5dff1e3",
					"4136fbba6e65309766a309933368988c", "413af4d7b300c1d0a7ac0579bf59dad8",
					"423c4093a73de59522859026af21cd5a", "42493fddcdbb3063db27c4a79b1af702",
					"431cf2bd375f6ad383234fe21b5dab14", "448c276659f8cb2ee6f51bc03b4c5cf7",
					"4617280614649289cf0613a4e18d4abe", "46be78cc4dbd13f0be1d129ce7bd364b",
					"4793095f2bbea352350f625171b743eb", "48076da534ff3db80179eab8d8c160f1",
					"486e0f4c2a81d236df7022ac0d1a4589", "4886072efcc92546c5abf435c514d28d",
					"48c1d7f9ef3238619984e8a83966e71f", "49328fc40bf5d26eacdff67e0ee488ed",
					"49968a20f794d2807cb76c8ed20c0987", "49e156b3744aa95f067dd0f298a4bab4",
					"4a02662d67fc79d9c5f607b2bca82b21", "4aa4324e4e9cd70fb3d97c7f2a1ad987",
					"4b2608b28c883c585e9676c47478aabf", "4ccadb29cecab09b09599ee43a871d27",
					"4cd53423c11db1b8e206baa9badf3002", "4d20b0d1dfabc1c012af29c882a70568",
					"51e770675f988a8ab8141940693ee01b", "52610e5ab9e2bfea82abed760de1ac2b",
					"52f0663980c713378662d98c9200bff9", "53f904385e547970308610a5c6929d6a",
					"543507ebce4635ab027b2812005a82f6", "544b46df9525a7afcea104926f2c8760",
					"548fd0bcb6409bfb8e9af33688d48fe4", "5499165665ef1b993416650718310494",
					"54a3555f72f402a451bbb13b2dbb0126", "5536674f98cd57350f595781cda79695",
					"559a77121e00dcccb07ee81a085e4674", "5606a22a0c9ef9b83c787b83e56cf35b",
					"562b1ce63fc868256414a67148b1fca8", "5678329ffcd6ddb15f15e2d38600a5b2",
					"56f4b307f67fb854676725a2b7a1ceb4", "571dd47510b60c3b352896a8e02b24f8",
					"58120e6553ab26d435c3fa78342af473", "587298ff7461d8d636057d035e7b1cf3",
					"58ca50df87185600609deeddc043ef4a", "5b8c12a4eccb9bfd9a6be25ff4ca1d7d",
					"5cc709c591a5f54353099635a54a806d", "5d047e0109db5b502861bca7f12f4b9d",
					"5e99a4e3a895116cd22b88e960a8c699", "6002fc2683cc9f09ff7efa2358850ee7",
					"60ae9b2817875a7b12bde2d731633617", "61c32758072be549fe1be48bc215bdb7",
					"6377c5614ae72ae276db0c67ea8970b2", "64ac8a5c0ce11a3668b6e8d35a7b225f",
					"6663d446d1c8eb5e15ea9a0e1419c715", "67e577d35b408357facdfc9d3ebbb8ed",
					"684532bbe33bee6184d1d1ee2bd4954e", "689526f71e218c8a9209efdfc63a3d35",
					"68ac8c764c79dbf192781c1d906407a7", "68c09021b327dbc147c1286c4adcdce7",
					"68c29e2ed95df0d4dadde358cd2b1756", "6a23bd827eaa9dacb11b29bca97b9864",
					"6a8bafea89a245467e399656b3e090ab", "6b8fb1a1c8ca92976d6c128c01d2c1bb",
					"6bc3dc590b93536798b69ba44d9e2ea7", "6cbe282021fd22db87c48933d241898f",
					"6d068bc457b4da11af03a5dc0a8c68f5", "6d069b8999e5faa7f4bdf65ca2150764",
					"6e1d782c25168445078ec5f033c2173d", "6e74f2a6e5895017dab9939856ca5ea1",
					"7084dcf8a17c982a06c69ace79abbbc8", "72d7b8639c269f4fc7887b0013cebb9d",
					"72f8d59c13e8d3add807147e025493de", "735b066d906070cf0f249e8a2b3edbf6",
					"74d92205a934f8271b0d491a63d8a330", "75981b27e19984b44b9f26bfac870e93",
					"75eaada898469d486e534b0a7e90d9fc", "75f14bbd4aac55dfde237d4976bfcdde",
					"781e3f5d14c9861d66673f9ccd43d884", "786dad27c6a92ba5e97d95420f56af17",
					"7912a7cfb3b2467dcbdba7ed5714c030", "791652bb6d544047bc01806d28181022",
					"7b907d148be03f6a6d9003cfb936d805", "7bb3fc7f05fbad0a7ce8dd7b42dfb5d1",
					"7bd1e33dcbf9ceae70b36095aeb3066c", "7bf7232463687628cd2fd36b0c8d807b",
					"7c2e5e9c28b234b83dfa78efa4effa4c", "7d327e21315e89b3e7f3b6957749a69e",
					"7e0c08156d3b9d04734cb4b93a595e2c", "7e89bfe174771a9a4029a554d51bcb70",
					"7ec77c94250269e567945af2b5a47956", "7ee8ef4971ceb1667ef35cdf8e47ba2f",
					"7f9170cb13a8faad4a7bfa62bf4780b2", "7ffa3763821571e4d13eeb32a748c569",
					"8032f44461f2f7425a008deea61590cc", "80bbe14ac5c938de6346a8c6b12be40e",
					"812ee432ea79c4d5b806f5876ac22cf7", "82965c639300cee93bbd6a215ee12ce1",
					"837134181029093f17746f635d643bcf", "86bb69c062b3d54b325b6217eb2e530b",
					"86fd4d179508ce012fccaa4548e7b926", "8740b53afa7784e1b55e15fa524da982",
					"87e153682fb9a52c5474a5adcf564d39", "890af74f63edcb1760425688e0cbd98e",
					"8af5e1b24f69dcff9c3e3cae32cb36b4", "8ba9163027f705593c815facd0028ef8",
					"8bfcc6b641874873589a5b654c08a67c", "8d46f5471c49ba356b6d27ce82076e97",
					"8d62ba9a76f01455a3d67eb915e8e259", "8ef13e5b53f799625567d9966ea51363",
					"8f0ac436b8912fbb42f7f8d419cc1c72", "8f3cf647639d1da8cb8f8ee2ceacf382",
					"8f9ad011dafa65ff36173d089fa80e36", "902ba9dee2e821e744a8953dcf222976",
					"9059dd42149b4f403f7becfe0071fc89", "9135064ba2ccdc9b0331cc2bc0fc9ba9",
					"91dc3f85997f3bcf99cfa23de2469473", "91eaf1a9f0b9e353b0e14097ebfc3d8f",
					"9213909b254ada6fccdf75c323e4f392", "92bbfda148b74dc7279dd2092572d632",
					"935f02db5585acf78c97b9f7cede96f4", "94208b442f57ac803a74c62c922405c0",
					"9562a7676eb8d36c6130ecc81dcc5501", "95785226a18d8897543e96a6f37d0c4e",
					"959b04775646069852b150789a43da35", "95ac9b1bcf7aa334d04188aa3d923f92",
					"96f19635a298de580d6397ce3ea5c613", "9714a34adf54a8c0ddddda74b1741be6",
					"977792f29ff8be53e403ac094101f1c4", "97aa82f1b68362265071b61a89e8560e",
					"97c8cc394305fe6de7888772b5a2e250", "97f83d62c86d92bbed1b5ed03a6ee1de",
					"98ec3cc4ef240c23de708f64f03dae8a", "994c8e2632be13bd05079601c345dd02",
					"9a38ecdd20fd31c556b2404afd50d4fc", "9acc0ca385138e88462cb61c94dcc24b",
					"9b2eb5aa6a2b6ffd3056799581786bb9", "9ba5a6e3e2b444dc97632c68b8828e12",
					"9baa42d2483402cc0ef4d08db7feef5a", "9c19c2134e8c20483d074bfe4d35c72c",
					"9c3a65a8a0de457a4112fa299559a714", "9c3d42608141082366569d1ac1e00b45",
					"9d55f79ec7b1e7857b1ef9bfeec266fb", "9e42a2720d27524251ccada0f6a97b8a",
					"9ef0a7ada108837b19f25e9f8c621876", "9f3c497ce476eba4148a55470312ec1b",
					"9f3f8a5a6a21cc8959317225fb1616f6", "a0758043871b38bbddbdb4fe8626d2ca",
					"a19148dc4e017057d39037e672ad36f4", "a197e39e399e95fa3cee39a7f66aeeca",
					"a1b99ddcbf8ef2ba72420ae8d86fab8c", "a5357b083228ea4a9986ad1a87ef77f3",
					"a661f3189a52713f5f0cacc1f40c22f1", "a6622c181005f9a5771751d31c6922fb",
					"a6d28d5ec9b5fc9feb6dd26e37062ea5", "a7733166d684bddfd5dea666bca32991",
					"ab2add34eeef6389d9aaa9836c038669", "aeb7db11278396b3cc75b8d6fd9e4701",
					"af6f94cd359710c5f47a1115080afd26", "afcad74515753ebf5a95d7b0ea362a38",
					"afd2bf931187749527c9893c1bdd97ee", "b1388e5a1806b12956e8f789cd1324f0",
					"b283a681bfe2843982ffc1ee0f39b8b1", "b335c5ec7affc62fdc4cd50c0a250a4c",
					"b3add1c50794955e35d41ae9b8589810", "b3b9a11de6da43c49eb6560f536aec3d",
					"b438498aa3ef83db1bf1f1bfa9eb9601", "b55a1e358cfc309daf875b3e01eb9b7e",
					"b64946a61425ff6b027dcb558b1c04bb", "b67f59d6d30fead70455af8872b7f3b5",
					"b6a62b818b00f5716aa1f4b4a76f0c57", "b70463ba06264ed7633cc1da87e5ea6b",
					"b93853b2af1e6ca01bc780477955d681", "b9cf6dd481f58f6306049f72882812eb",
					"ba1b03f8cdc36a6dc47e2c9e4f737eca", "bad3e9af07964a7bd5ebba5695e49a6f",
					"bb8e1f15018db79caa24ab48cc7add4c", "bc85c83506b9654eaac1109281b03779",
					"bd78600f3ccb16e05024c3d30cc71611", "bd7f21173e0a2b6187fd428bac42af2b",
					"be394dc3b936d4e22f6d69e097cef397", "bea9dd97898dcb6fec46e44a186a8ab6",
					"c0368e049685643741827675b53b6bcd", "c085d81646e29ad813014fc4a3bdb207",
					"c16fab9b78f15a45a1c7f287c2ee11e2", "c2a52c38f0e09b4bb67ed68ce6180a15",
					"c2dbf06dab0dc10ddd8976d6bf63d047", "c40650fcbdfb10ea780ca4210c8e63de",
					"c5072d939c317464ba0816e9d6056ed1", "c530da5a6162e6dc0b28342921428f0d",
					"c54564fbbcc51a4cc338017442d43991", "c55795bb77b8ca21cace5d844d43cdea",
					"c591d1b817bc4aca70e92a5794dc9bf7", "c65bb46a3ecdb4b453cbc6e6d3992e48",
					"c6803ad54a63c1b12cb32d23ca9bb92e", "c7706b36d847307b2cced8a031f38ca1",
					"c7a32bc986e1522652a02a6e844da6c9", "c860b6bf740318d9a2a01194e539f0e0",
					"c8688069106544637641ff1f2194a4f5", "c86e8dbdff17864013f942a9247c7bbc",
					"c870bf641da3d33df657e02850f45ef4", "c8d5fe9d1c58b4644ce641d24173654f",
					"c8d88ded43327d595cd437472a267f9d", "c8fdead4d56c760667507cfab651d23a",
					"c999ec6afcd65cdcba19199e173bbe01", "c99b6f8287ac256d04c07308d7ab882f",
					"c9e8b8f229a82777fa2bfa61516f148f", "ca372819f49f233fa43a93a7815701b8",
					"cb89fea2e6b11c7237d63d09e906117f", "cc646806b5f72db45d511ade2344c881",
					"cce59a04829d8f00c39ebacd2ac440db", "cd7a7be2e6f566038ab964fb812573a0",
					"ce0cd54e94527edbc0d90971d145db34", "ce3589418e301787cbdae21369eb9103",
					"cfb425fa780c4e5ec8698f6fb1818ada", "d03727c884e979d16e2522bf3b4c1cb9",
					"d0bf1987d129660972da27eeb5fa2a79", "d0fe4f8863b0fe7711efc3f9063b3b16",
					"d16247ccab6b4afa7f48842e46ba4582", "d287f83926e9d2a25842cc08b932675b",
					"d42d86d2906d70a3520f4b85c766354a", "d5a017c8de8275b59571e173fb5b759e",
					"d7ab029ddceeae6801623e60f843f58e", "d8262cc9ff4844bb64c12c2d030e5f99",
					"d97fbf88d834697f940623215ffef6cf", "da23c4eb117da1722b9c89ce6c0fdfe2",
					"dae67ec28df04e61ea9f1c1651552c7f", "db385861e20e131fbe662436e23d21b4",
					"dbd17946f8eb2220ed65259ed472eb0c", "dbdc04e5dd4ac0c3ffd6b8865ebca123",
					"dbe4f5d8349c4ef89b05ccbe212c4f5a", "dc620a9079d5fcfebecddd9e405548f0",
					"dd0f619577e815426b2fd53fbe8ebd26", "dd28324b115605c76045eeffbb0b4ec7",
					"ddc9afb33f080bc49b00de9e9215e2e2", "dddcc653aa4e4bf99ef67f395677f336",
					"dde3d4329b168d0df2a8b0f976bb8a75", "e092f552330b0c13a8606542cb1ea0a2",
					"e0f5054ec6bbb8aed904dcbd793987c1", "e1ef491c7862b588a1bb5de7f15b091d",
					"e2064c1d083d4339adf74f1a62309db0", "e229e78e9812d9d5a1c6ba5be4de5071",
					"e270fb6b3c1152ff6b7891b442eabc87", "e3794da21bc7681ac68791c00ab8eff1",
					"e457d36f88e42263baf30a04f1dd7f22", "e6b16a423313c837ab9fa4859aa038a6",
					"e6c7862a8cabfbfde6aebd84340616d1", "e6d0403519d6259660d4987c4566f4c4",
					"e70915f9a739e96765c29039d6f51607", "e777d5d8127971868cb3b769530a4fa5",
					"e8e79303b81824a594bbe9516a61d611", "e8f08da1e025d4080d35200d7ffdcda5",
					"e99f3c15a18b6c63a76839250d829737", "eac0dc6d76ab669ca787fdc4e83f79fd",
					"eb2925a07d57fd8265ca40aae8ec5f94", "eb3739dff5b52e7320afcadced567c50",
					"eb87e9da95fecd69705b3c0a3097dffa", "ec86d9fd49a5331d6dd0a6d7376d115d",
					"eca423392f251dec4842e127d47ae507", "ede139a8631bd3dc35a5416bd99f3594",
					"eeb1d9a77d928627fd949f51de94f1c6", "ef6a95b1924d38ab44e83ef81e35d175",
					"f18e98c2dce4d3eb4e1d323326c61fb3", "f24ff9c4d0a8dce50d08951c96f97864",
					"f2de6423222fe2b5a2918863707be388", "f33675663a220edae1548f76354cb140",
					"f3379cba563b4f5ddbedc3a91583daa4", "f3f325416982132aad53f68bf789bdd2",
					"f435e40172d753df8263ee8ab82b0cbe", "f4ca6dd5e662e23ca174001f9473f8b1",
					"f4e614482eca11a669373ee526dfacf7", "f538ed6899a3de503757b4dd0abcac47",
					"f54fba1a932a57f3ff152428dadcb620", "f6151e2a0c1cccc289d878688f7ef910",
					"f64eed6f6b7940c840b9a634d0ce334a", "f695297ca92b8339d556e48e3ef14975",
					"f80d65c2a902f87fe41992f080026c09", "f87d1886bcd6b2e733e79e4a7a8758c5",
					"f88358e365b7f2546a2a03a6a9fcd3fd", "fa06df7e32f01099c0049fe48876602f",
					"fa7178b26ad50245d57f9270010cbf32", "fb83beb6cfbcd77829e4d423f2a1d296",
					"fbcef62fe02392fe92f24052b246797e", "fbd0f5758a18df58e943929dc06846b3",
					"fccfcc24bbf83e00ccf5402fce826600", "fcd75a7bbce289c2e55c0884ba249692",
					"fd50ed2f65409ae12c2597d694dc1850", "fe2bb2fd2b77d74f384ea30d0dcc0d73",
					"027049cdf5d3673b0d5cb13740280e6b", "029026762acb71cb9069454c4a6e1dec",
					"04ffc6d4a2dd627300281298bbfe9a79", "05865c6db1ab496bc6e1093b6f1932c7",
					"0742a6a5c743c18db6622a18e2a5c755", "0994d29fe90021fa6708152125a383f4",
					"0ad0c1dae42f71edc43c545b68956a21", "0b1b20410b0127eef424e019d341965e",
					"134f529cb099a5adcf52f7161ef43552", "13bcc6f54c5078ec9352d92d0742b488",
					"17992810bb7dcb30c03055d2a648de8d", "1b1bad7ff2f2547c62c6b073f3e5daca",
					"1c70b864f480a596482203b4f78d11ea", "1d157849dd797e06c4fd9f9171a1ed70",
					"1e5e41b142976b8244a37e3569344b6c", "213860ae135de5ef325b09dd4bac0bbb",
					"284abf93e3e9b8443e4658c143d2b3fd", "2b22000e196dbdd6be2abb390bfa1023",
					"2c7dc1471ffc4bfe8512e755c7f061cb", "30e0bf30bf178a130988e39dc04d4493",
					"330df7fcf9ff9a15f3078e5d8bb9a6c8", "33c912072261a0b151539197d99d1fa9",
					"33e7d4e13d162f583311ba04424975ec", "359a730578701d75832b505827a930f7",
					"3fbb15cac4014dc26e638422fd9009dc", "4ab82b385d969973533dac84d124efe8",
					"4c05f43aa46d4979a408609ec7c2cec8", "4ca78d29aaf8d003ef75f85fe60a70f2",
					"4d03eddee75d5a3972577a8bc21181ce", "52eff79893550f827b6e31ad086fafe1",
					"56ec3471b2125cb476c6daaeaedb6b97", "57730cb9453596392b3a33ad3a31ae06",
					"590adc96457db24d3e5e0291d3f4ce31", "59dded29f74e2fbf9301f5037f4b8049",
					"5bbdad12c35db070a952ca7917534ee1", "61a3ff19a7ab5d2e2cd9d4f92611b2f9",
					"65a25bfe07250eed86caa1c1f2e5102b", "65c3bb57108ae8e9cde37d95b21cbbe5",
					"665d19116955ab7377d1782a2fb16670", "667686a1637a1f97faf6b8301911d622",
					"6777e64672c64d8fc21b44eb9ab32618", "683ab195e3d65dda3026734305d58863",
					"691640bc0e916ce5675d326618f9b4f5", "6ada8db0aeee1eea15bec77bca18edcd",
					"6b44728315adc1039241fd92c8437e9b", "6fbfc79ff6df3049551b21852074c2de",
					"6fd770f2ce09f4546d22ae25e95aaa24", "71ab8ad0c2d314d9d719ffed42fd6b1a",
					"755348341c762ccb10c961a67743f544", "792556b9f1a261262ceadb789501ef87",
					"7aefd1dc59490ddfe82401371aae89b9", "7c497e34e2300281715d152d4cba4c7b",
					"7e1347144d359b8609f15929d39925a8", "7e93a27d1a1f8d44630fe177f1e7f021",
					"840d723aa22cb0783c24e173350c9c7f", "856951f1df536f2ea3fdb1e5cd3659c6",
					"85e7fa1a1b6d371b8c2f703cc3d3e352", "88219e837a896e6ef7d88e892a61e8cb",
					"8cd54b700f34e6407b9f568b2dc79203", "8d376ea59cd4391bac5e069ef1b2dbba",
					"8edc2c85fa15c8606b636230637908b9", "99556b69cd6a5a158a1260d785772d26",
					"99aa66940f92d898a468a6ae8672a862", "9adb1261a6b3d26f9147263ab0b72663",
					"9bb926be9dc9737f28b8450e551736bc", "9c1c520321d02ce271e692472a90c03d",
					"9c9b4a0178567f6ee5605af752dcc0ea", "a0611b99abd5061289c160bd844225f5",
					"a58407ce199a2eac531aad2a11317917", "a79eca79792d4d8b256054243f755ed1",
					"a7c0ce0626a45070a5fa4a36d3578beb", "ac5b187d44bd1a6ae9aa484767b22623",
					"af425b68d5df686a826556294f16315c", "b335017a6f405e8bcb966d87aa8c9ccf",
					"b714fda7a73e4a0fd734f77d29401b2b", "b998a880b6fbb9beb5d0026b8ff6af82",
					"bc59300802b71457ef4d7cf50725277a", "be6f2819a57a3354627f142083a5403f",
					"c016d15c1bdd03f2387772ef3b8950a4", "c444ae861a37dde0bcb1329efbd53141",
					"c6a79ac68d6577581337d3879741159c", "cbef8eb2744769e65a0ffe19f781ae84",
					"cf7fdc9b2037c76a6a48cfc96f942b35", "d23d7838e117579e54c3b756ca1944c2",
					"d2db349ba31adb75c050b1ce91a1af80", "d81d481198a87268da86d2471cc6f664",
					"daf06900c46747b10bf5503be6574077", "df54233d0fdfb699fe1fb947f05dd563",
					"df8038042c10977632831547ea5eef68", "e38ad5f9e92884f783b81c579be9c243",
					"e45fe2045c6e22ebf17c4f9d31629f90", "ed02603b5706b721d374adb8bc54a7c5",
					"eddd8da3ba7f27ba55bb33210839ced1", "f04c1b6db961a539df076068f7ad4ae9",
					"f1af830b3ae7689283a7b64ce13a2a96", "f770e0c525a43621b722687355555966",
					"f973edfbc4eb0a99ca8dd4661f6fc172", "fa3d1c7de65e76a98b9f9418c7cfdc7d",
					"027229d98638c9208a970af930b7d5de", "039c152ee1e43229e4bb2b1420d65f7f",
					"0bc8bd481bec49f0988832f533522a08", "0c7061b93c891dda4cf35844226c55ec",
					"0ec4b47c9f9a59215f3b0bf392e82069", "0ef2fca90c733aa5d29f5fd1bb7b0c4e",
					"1028a419aeca9c37da4f7d5f4bf19683", "1583d25541acea92f340a31d73c00a51",
					"15b50a3215efb992d9bfc6db412fff47", "18db70147c94431a28555e1bde8a7023",
					"1f2d28d8247656a7452176e50aeddd61", "21023d803dd57af8e6e8fda9abdfd48d",
					"2203b749d2d667506704870d242e5ea7", "22f11076fa1d08035890801eb977607f",
					"23ff0209e229ea216754936feb4cb82b", "248c4c7b05e99b076673ecb8992215f6",
					"2ba23801d0b5d121d01019ed6e4e8ca7", "2bc6b21aed433c8ea04f8688770d0e59",
					"306d88522df2157f97ed2078d74c7b9e", "30f6d6f13a3e638b4dd11062ed6fc15b",
					"327449f944c99f8ef40e6d15617bb39c", "32a3e64221a05cf3cc4721a1061cede7",
					"32b549f820a50852b5e0f7ca4ba29032", "38af97b72cbe1f27c7d43fa22263062a",
					"39225950ad114369ffecae77a3ec8aaa", "39e8cc37c0a82432502d1f6039a71450",
					"3c2a0461db63f4ca9438cf20652453d9", "3d588e6f40b187cfedd54018aae6f6c0",
					"3d68eecde034b34fe8d312ee829c3dd6", "3e79cff7bb26e4cc1e29656a06d41dae",
					"4239c17591c695f9b9417a10080b8dd1", "441592aeea05149d818137096ec579a1",
					"49184758d84ec71821b4cd20aaf9a9d0", "4b2f50b7e63db80e55ce12af956d70a9",
					"510fde4823d47d86d2116efac5f22423", "517399265abaefef61655c4a2ded204d",
					"51fc513d3e207479cad74a84071a7eec", "52d72988d96d3aa6383549bebe62df48",
					"5632ccb041969c77d7902b4f42ae595b", "592b6ab36c128c8cab16c80404dfb0d9",
					"5de37cd28465ddd8b00e08e2a26605de", "5f8b8eeeae7d147df57ac7eaa5e22e5e",
					"60d3608a4575930124319a995e15ee93", "61b83bcf52f74c834478331250c9cfc8",
					"6497009f6b4e78d06e8080600c39fa99", "6664833f77f1af0c55da11776c094f86",
					"66e3b841d95ac9dee90c137fdcd7facc", "6e78897960f41d66a0b7db8813179750",
					"713f8f2122603e84b3c92e0165f7a2b2", "7313a7afef314260da129072a52a680e",
					"7b5a92b875694fdc87eb949a9a1da56d", "7d3a30550eef4e5559153c444a476418",
					"7f73945d21a7818ae4a969cf892364b5", "80622cf2b3320635b36b1b62ddf49fdb",
					"83ca1c815e7c8c1f245c0ce694f465f2", "856ac52486eab31032ab560e051c4032",
					"8a13ab07451c1a395b7780f68dcff3f7", "8d009a82860469de3629edf762d49500",
					"921608a4308cc282a6f8b81152ce01d5", "945fa9db0d0bf378d5fc911980f8600e",
					"95474f157d9d5e8d158695035763fcdf", "9678ddec4da3f301e7850c4ea4c5d359",
					"97458ca1edf03b44b94a3f853a37ad79", "a092c3afa3f12952489cd3189646cc1d",
					"a32e7ae8e9123c4861881d4baff66e8a", "a349a2267f7d8dd32b63cc51e287002d",
					"a42b465f686a8c012f5115d9fc2efecd", "a4a04e95e8208d0f687a173e7beeab22",
					"a8ec43c514d62f34daadad4b4c31137e", "ab7ab1f579a44afc657e8ebb7fb8f85b",
					"acb63b4f44de09df95b4357c58d8f0ab", "b0bc1cd70560faba60c9c9ad968ca5a2",
					"b0db25b9a58c1e46c25f8dc2352d4588", "b86abf218f84f52dcfd719e9940a56c6",
					"b946533b856d2e0fff2b2b3771e4b0e8", "bbd1e8fc202e3b630a5849830ec1a3b4",
					"bd3d02501b0be99da1395996204596fd", "bfcda2021c5197cb660cbe2a4be4d048",
					"c3508e24e4e3de1b911606a0450fc6b0", "c745c0b21729075ab8155f38ecba37c5",
					"c78efde2c056422382ce4ede64e63a64", "c8491be814e7325291fae3f398f5ad44",
					"c90f5d64311bc27107bc9a88f330ba0c", "d0533408d09c206da2d2a2cda94d4675",
					"d25e9c06ead4c43802e067ba47d3e6f4", "d2c50dbc9c806cf7a3a06131fb79d2ff",
					"d44d66a387eb789e64891f2583299cb0", "d50d30ef194357d980997e043760e114",
					"d7270dacf50f93d89e92b0a0eb93bf41", "d9da3089e036f6df7b4f523254a8216c",
					"db6bfd8267623a054974c15c95dd91aa", "db94b4c3dc56f35da6099de2ad758248",
					"dc1a382a7b972b9ee0be1b35ac122f53", "de09e14517d1e8a17db053444c466f5a",
					"df72d8076bffac7c22600ed3882227b7", "e2854982f7175b2aed2f465835e7c8ff",
					"e66d2fb1389988dbfe22416873caf470", "e71f9071fe13842921814232e44db379",
					"ebbe5b98c6a5aa4434209714923446e2", "ec24e7df7b6416b2099629211beefbff",
					"f0e8e8a0c3a40d33302abd8c7ede9901", "f431d7b5364056e9ca453e6539f119e3",
					"f4485d70864aa14145c6e19b22621505", "f7a8026e6f4c0669eb608e80f22b9034",
					"f8e33086883b5f03730075a10f4885ae", "f91c6af4ffa1eb7c3ffea6d305919758",
					"fa454d87ad429fa2821120350c357e0f", "fc96d3fda0a455f5ebef48f5e6e6a3b6",
					"07974bd117a96a4c7cd4d195478ddf90", "0cd777a1f7ecef91b513bd446c1d7dd7",
					"0d2c8b6c76079b672619f3fc12f9be43", "162fadbfe45dd6a78848358cfa71077c",
					"2602a34aa4e5ed0d48fc4aaf654ba1a9", "491ea2a0c496685e1de6e0dac44540b4",
					"4e368ca01d5d0307c85505f29f81ecf2", "50728e4a0b29aa50b3559c70c60b086d",
					"533b9dc402f0dcc40cfe17a3051eaac2", "53eeffc0c7315253eeff69529c1d0295",
					"58b15f73877c10c357dabba9ec793bd2", "5bb9f0eb72834264499a6ff1363cab87",
					"5e5578dbeea8ed3954b918680d193a7c", "6946219cdccfaf56064b174e7bb076df",
					"73ee771b3b9c92d30d034224c6182142", "7d1e761ac8140ba601345e353bbd4312",
					"8971714ca4ef171c28ab8604147913e6", "a090b05f43de7bddecd49b8e65e44b81",
					"a3e354c904f1cabc4bc445c377c51a9c", "a55ef4f3bfd0731e29fd49d2ba588b13",
					"b6dd83434024f88d102fd02906eb068b", "c29b0a27681e66edd9c27912e58d5bac",
					"c78eaf3bc07ea667dc5db17cdd14d53f", "d651749a7ad145d8cd580e61b985f182",
					"de3de8f8be39d59b96f5f46ee46f2887", "e2cc616fc969a8008da118ad2c1ac5ca",
					"e8b735f02a4138415f73d4358fa8987c", "ee0145136c2f8b6be5d47433163742a6",
					"ef72c8ab4d022a887645dad15185241f", "f45e11c32b777233a97271c3b81d20e7",
					"f75599deddfc49b48d6b8ca4fbb319f2", "fdce6836dc125cab38afee145b9edff1",
					"ffa73273ea7b75ece82160448d13dba4", "0361b8a0db94dff8ecdec22792bef133",
					"0686a7b82675268ac41d1de17e991cf5", "0e3ec6a72b3a3404cd12fc8b3f0d23fe",
					"138a32d87f05e8b3e44401507e8927df", "1399f9a4879c8a15ca12c2105c4f790d",
					"1d3ba08f8780b448710b29fea4e2cca8", "1e3b01b475a6f7f519d08c554b5b5d12",
					"2010e4184f9f71484334fcfa5e07e56d", "2de641e0f32193b3843603a54ca3758d",
					"3661166dc4129af58eba5f1c5f42f8e9", "3e16a4b818f390c2607ea0df56a1cefa",
					"3f011de055aa9d431dd4442afa25a161", "49698916bd9ff5f0d8075f418c7d6a80",
					"521d9a8869004fcea5d82ac7e5e04d72", "59d42423a7a93e88a8cbfbccf168fbe9",
					"6db5fd205ece9cb4d4acccdce4f7f0dc", "7e28f530a38e4436ccb417dcd75e18db",
					"8182158b62d44d9ed54dff044c964696", "9557cf6636a3f5d56dfe9d654e6f36a9",
					"a4184c79c49a76de7b37a945423eaec4", "ae0fd2b79bacadc341eb5450693a8546",
					"ae2ffd0a4527b564c45b6f53b45f456a", "b8d6cfc09bfaf94d771c7dd62f5d20a7",
					"bb964a0774d4313e8bdaa60c58808a4e", "be34c74782521d2738dc7b3fd5762b81",
					"c25787e5c72e6cdada080e94df9fe7f5", "deb7452c4eee587fcfaf1ca4139764ee",
					"e0cc9fd90926dea0eb37f1d42b9bbcfa", "e3559773d4152058f7fce38fcc10cfc3",
					"f6cc31450b2ce59a7faf5df5b98d68a6", "fc4f719abdf54caf9cd768a2297052a2",
					"02f30bfcf0875427f581d71b0ef9fe90", "030c3cdaa6c90861810008eafce3b4b0",
					"17a38fb5485aed4595ad540706ad8153", "1b834363fe98f23b0378c4c07fcf5f47",
					"1c2046e706a49c4b17f7a70f69a03958", "1c2a6d09ba871ad83b297c16ede9ed53",
					"26d591f165c51f16b5a613524f943b77", "2d153f0453540689cd3bd7461c443996",
					"30fabfffbf33a81f959e10cc8f242134", "35c2d76144423750c76b9c2c91e80e53",
					"41817e1ebbbdcec0c86e8e4418272a2a", "483ed3b62792418f9ea5ba2603162fca",
					"4ca554171660e89efff98be6458b6e6d", "58fec250c1adb8f70a85fcff1169d60e",
					"835513d5726d3a969467fb0e3f17c3a4", "8adca2ebd22bdb9d5c24ef75c908aa3a",
					"8eec8b2c34d93f7ea06a1163460c1660", "a0dec410d9ccd547efa1a3519971f67e",
					"c98603621e31d06e9f40cbfb402b4d46", "d27c9bd7140831993c924a7ca6703c42",
					"de833f6ddc2396a1768ff3d9d6ea5c2a", "e57f067f4e167f0368157f6ad5eb04f4",
					"ea275dc1f0a02d25555178a6f70fe34d", "f20c00a08252ce535272c5780f2b5ad9",
					"00877b7bffc20d833dceb4bf899aa1fa", "07489d057d2359dda05f319aadad1041",
					"0808992f02d77c8ebc9da5570775eaa7", "1448f89ed510ec7c4a751e38a211c863",
					"17fe6de07a9a3a04285adc8337a796c4", "1f827eef1c25a8d8d1c19b9a652ac999",
					"20ed675e0f0594f8c54ff3511520f9ca", "2411057084058dbcffa15cdda579d83a",
					"32fbbf9146f0044c482eca372e98ef5e", "50c6dd368e7ac2a82c8e77b9ceca0cba",
					"53a274d3916ca53de61c159efe598b50", "54fb2f92e8b4a064c36249c20b2c2934",
					"5802996cbeae8491a29d2af403d77fbc", "5fe29c4f5117ed396b1efa33dc6c9144",
					"61073b06d79b2dd179b4e6b6f8b9d527", "696d240e12c685e3b0868c251d01a943",
					"6a4c2654b4c50b0e0e5bd89418585bb8", "8c21734e3cdfafd122c80f1416122d39",
					"95877d9beed6316bd43e111be12c6296", "9eca762515d262e837d5c1229d350f99",
					"add612bc224c2b953fcdeb9a4e4299e5", "b597ec2c55d86e370f84ed0ae60a073e",
					"cbcc7b2f62280e6c33770c57385ba6aa", "d07a290111cbfd523ada7295666da69d",
					"d1421c1e72b9607e01286b932c0aa132", "d17b45bb5e31631c64d4a5b1dc74d037",
					"e231fd21d237d726f53f6b5ae52625f3", "ea8cc7177e4f44315a1d7c057fb2272c",
					"eae262e8c8e2a18e6471f82653bbd3f7", "f9a40c1b8cd99a9ef8e77084278fc8a1",
					"041b21f2bef0659af1af681998171897", "1ce8bdb3b9d9c0df1dde1d1db0898e68",
					"2bdd810338b39398e94f374f328e74ae", "332ee120bac5510779bb2d5d57e8b229",
					"36df1a38297442dca7f00b372b133767", "fa6c05e8070eb8f79f959d9644d83b5c",
					"2744c47654d7d55031635272ef516644", "2db72ecee53ac3c503562d38e21b8d3c",
					"2ea3060427014eaf1914450dcca3879a", "5acef0abaa8cb22caabbea87457121c0",
					"8d49815591ed36436425a482380c3b3b", "9885db5094c87eb6dbb52cb638c0dcb4",
					"ba4c2427d393bb77da71552c0489b798", "c52f76f7a0516216930d70fc20efa720",
					"cbc8615e5effe81f2cd5e9140c4d05bb", "ebe1f5da5e899d3c53334026c50948d8",
					"155c6d135041d96f745a70dd8af1f2be", "56b2cfe24d58bd1151fcb9abb2e99984",
					"66414ef677fd2107adcdfc76278616d7", "837ec1a3fdc2c637bfd2c637997ee9dd",
					"985231137ebce8139a885c8cde2138c6", "d0f6574a131acaccd73bfbca97ae43ba",
					"ed7a5fe870350fe54bc1a0349b8f8437", "efad7664be972653265c85f1759f0eeb",
					"19660465e8aeab0df3d7079bdfa3c22d", "4d50a4e4231436301936c19ab65a65e2",
					"5b9fced4777b1c8495105ee7105e2c9d", "793f3ef6e5de78b47c2aa657a2d41de4",
					"795fcbaec448249ba40883a275dab473", "85c08e77b7ed5e7ae9180f835b8de993",
					"867ca4bd01e98160a19ab3391b685dd8", "86d0c7bb2f099ab1b243a656cec4a171",
					"9ccedd3a954a40e29c68661b225d5a60", "c095aab62d0402ff455d4081dc2a3bcb",
					"e900b3b151316a1be22fd82c12a84463", "eec10553a5094e1bc5936853a236b7be",
					"f22099692ab5368a43da39e527818365", "10a9e491ef7a4fce8aa09dbf616c2e8b",
					"17c5fcc8bd7f4739a029ada6b2608796", "4fe896e1ac67fcc9f383f5df14dde066",
					"607a5f5a046545d684ed622124a48e9d", "a0af2f94d153b9d1bb0fe3277b8233c9",
					"7fcc541c0d584a9fab9d815280c25097", "8dbe5c3824aad768b4cea7a237b8c932",
					"af68a6adc902263d1620154a0fb619bc", "b5fbc6d4fed20a9767eeff2cd01d0480",
					"cc50bde5a0258ce13e4936c0e395cb7b", "f0761cb487056e438b66e750aec615ed",
					"62997c973ae244acbe2b56f18d022c20", "3c4224c2234616e210428f2e370b3b52",
					"97d919000e9e423598cbd6d73174b0ea", "f153af982bf66889fedc33c65fade5a4",
					"274301a60ba84711a95e5a1860eeeb46", "001462074b34e63aead65062f6846c3b",
					"0030cb71ecfc4e7728f898eaab73a127", "0150956a9980a5761dea765320522e99",
					"017b9441592d64396fa6d3ebbfce4b14", "018dec2f07630d8e409c57cce0a99d70",
					"01da0b6fc2571aa611849ef03df57359", "01fac46c0dce2db0eee78b1657582afe",
					"023df274c4a110d6098ec5ab0404acb3", "025e1dfc3ab8ae81add5bd44145b0b00",
					"031ab69b2ae813776db966f691a0e24c", "03a6ff4cbab0ce5ac91312209a3d6431",
					"03cd2d5ec56fc0f21be8bd959df953ea", "0496d866ea124136667bfbb2a3dee251",
					"04cfc529c00b0e59c27fd9d35a0c4e4a", "0552015b2d1a3cc0978538b571367a83",
					"05cd4050a082d8250e0a3f0d33f15dcf", "05da3981b95f3c915c58003032c540c8",
					"072a4fee8961a2dfd336e4e4f1e8aed8", "07335a8e1eb3af5b3d7681273c463627",
					"084455483933c172e14a8abdb7b57b2b", "0869277ea35250dfa326c34278a3b6a9",
					"0895e70e218727adefb4ac3f00208c60", "093c7a469d39903c8ebb72ccc7c475db",
					"097d4f043995749fcabd08d8a9e55eb3", "09d65dd5f0cd6bbde07deabab7c422e7",
					"0a216c961efd692cbcae946e375dd859", "0a62e40c0d11190320d9c6d376623fcc",
					"0ae68094a5e874a0110dac5876fb8993", "0bc4a4d9ed166aa9d213dc19d548e30b",
					"0be15799a8f47c90e82810b7abd2f43a", "0c098e19314a65cdb0a4f3bd0ad5455a",
					"0c381fd24a4bf0e908e7cb0e9b2ac82c", "0d03f1171070ec06929a5eaed176c6cf",
					"0d193b892a638957fb8564f0b9c7b911", "0e278ab556599e98d5444c5418a7f314",
					"0e6cb335db5345f83ed113301729aca5", "0e90002b6b7feadeed725d99f127ea69",
					"0ee8d8da645c3c6ceace183af9af4da8", "0f371cadaa11691b09c0028075c07059",
					"0f90e09d10646adf6a1bf65509e1089d", "0f97b55e152ad3a59693e3bc28817019",
					"109a9956baf7dc634f2415d12a5c5f1e", "10c984e635d9577db026c221401078af",
					"111df730f77a160f2333d48c5b4b2eb2", "112ca6e4b345c34b086ae14341c330aa",
					"11780939133c5e9f8b74c4cba5518933", "11e47af048e993e66765fd7e5434d887",
					"11e8a5db93bb04b24b373769a68827aa", "11e9c1aee41ac82625cd8fdc165c8260",
					"124dc4760d2dc1f24c82adf70106615a", "1273b3313e110c80d83235b8384b54f9",
					"12bc8c90b319f1512e02c346207ca306", "136eea07236fa78542263c66aebb23d7",
					"15268b4cf7b37be673cefa738a41d2d6", "158e026ef66aa6c76a2d1e2b824265b5",
					"15a1957be491d8bf2fdc820e407c066f", "15b301fbe652d2fc5f5ca075c9e62863",
					"164ce6ed868adf089c4159f08d152077", "166d1bec7aae09cd787d343c434b978b",
					"167693f5eb6fc13a372e1925c4ba7b8d", "16a9f40fc96a57970bdf04c35590a7da",
					"16ec24612037a329ec6394bdffa53336", "17abcd5b75f539cff6b8b10e08375080",
					"182c69226d94408b59b9e138f0ccb77f", "187edf32254b9bef9174d1805946dec3",
					"1928a0e140877b6d05e479f300b2d94c", "192a4df98ae70666f789b80307fa3111",
					"1980eed5bca3b61f9f34d8d62379a2b0", "1990f2bdb2ba4832e16341505a21d520",
					"19b606cb130890655f81714c81e91c7d", "1a04b506fa24553f2187bfe0b8c22845",
					"1a0ed5567dcc8f5e63ef8dcc503167fd", "1a15e2ee09b288c8c5be21fd3f2696c0",
					"1aaf2d3fc25622870127b3729b319e8b", "1b1fa02196a57b823b23be8da09aaa40",
					"1c7dfe1ca1609dcd210b6d0c32826f2b", "1d1ca8d6b1321bbeff7953dda354c8ec",
					"1e4a3e94f8f66a174e46da4075a5ee88", "1e6775ce47334614fcf0e2d08c988673",
					"1e89cf58088a979e1b6091a830cf6a0f", "1ea61a94efa0bbfa772991aaafa71eae",
					"1f004b343f9e967882dac7eb775be95f", "1fe3ccd9e209b852e449a5b93d70fdf8",
					"1ff4a5aee5b4b51813aff3f6740cb9f0", "2015574ab142a56d77e2e5ab1527e676",
					"210f9f25f30e39a0c711e81d158c5be5", "21613fa63d486f894897d94ae83e758b",
					"21629344e7424e49cf02f4ebabfc5ca7", "218c9798c3142fc6f0402e2a4fbce139",
					"21d4c9ea96d0d06c28d3fcce30930f1b", "222e7c8724cba4269c76394243c2b4d8",
					"2246267a0390487a0bba114b344a7412", "22482a43ff4c8a3d47c3b087f07a87eb",
					"22582b5137433a9cd50c95d2fcfab2c6", "228629eb714e16d460eedf7814df0a5d",
					"22a3f6fb5fbd6ac686ab07bdac3ea3fa", "233160ca820beb420199612462fb3177",
					"23646906f7508b8bdac1aab5454018d6", "23970a81dfa508d1a0692511b431d62a",
					"241c207a7c16f9240849ba3c5bffb707", "24a76bf874cc167adcebd3b3416246e7",
					"251639d208a4d5583dc55529cc097c51", "25200845a880a83b8fb52a961ca1103b",
					"255816e81aed1f31814f8bc3f42074b6", "25a18b50216bd647246bf0a60a14188f",
					"25bd08b6df1227da9bc3b66f112317ea", "26384fb030b778d2ec853da2479a1fc3",
					"269a3fd5eae1296561939c0f73fc70f1", "27b7f4b8a0dbb483c981be3265061c71",
					"27e2d9eacc050b829fcbb5dac749e0d7", "28644d1530d29718d1908efd6118895b",
					"286cf5200ea4d5de4a4f48a8d6f83991", "296af87f8b07a6c63cac2e500a65ff53",
					"296afb3360ccb89eebe76ceac2e8e41c", "299cc1772a06968a6ca58da5b7f7bce9",
					"29a58e1a905d3d0673769527fb289672", "29ab5b08376c0d20ae9e6bc45c1bf3ca",
					"2b1e5a5e897fbcb66732ee8e63806a9d", "2bd413e7346b9c524cac0193b7d2bec2",
					"2c09c9be4f0cef5ecc07f0714e20d0c4", "2d86d35c38ba5ed7b6e5ae84c6e7db2f",
					"2ef5a47a1344364447ef7d1f7ed90b11", "2f1eab0421cf216bd9cab3feb02f2b30",
					"2f24a7c6c0f67d6ae1487a7f881f923d", "2f6ef588df76ca1aca0e7cffbbe73a9b",
					"2f83b5d4b0487f8c42015d43432d3b25", "2fad15ceef4d4fd940bf635abac307be",
					"2fb826171b4b171e9e0bdb7fa0692c49", "304c198832ec03739e1a4faf68c921b3",
					"306ff275165c76e3ff8af81f38bcf4fd", "30ae7fd3a4b358bfb8fa20099de7eb75",
					"3109bed820509425e00bc459502c2265", "31b9d19b2572af082af30dcfff8ac613",
					"31eb298d42f67f6dc999c08ac257ef0f", "320dccc6070c2c30696dd80941e772c0",
					"3321758610a2c6b970262257ba1bb5ae", "336af1c7a6e33ad754f3332627ff48af",
					"348025fb13bfac8ec18169aa35873398", "34824b004ead4ecf45af18f0718817ad",
					"350f929305757271b8e9a6b4d0eb5206", "35f575bc3a95dac5ba8d328169a22b2c",
					"3607ccf78e90e2bd7cbcdcdfe881d904", "366036106e9d73e9f24d699e96acd8ad",
					"37a182c84700fc594cb008cf9059a98e", "38b2e4a18a872c3d3ed003bee8946139",
					"38bf9878fe973a905e153d575861d1b5", "39bf6fdd21cbf6bac4182f69a413c489",
					"39c5c83e33336ca7074226b527b5d9ee", "3a0a38f802ba8cd78a90d51f3007642d",
					"3a20b887b5e4e085e2ae8b364ea9f2aa", "3ad0e55afa6b2f8aa2ebd0985b27d5d2",
					"3b245836dfed9993b94901ac539128b3", "3b67cc400122e5859158da16fbf2abf5",
					"3beca7a7642b71c4ace0752f2cba9360", "3ca23afd5cd14443deddccce8cf560f5",
					"3d8f182172714b6ca5b6cf761c8d3ff2", "3e09188d763eb5f8cef00a39348f8eb4",
					"3e2a6380812c33f88023b2201d001c2e", "3e39049843e6dcdcb7a721903ef64b35",
					"3e42575b49f6a05e6cd6e32a39778b93", "3ed68fac2c649a94e699ae8b44603415",
					"3fdecfedb98c08db6931b45d25736e1e", "3fe8f0bf807391d56caf3c4987429c38",
					"40fe15bbdec4418a0bd4527719fbc389", "41ee3824cd0f71d859eadc4480b1def9",
					"42066fa0cb8621bafb7d4fe9e82d95c1", "424395f91f6c7cffd00b873a8327d187",
					"43176be3e461d2bc076b282a97cc59bf", "436ee650f4b16b84077901d952c9e26b",
					"43ed17d378c218e55aa36c1fed91357c", "4417914d184f0e01817851cad3d16f5c",
					"45187d370b35dbc0a52396f7b0244f45", "4613610f7a3219e9508296021b247393",
					"4717d1f85cff2721da4273c37e239205", "4843414ce71e5c7d1215f8100001a4b6",
					"48c9fd5a2b69139a003fa56d61a88425", "48ff2c6dcee156b20862e5d1d207c9b0",
					"49e8d4c75d8e3b88f91bddb6b450b718", "4a2dc7a57f32a14aa3027091276edf5f",
					"4a5bb5d0119598c645b90be33eba6dea", "4aa8c55a4a0d7fe91be8513740c16755",
					"4ace1f8a0b978be4ddd80c7fb8093872", "4ae75bfa5f88c0796a9dd981e452d26c",
					"4b1f0ed0a2f0939ad97c8e0c30093f49", "4bd77ec81d88b6e8251de15bb4432dfe",
					"4bd9d943244ff8814d764ed031fb3709", "4bddb094433c78254ecdb75abffca653",
					"4cb7afe6af97b104bdcd1049dbc2c1c2", "4ccad0f2a54588e216571c3534b16f30",
					"4d4d343a700b871989094a3379578389", "4d9c5db0f0de67be71bd617ee27fa3d5",
					"4e8a381f7de180a230f828214946201b", "4f299491f0bd3d3d90ca999fc73cdc56",
					"4f2e19fd6499da24e6971e45f2f6a915", "4f68164c6e319836fbbf3c412c4f5466",
					"4ffaea22fb522330dd102d1b1a9b76c1", "5053d16d6cbf43ab3b9311b20ca7da29",
					"513382c4f6b75b415910f8ba89c0dc45", "51ae43fa0dd00fc61b32ed50048b4de1",
					"5220a496f5942cb37ea4dbaa878481fb", "523ccfc07c094d448375f3c447d974cf",
					"5268b732013075b910df362b7120ba65", "5368e39d635abfd15d2c8371ade50228",
					"53deccc53da2f5e6e87003e8ca413162", "53f98581b13b8fdcccd1f128c0d09198",
					"544b90167cb2fcbc3992333f9945f214", "54c5cd10802993e6d68425ff9b657e24",
					"54ef541221f8d70f58f192a54f37aeca", "55d35427f2ba18885496391a64547380",
					"55e3d2916bfc2511275cefd3abcc2b32", "5661b96405efb94bdae2ca74fee57f19",
					"56f852af011260475cba3b98b2cf2b1e", "5728a7d00e134c06686af56f2b99c02a",
					"577e9bb6a54d6fa0f1bfa96df9c1db02", "57d1fcfeca06b0de4ec775b18d2cda6c",
					"599762b656b2789682195ac901b2c097", "59f92085b3e3c73c4a525021981e4a9b",
					"5a767355f14af1e27642017aef859656", "5a7af33c41aaa2e9958afd9336983929",
					"5aa88152cb68cff9a388ba647ab51965", "5ad56b8270502988367b5a0af4aed207",
					"5b77d39183d96a51af81b9c32c966554", "5baf5f987740c31e88732c7f865c821a",
					"5bbc2fa5af33d022d4a8ec4f2a099c75", "5c1b4cee13b0e311b2bec8c089581a16",
					"5c3a7960a991beaf30cff7a9e0a6fd0d", "5c4a8dda3a888748e734e11109e9b645",
					"5c9c0d009103ae1309345dbcaaa6060d", "5dc0c4e9e2782096b38cfba9b1bc699c",
					"5ed5415ddfcef24a3c742893ce69fa2d", "60108f3fd9df0a34dbba27ab0fbe2a11",
					"60dd6d6ee23bcc82dc0a091a29ac8e87", "612726dc5ef8793affcdc11156c14931",
					"61b117786d57150427efc14f477261a6", "62076649b528008660ad05e2518c580f",
					"62152bacc8e14b053ea3ebf19df5f787", "6253a86a1a6ab48b6bf44a5a92bf9cac",
					"631edcbb1c8a9d90a6a7608633cdc435", "6355e49993659bca89428d3a62500b72",
					"641a544ed9e9efcf6ce447d998c7516e", "64c00141f43eba2bca6ee2437e0dbda8",
					"64f6cc079f811d4bc1d31009275200ab", "653c84734d3e6ca91f4a06bd06f064dd",
					"653e612375e13747130f01c489af8bef", "65a3a20b4feb181fb34e0f9f03a873b9",
					"65c2a185b73321516924bfddc77d9662", "669cdb652d67bfe6287ced93873f271f",
					"66ae756b8ab22a570d0264533469c482", "66ed8289292cd0e0ba070f4a007f8674",
					"670351e16000871b65364d14042c3e3f", "670f0039d31de26c0ec941d7f6ec11f1",
					"676baf618834e5043b7620fecc7e70f1", "67eb81c96f0fc5ef5136218f6723a935",
					"686df1c291263f122aed1d4fe4155331", "69155851be92e49c5b349364547f3492",
					"69a96603ca35945775538ee291425bad", "6a78382334b9204a4655c0ec5d1e414d",
					"6ac360a0029aa65c09670045be92ac5d", "6afcf1ce0a638d1c74a7abb1bcc647bf",
					"6ba877c17353509537e718ff36139e51", "6c6cef284d1b9b5c4fae05da8b004880",
					"6ca896a0d6bc53d3b08cb621cda1de0f", "6cc517d33f134d013d9dc326becb3919",
					"6ce44812b925e1fbe9f908b84411b865", "6cf67f39f3578aac70f209441a33ded3",
					"6d1f2a859fd525cec984c85633a603ef", "6da62b1541d24059c5ace8a3aaf559e5",
					"6de0efd40abe3f9ea6805b419d705d7e", "6e8126879ea9a65c2cb0f855156cf56e",
					"6e9db1a5129483730182e9fc51d54477", "6ef8126fed8ac4bf4a2a1a17c819fb36",
					"6f83a4bebfa7ba5a472bb21ae4b3e0dc", "6fa1e5e2f9bd18aaa21775cb1c01c30f",
					"707dec239443e404d88d20f44770cc61", "70a03de3785c1d6c296f587f04c07609",
					"711538ba2bf77f2352403deb26c3c7de", "71b5f0b62b360cfb90914ae4039e4927",
					"722402c21817697957723930f51b914f", "72666361c13ab153e73056958ce1434e",
					"7380aca1cf803ba8e63e8ada911ded6f", "7400f2fbc055c82351f5c90127003b56",
					"740bc01485e473e313442476836b83d8", "74863a092d8d1cbd0f0cc4992b8f24c8",
					"76081cf01b81f1bdb4f35fcfa3e8ae14", "7727fa8e25ec50d0ddb9b477ab08b2aa",
					"779dbbf667e534609deef4710716807c", "77a429b7a0ec5414e2b8c5e755c1e4e6",
					"77fd2e8271441482083330ec351d536c", "7807976d09c794dfc32a4a59b12121d0",
					"782cc059e5279c9a6052226ec4bf3c75", "789af18d6c3b98292fff31d625093f14",
					"78be1ed0bc2d7678956d4221554504fa", "78fa63a02d6daa3fce29c1dc9017b294",
					"790f20976e6090780de62700c671be24", "7a4a602b06667bc07f5dfb48d6f92503",
					"7af07a819eac5c58db1164505b6731a6", "7b6690c86a81a719300333a309e4496e",
					"7cf85df4ccfdac33831efbe783199330", "7dc40492cca9a081206d72985236f44c",
					"7ded3cec68cc8bfe5270759ae7b5a251", "7e2bcd8e99c595086af930c9f1bdb413",
					"7eb2a889957725443e197e12865c45a1", "7f353be83bdac0d74d655c1bc5dbe0e4",
					"7f68e3dc2fcdfcdf9cbefd5d3a08ce09", "80b2a64dc80a1da82c33e4e8f644b4a6",
					"80defca97c501818d523d590feae9316", "80f38c23e2b7604254f5d00c3eb249df",
					"81a4295ecbd25d4a4fc19837c5f13b9f", "81a629b9bb8764feddc6fcc3fdf231f6",
					"82ced23f6bacf578d0994d410063df7d", "8303ea791856443770471497d1791bda",
					"830ec2dd879df2699e7859b0183375fe", "8324e61cd404026757831fccd834e1f6",
					"83c93d152c47da0b51db6a6113bfc03b", "83d6d9c7e367c232fa7cf8f98ca32b20",
					"83dd69ce8f8fe5abece118382e82093f", "84ce27bd27371f97c620bc58120d7720",
					"8522ef24d50be03394758fcba3bd918a", "856bbe70f7400035505c1fd2f6993842",
					"85899c463763ebdaa4ff9e55e0499017", "858bfcdb031631a269ce5171a33a15ad",
					"85bdcedd2f4b654494a4960b7ba33fbb", "863ee07448ccdbded2c2627a16af2163",
					"866d44e94721ecb4908f0464418b5694", "867b024353c531f9f1f95c3596d65129",
					"86e16faffcba3897b2e82c0e683ff1b3", "86eaf5de051bf5fa70cad3649f0439a9",
					"8771401e2e9ce317c3513fc11dad17cf", "8781b926c6f7d8f6a5f3684a15f77c55",
					"887188257cd5d14df053116531321c72", "888db14ec7b2d20ee04947bd25b90dca",
					"88c1fded1b15d79cc819efd9e8bfa588", "890ab6afd8e982a853dc0d0174e2e22c",
					"8949065798ae82b87059d15694b22ee3", "89c8f8e788fea74f16dde7a75b371c52",
					"8a048ca031191d7f4f30327c9a945167", "8ad6f4807af51cf5f3d3ae3d1487c863",
					"8b20d27eab1426c145079ee0195ea581", "8b50494760e2ce8e3aa82ccdccc71418",
					"8b69deb5a3a2c69b5e6004325728ea07", "8c17461b39653386738a03bf64c47d76",
					"8c39256231032fcc9681fef1799bff9c", "8c9368e6a96f018328a2ad77129ff7f1",
					"8cb261eb271c02c2f4253664bc70a834", "8ce341c684d173fc68660a2de77ec73f",
					"8cf714383cc7b6e928ed35a1bea084cb", "8da955f2e842bf519e545246e7e6bae7",
					"8dad3dcd6a1d046e8e44afddd6da0a9d", "8ddd2cd8579ce2896af62bdccf52e61d",
					"8ebc7762c767c334f0bc214f14c2789f", "8f4e3b278c21bf795f0529cc34e7228d",
					"8fe5f2fb4dc3558e4835cd408f637d8e", "8ffc269a3f065b4773b49e110bc93ad8",
					"900046cfa465d6e371bc1d8b659df2bf", "90cea9701e6e162e0bb4eb364d9a9c7f",
					"9152287fb1a5aaecf3633df9a0983a7a", "91ca6b6d629fd2d64ed4626a514fc9d1",
					"91ea761547b22bf9c8948cdc4f5a91aa", "92177aaaaad67416b4b3fdc1b8f87824",
					"9289dadb9cc9b890c1959fd3d712de39", "92a1eb63473ca98c0f5e2e03eb2d06e8",
					"92fd42040953fe20e5940bfe8ec992b8", "935183169c1781cd75fe91f6502eec76",
					"9355cbf725342876c59df36ed7971821", "938c3ff0cb9afdd872b1db27b0ea5e90",
					"93aea7e447bb051208442a241e3172e9", "93b35ad93f9063378f2d8fca28e5ea85",
					"94045b83d910c2a7927ee9f3d4fe1a61", "94393ac45b0ea3dfe6298824e7a090b8",
					"94ad0323b234fbe667c6fca608d274f3", "94b28fbe489c552b1d23c74d46c8a8f5",
					"95cc8167b1366089bdb1e37a053d8ee8", "965e483e54733f7d775d6251297c72b2",
					"966a0509e180241457b576eb0cc2ff20", "968edf205395e9444e56fc529c4b5590",
					"96b7defc21faa33b497e7582e6374395", "978e68a331a710fc9d41f96975955877",
					"97b2f16261dbd1a2414e8c0bd3e21770", "98716867ed88672e691747c66a77a59c",
					"98da1ece529dcd471dc2d48ebc8e5948", "98f66f6b6f214142ad36b6e8017d3487",
					"994d28a8c57a82c06c7411de3360c17e", "995781d37cae8beb8f41a394db4deb40",
					"996a9229b25f0647ca446a2a65e61714", "997c192d1c1d1cb23cb3182ad9590266",
					"998dd2bf9b347c2afe1af6c42b50a5d0", "99cc82d2a4c931fc48a76734eace20d0",
					"9a2d6547a1ec32c557b5aa7c7045f7bd", "9a363e3fff4a45eaef13de1668720e4a",
					"9a4db18c3c4b22f9d0b9d5fa144fb5c1", "9a621d6bd86d197666b146e81d2d5c28",
					"9a75a4d95d86623720a480bfb3556938", "9a7e231ec201adc7b3444a0a58772589",
					"9a802cd730a466467b4f17fc41468094", "9abfd59f54cfbdf96e2a337b25efc42f",
					"9ba0fd03741c8387720b3e22e245223a", "9bd6bd81cc638d7f49c479bfec923796",
					"9bef6c2a27a581d73797e69f7d24054d", "9bfc92cb72850c0c11f71990d5ef9959",
					"9c056ad9fd79d2574d262e252dd697b2", "9c0faee0cb04d35244dc0d914a8b2bed",
					"9c27fe1d9aefb7a4bf42df8f28971013", "9ce8782cbdd02cc53f0117bbf0a83732",
					"9d59168243909d0c55f4f020ea4dc84a", "9eb209f612d34e341be0f784f4dd8c8d",
					"9ebe53a5288ccdeeee42654dc157fed6", "9ef197728f22b22dc70676ca95dca410",
					"9f0739a7cefd3184cb0a24591b92d189", "9fdf9cec5580699d493070f44c2b414a",
					"9ff625c7a1e0927bf30d9314c9e7a9c8", "a02e7f55999d88f83590889f25606e37",
					"a0a8dd1f8077c82edfe9d2fb303e256f", "a1f1a88b32644184806f57630fa19695",
					"a22287dc0b3e5f795c775495b7aef06c", "a2d0f0bf4b22309a1b16f12b1a6bb985",
					"a2e76e5617d288c0b782d123ed229877", "a3086fb8ecfa4c5a50d52f4b718818d0",
					"a31a46a77f66c5ab60b035239ab29803", "a33361f7901b0d11514f2375c157bc6b",
					"a354ef9217c353afd544e4a02af16126", "a3aac38522bde0b5261334122f550612",
					"a4087ffc9c078c3f6d090a909260295a", "a4a7079704b56627b48d4345f27d75e0",
					"a4a71e27c90d6819f4c918642ad52e20", "a4bdea975ba91c0be5ec4ae3d3f11b95",
					"a4d121dc176f05a99e9f2996ce4fbbdd", "a540f5d327fdac157ed7e568e8fde0fd",
					"a6fa5b8bcd68215b1bd9ac7fe6abf953", "a703982a364b0153e58de82af50d5fc7",
					"a70b3a679084d935438deafad7f3f383", "a716f13571cc633c86ce38dd22640644",
					"a730809e90d8f7cb30d8d138ccf5092a", "a76b94e34268bd610b8f5bca90b4cd7c",
					"a8068a25ffceeee4e30ab0c14492fa38", "a827137b27b9304ece528cc8bbe83620",
					"a911d612b8d9216f724a38d70a3569cb", "a94a97beda37fa7ca6f327aba6568f8a",
					"a9759bc6b94999bb227a1b15784a6bc7", "a9787316cc7832aa546f168f2c699b30",
					"a98513849b05355d1111f88970b8f4af", "a996eeac2a97b16a6c8d44bbfbadae37",
					"a9bec5e04dca2f050c8956a94414df21", "aaaf3d22b01e16c0237812db8c24c893",
					"ab88dce5e27b496261430a35be19ede0", "ac7af84c6b75f33878e6a9588c12e33b",
					"acb778cecc94816dcbdf2cbabde9845e", "acef47a0f3f5197d0e2107e5937873f6",
					"acf5cc48f3670ca71e0e8a2ff0978945", "ad782a7fb2ad7c6dd178240d01cbf892",
					"aefa0275486b1eaf3193eabdd048b0ff", "af5e3c59337357deb00a7e357c0e0a28",
					"afeff03b9aeac022c0f87a30f2b19634", "b01c147a5e89280b48e2bb031da785d1",
					"b0603a89de14271b59fa32eb8eb08d30", "b08f432131e598b527efd5c056ad28d6",
					"b0cbc8b2b63539f56ce351e8a1fb5005", "b0ef7bd81d876fe994151bd7f71ee0b5",
					"b11954df8c27cefb3bfc59b49eabdc6b", "b30b6241ca73ff8059cefd7f37c8bc16",
					"b40f01459a361fdbfe185b09c7b00d06", "b45e2d535079b92202c4add9c85b18ce",
					"b4a6a657c33bc654dcbf7445942706d8", "b50fc93a7e945215d6b335d4edf189cb",
					"b5156680401a72dfdd47ac679e3dd126", "b521180c1a8df5890af554314f6b4b44",
					"b52334abd0b7c3b2ce7044d7c621215c", "b54b9027cff27cec0a8cc2688c761641",
					"b57d1e04116884b082ae56b1829fc8f5", "b5801ffe89eb4b2f49aa10c94e880a07",
					"b83cdccec0fe20897e6b545d484ed7a9", "b92332de49f40fe5681a3c70756aaf37",
					"b9280b65e799617e99baea58c97d08d2", "b93e171ba8de74d0285d9928a004e5e5",
					"b9a1edab8a920de6b2a8159f0acdf964", "b9a34a70987530e033882713eb878650",
					"ba33a226ec855cddd59eb96b49c85335", "ba56dde5d56b99a9fbd4d67c56591860",
					"ba86a69d61d7de42c9e3fe6d70ec3f6a", "bb82dc1ca2f2ea48d54324914eb976a6",
					"bb88c8b7341cb0e6e3841448f9a13137", "bbbeaecde3e942253b398fd4ea478c90",
					"bc304eceb7c9b89de994a61188f25f4f", "bc43519885e213a26cd7f2267657a8d0",
					"bce325b9e50ea7f66e5e1afe0b1cccbc", "bd1f00dd996767dfdc2452e2de772118",
					"bec971dcc5f95d4a011c38ec16ac11c3", "bee9d8d3e2f0d9203dd5c9a60104e288",
					"bf554cad21118c565fd1ed037e95fd83", "bf7ff970d8f83ad2e9e25bc23af57c78",
					"bf94b4d417b13eed5d68e1626903ad29", "bfdccc116fae474118d9516a0f548630",
					"bfe36a9184313b5d1c5e5797c79c00ec", "bfedf4fa77ce49b70bf5006d6d1bdc05",
					"c00782602eedf43de9494e4c69638e1a", "c092a4398f6a77dd63148c62e3ef04d4",
					"c0ca41d4d6a548fd3a3c6cd304f33038", "c1919398fda5905a76d90f8c420a2754",
					"c2342f2270c1a0115987d9e31e09bad8", "c2b8e8d38eae1d4e783a1f50d77fa5b7",
					"c2c3eef40c7325a08bb6afd9e44961a6", "c3c2293dffbad1d7625b7d730a5e4079",
					"c3d86e3910e641e74f162494dc331445", "c3fb5c49eb63215f564a5b1c2581c07c",
					"c402d1fbf8df7133126f950569d758f5", "c4206472f049f0abe6d5a48aa5889d20",
					"c439ed67c57e85dcf0c008ab863e2f84", "c449c69b3c32386e779e7dc3478407f6",
					"c44efc13243ba91df697e3fbcf225f45", "c79a665aa058283370f7c5dc7121c80f",
					"c8ab37b42f0f3a0e702b2d63d4c40cb3", "c8b20f8280097f020e5189d7865a0cb2",
					"caff62f247d7c9af9edf30a361a3a537", "cb91fe9456edfa700d707cab748beeec",
					"cb96eb7d7c7488511257692a3ec49f0a", "cc1acd98cb8614d407ba50594f82d2fb",
					"cd316e8de93e8f5dc0dfae92ea5c732a", "cd46097668604de38ed92d0b5e8f619d",
					"cd67d0010fa88b2c4ac2d2263aefad1b", "cdb9f3515b2a5b30920cd6d537e081a9",
					"cdc912cc872d0b68acbeb391a9b0378a", "ce15b348a330bad4375dd5485a4bf1a0",
					"ced1caf4ab3b5a1d44f0707c48ecf23b", "cf3063ceef121eab621886d09e3bb91b",
					"cfa5320bdd81cc3aa8a29f4c4f4f1f98", "cfea7a609323311127f5107bd2273e43",
					"d04d659cdc33f79c9da11ebf650d148d", "d06d5c5429a45f7f0b9941c575377155",
					"d0d6f35adcfe0f82b267ee491157fa36", "d1626a50c424ec11b49212aee3dd19ac",
					"d1ce933d38af71b88c55631ddf8dbcd8", "d20a412fc7b7fbf6b2a01ece78444f1b",
					"d234133f0da8c71797c19e1a0e851bec", "d2ab0b684acbae0e7c7dd983ee5e4e83",
					"d3707637a0b247a18c54cb2c6f58a2b1", "d3b81d1642be9c6f35c3d703744e269c",
					"d408369d370e6a5927e009c4d1a65483", "d4b9f49d1a800131212e866f41ddd188",
					"d4dc95f2ba3934e0d8a6d258501c9040", "d51c4299bac92ea458c79ac5a3c974fb",
					"d543fed9799a445f2b04b7523d44d175", "d5e2ce8de6a26286b486cf9af07c237c",
					"d6370646150d7e721afc63e8f199b591", "d6cbf0680cc4867c4ba80d72a5f82227",
					"d7e8f061a1f9665585e4547aa69e4146", "d7ecd87896cbbcee44e901b32caf997a",
					"d81f0ec608c24f74ee68fef88e23481b", "d857c3091d193b2bdbf4731a39a9d895",
					"d8b2dbf1dabfa630252e10b1e46edd3a", "d936ad103d6273c62c550c86468490ca",
					"d95b96d576e53a69eab684584c94bed7", "d9d407fe1a28e5eec1095d6c2dfb2c34",
					"da1d4442ea3a676ec5a5e2d8e99eec7b", "da71001f66163909f04e4735d1f8664f",
					"db050ff6e5fc76e8b3e015925cf1000e", "db0f192dea68013a75ebe711beb7210a",
					"db5c214e9229eed897344fe0bbfeb052", "db8fccb5959bb5500a28cd860c520a9d",
					"dbc769946248107bb7a85576e7c7ee2a", "dd0ce2cf9ec86b420a085a151b15908f",
					"dd5ae479834a1dd7739595b313cf06aa", "dd6ec0eeb1e941c098da1cf2ba730a66",
					"ddc670272fff834237aee75dcac55fe8", "def18a87e29ff4af983a04aaca04bcba",
					"df49deb8740228fd3f9c411910ce2c85", "df975da44246ff22828176bab07afeec",
					"dffdda01e1364b763232f4986cca0152", "e01a4204602ea68cba41ff1b54544e44",
					"e07f99315d61fb652a49f26e8755ed65", "e08f449da18c58a08bdd771d0fe13769",
					"e10e98f5f22041f305d1b32a55c0bca7", "e1325f66be80e2fb6c044b6ffeee21f2",
					"e248c7ec1268b5ff3a37d1eba8362e4f", "e24ae91e62511483b6a17e2161b824e2",
					"e2ea8ecf41302d1d5e7b4f33c2a49b95", "e2ecddb8830fd5e77743b9a97f7593ed",
					"e4407275bd0bb68ae0a5a94e2e8a0a7e", "e4444ba8b25b571165ac04c7d036d07d",
					"e46d90114b7167fd8b010d80b13a4715", "e48de0d192ba0a62bd6e0d06d52cfcb7",
					"e49221e536a421d9325e2a34ca944e1d", "e50da84b0f38cf3d5e2b7d4b58ada5d0",
					"e56fb0f28138de6e7f65c59739fd438c", "e61455ebf73047b429f5967800119c06",
					"e62a59a00a7509402f4254795602dff5", "e64c48c7c236842d309c3d9ef65f4b23",
					"e6e2699ce5a87365cb9ddd6d8f23a9ff", "e70bc4778ba528a02e8e9c0501be3986",
					"e80b91c810c30aeda898ecd4fe30f08f", "e827693a606c6c93933623d54780bb7e",
					"e8b32ead6d51d200f3634afc36db02ba", "e8cf442ca7ee206c0c5c1b5f2ad2a4d7",
					"e916bdc8b550d865643c18c3fd1c96bc", "e96d4cc4e77d81ee3800246cf7a435d0",
					"e974d2341970d586ea64747384023950", "e9a4d01b160b63d42cf84ceb58198322",
					"e9c98e2e8fc2008896bb2fc2a59668ca", "ea2e2ec43466627130dc53668f614edd",
					"ea3e7cc6d57bbac27f1a4d3673330520", "eb7754e5f55a7fb5200f9718154d05fe",
					"ed2b5ab5f2861047b2783c6e6ec3549d", "ed7f0ca46887ce617f04cb40ffbc417b",
					"ee1a28309b563ee763906a478c47943f", "ee50c642e122945b1602b575308bd858",
					"ef5fb0daceb2b1632746f879c125536d", "ef6b42d34826e5d1ae1196e95b8371c0",
					"ef8af348345795672ec1fcb657085a20", "eff993c6dcb9f116eca71525c959ba8a",
					"f0c62e7b6e9f741e85d767767d0be45c", "f0d1a3a3f2d07ade719c97308921fddb",
					"f1857c686962cf6c811e9ea99199257d", "f1a072d80f38cb4bf85f78f896795c70",
					"f1bc13a41bb5db8277dc325bcd9cb530", "f1ec4ba8a7e2b9ba07457b62b3655e5f",
					"f274ad66b1c8e7f6149050f6fbbb26a3", "f2920c4752ece27aecdc8b43ef81c027",
					"f2c6d21a1d85c3f144ff894f3387f87f", "f30e8f7d4caeedc2272305e992c5b0de",
					"f32b5ff8197db700235662c5f86539e2", "f33a36029454feaab61dad2640797038",
					"f3a11195723b144472e77f0191104a95", "f3c75e1c5c3cc3666241036c2e94625d",
					"f3dc48158442c0848151dda4985c4f88", "f43b9a1962236a673ddfbaf27dbb3c4f",
					"f4c6f03d94a1da6983909e9cc3ddbe75", "f4cbc3f8ab60facf014d72c7050cb9e1",
					"f4d91e475e10ede4ddeb17f3e02147a3", "f616f842f63e780010f758fe3ea95fd8",
					"f761c433e70e7362b2476fc341c7cebf", "f782394e5728ad9fb7e05298f0f1acd5",
					"f86f374346d161da3b6cb4c60ba1a04d", "f92611d7115899db7aa9cb9ea5ca76c0",
					"f95ad45a4bba609d9101e1b7407806ee", "fa89bdbc59fab1cac8cd5ea8dc25f077",
					"faafcb3c6c7b69c726401a4edc37ed99", "fb17efa232667ad1b404117018ff7d80",
					"fb428c521cdad6dfe64416982fd45b1b", "fc08bffd62e91679886d5511169ed2d9",
					"fc94ef573ef59303351e89cd5a1b675a", "fcf6e30dd988aeb02413a7ce553455c2",
					"fd0778da6ddb24b6a79862d95abd171e", "fd5531ed785429e2b28a55a09f23bc22",
					"fd918bd224f30f98635511f14cf33154", "fe68d69714981d7e367d14f6de85d4f9",
					"fe7295b7a26198761454e5f1a16d3fcc", "fecf45bd1c97ce795537f8f232486632",
					"ff74b5f65d5e3e80b442f2a7a3015faa", "ffd75d12cbcf82bac61d2944ec006d77",
					"00a0a726393049026eec281ae294486a", "01134dcf1f16678b20b9f472b42b35dc",
					"012f9970c015f16f3be39e7499b61649", "03a6946fe865134b15062d2e857d7e03",
					"047d5d52adc2afb0d62c52e17b8e9fec", "0572f74df287c4ca43d5b84182d1d3ad",
					"05ea3818abbfa8654a1c26f5ac3d617f", "063e27f4847f8859ce0f4fd4ec276397",
					"0656ffd50f8ff5d5da65752052fde653", "06a9003839a485f71a5c18fa9420de02",
					"06b4e22040e8967251b6fd4f48a5846a", "075a23c1de32a4aef919e485e894916b",
					"08c624f6bbd9a4c7c2a68ac70c065b81", "08caa6dc0705b0cfabdb39afda89cb54",
					"0bd0c51e9b3d1023d8fc710615b005b9", "0bd1d8d4439eebe9c5833d8d92c64c1e",
					"0e6d0dbda25e0503dc3a03bc020b146d", "0edfa6f1c2deca7a12000320e4501490",
					"0f7ac8f05362d06e31de41959b83a6b8", "0fc16957f464f89d3d128d454de382a9",
					"10d392d5088705f845add72f06996403", "11663ac187cb1879de1e7d2578ddfbaa",
					"135a73691db71fc89ae3fc68448183de", "1362299f97add34b394d508b1616384d",
					"13d6634007ce765e4f5442c4a882d3c2", "14950a926b3d395c3347eacbed6b850c",
					"15d560017e62a8fd1a77fb5c166e4cd5", "17228b63e1974e39b111641e9e5aad17",
					"1732cfd8a64da51cddec3aa1becd61d0", "1981bca5b0feb4f0eb092e90799f1dbb",
					"1cbe24eed6d79f6fb3be8f2a852fb817", "1cce50bac1c674a0a82be83cfbbc359b",
					"1cd7a27a2d1d1f2b8db62823336ec4f7", "1d7a0a53a666283c296f50f47902f164",
					"1e68d442b4872b14472db139e39bec35", "1ffd01efeff1e2d47b13b638e0ea4f90",
					"20e33fc5b40909da2604c0b9b06c713d", "21fc7017847de6a43bd99ddb3d2cb7b9",
					"22329b3844ee49dd275390db6532ccc4", "228195d4f86ffd42ecea42ff2153ffc9",
					"23d0b0354a8b48b5749d3d660dd5215e", "23ed1a6e7953bb04c60a1880a1dbdd13",
					"241fedb12897ca6e4f1ccf2cacdf1e9a", "2433f89b50c4a15ef6efd4eed9cc58ae",
					"24f6b09a2d73beb548ab055c82b86d47", "2540bc026d5033db2db7c20d2214fc63",
					"282c526d0c179b906fa98a42a68e2e15", "28eb8a82861eecfea84d3ed3b9d483a6",
					"29965d5548621660cb44f19b63676c69", "29c6666d24a5f694f6df6011f64283f1",
					"2b947ccf6ec0a4ab3ae6f52a01bd65fd", "2bb9bfcf41fa11ef0373f7bc70b813fd",
					"2c57b1b0f7c07fd3778b54bbc2efd9ad", "2cf7504c2d9800ca32626418744df269",
					"2d14aa19a7cedf5d1ffa66b42ce9cf44", "2e68ca3239343b96dd066e8d10a91c94",
					"30187bd47d692869ef0a8a9665733712", "30af9b83700a011e119db265ea6672a8",
					"31e483c23da2558862ad0637c48ffb6c", "3235ade67e2b630214ac793138b61802",
					"32b6c37cdd254ea71bfd3b24d1947c4a", "3482b0d95eddaa46a56dabda366deee6",
					"36069ca06f357f54e1d96f41e2adb4bf", "36131f81c30f1343f20bd9c855149e28",
					"380fb64f57654c57df7a060e5e9c19be", "3a17c5bdce9b0f10697c5c44cd15a812",
					"3bb4413b7919d4df17031aca459d9ade", "3d056fc1e5ec75905075e6e5f8764e17",
					"3e6e15f5dc7eb669fc04726134b2d509", "3eab87676a0346dadfbaea469f802209",
					"3ec56e433ed713ba82db6d264b5d5270", "42001b8b44ef31dba0f3feb4279cddf2",
					"4272f6abdfd5519427a4fae5f247bcf5", "429394ef0891e168fd0ed8403c6fc871",
					"42eee9cb8006ab4ff825c5e8ae741ba3", "43f3bf653f2729c54548d8036bcf7892",
					"43f83afdd9e09abf9c9f304f933ec377", "444a433dd5a2c35c62510a2f0919fbd4",
					"4540da3ae74785f7d3cf03a7b4ce4557", "45b55f02e8b1d5e38d99ee7577d5397c",
					"45ff692d8b6a5ba0d14a8a0449710436", "4674eefa1c1e6552514e2e70fe2ad65d",
					"479f462885c46d82cfba5c2231942f02", "4863865d85a53f72b7468920f0d10720",
					"499d22d50aaca264ad87dbf24c514005", "49f37ed31ee10326896ff3ae7213d46d",
					"4ac0b4f9204f83d7586ea6d14bc22bef", "4bbcf60c42724e8ec77df1873b775662",
					"4cd897bc34d9c15e45c2d4b0a345cbc2", "4d967037ab368aae48a4db43df0ab758",
					"4e0fc2d36be7541faad70b97ae450cbc", "4f20319bf81d0312d2011de1188e9145",
					"50efdffce30c9f99d31886013b857e21", "514829263a1a7985c70b249981e4c0a3",
					"5331b38da73f0ce46c4358cc642f087d", "53b670206689dda43ccea56fee9fb454",
					"55738af24222464df64d8824557e6f04", "56582b0e2ce28bc5ea61ef8156984e72",
					"57002e941649f3bd88ba5c920b32636a", "57aa6047577f62b2c21e939c69a458c0",
					"583cb1845f5afab9d7fe0e189c766b12", "592bd6da6b4114ffbf328efc763b75d9",
					"5b9a8547129fbb022c37eee31cac985f", "5bcf73f9a8696d3d1bb57e1a11c6310f",
					"5c0673ea40f8940f05be176f70929f75", "5c9fef2ebd8dd951a5d93d9c1b9cef04",
					"5cba5525b892bc71cc49536e949f27fc", "5dc4eccc8c6ce8075288906efc7f1d55",
					"5e29f82205d806da273ea3feedf4c5ec", "5e5335a045690b6f51ed41b86fd903a6",
					"61c4d82aaed05636cb4209c25bed00be", "6288c84413d7c77e31e2839be3efc361",
					"63d53a2a01a80ea4c390a814bca093a4", "644effff2548d39b162aa636ed6b69dd",
					"65bbe7328f1fd7cadd1ebb0f687c3170", "665848966f762d9ecf866920bc2835b2",
					"6763cad26ab60ee2338843c4b46bb441", "67d18073778ed9d1b1ae8357d4c8b0d2",
					"67fba1fec53fa8e6f7d9f431a66c0ef1", "6802e3e01d7c8f668fadc2ff5bfb75d3",
					"688491743bc8434b3a707e6128ee6c48", "69ed54e996a15ebfe73a33f89b9da528",
					"6b90603027e158227ee95f3daf6b8ff0", "7068f6aa106baebb53da361dae3d8e05",
					"720ad5bd523ece632964254905eb661e", "7278944e2767b15201951c8e910cadc1",
					"7528261f43d904ba5bb3f046add2105e", "753718b7792f9fcd36a73f6deeedb965",
					"7559b937d45cb6891095676c3589321d", "75951606b1c8036c49fb0bdf98cf39a0",
					"765efc9b9b254ca663cd86ca3cfa2caa", "781372958542d4aa4350c93795e63522",
					"784b7b4f805b19c3ef13ba6f3019a9e2", "789531fb5ba6c7e8d53068448ba25e40",
					"7922bb35b97667a8b2db38ddf7c00062", "79889a8a878c03a58a3d31c9ad995327",
					"7a557b7040c6539af3fa31a9dd04483e", "7a5a040862600dd29b6b622cb813d2d5",
					"7a5a8c0248046a41a2f23881693f5bb9", "7ce88688b10a1c01f73ac59f52148d67",
					"7d53b4c3f8bc20ad22d1896b5062db60", "7d9c864ac335d22942aef0caa57cef79",
					"7e10c23ba610ff40176bf8060cffff27", "7e7d4bd5b74eaed25991e5fc319155de",
					"7edbe1168190d95b12f54de747f52c69", "7f512be2bfaba3c7d841418df5988077",
					"7f7d27c507cb89429d9ce40e073eb94a", "804068b7e51eefc8b8d448862e93eb5e",
					"81dd07e79791de38f1ea37b44688526a", "81fa6691ed48b75132108834d5efd61d",
					"821a36f72fed26bcd066d87c4fb36842", "8579063a1f00f962384a2044d64a8328",
					"859a42816d2e6214ce371cbc6ca756eb", "85a558d7da9de80ca4a77c962274267d",
					"8627e1a4df59f631c313d1096bf957d4", "89eab558c678fd93bf2108aa69de60fc",
					"8abe11b234525fb6e3463b3aad0130d7", "8b17d37828e2168055977e43416a3841",
					"8c3ee59de71df2734a10f93c585e85c0", "8e2cde4d4dd3106ec242b4a96d00863d",
					"8e2eb3030eb88f8ddd68d9b37915bd29", "8fbff29cab517dc0f5bb958f7e33e1b8",
					"8fd9c577d3a96a6c28336ca9f142dc83", "928d3a27eb1f1ed7e076be549e3d439b",
					"92d7512b4bfdb750ab5875fa1e63583c", "93e7583831a48800a8e0027b3d6a25b5",
					"93fe53f99fcb87c0a6c7e645e6157700", "94778d03e1e16de2a4abcee29a01d7f2",
					"962f8a4f50e2879bb7f60243265a6f6e", "9724a53656633e356c9565e4f5925b34",
					"97e2911ad45b102100421493bbca837f", "989e3d3b6b0cfc8c6a91e14b8524b858",
					"9957a7c2f51974fb97eb1225f570f9f4", "9a024a0be6bd0f158cbc067c1b24383d",
					"9c2891f5281fd49908203cb5902d03e3", "9c59c7cfb8d2fc32947fa3b151d76842",
					"9cd5839dd4399aac9a9b5194535093e8", "9dab5cfdfde407fac9abaf4111d2d4a3",
					"9f66a0130bcff03dac777396fe2b974f", "a0dd4a5d3ef19243fcb82aac21fc9885",
					"a13d80fd4f10d24d8626f9818110de98", "a19113000d4f1bf6b3f3594f508ffced",
					"a1ecb3c98d71fc13cf5931cf9b197b9b", "a32dc8e5e2da3566f2a0bd551cd16282",
					"a42fc813fbda115b7f14f29c76cc2ce4", "a4374b0302622c8b1aebea26517f955f",
					"a497e570d882fd8ab9707a0f15e810e9", "a4c90334e7304c51938be7fbb7412dd1",
					"a54d6739bbf73e78dd41b512adddb0ab", "a59e49e2c0858be536b329c68a3f0d81",
					"a5b918495048c5c1b84cfd60a46524d8", "aacdd8f9ca77f88f3d47008ec7b93b17",
					"abbaf396bf22aaa46342b015bc0d9576", "abe5e49e0c3b15242a2128a92560979f",
					"acc46ecd5aed92e37acbbbf6e8351ed6", "ad3290815880ab5c9705b2f061386549",
					"ae44a5b87f9039d8c140d09f532ef508", "ae51248dfa6da013c3ff90a947576719",
					"aecd40518674ce2242075019b51e002d", "af6426a1356a4a6b9512717e004f856f",
					"af9d088dc5e347bd5b9e12f008010532", "afb71966474a4dcb1530b7cbf288e957",
					"afd79f516da95d771c8472ed445e3882", "b049ccdcd9ecaaf6bfc019a679a0196e",
					"b04dca94cf5995fb43620454e38b65aa", "b1c47281da98f661e1e2808d9baf1e29",
					"b1dbf8c79d6b0bdf684c701d20ab1b86", "b410ba05aace97ede575a42aabf7ba37",
					"b4b4aa951453c7d8bb271e6814e8ac86", "b4e97869969afe6ed36d8859f3397127",
					"b6ebb365067c125f675f01867e268d73", "b7e5d31b65ba87a6ee19b3866966e642",
					"b7f24795c211dbc05380a0d47c3ae06c", "b814363d1037cb921549010fb59715da",
					"b8e4698bee4c0c808dc75a5e7a841351", "b931004357278658fe8bee3b90203ad4",
					"b9925ab53741ab7616899bf9163bdd8b", "b9becaf4cf2cee856ef1fae8e33ce9b1",
					"bac1f421b39c79863f217424688d2bfc", "bb5d75642198ad04c10a8d4cac8d76f9",
					"bb6797736fc1aef880ecb58e290dce93", "be39a3b751550fd34a082b2c9fa9b652",
					"bf0c365fd03c2e2d6e89af9c2708e427", "bf4ac3c1491df9683cdf30a7b4a509ed",
					"bffaf9a846313049ff1b5ee94def5fec", "c0b03ee27999f136c2d8d2cae3c7d30e",
					"c0ded0a33894bbf72a41c5e6b59a6e4f", "c11a44fead39829b67698af40ea5195d",
					"c1898d9f4505a28f926c51d11cb9a47a", "c2a96c425269b073b90dfda86d844b28",
					"c394f8eac91118e46a3356b85ef345ad", "c4b447fa2663ccd7bc811e62407372ff",
					"c56f0a86ef403b69fe05e79a8d33db5d", "c5ca1e51abc980aae1f6e7ca877f1531",
					"c775584938dfbcdaea5f78bc0b6f856f", "c78171b89db73c293e906f377bb36ec1",
					"c7dbe09f9f368a1abd343c076a981359", "c88da115f3da14d878c704b24c629c7e",
					"c92fec3dd22a604356caf5f23c542f09", "c991275ea1e06adce78237bf8e3019f6",
					"c9c911f6b4864e98ad1cc2283cb19b80", "cb91859ab8ecde949768c48bc97c0df4",
					"ccf43853eb562682ef9e347e70675c59", "cdb4c1644d3266ae98da6cb9957de389",
					"cdeef829f83de62ca02b7efe043561d4", "cf11f64be793f163b7bdac7290313f7f",
					"d00ca8c0aa1d77558b9f403024ebd260", "d0a50d0a8295496873a6e54763f41354",
					"d25788fbb9733e87a888dfea2ab16f98", "d29e38e6e23e3ebdd9fcba1d2ad77d64",
					"d3e556ba5eeb35e36394346410d7e452", "d6ebd5b48c850b174247c49f20ac02af",
					"d807188c10163d6f9720f71ab2b310b2", "d8edb9ea574cc0ee388ad0026bcc1a14",
					"d9341f31d1563ffb887638b375f70869", "dae9a0f0229f867dc12bf2058921ff5f",
					"dba0809a5b544caece8a6d60f2682a86", "dc07e86342c2f8f0f89bcd7d693275a7",
					"dc1f0803d47ee0187e2a0e697354b1f8", "dcfba427e6ac3ab7d61619feb20ee2f0",
					"ddedcd857eddefa4520df29736208036", "df411349d829bcf7ec341043ca749c68",
					"e261d5f12fe8f4ad2292843a425a4c83", "e27e206de3ee60a33f556aadbeff55ed",
					"e2a07109f760d07397d397a65d5a4ea1", "e322369769d7420e192d97c8fd57552e",
					"e3bd7fb9ed358eb2c87f655f09640935", "e6ec6e9143fd00e6aa89e93fbf412e0f",
					"e73619b44f5ada0e67d6ba074811cbd0", "e785c1286d1d606d1707fc42771f1187",
					"e8756c4b5d8e0ff3c16ae9a3d53099d3", "e8cb15785d293c869a6e4bf488e0b774",
					"e9175044740a0c014ded6d7812a7a67c", "e9be090166da0d6b411df074067b43bd",
					"eca164ed70e863fe379af6f3012093ca", "ece85d5cb49ca968b76668c8961c9c4f",
					"ed22cb3d261e7bfa0701b277754b0870", "ed3b9e7c1e957b3a853636a8d611969c",
					"eddbb9ebdf4a646015cc9136eabbc04d", "ee2c15b74fb1ded83aa440f8cf6d8893",
					"eef6753dcb8deb394c8a2b0a9ec7e37f", "eef7aea060afeef34f40e2663daca7ec",
					"eef8531db2f7d26f72ef72031805ad52", "ef70dad082773a4c9be422219b83824d",
					"efe617259f72c9fefda6b415ce8fd7e0", "f663d968c93314c0d3ab245d31f4eda5",
					"f77c4294f64f290253877a60405af145", "f83b7b73d5eb0ebca0f3214873bceff5",
					"f947d9bdff600165419bfb75d0796ed8", "fa9dfae53239d969e763d11df60babc5",
					"faf701e8abf9732458dc845879cd48aa", "fb1e979f1de70ed594f9946c46dd30de",
					"fcce509335d2849d802e0c1b79b4f514", "fd494d70d465bf2fe4fab60a9fe4e631",
					"fe6b53ca7f37de2c769f36e32fac7661", "ffa4c4dd25524a28f68eeeb27b4f8bf1",
					"050fc15764fb7dcc8fd97331084403a7", "0750462fa5eaa3589552d4d2a3cea3bb",
					"1651df67e65081dd751245d123ebf5ea", "1872853ef376e9a4eeeb8627595ad174",
					"21dace98d908967de3c7057d60fe113f", "2919bcfdcdc3521be329019190f678e3",
					"3aa4de0c84745649fc51b8a35c231ade", "3aa520ca97afb6154761cd2e12cbb32a",
					"3da474f14591762ec09cd31860a34f58", "4125138b003ff67f00d4b1f7cdad4804",
					"41669256634b71bc604203c7dbb45467", "554c119574cb3a0543cb3a83847e4eaa",
					"5824d44439acb0278531b14093764f0a", "5abda8c300ec6dd8adad4458058a49fc",
					"6176e616c11f76aa216d8e9e390f7066", "66bec9eb17d6e8089cd76b7203b10492",
					"712ebc95fd87ac83b930e3f35f645e75", "762953885aaef8a30a273f1582ef8d06",
					"7af02dfdb7ae374be7d03e2b035e3bd1", "8253a933d676aa0dd906b513b70bb83a",
					"8b8578ed3dda1c0a5d3250705f4a9a13", "8d28ca0c42ad6290cf2282f6a8e9ce28",
					"907e8cdd4f1970ba949c5b3f9598b87e", "9562e33368a1844a478c3a22303fc56a",
					"957fe126a5a26a518227e7a7f8b88fb7", "a517c270b15d1787a3a879f340af0617",
					"aa9ac14ef8a0d2c09582fa87c6c667b5", "abf30d8b6e498b54a0056d1969a7100b",
					"b8161e7f680c40bd975e4422e207f92a", "be486f674f30f315ce9df4b08970e9af",
					"c2ba254a892e40355e22abdaa26fdd82", "e9a22a830052b1070865d10d3902a7e5",
					"0447fb4fd9d9d6fdace223177f484f26", "0d5f1771e08dfbce81816b855da2e18d",
					"10a617b02a89d81c44948efc1f982cf2", "16587d1dba9b20b8e6ee2968111ff33e",
					"1d3aa44308bb03a1352d160c822aa963", "1e70f22bc295c523841a1f3ef553bd34",
					"1f38fb8ccae25f38694680abb762d7c9", "26836c0aa2e009ebe1bfe5456729dc8e",
					"3431f88c0193168653571d70702616b2", "3dccb7eb1e7d54f93538fd1468b9e8ad",
					"3e4a7aa3f7354f7461418a667b6a4141", "42c3fb056bfd6c35355b06fc90ae306e",
					"4dbe7b8e67bbfce7b5ab5eab2d6f5ac5", "58eeca51b9e26f0c7c271ec5ede97278",
					"6683086e22b8a22c41a600b918b52b75", "67f19406fae0ee8f56d4f3e36ad839a1",
					"68654a6103cb1fe6b566d60de9109138", "6d884037ad3326dc1dd04a6c0ff76ed8",
					"9330341286eb2d1fa05a55c73fff6725", "936dcac0a4570fdd19f89fa9070f52ca",
					"98172696a82900c7b6879664d4406dc4", "9f81daf89466093abe2c6987b8a06a43",
					"9fb90a0c2c25e03b9da43a80a47f718d", "a1f4741f1c561b477d5d3b657de8576c",
					"a20bf4fc2636e4a7e5cb365edd4e3774", "aaa40b41c4041b7439fa7917216439c0",
					"aaf67f68557e8ae83cf8626e20022313", "ae1f6212bcebc33c226ce9591f50c3ed",
					"b591720c3d84f45b7e7b01fb5e9a199d", "b5a0f866aca6467a8f42e7005fc01821",
					"bd1f24b768ac97f26bb0c37f7f7c81b1", "bd50f853b32b24b36d9d03bdda2bd64b",
					"c109e5e1982380c308da9719765b5c32", "c7edb6b4433039e983ee17f9915d3bf6",
					"d3348930ff7691dda1b5b08304fc3a94", "e325f616bbf5434c52037f198d1320cf",
					"f4225b2b373835f77e803be3931009b5", "fa8ad855184a4209b8b681ecfe467cc8" };
			for (String site_code : SITECODES) {
				transactionUpdateTrgtField(site_code);
			}
			
			String [] STATS_DTTMSS= new String [] {"20210106","20210107","20210108","20210111","20210112","20210113","20210114","20210118","20210119","20210120","20210121","20210122","20210123","20210124","20210125","20210126","20210127","20210319","20210322","20210323","20210324"};
			for (String STATS_DTTM: STATS_DTTMSS) {
				transactionUpdateTrgtField2(STATS_DTTM);
			}
		} catch(Exception e) {
			logger.error("err ", e);
		} finally {
			System.exit(-1);
		}
	}
	public boolean transactionUpdateTrgtField(String site_code) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					Map param= new HashMap();
					param.put("SITE_CODE", site_code);
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtCampMediaStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					status.setRollbackOnly();
					res = false;
					logger.error("transactionUpdateTrgtField Exception ==>", e);

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("transactionUpdateTrgtField 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	public boolean transactionUpdateTrgtField2(String STATS_DTTM) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					Map param= new HashMap();
					param.put("STATS_DTTM", STATS_DTTM);
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtComStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtMediaScriptStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtMediaStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtAdverStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateTrgtCampStats"), param);
					sqlSessionTemplateBilling.flushStatements();
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					status.setRollbackOnly();
					res = false;
					logger.error("transactionUpdateTrgtField2 Exception ==>", e);

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("transactionUpdateTrgtField2 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	
	public void frmeDeleteData() {
		try {
			String flagFileName= "FRME_20210520_20210521";
			
			String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
			String STATS_DTTM = "";
			try{
				JSONObject json = JSONObject.fromObject(JSON_STR.toString());
				STATS_DTTM = json.get("STATS_DTTM").toString();
			}catch(Exception e) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			if(STATS_DTTM==null) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
			
			Calendar cal= Calendar.getInstance();
			try {
				cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
			} catch (ParseException e1) {
			}
			
			boolean ret= true;			
			if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
				logger.info("KILL ME");
				System.exit(0);
				
			} else {
				try {
					Map param = new HashMap();
					param.put("STATS_DTTM", STATS_DTTM);
					ret = transectioncnvrsfrmeDelete(param);
					
					logger.info("SUCC param:{} ", param);
					
					if(ret) {
						cal.add(Calendar.DATE, 1);
					}
					
				}catch(Exception e) {
					ret= false;
				}
			}
			
			JSONObject jsonOut= new JSONObject();
			jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
			
			logger.info("jsonOut:{}", jsonOut);
			
			String filePath = String.format("%s", logPath +"../");
			File file = new File( filePath + flagFileName );
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			} catch (IOException e) {
			}
		    out.println(jsonOut);
		    out.close();
		    
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	public boolean transectioncnvrsfrmeDelete(Map param){
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("transectioncnvrsfrmeDelete START");
					
					List<Map> list= sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "SELECT_ADVERID_LIST"), param);
					for( Map adverid : list ) {
						param.put("ADVER_ID", adverid.get("ADVER_ID"));
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_ADVER_DAY"), param);
						sqlSessionTemplateBilling.flushStatements();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_COMBI_DAY"), param);
						sqlSessionTemplateBilling.flushStatements();
						logger.info("STATS_DTTM : {}, ADVER_ID : {}", param.get("STATS_DTTM"), param.get("ADVER_ID"));
						Thread.sleep(100);
					}
					String [] ADVRTS_TP_CODE= new String[] {"01","04","05","06","10","11","16","17","18","19","26","28","31","35","36","37","40","41"};
					for( String Code : ADVRTS_TP_CODE ) {
						param.put("ADVRTS_TP_CODE", Code);
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_DAY"), param);
						sqlSessionTemplateBilling.flushStatements();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_IMG_DAY"), param);
						sqlSessionTemplateBilling.flushStatements();
						logger.info("Code {}", Code);
						Thread.sleep(500);
					}
					String [] FRME_CODE= new String[] {"WN01E28C01W021","WN01E28C01W022","WN03E07C01W021","WN03E07C01W022","WN03E07C01W023","WN03E07C01W024","WN03E07C01W025","WN03E07C01W026","WN03E07C01W027","WN03E07C01W028","WN03E07C01W029","WN03E08C01W021","WN03E23C01W021","WN03E23C01W022","WN03E24C01W021","WN03E24C01W022","WN04E07C01W021","WY02E24C01W021","WY03E24C01W021","WY03E28C01W024","WY04E05C01W021","WY04E05C02W021","WY04E05C03W021","WY04E06C01W021","WY04E06C02W021","WY04E07C01W021","WY04E07C02W021","WY04E28C01W022","WY04E28C01W023","WY05E01C01W021","WY05E06C01W021","WY05E12C01W021","WY05E28C01W021","WY06E02C01W021","WY06E07C01W021","WY06E28C01W021","WY06E28C01W022","WY09E07C01W021","WY09E07C01W022","WY09E07C01W023","WY09E07C02W021","WY10E07C01W022","WY10E07C01W023","WY10E07C01W024","WY10E07C01W025","WY10E23C01W021","WY10E28C01W021","WN01E24C01W061","WN01E28C01W061","WN03E07C01W061","WN03E07C01W062","WN03E07C01W063","WN03E07C01W064","WN03E07C01W065","WN03E07C01W066","WN03E07C01W067","WN03E07C01W068","WN03E07C01W069","WN03E12C01W061","WN03E24C01W063","WY02E25C01W061","WY02E28C01W061","WY02E28C02W061","WY02E28C02W062","WY02E28C02W063","WY03E24C01W061","WY03E24C01W0610","WY03E24C01W0611","WY03E24C01W0612","WY03E24C01W0613","WY03E24C01W062","WY03E24C01W063","WY03E24C01W064","WY03E24C01W065","WY03E24C01W066","WY03E24C01W067","WY03E24C01W068","WY03E24C01W069","WY03E25C01W061","WY04E03C01W061","WY04E06C01W061","WY04E06C02W061","WY04E06C03W061","WY04E07C01W061","WY04E12C01W061","WY04E24C01W061","WY04E28C01W061","WY04E28C01W062","WY04E28C01W063","WY05E01C01W061","WY05E04C01W061","WY05E25C01W061","WY05E28C01W061","WY05E28C01W062","WY06E01C01W061","WY06E06C01W061","WY06E07C01W062","WY06E07C01W063","WY06E23C01W061","WY08E07C01W061","WY08E28C01W061","WY09E07C01W061","WY09E25C01W061","WY09E25C01W062","WY09E25C01W063","WY09E25C01W064","WY11E25C01W061","WY12E07C01W063","WY24E25C01W061","WN01E03C01W121","WN01E07C01W121","WN01E09C01W121","WN01E28C01W121","WN01E28C01W1210","WN01E28C01W1212","WN01E28C01W1214","WN01E28C01W1215","WN01E28C01W1216","WN01E28C01W1217","WN01E28C01W123","WN01E28C01W124","WN01E28C01W125","WN01E28C01W126","WN01E28C01W127","WN01E28C01W129","WN03E05C01W121","WN03E07C01W121","WN03E07C01W122","WN03E07C01W123","WN03E07C01W124","WN03E07C01W125","WN03E07C01W126","WN03E07C01W127","WN03E07C01W128","WN03E07C01W129","WN03E08C01W121","WN03E23C01W121","WN03E23C01W122","WN05E07C01W121","WY01E03C01W121","WY01E06C01W121","WY01E24C01W121","WY01E24C01W123","WY01E24C01W124","WY01E24C01W125","WY01E24C01W126","WY01E24C02W121","WY01E28C01W1210","WY01E28C01W1211","WY01E28C01W1212","WY01E28C01W1213","WY01E28C01W1215","WY01E28C01W1216","WY01E28C01W1218","WY01E28C01W1219","WY01E28C01W122","WY01E28C01W1220","WY01E28C01W1221","WY01E28C01W123","WY01E28C01W124","WY01E28C01W127","WY01E28C01W128","WY01E28C01W129","WY02E03C01W121","WY02E24C01W121","WY02E25C01W121","WY02E28C01W121","WY02E28C01W122","WY02E28C01W123","WY02E28C01W124","WY03E07C01W121","WY03E07C01W122","WY03E07C01W123","WY03E07C01W124","WY03E07C01W125","WY03E07C01W126","WY03E07C01W127","WY03E09C01W121","WY03E12C01W121","WY04E01C01W121","WY04E03C01W121","WY04E07C01W121","WY04E07C01W123","WY04E07C01W124","WY04E07C01W125","WY04E07C01W126","WY04E07C01W127","WY04E09C01W121","WY04E09C01W122","WY04E12C01W121","WY04E24C01W121","WY04E24C01W122","WY04E25C01W121","WY04E28C01W121","WY04E28C01W122","WY04E28C01W123","WY04E28C01W125","WY05E28C01W121","WY05E28C01W122","WY06E02C01W121","WY06E07C01W121","WY06E07C01W1210","WY06E07C01W1211","WY06E07C01W1212","WY06E07C01W1213","WY06E07C01W122","WY06E07C01W123","WY06E07C01W124","WY06E07C01W125","WY06E07C01W126","WY06E07C01W127","WY06E07C01W128","WY06E07C01W129","WY06E09C01W121","WY06E12C01W121","WY06E24C01W121","WY06E28C01W121","WY06E28C01W122","WY08E07C01W121","WY08E25C01W121","WY09E07C01W121","WY09E25C01W121","WY09E25C01W122","WY10E07C01W121","WY10E07C01W122","WY10E07C01W123","WY12E07C01W121","WY12E07C01W122","WY12E25C01W121","WY12E25C01W122","WY16E07C01W122","WY16E07C01W123","WY18E07C01W121"};
					for( String Code : FRME_CODE ) {
						param.put("FRME_CODE", Code);
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_ADVER_W02"), param);
						sqlSessionTemplateBilling.flushStatements();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_ADVER_W06"), param);
						sqlSessionTemplateBilling.flushStatements();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "DELETE_FRME_ADVER_W12"), param);
						sqlSessionTemplateBilling.flushStatements();
						logger.info("Code {}", Code);
						Thread.sleep(500);
					}
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					status.setRollbackOnly();
					res = false;
					logger.error("transectioncnvrsfrmeDelete Exception ==>", e);

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("transectioncnvrsfrmeDelete 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	public void cnvrsRenewMigration() {
		try {
			String flagFileName= "RENEWNCL_20210323_20210325";
			
			String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
			String STATS_DTTM = "";
			try{
				JSONObject json = JSONObject.fromObject(JSON_STR.toString());
				STATS_DTTM = json.get("STATS_DTTM").toString();
			}catch(Exception e) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			if(STATS_DTTM==null) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
			
			Calendar cal= Calendar.getInstance();
			try {
				cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
			} catch (ParseException e1) {
			}
			
			boolean ret= true;			
			if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
				logger.info("KILL ME");
				System.exit(0);
				
			} else {
				try {
					Map param = new HashMap();
					param.put("STATS_DTTM", STATS_DTTM);
					ret = transectioncnvrsRenewMigration(param);
					
					logger.info("SUCC param:{} ", param);
					
					if(ret) {
						cal.add(Calendar.DATE, 1);
					}
					
				}catch(Exception e) {
					ret= false;
				}
			}
			
			JSONObject jsonOut= new JSONObject();
			jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
			
			logger.info("jsonOut:{}", jsonOut);
			
			String filePath = String.format("%s", logPath +"../");
			File file = new File( filePath + flagFileName );
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			} catch (IOException e) {
			}
		    out.println(jsonOut);
		    out.close();
		    
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	public boolean transectioncnvrsRenewMigration(Map param){
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_RENEW_NCL_MIGRATION START");
					
					// 04','10','16','17','34','37','40','41
					String [] ADVRTS_TP_CODE= new String[] {"41"};
					
					for( String tpCode : ADVRTS_TP_CODE ) {
						param.put("ADVRTS_TP_CODE", tpCode);
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_NCL"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_HH_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_RENEW_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_ADVER_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_MEDIA_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "INSERT_RENEW_MIGRATION_COM_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();
						
						logger.info("tpCode {}", tpCode);
					}
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_RENEW_NCL_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_RENEW_NCL_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	
	public void pcodeMigration() {
		String flagFileName= "FIXPSTATS_20200722_20200921";
		
		String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName); //logPath +"../" "FIXCONV"
		String STATS_DTTM = "";
		try{
			JSONObject json = JSONObject.fromObject(JSON_STR.toString());
			STATS_DTTM = json.get("STATS_DTTM").toString();
		}catch(Exception e) {
			STATS_DTTM= flagFileName.split("_")[1];
		}
		if(STATS_DTTM==null) {
			STATS_DTTM= flagFileName.split("_")[1];
		}

		if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
			logger.info("STATS_DTTM {} BREAKED", STATS_DTTM);
			return;
		} else {
			logger.info("STATS_DTTM {}", STATS_DTTM);

			boolean ret= transectionRuning( Integer.parseInt(STATS_DTTM) );
			
			if(ret) {
				Calendar cal= Calendar.getInstance();
				try {
					cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
				} catch (ParseException e1) {
				}
				cal.add(Calendar.DATE, 1);
				
				JSONObject jsonOut= new JSONObject();
				jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
				
				logger.info("jsonOut {}", jsonOut);
				
				String filePath = String.format("%s", logPath +"../");
				File file = new File( filePath + flagFileName );
				PrintWriter out = null;
				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
				} catch (IOException e) {
				}
			    out.println(jsonOut);
			    out.close();
			}
		}
		
	}
	
	public boolean transectionRuning(int STATS_DTTM){
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		Map param = new HashMap();
		param.put("STATS_DTTM", STATS_DTTM);

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("REVISION  START");

					List<String> CODES= Arrays.asList("04","10","17");
					for(String ADVRTS_TP_CODE: CODES) {
						param.put("ADVRTS_TP_CODE", ADVRTS_TP_CODE);
						
						startTime = System.currentTimeMillis();
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updatePCODE_RECOM_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();
						resutTime = System.currentTimeMillis() - startTime;
						logger.info("REVISION {} (TBRT)  : {}, {}, {}", "updatePCODE_RECOM_STATS", param, resutTime + "(ms)");
					}
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("REVISION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("REVISION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public void convNcl() {
		String JSON_STR = FileUtils.readJsonFile(logPath +"../", "FIXCONV"); //logPath +"../" "FIXCONV"
		String STATS_DTTM = "";
		try{
			JSONObject json = JSONObject.fromObject(JSON_STR.toString());
			STATS_DTTM = json.get("STATS_DTTM").toString();
		}catch(Exception e) {
			STATS_DTTM="20200409";
		}
		if(STATS_DTTM==null) {
			STATS_DTTM="20200409";
		}
		logger.info("STATS_DTTM {}", STATS_DTTM);

		
		transectionRevisionRuning( Integer.parseInt(STATS_DTTM) );
		
		
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
		} catch (ParseException e1) {
		}
		cal.add(Calendar.DATE, 1);
		
		JSONObject jsonOut= new JSONObject();
		jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
		
		logger.info("jsonOut {}", jsonOut);
		
		String filePath = String.format("%s", logPath +"../");
		File file = new File( filePath + "FIXCONV" );
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		} catch (IOException e) {
		}
	    out.println(jsonOut);
	    out.close();
	    
	}


	public boolean transectionRevisionRuning(int STATS_DTTM){
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		
		Map param = new HashMap();
		param.put("STATS_DTTM", STATS_DTTM);

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("REVISION  START");

					startTime = System.currentTimeMillis();
					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "updateMOB_CNVRS_KPI_STATS"), param);
					sqlSessionTemplateBilling.flushStatements();
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("REVISION {} (TBRT)  : {}, {}, {}", "updateMOB_CNVRS_KPI_STATS", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("REVISION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("REVISION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	public void mobKpiMediaStatsMigration() {
		try {
		String flagFileName= "MOBKPIMEDIASTATS_20200101_20200504";
		
		String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
		String STATS_DTTM = "";
		try{
			JSONObject json = JSONObject.fromObject(JSON_STR.toString());
			STATS_DTTM = json.get("STATS_DTTM").toString();
		}catch(Exception e) {
			STATS_DTTM= flagFileName.split("_")[1];
		}
		if(STATS_DTTM==null) {
			STATS_DTTM= flagFileName.split("_")[1];
		}
		logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
		
		Calendar cal= Calendar.getInstance();
		try {
			cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
		} catch (ParseException e1) {
		}
		
		boolean ret= false;			
		if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
			logger.info("KILL ME");
			
		} else {
			try {
				String [] ADVRTS_PRDT_CODE = new String [] {"01","02","03","05","07"};
				String [] ADVRTS_TP_CODE= new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41"};
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("STATS_DTTM", STATS_DTTM);
				for (String prdtCode : ADVRTS_PRDT_CODE) {	
					param.put("ADVRTS_PRDT_CODE", prdtCode);
					for( String tpCode : ADVRTS_TP_CODE ) {
						param.put("ADVRTS_TP_CODE", tpCode);
						ret = transectionMobKpiMediaStatsMigration(param);
						logger.info("SUCC param:{} ", param);
					}
				}
				
				
				if(ret) {
					cal.add(Calendar.DATE, 1);
				}
				
			}catch(Exception e) {
				ret= false;
			}
		}
		JSONObject jsonOut= new JSONObject();
		jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
		
		logger.info("jsonOut:{}", jsonOut);
		
		String filePath = String.format("%s", logPath +"../");
		File file = new File( filePath + flagFileName );
		PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
			} catch (IOException e) {
			}
		    out.println(jsonOut);
		    out.close();
		} catch (Exception e) {
			logger.error("err ", e);
		}
		
	}
	
	private boolean transectionMobKpiMediaStatsMigration(Map param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_KPI_MEDIA_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_KPI_MEDIA_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_KPI_MEDIA_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_KPI_MEDIA_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_KPI_MEDIA_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	public void transectionMobCtgrStatsMigration() {
//		try {
//			String flagFileName= "MOBCTGRSTATS_20210222_20210407";
//			
//			String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
//			String STATS_DTTM = "";
//			try{
//				JSONObject json = JSONObject.fromObject(JSON_STR.toString());
//				STATS_DTTM = json.get("STATS_DTTM").toString();
//			}catch(Exception e) {
//				STATS_DTTM= flagFileName.split("_")[1];
//			}
//			if(STATS_DTTM==null) {
//				STATS_DTTM= flagFileName.split("_")[1];
//			}
//			logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
//			
//			Calendar cal= Calendar.getInstance();
//			try {
//				cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
//			} catch (ParseException e1) {
//			}
//			
//			boolean ret= false;			
//			if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
//				logger.info("KILL ME");
//				
//			} else {
			boolean ret = false;
			String STATS_DTTM = "20210517";
				try {
//					String [] ADVRTS_PRDT_CODE = new String [] {"01","02","03","05","07"};
//					String [] ADVRTS_TP_CODE= new String[] {"01","04","05","06","08","10","11","13","14","16","17","18","19","22","24","26","27","28","29","30","31","32","34","35","36","37","39","40","41","99"};
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("STATS_DTTM", STATS_DTTM);
//					for (String prdtCode : ADVRTS_PRDT_CODE) {	
//						param.put("ADVRTS_PRDT_CODE", prdtCode);
//						for( String tpCode : ADVRTS_TP_CODE ) {
//							param.put("ADVRTS_TP_CODE", tpCode);
//						//	ret = transectionMobCtgrStatsMigration(param);
//						//	ret = transectionMobCnvrsRenewCtgrStatsMigration(param);
//							
//							logger.info("SUCC param:{} ", param);
//						}
//					}
					
					// 시간별데이터 마이그레이션 						
					int date = Integer.parseInt(STATS_DTTM);
					String[] STATS_HH = new String[] {"14","15","16","17","18","99"};
					for (String HH : STATS_HH) {
						param.put("STATS_HH", HH);
						if (date >= 20210219) {
							ret = transectionMobCtgrHHStatsMigration(param);
						}
						//ret = transectionMobCnvrsRenewCtgrHHStatsMigration(param);

						logger.info("SUCC HH param:{} ", param);
					}

					
//					if(ret) {
//						cal.add(Calendar.DATE, 1);
//					}
					
				}catch(Exception e) {
					ret= false;
				}
				
		//	}
//			JSONObject jsonOut= new JSONObject();
//			jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
//			
//			logger.info("jsonOut:{}", jsonOut);
//			
//			String filePath = String.format("%s", logPath +"../");
//			File file = new File( filePath + flagFileName );
//			PrintWriter out = null;
//				try {
//					out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
//				} catch (IOException e) {
//				}
//			    out.println(jsonOut);
//			    out.close();
//			} catch (Exception e) {
//				logger.error("err ", e);
//			}
		
	}
		
	private boolean transectionMobCnvrsRenewCtgrHHStatsMigration(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_HH_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CNVRS_RENEW_CTGR_HH_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_HH_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CNVRS_RENEW_CTGR_HH_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_HH_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	private boolean transectionMobCtgrHHStatsMigration(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CTGR_HH_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CTGR_HH_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CTGR_HH_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CTGR_HH_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CTGR_HH_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	private boolean transectionMobCnvrsRenewCtgrStatsMigration(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CNVRS_RENEW_CTGR_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CNVRS_RENEW_CTGR_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	private boolean transectionMobCtgrStatsMigration(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CTGR_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CTGR_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CTGR_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CTGR_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CTGR_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;
	}
	
	public void migrationMobMediaChrgStats() {
		try {
			String flagFileName= "MOBCHRGSTATS_20210401_20210420";
			
			String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
			String STATS_DTTM = "";
			try{
				JSONObject json = JSONObject.fromObject(JSON_STR.toString());
				STATS_DTTM = json.get("STATS_DTTM").toString();
			}catch(Exception e) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			if(STATS_DTTM==null) {
				STATS_DTTM= flagFileName.split("_")[1];
			}
			logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
			
			Calendar cal= Calendar.getInstance();
			try {
				cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
			} catch (ParseException e1) {
			}
			
			boolean ret= false;			
			if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
				logger.info("KILL ME");
				
			} else {
				try {
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("STATS_DTTM", STATS_DTTM);
					ret = this.transectionMobMediaScriptChrgStats(param);
					
					if (ret) {
						ret = this.transectionMobMediaChrgStats(param);
					}
					if(ret) {
						cal.add(Calendar.DATE, 1);
					}
					
				}catch(Exception e) {
					ret= false;
				}
				
			}
			JSONObject jsonOut= new JSONObject();
			jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
			
			logger.info("jsonOut:{}", jsonOut);
			
			String filePath = String.format("%s", logPath +"../");
			File file = new File( filePath + flagFileName );
			PrintWriter out = null;
				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
				} catch (IOException e) {
				}
			    out.println(jsonOut);
			    out.close();
			} catch (Exception e) {
				logger.error("err ", e);
			}
		
	}
	
	private boolean transectionMobMediaChrgStats(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_MEDIA_CHRG_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_MEDIA_CHRG_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_MEDIA_CHRG_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_MEDIA_CHRG_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_MEDIA_CHRG_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;	
	}
	
	private boolean transectionMobMediaScriptChrgStats(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_MEDIA_SCRIPT_CHRG_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_MEDIA_SCRIPT_CHRG_STATS_MIGRATION"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_MEDIA_SCRIPT_CHRG_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_MEDIA_SCRIPT_CHRG_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_MEDIA_SCRIPT_CHRG_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;	
	}
	
	
	 

	public void migrationMthStats() {
			try {
				String flagFileName= "MOBMTHSTATS_20200401_20210501";
				
				String JSON_STR = FileUtils.readJsonFile(logPath +"../", flagFileName);
				String STATS_DTTM = "";
				try{
					JSONObject json = JSONObject.fromObject(JSON_STR.toString());
					STATS_DTTM = json.get("STATS_DTTM").toString();
				}catch(Exception e) {
					STATS_DTTM= flagFileName.split("_")[1];
				}
				if(STATS_DTTM==null) {
					STATS_DTTM= flagFileName.split("_")[1];
				}
				logger.info("flagFileName:{}, STATS_DTTM:{}", flagFileName, STATS_DTTM);
				
				Calendar cal= Calendar.getInstance();
				try {
					cal.setTime( DateUtils.getDate("yyyyMMdd", STATS_DTTM) );
				} catch (ParseException e1) {
				}
				
				boolean ret= false;			
				if(flagFileName.split("_")[2].equals(STATS_DTTM)) {
					logger.info("KILL ME");
					
				} else {
					try {
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("STATS_DTTM", STATS_DTTM);
						String [] ADVRTS_TP_CODE= new String[] {"01","04","05","06","08","10","11","13","14","16","17","18","19","22","24","26","27","28","29","30","31","32","34","35","36","37","39","40","41","99"};
						for (String ADVRTS_CODE : ADVRTS_TP_CODE ) {
								param.put("ADVRTS_TP_CODE", ADVRTS_CODE);
								ret = this.transectionMobMediaScriptMthStats(param);
							if (ret) {
								ret = this.transectionMobCtgrMthtats(param);
							}
							if (ret) {
								ret = this.transectionMobCnvrsRenewCtgrMthStats(param);
							}
						}
						
						
						if(ret) {
							cal.add(Calendar.DATE, 1);
						}
						
					}catch(Exception e) {
						ret= false;
					}
					
				}
				JSONObject jsonOut= new JSONObject();
				jsonOut.put("STATS_DTTM", new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString());
				
				logger.info("jsonOut:{}", jsonOut);
				
				String filePath = String.format("%s", logPath +"../");
				File file = new File( filePath + flagFileName );
				PrintWriter out = null;
					try {
						out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
					} catch (IOException e) {
					}
				    out.println(jsonOut);
				    out.close();
				} catch (Exception e) {
					logger.error("err ", e);
				}
			
		}
	//월별 mob_cnvrs_renew_ctgr_stats
	private boolean transectionMobCnvrsRenewCtgrMthStats(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_MTH_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CNVRS_RENEW_CTGR_MTH_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_MTH_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CNVRS_RENEW_CTGR_MTH_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CNVRS_RENEW_CTGR_MTH_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;	
	}
	//월별 mob_ctgr_mth_stats
	private boolean transectionMobCtgrMthtats(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_CTGR_MTH_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_CTGR_MTH_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_CTGR_MTH_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_CTGR_MTH_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_CTGR_MTH_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;	
	}
	//월별 mob_media_script_mth_stats
	private boolean transectionMobMediaScriptMthStats(Map<String, Object> param) {
		boolean result = false;
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {

				boolean hisYN = true;
				long startTime = System.currentTimeMillis();
				long endTime = System.currentTimeMillis();
				long resutTime = 0;

				try {
					logger.info("INSERT_MOB_MEDIA_SCRIPT_MTH_STATS_MIGRATION START");
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "MOB_MEDIA_SCRIPT_MTH_STATS"), param);
						sqlSessionTemplateBilling.flushStatements();
					
					resutTime = System.currentTimeMillis() - startTime;
					logger.info("INSERT_MOB_MEDIA_SCRIPT_MTH_STATS_MIGRATION (TBRT)  : {}, {}, {}", param, resutTime + "(ms)");
					
					res = true;
				} catch (Exception e) {
					hisYN = false;
					logger.error("INSERT_MOB_MEDIA_SCRIPT_MTH_STATS_MIGRATION transectionRuning Exception ==>", e);

					status.setRollbackOnly();
					res = false;

				} finally {
					if (hisYN) {
						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;
						logger.info("INSERT_MOB_MEDIA_SCRIPT_MTH_STATS_MIGRATION 종료  : (TBRT) :{}, {}", "", resutTime +"(ms)");
					}
				}
				return res;
			}
		});
		return result;	
	}
	
	/**
	 * MOB_CAMP_MEDIA_UNIQ_CLICK_STATS -> MOB_CAMP_MEDIA_UNIQCLICK_RENEW_STATS 마이그레이션
	 * No 사용 하는 버전
	 * 
	 * @author  : sjpark3
	 * @since   : 2021-12-08
	 * @version : 0.1
	 */
	public void migrationUniqStatsUsedNo() {
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		int procRaw = 2000;
		int firstStart = 1;
		int firstEnd = 434395916;		// 2021-12-09 제외한 MAX 값
		int firstLimit = (int) Math.ceil(firstEnd / procRaw) + 1;
		int seccondStart = 434364963;	// 2021-12-09 MIN 값
		int seccondEnd = 437009426;		// 2021-12-09 MAX 값
		int seccondLimit = (int) Math.ceil((seccondEnd - seccondStart) / procRaw) + 1;
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		long resutTime = 0;
		Map<String, Object> param = new HashMap<>();
		
		logger.info("uniq click migration START");
		param.put("MODE", "normal");
		for (int i=1; i<=firstLimit; i++) {
			param.put("START_NO", (i-1) * procRaw + 1);
			param.put("END_NO", i * procRaw);
			
			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"migrationUniqClickRenewUseNo"), param);
			sqlSessionTemplateBilling.flushStatements();
			
			if (i%500 == 0) {
				logger.info("uniq click migration success NO : {}", i * procRaw);
			}
		}
		
		param.put("MODE", "other");
		for (int j=1; j<=seccondLimit; j++) {
			param.put("START_NO", (j-1) * procRaw + seccondStart);
			param.put("END_NO", j * procRaw + seccondStart);
			
			sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"migrationUniqClickRenewUseNo"), param);
			sqlSessionTemplateBilling.flushStatements();
			
			if (j%500 == 0) {
				logger.info("uniq click migration END_NO : {}", j * procRaw + seccondStart);
			}
		}
		
		endTime = System.currentTimeMillis();
		resutTime = endTime - startTime;
		logger.info("uniq click migration END (TBRT) : {}", resutTime + "(ms)");
	}
	
	/**
	 * MOB_CAMP_MEDIA_UNIQ_CLICK_STATS -> MOB_CAMP_MEDIA_UNIQCLICK_RENEW_STATS 마이그레이션
	 * 
	 * @author  : sjpark3
	 * @since   : 2021-12-08
	 * @version : 0.1
	 */
	public void migrationUniqStats() {
		/**
		 * 1) 처리 일자 파일 로드
		 * 	-파일 형식 예 : start-
		 * 	20211007
		 * 	20211008
		 * 	-파일 형식 예 : end-
		 * 2) 가장위의 날짜만 가져와서 처리
		 * 3) 가져온 날짜에 맞는 ADVER_ID 리스트를 가져옴
		 * 4) 가져온 날짜와 ADVER_ID 를 기준으로 마이그레이션 수행
		 */
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		String procStatsDttm = getProcStatsDttm("UNIQ_STATS_20211007_20211213");

		if (!procStatsDttm.isEmpty()) {
			Map<String, Object> param = new HashMap<>();
			param.put("STATS_DTTM", procStatsDttm);
			List<String> adverIds = getUniqClickAdverIds(param);

			long startTime = System.currentTimeMillis();
			long endTime = 0;
			long resutTime = 0;

			logger.info("uniq click migration START");

			for (String adverId : adverIds) {
				param.put("ADVER_ID", adverId);

				sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"migrationUniqClickRenew"), param);
				sqlSessionTemplateBilling.flushStatements();
			}
			endTime = System.currentTimeMillis();
			resutTime = endTime - startTime;

			logger.info("uniq click migration END (TBRT) : {}", resutTime + "(ms)");
		}

	}

	/**
	 * 마이그레이션이 필요한 날짜 1개 읽어오는 메소드
	 *
	 * @param 	: dateFileName(파일명)
	 * @author  : sjpark3
	 * @since   : 2021-12-08
	 * @version : 0.1
	 */
	private String getProcStatsDttm(String dateFileName) {
		String statsDttm = "";
		List<String> retryList = new ArrayList<>();

		File file = new File(logPath + "../");
		File[] files = file.listFiles();

		try {
			if (files != null) {
				logger.info("read stats_dttm START");

				for (File readFile : files) {
					String readFileName = readFile.getName();

					if (!readFileName.contains(dateFileName)
							|| readFileName.contains("_ing")) {
						continue;
					}

					String procFileName =
							readFile.getAbsolutePath() + "_" + System.currentTimeMillis() + "_ing";
					File procFile = new File(procFileName);
					readFile.renameTo(procFile);

					BufferedReader fr = new BufferedReader(new FileReader(procFile));
					String lineData = "";

					while ((lineData = fr.readLine())!= null) {
						if (lineData.isEmpty() || lineData.length() > 8) {
							continue;
						}

						if (statsDttm.isEmpty()) {
							statsDttm = lineData;
						} else {
							retryList.add(lineData);
						}
					}
				}

				logger.info("read stats_dttm END");
			}
		} catch (Exception e) {
			logger.error("can't read file ==> {}", e);
		} finally {
			file = null;
		}

		if (!retryList.isEmpty()) {
			File retryFile = new File(logPath + "../" + dateFileName);
			PrintWriter out = null;

			try {
				logger.info("save not used stats dttm START");

				retryList= Lists.newArrayList(Sets.newHashSet(retryList));
				retryList.sort(Comparator.naturalOrder());
				out = new PrintWriter(new BufferedWriter(new FileWriter(retryFile, true)));

				for (String retryLine : retryList) {
					out.println(retryLine);
				}

				logger.info("save not used stats dttm END");
			} catch (Exception e) {
				logger.error("can't save file ==> {}", e);
			} finally {
				if (out != null) {
					out.close();
				}
				retryFile = null;
			}
		}

		return statsDttm;
	}

	/**
	 * 마이그레이션이 필요한 날짜 전부 읽어오는 메소드
	 *
	 * @param 	: dateFileName(파일명)
	 * @author  : sjpark3
	 * @since   : 2022-03-16
	 * @version : 0.1
	 */
	private List<String> getProcStatsDttmList(String dateFileName) {
		String statsDttm = "";
		List<String> statsDttmList = new ArrayList<>();

		File file = new File(logPath + "../");
		File[] files = file.listFiles();

		try {
			if (files != null) {
				logger.info("read stats_dttm list START");

				for (File readFile : files) {
					String readFileName = readFile.getName();

					if (!readFileName.contains(dateFileName)
							|| readFileName.contains("_ing")) {
						continue;
					}

					String procFileName =
							readFile.getAbsolutePath() + "_" + System.currentTimeMillis() + "_ing";
					File procFile = new File(procFileName);
					readFile.renameTo(procFile);

					BufferedReader fr = new BufferedReader(new FileReader(procFile));
					String lineData = "";

					while ((lineData = fr.readLine())!= null) {
						if (lineData.isEmpty() || lineData.length() > 8) {
							continue;
						}

						statsDttmList.add(lineData);
					}
				}

				logger.info("read stats_dttm list END");
			}
		} catch (Exception e) {
			logger.error("can't read file ==> {}", e);
		} finally {
			file = null;
		}

		return statsDttmList;
	}

	/**
	 * 마이그레이션이 필요한 광고주ID 리스트 읽어오는 메소드
	 *
	 * @param 	: param
	 * @author  : sjpark3
	 * @since   : 2021-12-08
	 * @version : 0.1
	 */
	private List<String> getUniqClickAdverIds(Map<String, Object> param) {
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		final List<String> adverIds = new ArrayList<>();

		transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res = false;

			public Object doInTransaction(TransactionStatus status) {
				try {
					logger.info("get uniq click adverIds START");

					List<Map> resultMap = sqlSessionTemplateBilling
							.selectList(String.format("%s.%s", NAMESPACE, "selectAdverIdsAdverStats"), param);
					for (Map map : resultMap) {
						adverIds.addAll(map.values());
					}

					logger.info("get uniq click adverIds END");

					res = true;
				} catch (Exception e) {
					status.setRollbackOnly();
					res = false;

					logger.error("cat't select adverIds => {}", e);
				}

				return res;
			}
		});

		return adverIds;
	}
	
	/*
	 * 전환 원장데이터 삭제시 해당 전환 전체 데이터 마이그레이션 진행 
	 * 테이블 : MOB_CNVRS_RENEW_COM_STATS,MOB_CNVRS_RENEW_KPI_STATS,MOB_CNVRS_RENEW_MEDIA_STATS,MOB_CNVRS_RENEW_ADVER_STATS,MOB_CNVRS_RENEW_CTGR_HH_STATS,MOB_CNVRS_RENEW_CTGR_STATS
	 * @author  : dhlim
	 * @since   : 2022-01-13
	 * @version : 0.1
	 *
	 */
	public void conversionDataMigration() {
		String NAMESPACE = "convMigrationMapper";
		ArrayList<String> tableList = new ArrayList<String>();

		//시간 테이블 재 배치

		tableList.add("MIGRATION_MOB_CNVRS_HH_STATS");

		tableList.add("MIGRATION_MOB_CNVRS_RENEW_HH_STATS");

		//전환 삭제후 다시 말려야되는테이블
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_COM_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_KPI_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_MEDIA_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_ADVER_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_CTGR_HH_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_RENEW_CTGR_STATS");


		//구 전환 테이블 재 마이그레이션

		tableList.add("MIGRATION_MOB_CNVRS_STATS");
		tableList.add("MIGRATION_MOB_CNVRS_KPI_STATS");

		// MOB_CNVRS_DTL_STATS 기준으로 마이그레이션되는 테이블 제외
		// tableList.add("MIGRATION_MOB_CNVRS_ADVER_MEDIA_STATS");
		// tableList.add("MIGRATION_MOB_CNVRS_MEDIA_STATS");
		// tableList.add("MIGRATION_MOB_CNVRS_ADVER_STATS");
		// tableList.add("MIGRATION_MOB_CNVRS_INTG_STATS");
		// tableList.add("MIGRATION_MOB_CNVRS_COM_STATS");


		ArrayList<String> statsDttms = this.getStatsDttm();
		ArrayList<String> tempMths = new ArrayList<>();
		logger.info("ConversionDataMigration Start");

		if (statsDttms == null) {
			logger.info("FILE IS NOT EXIST");
		} else {
			for (String statsDttm : statsDttms) {
				Map<String , Object> param = new HashMap<String ,Object> ();
				param.put("stats_dttm",statsDttm);
				tempMths.add(statsDttm.substring(0,6));

				long startTime = System.currentTimeMillis();
				long endTime = 0;
				long resultTime = 0;

				for (String queryId : tableList) {

					sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, queryId), param);
					sqlSessionTemplateBilling.flushStatements();

					endTime = System.currentTimeMillis();
					resultTime = endTime - startTime;
					logger.info(queryId + "_result time - {}(ms)", resultTime);

				}
			}

			// 전환 월통계 재터리
			conversionMthDataMigration(tempMths);
		}
		logger.info("ConversionDataMigration Complete");
		
	}

	/**
	 * conversionMthDataMigration
	 * 전환 월통계 재처리 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-2-10
	 */
	private void conversionMthDataMigration(ArrayList<String> tempMths) {
		String NAMESPACE = "convMigrationMapper";
		String nowMth = "";
		ArrayList<String> mthTableList = new ArrayList<>();
		List<String> statsMths = new ArrayList<>();

		mthTableList.add("MIGRATION_MOB_CNVRS_RENEW_MTH_STATS");					// 재처리에 사용할 mapper
		mthTableList.add("MIGRATION_MOB_CNVRS_RENEW_CTGR_MTH_STATS");
		statsMths = tempMths.stream().distinct().collect(Collectors.toList());		// 중복 월 제거
		nowMth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));		// 현재 월 추출
		statsMths.remove(nowMth);													// 현재 월 재처리 제외

		for (String statsMth : statsMths) {
			Map<String, Object> mthParam = this.getFirstToLastDate(statsMth);		// 시작/마지막 일 추출
			long mthStartTime = System.currentTimeMillis();

			for (String mthQueryId : mthTableList) {
				sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, mthQueryId), mthParam);
				sqlSessionTemplateBilling.flushStatements();

				logger.info(mthQueryId + "_result time - {}(ms)", System.currentTimeMillis() - mthStartTime);
			}
		}
	}

	/**
	 * 전환 원장데이터 삭제시 해당 전환 전체 데이터 마이그레이션 진행 
	 * 파일 을 읽어서 해당날짜 가지고 오는 메소드 
	 * @author  : dhlim
	 * @since   : 2022-01-13
	 * @version : 0.1
	 *
	 */
	private ArrayList<String> getStatsDttm() {

		ArrayList<String> statsDttms = new ArrayList<String>();
		String prefix = "CONV_FIX_DAY";
		String convPath = logPath + "convfix"
				+ "/";
		File Folder = new File(convPath);		

		if (!Folder.exists()) {
			Folder.mkdir();
		}
		File file = new File(convPath);
		File [] fileArr = file.listFiles();
		try {
			for (File readFile : fileArr) {			
				if(readFile.exists())
				{
					String fileName = readFile.getName();
					logger.info("readfile - {}", fileName);
					
					if(fileName.indexOf("_ing") > 0) {
						continue;
					}
					
					if (fileName.indexOf(prefix) == -1) {
						continue;
					}
					
					long millis = Calendar.getInstance().getTimeInMillis();
					String file_reName = fileName +"_"+ millis +"_ing";
					File file_Tmp = new File( convPath + file_reName );
					readFile.renameTo( file_Tmp );

					BufferedReader inFile = new BufferedReader(new FileReader(file_Tmp));
					String sLine = null;

					while( (sLine = inFile.readLine()) != null ) {
						statsDttms.add(sLine);
					}
				} else {
					logger.info("Conv_Fix_File is not exists");
				}
			}
		} catch( Exception e) {
			logger.error("Fail Read File - {}" , e);
		} 
		return statsDttms;
	}

	/**
	 * getFirstToLastDate
	 * 재처리용 해당월에 시작/마지막 일을 구하는 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-2-9
	 */
	private Map<String, Object> getFirstToLastDate(String statsMth) {
		Map<String, Object> result = new HashMap<>();
		DateTimeFormatter firstDateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyyMM")
				.parseDefaulting(ChronoField.DAY_OF_MONTH, 1).toFormatter();
		LocalDate parseDate = LocalDate.parse(statsMth, firstDateFormatter);
		String firstDate = parseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String lastDate = parseDate.with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		result.put("stats_mth", statsMth);
		result.put("first_date", firstDate);
		result.put("last_date", lastDate);

		return result;
	}
	
	private String getNowDateTime() { 
		LocalDateTime now = LocalDateTime.now();
		String formatter = now.format(DateTimeFormatter.ofPattern("yyyy_MM_dd")); 
		return formatter;
	}

	/**
	 * MOB_MEDIA_SCRIPT_CHRG_STATS -> MOB_MEDIA_CHRG_STATS 마이그레이션
	 *
	 * @author  : sjpark3
	 * @since   : 2022-03-07
	 * @version : 0.1
	 */
	public void migrationMediaChrgStats() {
		/**
		 * 1) 처리 일자 파일 로드
		 * 	-파일 형식 예 : start-
		 * 	20211007
		 * 	20211008
		 * 	-파일 형식 예 : end-
		 * 2) 파일의 모든 날짜를 가져와서 처리
		 */
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		List<String> procStatsDttms = getProcStatsDttmList("MEDIA_CHRG_STATS");

		if (!procStatsDttms.isEmpty()) {
			for (String procStatsDttm : procStatsDttms) {
				Map<String, Object> param = new HashMap<>();
				param.put("STATS_DTTM", procStatsDttm);

				long startTime = System.currentTimeMillis();
				long endTime = 0;
				long resutTime = 0;

				logger.info("media chrg migration START- {}", procStatsDttm);

				sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMediaChrgStats"), param);
				sqlSessionTemplateBilling.flushStatements();

				endTime = System.currentTimeMillis();
				resutTime = endTime - startTime;

				logger.info("media chrg migration END (TBRT) - {} : {}", procStatsDttm, resutTime + "(ms)");
			}
		}
	}


	/**
	 * MOB_MEDIA_PAR_WK_STATS 날짜지정 재처리 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-03-16
	 * @version : 0.1
	 */
	public void migrationMediaParWkStats() {
		/**
		 * 1) 처리 일자 파일 로드
		 * 	-파일 형식 예 : start-
		 * 	20211007
		 * 	20211008
		 * 	-파일 형식 예 : end-
		 * 2) 파일의 모든 날짜를 가져와서 처리
		 */
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		List<String> procStatsDttms = getProcStatsDttmList("MEDIA_PAR_WK_STATS");

		if (!procStatsDttms.isEmpty()) {
			for (String procStatsDttm : procStatsDttms) {
				Map<String, Object> param = new HashMap<>();
				param.put("STATS_DTTM", procStatsDttm);

				long startTime = System.currentTimeMillis();
				long endTime = 0;
				long resutTime = 0;

				logger.info("media wk migration START - {}", param);

				sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE,"migrationMediaParWkStats"), param);
				sqlSessionTemplateBilling.flushStatements();

				endTime = System.currentTimeMillis();
				resutTime = endTime - startTime;

				logger.info("media wk migration END (TBRT) : {}", resutTime + "(ms)");
			}
		}
	}

	/**
	 * MOB_MEDIA_SCRIPT_MTH_STATS
	 * MOB_MEDIA_MTH_STATS
	 * MOB_CAMP_MTH_STATS
	 * MOB_ADVER_MTH_STATS
	 * MOB_COM_STATS_MTH_INFO
	 * MOB_CTGR_MTH_STATS
	 * MOB_MEDIA_PAR_MTH_STATS
	 * MOB_CAMP_PAR_MTH_STATS
	 * MOB_ADVER_PAR_MTH_STATS
	 * 날짜지정 재처리 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-03-16
	 * @version : 0.1
	 */
	public void migrationAllMthStats() {
		/**
		 * 1) 처리 일자 파일 로드
		 * 	-파일 형식 예 : start-
		 * 	20211007
		 * 	20211008
		 * 	-파일 형식 예 : end-
		 * 2) 파일의 모든 날짜를 가져와서 처리
		 * 3) 말일 -> 첫일로 넘어갈때 해당 월 데이터 딜리트 시킴 (20210430 -> 20210501 이면 202105 딜리트)
		 * 4) 3번으로 인해 한번 재처리할경우 월단위로 날짜를 넣어줘야함 (20210401 ~ 20210430)
		 */
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		String tmpStatsMth = "";
		List<String> procStatsDttmList = getProcStatsDttmList("MTH_STATS");
		List<Map> pltfomTpCodes = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectPltfomTpCodes"));
		List<Map> advrtsPrdtCodes = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsPrdtCodes"));
		List<Map> advrtsTpCodes = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsTpCodes"));

		if (!procStatsDttmList.isEmpty() && !pltfomTpCodes.isEmpty()
				&& !advrtsPrdtCodes.isEmpty() && !advrtsTpCodes.isEmpty()) {
			for (String procStatsDttm : procStatsDttmList) {
				long startTime = System.currentTimeMillis();
				long endTime = 0;
				long resutTime = 0;
				long tableStartTime = 0;
				String procStatsMth = procStatsDttm.substring(0, 6);
				boolean deleteFlag = (tmpStatsMth.equals(procStatsMth) && !tmpStatsMth.isEmpty()) ? false : true;

				tmpStatsMth = procStatsMth;

				// MOB_CAMP_PAR_MTH_STATS, MOB_ADVER_PAR_MTH_STATS 딜리트 별도처리 (공통 PK 이외의 PK 를 사용하기에 별도처리)
				if (deleteFlag) {
					Map<String, Object> param = new HashMap<>();
					param.put("STATS_MTH", procStatsMth);
					int startDate = Integer.parseInt(procStatsMth + "01");
					int endDate = Integer.parseInt(procStatsMth + "31");

					logger.info("mth delete START - {}", param);

					for (int deleteDate = startDate; deleteDate <= endDate; deleteDate++) {
						for (Map pltfomTpCode : pltfomTpCodes) {
							for (Map advrtsPrdtCode : advrtsPrdtCodes) {
								for (Map advrtsTpCode : advrtsTpCodes) {
									tableStartTime = System.currentTimeMillis();
									param.put("PLTFOM_TP_CODE", pltfomTpCode.get("PLTFOM_TP_CODE"));
									param.put("ADVRTS_PRDT_CODE", advrtsPrdtCode.get("ADVRTS_PRDT_CODE"));
									param.put("ADVRTS_TP_CODE", advrtsTpCode.get("ADVRTS_TP_CODE"));
									param.put("DELETE_DATE", deleteDate);

									try {
										sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobCampParMthStats"), param);
										sqlSessionTemplateBilling.flushStatements();

									} catch (Exception e) {
										logger.error("deleteMobCampParMthStats err", e);
									}
									logger.info("deleteMobCampParMthStats - succ - {}",
											(System.currentTimeMillis() - tableStartTime)+ "(ms)");

									try {
										sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobAdverParMthStats"), param);
										sqlSessionTemplateBilling.flushStatements();
									} catch (Exception e) {
										logger.error("deleteMobAdverParMthStats err", e);
									}
									logger.info("deleteMobAdverParMthStats - succ - {}",
											(System.currentTimeMillis() - tableStartTime)+ "(ms)");
								}
							}
						}
					}

					endTime = System.currentTimeMillis();
					resutTime = endTime - startTime;

					logger.info("mth delete END (TBRT) : {}", procStatsDttm + " / " + resutTime + "(ms)");
				}

				for (Map pltfomTpCode : pltfomTpCodes) {
					for (Map advrtsPrdtCode : advrtsPrdtCodes) {
						for (Map advrtsTpCode : advrtsTpCodes) {
							Map<String, Object> param = new HashMap<>();
							param.put("STATS_DTTM", procStatsDttm);
							param.put("STATS_MTH", procStatsMth);
							param.put("PLTFOM_TP_CODE", pltfomTpCode.get("PLTFOM_TP_CODE"));
							param.put("ADVRTS_PRDT_CODE", advrtsPrdtCode.get("ADVRTS_PRDT_CODE"));
							param.put("ADVRTS_TP_CODE", advrtsTpCode.get("ADVRTS_TP_CODE"));

							logger.info("mth migration START - {}", param);

							// MOB_MEDIA_SCRIPT_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobMediaScriptMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobMediaScriptMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobMediaScriptMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobMediaScriptMthStats err", e);
							}
							logger.info("mth migration migrationMobMediaScriptMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");


							// MOB_MEDIA_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobMediaMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobMediaMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobMediaMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobMediaMthStats err", e);
							}
							logger.info("mth migration migrationMobMediaMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_CAMP_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobCampMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobCampMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobCampMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobCampMthStats err", e);
							}
							logger.info("mth migration migrationMobCampMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_ADVER_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobAdverMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobAdverMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobAdverMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobAdverMthStats err", e);
							}
							logger.info("mth migration migrationMobAdverMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_COM_STATS_MTH_INFO 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobComStatsMthInfo"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobComStatsMthInfo - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobComStatsMthInfo"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobComStatsMthInfo err", e);
							}
							logger.info("mth migration migrationMobComStatsMthInfo succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_CTGR_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobCtgrMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobCtgrMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobCtgrMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobCtgrMthStats err", e);
							}
							logger.info("mth migration migrationMobCtgrMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_MEDIA_PAR_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								if (deleteFlag) {
									sqlSessionTemplateBilling.delete(String.format("%s.%s", NAMESPACE, "deleteMobMediaParMthStats"), param);
									sqlSessionTemplateBilling.flushStatements();
									logger.info("deleteMobMediaParMthStats - succ");
								}

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobMediaParMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobMediaParMthStats err", e);
							}
							logger.info("mth migration migrationMobMediaParMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_CAMP_PAR_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobCampParMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobCampParMthStats err", e);
							}
							logger.info("mth migration migrationMobCampParMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");

							// MOB_ADVER_PAR_MTH_STATS 재처리
							try {
								tableStartTime = System.currentTimeMillis();

								sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationMobAdverParMthStats"), param);
								sqlSessionTemplateBilling.flushStatements();
							} catch (Exception e) {
								logger.error("migrationMobAdverParMthStats err", e);
							}
							logger.info("mth migration migrationMobAdverParMthStats succ - {}",
									(System.currentTimeMillis() - tableStartTime)+ "(ms)");
						}
					}
				}

				endTime = System.currentTimeMillis();
				resutTime = endTime - startTime;

				logger.info("mth migration END (TBRT) : {}", procStatsDttm + " / " + resutTime + "(ms)");
			}
		}
	}

	/**
	 * PLUSCALL_CAMP_MEDIA_HH_STATS, PLUSCALL_CAMP_MEDIA_STATS 를
	 * MOB_CAMP_MEDIA_HH_STATS, MOB_CAMP_MEDIA_STATS 를 사용하여 마이그레이션 하는 메소드
	 *
	 * @author  : sjpark3
	 * @since   : 2022-04-04
	 * @version : 0.1
	 */
	public void migrationPluscallStats() {
		/**
		 * 1) 처리 일자 파일 로드
		 * 	-파일 형식 예 : start-
		 * 	20211007
		 * 	20211008
		 * 	-파일 형식 예 : end-
		 * 2) 파일의 모든 날짜를 가져와서 처리
		 */
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		List<String> procStatsDttms = getProcStatsDttmList("PLUSCALL_HH_STATS");

		if (!procStatsDttms.isEmpty()) {
			for (String procStatsDttm : procStatsDttms) {
				List<String> siteCodes = new ArrayList<>();
				Map<String, Object> param = new HashMap<>();
				param.put("STATS_DTTM", procStatsDttm);

				long startTime = System.currentTimeMillis();
				long endTime = 0;
				long resutTime = 0;

				// PLUSCALL_CAMP_MEDIA_HH_STATS 처리
				logger.info("pluscall hh migration START - {}", param);

				try {
					siteCodes = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectPluscallSiteCodes"), param);
					param.put("SITE_CODES", siteCodes);
				} catch (Exception e) {
					logger.error("Could not get pluscall siteCode list - ", e);
				}

				if (siteCodes.size() > 0) {
					try {
						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationPluscallCampMediaHhStats"), param);
						sqlSessionTemplateBilling.flushStatements();

						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;

						logger.info("pluscall hh migration END (TBRT) : {}", resutTime + "(ms)");

						// PLUSCALL_CAMP_MEDIA_STATS 처리
						logger.info("pluscall dd migration START");

						startTime = System.currentTimeMillis();
						endTime = 0;
						resutTime = 0;

						sqlSessionTemplateBilling.update(String.format("%s.%s", NAMESPACE, "migrationPluscallCampMediaStats"), param);
						sqlSessionTemplateBilling.flushStatements();

						endTime = System.currentTimeMillis();
						resutTime = endTime - startTime;

						logger.info("pluscall dd migration END (TBRT) : {}", resutTime + "(ms)");
					} catch (Exception e) {
						logger.error("migrationPluscallStats FAILED - ", e);
					}
				}
			}
		}

		logger.info("migrationPluscallStats END");
	}
	public void diffPoint(){
		//String adverId = "fastcampus,enuri,imath,mootoon,mega,itembaydmc,a1locknlock,edfmd,genius-cp,kmong,su16444,drdifferent,hfashionmall,keynotysystem,87sarang,Coloso,tour2000,mega2016,hanasys,abbongbra,dknoonnopi,skylife,hiphoper,beaucre,nutricomeon,livart,pagodastar,midasb,jdoubleu01,gaenso,frombio,neemee1,lhp24482,motomoto,beauty025,stockcom,babathe,Zoomex,snowball,itmichaa,dhdesign2016,minhye3,ajjuncmall,tbhglobal,mongdies360,vitalslim,bangwuri123,copierland,table2022,ilove622,batk,chilsungmall,locknlock,dswell01,trendkorea,cbti,brainworks,hackersac,wingsmall,doctoron2017,beston,jenacell,hilightbrand,mulamens,nexeno2o,paranhanul,hamkecoffee,paperbag,cnnapparel,growmkt,naning9_,ms1003,kbchachacha1,intertour,maymay1102,mrpizza77,nanocumin,rurugold,uit315,kangolkids,onnurieye,gosipass1,tp_comento,chang2030,thebanchan,sleeper2020,rowonholding,thecandles,biosdream,coreana10,onsen,fasbofficial,nucstore,bandaikr,venetmeal,artworkdream,gcosmo,fixu,hackersut,isoi,yournature,clomount,melaxin,hucheum,howchon,itemmania3,megarussel2,anbio,tradlinx,everbiogroup,eduwill_pf,shoemarker1,arbonobra,eccogolfkr,depanse,sdreamfnb,samihun3,acocoblack,fuse,sinbiang,dcollect,megacst4ad1,hackersecpa,zipbanchan,filezaru,hoonjaya,stantop,under70,bedding,popcon,pretty,folderstyle,remarket,dmc_sdu,greyraise,theknitco,lactiv,9tems,myanycar33,Nhermes,dbrud89,woodtherapy,yadahcos,elawfirm,depound,acrmart,monomart,bnrmall,ulineclinic,mykindkorea,maatila,jenny1,artchain,goldpang,funiturs,8room,lovejoo0311,firstage,twinkring,baeoom,teacherpass1,maykids,coffeecg,emonshome,wonlog11,bylynn,jnkscience,looktol,urogyn,junjewelry05,coreadog,vosmarket22,storynine,lush,nflstyle,idskin,labnpeople,bagstay,mustela,geuchanggo,holicn,utosoft,shm8920,instaget,happyprince,vely315,ilovej,slimplanet,npine,paleo,saatads01,gettyimages,bespin_gbu,we,coreplanet,wellcindy77,boen,hera770,planets,sumnfit,chungsos,atower2015,thenature,ysretail,comfpro,varzar,liberclassy,gongone__,tswcos,glamd,elishacoy0,sonnetbride,vcservice,had010203,gcu2010,tged,areukeskin,johnpaul,drkos,bocaci,ohprint,qkrdptjd2000,onnuri,parklon,goldria,fooding,edu2080,wjbooks,kysooc1,theclass,cookiehouse,cleanupk,ijinro2wan,lhy6426,bdu,100yearceo,petfood109,sopiagagu,myskycws,beburbank,eduhansol,shenb,pnt_shop,likeps,storebom,playtex,cheongdamu,willstore01,edenlaw,poopoomonster,rose2014,illui,abowl1590,avention88,signeasy,fitskr12,onyoupharm,imdak,cocomimi93,oxygen,nextengine,roksofa1,teamflex,palgongkimchi,bloom1008,briochin,mbaby,coreplanet3,bieninter9,tirzcommerce,nelson,ybmclass,lavitem,amcosmetics,onadno1,bluemoonkorea,hsc2017,slowrabbit,potterybarn,mizmin,soog0728,ubesta01,fitboonkorea,harimpet,hoopmagicair,sognomall,monsterf,pontree,ceragemon,ws,wjsaor,knlondon,mudecosmetic,S2ND,varihope2020,kiwongan,minsoo299,bonif6288,jcsint7,hisaeng,gyeon,hhsdkfk,byulstar,amadam4060,gngngface,welcronmall,keyplace,thenaplus,herren,youngmill,gwss,mayjune0506,powerplayer,duit,hms4070,sambuja162,saeropnl,hyeongjin85,bodydak,ladyplus,swanicoco,pk,meridiani,ellelipstick,pmgland,ryanps,carfixer,likalikalife,samsungfund,krtmkt,kimdy5000,sview,goodaypet,ehqhrgml,coffeemall,vtcosmetics,muro86,ypp9249,eseltree,dalpong,hansclean,peoplenco,tiformen2,malba,arecipeofgirl,azavino,urbanknife1,influx,aire,lemouton,ysloves,livingjoa,gmfurni,lawfirmgammyu,jhbros0405,waterwel,dayr,yedam_korea,dori,midwin,jbros,repzlekor,buzzwing,pentas01,arztin,omron4210,creve9,noriterboard,calmamall,holicme,livlab,lowellint,liriojewel,nmgdermatix,forencos,petto2018,dwhome,freepany,dopamines,class101,seeoneeye,origo,ssoook,cocoya0615,soramclinic,junmart1,hotelpass,ebsim,ept666,shimmi327,yolofnc,tproject,artish,cleandogam,casioland,hansphama,kccworld,dahilskin,cmtowel,footsell,bagstage,andalou,ahddlsp12,lottegfrkappa,nan_czpig,acteryx,namuprep,fuhrer1,lofn,dionehome7788,honeytip0326,taekwondo,toxn2,vs_nas,galleriajigi,daehangobchan,mustit,ibimcos,chaye,brww,waveent,samsung-fire,namuem1,emat,breatheb,stylenoriter,cafealtro,celebview,k1water,dermaforet,candlejoy,tenthree,numbuzin,mscorp0301_in,ledstudio,createats,limkwoo,samhongsa,patio42ap,dentiluv,mychef00,jukebox25,rosemong123,valenciahome,wamd,holanubes,biomist111,ivenet01,petshield,benign1120,likeastarshop,syeng,eduino,kmo601,dduknice4,tonermart,planmax1472,topresso,zenabella,geniusfnb,mantory,housej2157,drbob,dkjubang,boby7,kht1,surgitech,cellnb01,bardot,besthouse,vipsvita,simonplanning,firepass1,autodccar,naegapick,lulus0101,roma4923,roompacker,99flower,takeasy,megacoffee,chakanfactory,efront,snubluecom,hanssem_int,leeds21,rnshop,truelife,lenslala,indain77,slowlab,jdhwwy,hugme2,mvnofreet,dodot,iehome,brandps76,dmcdaesung,bakingmon,annanlove,wishe,rakim03,lemirage,benetus,nlife07,tpffprxh,hkvenus,damsan,chichi,ablue00,soltechnet,oldgoods11,nouhaus,bonmal,rdg1345,theselfie,lillycover,mywonder,neurx,activelife,iceman57,rabyola,dewphj0713,snpcompany21,denomics,dasoda18,elkara,nobly7220,decoroso,gkwls5767,smilecat,dearmami,angelssk7,aillen1351,bindesign,mnkpartners,dailylike,breezeproject,edocom8680,twoponds1,minwise,iroholdings,hjcookee,gywkehd,kiumhouse,nb2014,evanchris,lindsay,jardin89,drall,demaille,safari1962,sbrown7528,jmonde,gerio,dummarket,delyvely,valen007,covak,brocom91,leo2002,policepass1,montang12,savit,lamicheps,yigangyi,banul,daindain,kanuda,saera1978,ssenyp,kistem,bathmaster202,smecretina,lookds,chunjili0327,thewallstyle,gmpharm,gmarkettour,riena,jamjamstory1,chaewoodak,namunlife,dsfood,galleriaucup,womensskill,aprilwaltz,sioris,tunachef,nowcos,sshyundent,ddheart,seawave01,gsccoffee,interdenim1,dapojang,junglemonster,fooddoctor,ijun,kkllim_inc,neemee2,bfhani,jellymomco,airit,ekbeauty,code4,sartorius,onga,benow_fwee,madvanilla,sonatural,afterdental,joegrande,skyskysky,yikcr,yelkao,faumkt,purumfs,yourgreen,peteros12,euboskorea,spacev,ssunap,simplr7,yvesrocher,foodidol2man,superiori,lydiarnc1,gamemania,hisfit,skyeyeplus,daum1,sparkplus,cocoro0312,onleaf,onyoupharm2,10000w,zenwood,undervi1,sont7009,plafarm,adonislotte,sinfabric25,jh1004,fritte_,addwings,gasiwa,joyyou,lindashop1,mrbossam,rrbbsss,dermaline,yogisso,zcubespace,hackers,kingcarwash,cellmula,bedightcokr,travelgear,prettyps,saturday09,chak333,tyf,ppi6569,thenlaw1,thestones,seoulhifu,shhom,bwkang70,penius,jntek,slowcompany,trainersiu888,urbanflower,intheroom,nsr1,drlean,theralogic,getvenuscokr,mrchallenge,centumcore,louisviel,kamkko19,ssilafood,elancokorea,bettergo,mlinecsi,drsmaster,supplydemand,thehamsp,bkcp2107,hong8924,saint070,bowlow1,zmdcp0203,herbeautecokr,i2607,dogpure,pocheon2022,loungelitnl,merryplanet,btmr88,madamade,spicus,beachstory,bdkorea,volar8455,oseque,barulab,koozpet,jobplanet_mkt,jlcomms,byjimi,maisonys,urbantonic,mamitie2021,dearson,yesimin,ironstarfitne,genco,euyira,lsinter1207,skinmed,redwing,wherehouse,cdmd2022,mushroomd,points1304,cilantrohub,flapkorea,instatoday,wooju138,opskin_mo,goldman,nhlyvly,darcom,ketomarket,mibefairs,imost_77,florist4023,guardykorea,chungsogooda,autopremium,flot_1,sofak0512,chungsomon,apharm,n365day,chudak12,alotof,crowngoose,larpus3,shhombeu,ninemila009,mss73543,wakerkorea,jch1337,litandard,samaglobal,bobmukza,goyoo1,sportstop,leemybeen,alivelab,rodemfood,stvita11,grigoshop,eightsugar88,yjf3996,dspero,noteprs,kemglab_ss,illonkorea,ilmiri2020,cure31,bodybogam,mohch611,wjpass,cbwcom,mpgkorea,aaronshop,link4710,naturewave,soogee77,yedang_food,soonsoojaa,biosia,abm_nymall,wow4kmy,safelife0119,mfrental,meshmarketing,withyouclinic,fpvmf01,voga0440,sessok,irecipe,byrodam,ovcos,imkshop,dhdh9902,drmeta2,joyparty79,allmat25,ljh24246,blessyou222,graceu,maylily0310,star0590,sjpcrime001,funbeerking_,drmeta3,epassbiz,greant,dhuman,guy0906,muchmerch,thisisrealad,healthystore,allthen1,eatraders,okj2233,charlotte,beautymon2,volvo_hmotors,reinsein,hmscut21,kottiuomo,bed072,julgran01,modengift21,ackkorea,mingyune,hongjinp,dearcus705,kyesangkjy,666buger,topface,woojoovely12,gd5b1c,ssongs,wsdental,sdmshop,insideme,gold111,onecare1945,now123098,cancer1,otpenglish,rinwrinww,dounni,needly,diskdr454,ourhomemall,msgbio,beuison,mfgcompany,kormat,ccanccanync,sojukdoo,dbbs_4kids,healingyou_1,hearkorea,puriskin,becoming27,kocofirst,makinalab1,rebrigen,bleugate,coffeenyss,sericosmetics,withmun,herbaland,kpchstore,neurolnbs,greeneye,pmulti,sleepingbee22,c114,174,lerame,ineltree,friendu2,bbcompany,essoco,drmeta4,buyroad,sdc2,goodjjr2,bohandaum,rovnik,hlscience,hnk0521,yy90yy,afrimo,jualae,point1303,ziocos,delandis,piumuniform,hackers_law,healim71,geolo,cebien0408,jmj0544,therabnh,ares7277,onddam,firsteye,foodology,mktraum01,daehanbeer,Haroutine,philosophia,hongjinjoo,01041514050,hunethunet,shionle,abijouclinic,naturalmom,vhckcjsrnr,sixft,hanganggolf,artcreo1,ggolab,medijewelry,daehoga,koresupply,mattbag,ph365,ourhomemall_s,engenertek,ckpc1947,stayhouse,codegreen,zinon,jjman11,fiamg013,abcderm,ananan7979,daltt,gumiindong2,niff12,rlagnsxo,pure,dayg,reallike,maisonrepere,doctorcar,trymoney,winplus1996,deiro9596,jstar1000,acecoupang,kkokdam12,mobonptest,smilechina,bdenc,chasbean1,c_intween,niunkiok,doctors17,damsoyu22,valiant,virus1025,dreamingu20,prelude_kr,kaareklint,popsocketskr";
		//String adverId = "103kwj,1life,2001outlet,2008jst,2bstory,2chadol,4xr4xr,59flower,777english,777tire,95problem,990kimbop,99231300,abmeat,aconella,acpang,acrohani,acslfit,adsoul,aermall,afri2019,aftermonday01,ag3765,agaoo,agapefood1,agern,aghealthmob,ahckorea,ahree122,aioinc,akancase,alaqlanc,alicegohome,allbaro,allfree,allthen,alsqod888,alswo1314,amcosmetics,amerryaround,amo444,amomakr,amorjewelry,anais,another123,ansmedi88,anyguard,apharm,apharmhealth,apluslasek,apoaofficial,apomederm,appacompany,apssp,aqostudio,arecipeofgirl,aromigagu,asclo,asiaehrd,asiatooltech,asiatooltech2,aslada011,asolution,asuhelen,atny1,atosuave,auction1_kr,autoplus,avalinc,aycar,babolove,babymamfood,babymeal,bada4833,baeulhak,balibiki,bangbang99,banillastory,barangsori,baroko,barton,barunsesang,basic,bbqtown,beckhee,beginning01,benito,bentonkr,betterfinger,bettermonday,bfhani,bgfretail,bieninter9,biosia,biumenzyme,blingcook,blinkk,bluepharm,bluepops,bmplus,bncmarket,boazent,bodamskin,bodylogic,bodypim,bodyskin2,bogung1,bonadea123,bonajour,bonami87,bonest,bonps,boomst,boomst2,bosod,boutiquem1,boxcorea,bozagibag,brainpark102,brtech0912,bryc,bseye,bsfood,bsrental,bswatch,btbd83,btglobal,buckaroo,buddypetkr,bulrogeon,bylogin11,cafein5959,camper,carahan,carspace,catpre,ca_bigma2,cellborn,cellicon06,chajung,chakanshoes,cheatkey,chicfox,chimielab,chorokmaru,chungchoegg,chungdamahm,chunjili0327,cinderella1,clakr,classs,cleanclio1972,clio,cmh0709,cmnmstudio,cncocharge,coakorea,cocojane1212,commawang,comzip,conchwear,cookat,cookips,coolean99,cosnature_,cosniz,cottondog,coupang1,cozan2023,cozynest,cr5,crazyskin,creamycj,creme01222,crision90,cuchen1,cuckoo,cuckoorental,cultwo,cyclean,dabagirl,dachaeum01,dachaeum02,dadama8850,daehoga21,dailymeal,daliaspa,dalpong,danawa,daprs2015,dasanhani,daydreamer,daywalk,dbapdl,dearmami,debbiemeyer,deconuri,delyvely,dermadog,desart,desimone2022,deviwear,dfcorpo,dhouse,dhzns,diamantista19,digitalartdc,digue,dioderma,discosalon,dixeng,djgirls,dlawltjs,dldnfl3,dm5985,dnaturebio,doadream,dodry1234,dogkb,dogpre,dolbiway,dolphinwhale,donchicken,dongafairs,dongwon,dongwonmall,dori,dorocyshop,dorosiwa,dose79,dr505,drdifferent,dreamsjoseph,drivingschoo,drkimbio,drmeta,drmeta2,drmeta3,drmeta4,drmeta5,drmeta7,droracle,drplinus,dsmaref,dsrieti,dualsonic,dy0118,e1bluesun,earpearp,ebsmid,ecctv,ecocation_2,edisoni,eduwill1,egpis,eh8339,ekdlxk,elagodesign,elgacompany,elim392768,elumcampus,elva0820,elysia950,endskin1,enfp,ennote,envital_m,eocnfqkek01,eomhj,eoseye,epasskorea,eptmqk12,essenlue,etland,etooscenter,eumsae00,eunha,eurohomme,everjoy,eyecatcher,ezfolding2,faple,fastfive,fastlane,fexta,ffull,filip81,fillincell,firstfnc0111,fishingsale,flapaqj,flaqb1,fndnetdf,fodcomm,foming20,foodjang,fortunade,foxwig,funbeerking_,funbiz,fursys1201,gagushow,gagustore,gainge0691,gain_mkt,galleriajigi,galleriaucup,garde9090,gbstyle12345,gbx1016,gd5b1c,gecl,gen3289,generalnet,germen7,giftishow,gilman77,girlsdaily,giv486,gkchair,gksdk1103,globon22,glossbro,glydeofficial,gnh181106,gnmi2812,gntheslim,godak333,gogisang,goldenbo2,goldii,goldinjew,goldkimchi,goldn,goldonehotel,goldplate,goodfair365,goodluck,goodus2019,goqualgoqual,gosicenter,grafen,grandocean,grasshopper,graychic,graycity0510,graymelin,groasting,groominglab5,growcamp,growmkt,gseng777,gtgear,gtsglobal,guylook,hacchina,hackersfn,haedangmedi,hairnmi,halocompany,hamocell,hana9611,handydoc,hangloo,hanool7777,hanoomaul3,hanoomaul4,hanspumpkin,hanssem_on,hanstyle4543,hanwha1,happypack,harvestpick,hashcar1,hatest1,haum1201,hawkseye,hboxkr,hcshcs5445,hdfurni,health22,heartyspoon,hec2725,hecnbio20,heis_mobion,hellen88888,helloboy,hellotv,helloyoonsoo,heodak,heriter,hesol365,hibook,hidiva4454,hifather01,hihijinam,hitlighting,hiwellkorea,hiwellkorea1,hkkorea0113,hlikorea,hnc660,hnsmall2,holia,hollyshop1,honeykiz,hotkimchi2,hotsunglass1,hoyun,hsc2017,hskim523,hubnet2022,huit8,hunetb2b,huni,huonsnatural,huvitled2,hventures,hwlee0922,hy8105,hyfresh2,hyoo,ibookland,idepureun,ifemme,ihanami,ikongcoffee,imagedc,immorta,imon,indongfn,indusweb,inglesina,innerbly,innoplan,insfriends,inus2018,inwonlee,ippngirl,iroa,isoi,itslighting,jadam1,jalsanday,janecourt,jangsu9184,jangys0804,jayhan,jaysbuddy1,jeilleisure,jejussok,jenny1,jeong705,jhind,jikbbang,jinkotec2,jjksports1,jjub0524,jm057,jmpack1,jnjcompany,jnkosmi,joeunmadi,jogunshop,jooyon,josammosa,jsc9674,jscorea,jspacking,jstyle07,jull,junenjuly,justone,justy763050,juvismall,juvis_smart,jwansimfood,jynetwork,k2ci00,kantukan,kclub,kcw150924,kemglab,kemglab_ss,kenifely,kfaiad,khcuac,kimboknam,kimyoung,kingcarwash,kingsmedia,kinosiss,kjpt,kkalmini,kkbag,kkcor,kknd5140,kmaster,kmbio0123,kmjar4799,knchoice,knpinter,koharu,kohonjin,kokos,kolonsport02,komazoncsi,korusec,ksilverex,ksjcup82,kumsolip,kundal1,kwonjinps,kyk6182,kylenhans,lacorvette,ladygagu,landpass,lappkorea,laroom,lawfirmak,lawfirmgammyu,layeon,lctpw0303,leadersfp,leanback,leatherless,leehyojunz,lhr0920,lifehacking90,lifepharm,limebanchan,limkwoo,limu,linkyoues91,lipson01,llavv,lmwshop,loloten,lottegfr,lottegfr02,lovescene2,lsnmall,ltpro,luaronnie,luxyqueen,lylon01,mackus,madamboutique,madamgrace,madformodern,magazinetalk,maison5460,makissue,mamaju,maninstore,manish1912,marieclaire,marieclairead,marketing7,mastideco,maxnchic,mayblue,mbest77,meatbox123,medi1914,medicube,medifferent,medimeplus,mediqb,meditamin,memoryfriend,meosidda_,mgdj,miall,miall_h,micepeople,middleage,miguhara,milovany,mimacmkt,min3111,minagram,minbony,mindprs,minepscom,minishtech,minuet,mirocleanpot,misodameun,miyawa,mi_hyun1020,mjr77,mkload2,mnbsolns,mnbsolns2,mnhouse,mnochoice,mobonhesome,moda,modetourpkg,modoo,modoofarm,mogo1023,mogrami_mobon,mogrami_ss,mokwol22,momnest,momotee,money6777,monica2012,monshop,moonstation22,mopan,mosimwig,mothersall,move1,mrbossam,msearchmkt1,mugem,mulawear,mustit,mychef00,myid332,myoung119,nabbo,nacf1234,nakzi114,nanajean,nanasalon,nanocumin,nanosoft,nan_czpig,napkin,naturalhealth,natureone1,nc_lgcare,neardentwj,neo123,neocock,neoflam,newjangsoo55,newmoris312,newzzle,niceday,nkbiokr,nkmax,nlatex,nnlab,nogoon4780,nonhyun888,noseworkshop,nots,nowinparis,nps,nrjjongro,nsr1,nubizio14,nunevit2,nutri25,nutrione,nymadisondent,n_drwell,oa23,obge,ode,officeplus,ogi1702142,ohmyschool,okmedi22,olens0310,olitcompany,olivegreen,olssen,omegasun,oneflit,onelove,ooafit,opikorea,orangeave,orangeflower,otthonos,ovenmaru,owoo77,p31online,pacc1,packus,pado7799,pagoda21,pagodastar,pakco73,pandatv1,pandatv2,pandatv3,pandck,paolo20,park6742,partykorea,partysu,paseco21,pastelclay,peachalice,pediitn2017,peergen,penekton,pengsamsung,pethooh,petitbabee01,pgarments,pggraphy,phd34,phizigi,pibudan1177,pilot123,pinkelephant,pippin11,pizzahut2,planj,pm2100,point1303,poping2,porkcenter,pple8400,ps2005kr,pureda,qmom,qngirl1,qwer2669,rabbitboy,rainbownatu,rangpang,rangstable,realcoco,ref,rejuran,rekoi,reogagu2,reogagu3,req0849,reskin,rethink,reversegn,reylee12,rhdrk01,rich2449,richwell,rilarila,rnckorea,rntlqdd,rodaenty,rohol,romanson102,romantic,romistory,roniel,rose2014,rovectin,rowonholding,roychestore,rpangel,rudiastory,rudiments,rythmical,saint1801,saltandchoco,samboon,samihun3,samsungcard1,samyangad,sangdogagu,sappira2,sarangplus,sc3011,schooldepot,sdulife,secondeyes,secret02,secretlabel,secretshop,segyero2825,sejinskin,seldilab,seldilab_1,selfish,sennheiser1,seojinps,seoulpacifice,sh9441112,sheisbag,shelton1,shesmadam,shiftshower,shine802,shinsegaels,shinsegaetv,shoetec,shoppulmuone,shopyido,showroom,sid,sidiz1201,silkyhug,simplecm1,sisjin,skb2241,skin1,slimcook,smartooth,smd8275,smhpizza,snbeye1,snowpeak1,snr365,sog9com,sogreat,soim,sojukdoo,solgar1,solus,songam,sonnobed,sonyj925,soonnoc,soopsori,soosoo,soriinc,soulfnu,spash1,speed6177,spoons,spoonsense,ssaka1588,ssamoi32,ssgtv_app,sshyundent2,sslim3531,sslkh70,ssumenam2,starad,stares76,stjhero1,stlsnow,stlyf,stocks_ad,style0008,stylecrew,styledump,styleggom,stylenoriter,styleonme1,sulsungfarm,sultang100g,sumeunmiribul,sungjinhds,superbly,supercar,suplant,supplyroute,surfmall,swtour,syne3153,syspang_mezzo,sy_200jang,sy_alley,sy_hifoodmark,sy_kkoji,sy_yunchobap,taeju38,takart,taling,teabless,ted0486,tekville,thanqseoul,theaesoo,theblessedmoo,thechaeso,thenamjacom,theoreum,theps2,theselfie,theways,the_art20,thinkmen,thinkwarem,threevia,tieut88,tinycosmos,tlswhddnjs13,tlswhddnjs14,tlswhddnjs77,tmdqja1209,tnsdl0111,toph100,topspot,torilight,totals,tourvis1,transurfing,traumitacc,trendchise,tribons,trombeair,trudygirl7,twohim,tycura,tys8620,ugly2018,ultrav_1234,under70,unikorea,uniqlo,unlogistics,urangnatural,urbanlady,urbantheroom,uroman21,uuuk2,uzumarketo,valansis110,valoin,venetmeal,vhdlwms1,vicpetshelter,victoriapet,vipstreet,visangedukr,visangedukr1,visviva,vittz1,vittz2,vivelab,vividcoco,vlifps,voyager,vsinter,wac1212,walch0306,wallplan,waniwani,waskor,wec0,wecan,wegifti,wegifti1,welder75,welkeeps,wellpace,weplus,whatthephone,whdost,wheenara89,wildat,wiprex,wisedental,wisihealthy,withluxury,witkorea,wjsaor,wltns7979,wmtr,wodqkswlq,woman,won132411,wonlog11,workepass1,wowpress,wowvintage,xddesignnss,xsole,yakcho2166,ybmedu,yejimiin,yeorihan,yesfurni,yesimin,yheasin,yhee1751,yk2005,yonseibon,yoojian76,youbyeolna,youngdo90,youngtoysmall,your10x10,yournature,yshop2010,yskseoul,yummiebyht,zaol00,zelda2019,zemmaworld,zinwon_be,zinwon_li,zoozoom,zumdi";
		String adverId = "33tteokbokki7,abana,aippo,ap_sandbox,audwns123,bamo,beready,bluepebbles1,brandepot1,brandholder,bskorea102,caraz7,celligram_s,chosungift,cntdream,daesangdsn,dbrclfdl,edforce1,eoqkrskwk2022,eosika,fishingsale,gooddays,goodrice1,hpsmile0701,hunia76,jdtkorea,kerasal99,khj75758,lguplusb2bmkt,mamaforest,manufaktura,miso-7722,mmbio,mscare,naegapick2022,nanowellshop,newyork1,nnlee,ohocorp,ok-golf,ont-official,organiclab,pobsmbsool,purunsoop,rentalhappy,roadrock1,stickshock,superrice,tabbludens,therapuez,therich,tmcncorp,wearopneg,whgustj66123,yin0310036";
		String[] adverIdlist = adverId.split(",");
		String NAMESPACE = "hHtoDDAdverMTHhhMapper";
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		long resultTime = 0;
		Map<String, Object> updateData = new HashMap<String , Object>();
		// sitecode 를 구하고
		for (String adver : adverIdlist) {

			startTime = System.currentTimeMillis();
			endTime = 0;
			resultTime = 0;
			try {
				updateData = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "getDiffPointData2"), adver);
					// DIff 차이 업데이트
					if (updateData != null &&
							updateData.size() != 0) {
						//logger.info("updateList - {}", updateData.toString());
						if ("ok".equals(updateData.get("ret"))) {
						//	sqlSessionTemplateDreamSearch.update(String.format("%s.%s", NAMESPACE,"updateDiffPointData"), updateData);
						//	sqlSessionTemplateDreamSearch.flushStatements();
							logger.info("UPDATE dreamsearch.admember SET POINT = POINT + {} WHERE userid = '{}'; ", updateData.get("DIFF"), updateData.get("userid"));
						}
					} else {
						//logger.info("Select null AdverId - {}",adver);
					}
				} catch (Exception e) {
				logger.error("updateDiffPointData FAILED - ", e);
			}
			endTime = System.currentTimeMillis();
			resultTime = endTime - startTime;

			//logger.info("updateDiffPointData result time  - {} (ms)", resultTime);
		}
		logger.info("End of Adver Point ");
	}
}
