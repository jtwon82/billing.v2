package com.mobon.billing.branchUM.service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.ExternalLinkageData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.branchUM.service.DataBuilder;
import com.mobon.billing.branchUM.service.SumObjectManager;
import com.mobon.billing.branchUM.service.dao.SelectDao;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class DataBuilder
{
  private static final Logger logger = LoggerFactory.getLogger(DataBuilder.class);

  
  @Autowired
  private SelectDao selectDao;
  
  @Autowired
  private SumObjectManager sumObjectManager;
  
  @Value("${conversion.delay.time.minute}")
  private int convDelayTimeMinute;
  
  @Value("${change.product.adgubun.list}")
  private String changeProductAdgubunList;
  
  @Value("${profile.id}")
  private String profileId;

  
  public void codeMapperX(ClickViewData record) {
    StringTokenizer st1 = new StringTokenizer(this.changeProductAdgubunList, "[");
    while (st1.hasMoreTokens()) {
      StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "]");
      String tmp1 = st2.nextToken();
      String tmp2 = st2.nextToken();
      List<String> adGubunList = Arrays.asList(tmp2.split(","));
      if (adGubunList.contains(record.getAdGubun()))
        record.setProduct(tmp1); 
    } 
  }
  
  public void codeMapperX(ConvData record) {
    StringTokenizer st1 = new StringTokenizer(this.changeProductAdgubunList, "[");
    while (st1.hasMoreTokens()) {
      StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "]");
      String tmp1 = st2.nextToken();
      String tmp2 = st2.nextToken();
      List<String> adGubunList = Arrays.asList(tmp2.split(","));
      if (adGubunList.contains(record.getAdGubun())) {
        record.setProduct(tmp1);
      }
    } 
  }







  
  public int dumpShopStatsData(ShopStatsInfoData data) {
    try {
      if ("mobile".equals(data.getPlatform()));

      
      logger.debug("add list - {}", data.generateKey());
      
      return 0;
    } catch (Exception e) {
      logger.error("err dumpShopStatsData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpSkyClickData(BaseCVData data) throws IOException {
    try {
      data.setType("C");
      data.setClickCnt(1);
      
      if ("sky_m".equals(data.getProduct()) || "mbb"
        .equals(data.getProduct()));


      
      logger.debug("add list - {}", data.generateKey());
      dumpActData(data);
      
      return 0;
    } catch (Exception e) {
      logger.error("err dumpSkyClickData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpSkyChargeData(BaseCVData data) throws IOException {
    try {
      if ("sky_m".equals(data.getProduct()))
      {
        data.setProduct("mbb");
      }
      
      logger.debug("add list - {}", data.generateKey());
      
      dumpActData(data);
      
      return 0;
    }
    catch (Exception e) {
      logger.error("err dumpSkyChargeData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpChargeLogData(BaseCVData data) throws IOException {
    try {
      if (!data.isChargeAble());

      
      if ("ico_m".equals(data.getProduct()) || "m"
        .equals(data.getPlatform())) {
        data.setProduct("mbe");
      }

      
      if (data.getViewCnt() < 1) {
        data.setViewCnt(1);
      }
      
      logger.debug("add list - {}", data.generateKey());
      
      dumpActData(data);
      
      return 0;
    } catch (Exception e) {
      logger.error("err dumpChargeLogData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpDrcData(BaseCVData data) throws IOException {
    int result = 1;
    try {
      logger.debug("drcCharge key - {}", data);
      
      SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
      Date date = new Date();
      String ymd = yyyymmdd.format(date);
      
      String gb = data.getAdGubun();
      String sc = data.getSiteCode();
      boolean clickChk = data.isClickChk();
      
      float point = data.getPoint();
      float mpoint = data.getMpoint();


      
      String chk_key = "banner_" + data.getAdGubun() + "_" + data.getMcgb() + "_" + data.getNo() + "_" + data.getScriptNo() + "_" + data.getKno() + "_" + data.getAdvertiserId();
      if (chk_key.length() > 32) {
        chk_key = chk_key.substring(0, 31);
      }
      data.setKeyCode(chk_key);
      String chkData = "";
      String kno = "";
      if (StringUtils.isNotEmpty(data.getKno()) && data.getKno().length() > 13) {
        kno = data.getKno().substring(0, 13);
      } else {
        kno = data.getKno() + "";
      } 
      data.setYyyymmdd(DateUtils.getDate("yyyyMMdd", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(data.getSendDate())));
      data.setKno(kno);
      data.setType("C");

      // 플러스콜 유효콜 타임이 1초 이상이면 클릭수 증가시키지 않음
      // 플러스콜 DB전환 값이 있으면 클릭수 증가시키지 않음
      // data.setClickCnt(1);		// 기존
      if ("08".equals(G.convertPRDT_CODE(data.getProduct()))
              && (data.getAvalCallTime() >= 1 || data.getDbCnvrsCnt() >= 1)) {		// 변경
          data.setClickCnt(0);
      } else {
          data.setClickCnt(1);
      }
      
      if (data.getScriptNo() > 0 && data.getScriptUserId() != null && data.getScriptUserId().length() > 0) {
        if ("mct".equals(data.getProduct())) {
          result = 0;
        } else if ("mbw".equals(data.getProduct()) || "mba".equals(data.getProduct()) || data
          .getProduct().equals("mba_no_script")) {
          data.setProduct("mbw");
          if (8126 != data.getScriptNo() || (8126 == data.getScriptNo() && clickChk)) {
            logger.debug("add list - {}", data.generateKey());
            result = 0;
          } 
        } else if (!"PE".endsWith(data.getAdGubun()) && !"CA".endsWith(data.getAdGubun())) {
          logger.debug("add list - {}", data.generateKey());
          result = 0;
        } 





        
        if (data.isClickChk()) {

          
          BaseCVData ms = this.selectDao.selectMediaInfo((ClickViewData)data);
          
          if (ms != null) {
            ExternalLinkageData link = new ExternalLinkageData();
            link.setSdate(data.getYyyymmdd());
            link.setUserid(data.getAdvertiserId());
            link.setSite_code(data.getSiteCode());
            link.setGubun("AD");
            link.setMedia_site(data.getMediasiteNo());
            link.setMedia_code(data.getScriptNo());
            link.setMedia_id(data.getScriptUserId());
            link.setAd_type(ms.getAD_TYPE().replace("_", "x"));
            
            link.setType("C");
            link.setClickcnt_mobon(1);
            link.setTransmit("S");
            link.setPoint(data.getMpoint());
            link.setDumpType("externalCharge");
            
            link.setSend_tp_code("03");
            link.setType("SEND");
            link.setHh(data.getHh());
            link.setClickcnt(1);
            ExternalInfoData tmp = link.toExternalInfoData();
            if ("Consumer".equals(this.profileId));
          } 
        } 
      } 

      
      dumpActData(data);
      
      return result;
    } catch (Exception e) {
      logger.error("err dumpDrcData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpNormalViewLogData(BaseCVData data) throws IOException {
    int result = 1;
    try {
      if (StringUtils.isNotEmpty(data.getAdvertiserId()) && !"PE".endsWith(data.getAdGubun()) && !"CA".endsWith(data.getAdGubun()) && 
        !StringUtils.isEmpty(data.getAdGubun())) {
        
        if (data.getViewCnt() < 1) {
          data.setViewCnt(1);
        }
        
        logger.debug("add list - {}", data.generateKey());
        
        dumpActData(data);
        
        result = 0;
      } 
      
      return result;
    } catch (Exception e) {
      logger.error("err dumpNormalViewLogData msg - {}", e);
      return 1;
    } 
  }
  
  public int dumpPlayLinkClickData(BaseCVData data) throws IOException {
    int result = 1;
    
    try {
      data.setClickCnt(1);
      
      dumpActData(data);
      result = 0;
      return result;
    } catch (Exception e) {
      logger.error("err dumpNormalViewLogData msg - {}", e);
      return 1;
    } 
  }
  
  public int dumpPlayLinkChargeData(BaseCVData data) throws IOException {
    int result = 1;
    
    try {
      if ("ad".equalsIgnoreCase(data.getChargeType())) {
        data.setMpoint(0.0F);
      }
      
      if ("media".equalsIgnoreCase(data.getChargeType())) {
        data.setPoint(0.0F);
      }
      
      dumpActData(data);
      
      result = 0;
      
      return result;
    } catch (Exception e) {
      logger.error("err dumpNormalViewLogData msg - {}", e);
      return 1;
    } 
  }








  
  public int dumpMobileChargeLogData(BaseCVData data) throws IOException {
    try {
      logger.debug("add list - {}", data.generateKey());
      
      dumpActData(data);
      
      return 0;
    } catch (Exception e) {
      logger.error("err dumpMobileChargeLogData msg - {}", e);
      return 1;
    } 
  }






  
  public int dumpShopConData(BaseCVData data) throws IOException {
    try {
      SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
      Date date = new Date();
      String ymd = yyyymmdd.format(date);
      
      data.setYyyymmdd(DateUtils.getDate("yyyyMMdd", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(data.getSendDate())));
      data.setScriptUserId(data.getScriptUserId());
      data.setPoint(data.getPoint());
      data.setType("C");
      data.setClickCnt(1);
      
      if ("mbw".equals(data.getProduct()));


      
      logger.debug("add list - {}", data.generateKey());
      
      dumpActData(data);
      return 0;
    } catch (Exception e) {
      logger.error("err dumpShopConData msg - {}", e);
      return 1;
    } 
  }
  
  public int dumpRtbDrcData(BaseCVData data) {
    try {
      dumpActData(data);
      
      BaseCVData rtb = this.selectDao.selectRtbInfo(data);
      if (rtb != null) {
        data.setRtbUseMoneyYn(rtb.getRtbUseMoneyYn());
        
        logger.debug("rtb - {}", rtb);
        return 1;
      } 
      return 0;
    }
    catch (Exception e) {
      logger.error("err dumpRtbDrcData ", e);
      return 1;
    } 
  }






  
  public int dumpShortCutData(BaseCVData data) throws IOException {
    int result = 1;
    try {
      data.setPlatform("M");
      data.setProduct("mbw");
      data.setServiceHostId(data.getServiceHostId());
      data.setScriptUserId(data.getScriptUserId());
      
      SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
      Date date = new Date();
      String ymd = yyyymmdd.format(date);
      if ("C".equals(data.getType())) {
        String chk_key = "";
        data.setKeyCode(chk_key);
        
        String chkData = "";
        if (data.getYyyymmdd() != null && "".equals(data.getYyyymmdd())) {
          data.setYyyymmdd(DateUtils.getDate("yyyyMMdd", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(data.getSendDate())));
        }
        data.setType("C");
        data.setClickCnt(1);

        
        if (chkData == null || chkData.length() < 1) {
          logger.debug("add list - {}", data.generateKey());
          
          result = 0;
        } 


        
        dumpActData(data);
      } else {
        
        logger.debug("type is not click");
      } 
    } catch (Exception e) {
      logger.error("err dumpShortCutData 2 msg - {}", e);
    } 
    return result;
  }












  
  public int dumpActData(BaseCVData data) {
    try {
      logger.debug("data-{}", data);
      if ("N".equals(data.getStatYn())) {
        logger.info("chking adfit else {}", data);
        return 1;
      } 
      if ("ConsumerBranchAction".equals(this.profileId)) {
        
        if (!"C".equals(data.getType()) && 
          !"mbb".equals(data.getProduct()) && 
          !"ico".equals(data.getProduct()) && 
          !"mbe".equals(data.getProduct()) && 
          !"trueman75".equals(data.getAdvertiserId())) {
          return 1;
        }
        
        if (StringUtils.isEmpty(data.getKno())) {
          data.setKno("0");
        }
        logger.debug("actionlog data - {}", data);

        
        HashMap<String, Object> map = new HashMap<>();
        map.put("yyyymmdd", DateUtils.getDate("yyyyMMdd", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(data.getSendDate())));
        map.put("keyIp", data.getKeyIp());
        map.put("pCode", data.getpCode());
        map.put("shoplogNo", Long.valueOf(StringUtils.longCast(Long.valueOf(data.getNo()))));
        map.put("siteCode", data.getSiteCode());
        map.put("advertiserId", data.getAdvertiserId());
        map.put("scriptNo", Integer.valueOf(data.getScriptNo()));
        map.put("scriptUserId", data.getScriptUserId());
        
        if (StringUtils.isNotEmpty(data.getKno()) && data.getKno().length() > 13) {
          map.put("kno", data.getKno().substring(0, 13));
        } else {
          map.put("kno", data.getKno() + "");
        } 
        if ("V".equals(data.getType())) {
          map.put("vcnt", Integer.valueOf(data.getViewCnt2() + 1));
          map.put("vcnt2", Integer.valueOf(data.getViewCnt2()));
          map.put("ccnt", Integer.valueOf(0));
        } else if ("C".equals(data.getType())) {
          map.put("vcnt", Integer.valueOf(0));
          map.put("vcnt2", Integer.valueOf(0));
          map.put("ccnt", Integer.valueOf(1));
        } 
        map.put("point", Float.valueOf(data.getPoint()));
        map.put("actGubun", data.getType());
        map.put("adGubun", data.getAdGubun());
        map.put("product", data.getProduct());
        map.put("mcgb", data.getMcgb());
        map.put("sendDate", data.getSendDate());
        map.put("bHandlingStatsMobon", Boolean.valueOf(data.isbHandlingStatsMobon()));
        map.put("bHandlingStatsPointMobon", Boolean.valueOf(data.isbHandlingStatsPointMobon()));
        
        if ("ico".equals(map.get("product")) || "mbe".equals(map.get("product"))) {
          map.put("actGubun", "C");
        }
        else if ("floating".equals(map.get("product"))) {
          map.put("product", "nor");
        } 

        
        if ("mbe".equals(data.getProduct()));







        
        logger.debug("add actionlog - {}", map);
        ActionLogData actionVo = ActionLogData.fromHashMap(map);
        //this.sumObjectManager.appendData(actionVo, true);


        
        if (!"0".equals(data.getKwrdSeq())) {
          data.setIntgYn("Y");
        }
        if ("Y".equals(data.getFromApp())) {
          data.setIntgYn("Y");
        }
        if (!"0".equals(data.getAdcSeq())) {
          data.setIntgYn("Y");
        }
        if ("Y".equals(data.getCrossbrYn())) {
          data.setIntgYn("Y");
        }
        if (data.gettTime() != 0) {
          data.setIntgYn("Y");
        }
        if ("Y".equals(data.getCtgrYn())) {
          data.setIntgYn("Y");
          try {
            long l = Long.parseLong(data.getCtgrNo());
          } catch (Exception e) {
            data.setIntgYn("N");
          } 
        } 
        if ("UM".equals(data.getAdGubun())) {
          data.setIntgYn("Y");
        }
        if ("Y".equals(data.getIntgYn())) {






          
          map.put("intgYn", data.getIntgYn());
          map.put("crossbrYn", data.getCrossbrYn());
          map.put("fromApp", data.getFromApp());
          map.put("kwrdSeq", data.getKwrdSeq());
          map.put("adcSeq", data.getAdcSeq());
          map.put("tTime", Integer.valueOf(data.gettTime()));
          map.put("ctgrNo", data.getCtgrNo());
          map.put("ctgrYn", data.getCtgrYn());
          map.put("UM", data.getKno());
          map.put("chrgTpCode", "01");
          map.put("svcTpCode", this.selectDao.selectMobonComCodeAdvrtsPrdtCode(data.getProduct()));
          map.put("advrtsTpCode", this.selectDao.convertAdvrtsTpCode(data.getAdGubun()));
          logger.debug("map data - {}", map);
          
          ActionLogData vo = ActionLogData.fromHashMap(map);
          if (!"0".equals(vo.getKwrdSeq())) vo.getIntgSeq().put("kwrd", data.getKwrdSeq()); 
          if ("Y".equals(vo.getFromApp())) vo.getIntgSeq().put("fromApp", "0"); 
          if (!"0".equals(vo.getAdcSeq())) vo.getIntgSeq().put("audience", data.getAdcSeq()); 
          if ("Y".equals(vo.getCrossbrYn())) vo.getIntgSeq().put("crossbrYn", "0"); 
          if (vo.gettTime() != 0) vo.getIntgSeq().put("tTime", data.gettTime() + ""); 
          if ("Y".equals(vo.getCtgrYn())) vo.getIntgSeq().put("category", data.getCtgrNo()); 
          if ("UM".equals(vo.getAdGubun())) vo.getIntgSeq().put("UM", data.getKno());
          
          vo.setIntgLogCnt(vo.getIntgSeq().entrySet().size());
          logger.debug("intgSeq.size - {}, intgSeq - {}", Integer.valueOf(vo.getIntgSeq().entrySet().size()), vo.getIntgSeq());
          
          if (vo.getIntgSeq().size() > 0) {
            //this.sumObjectManager.appendIntgCntrActionLogData(vo);
          }
        } 
      } 
      
      return 0;
    } catch (Exception e) {
      logger.error("err dumpActData data - {}", data, e);
      return 1;
    } 
  }




  
  public void dumpExternalBatch(ExternalInfoData data) {
    if (StringUtils.isEmpty(data.getPlatform()) || StringUtils.isEmpty(data.getProduct())) {
      BaseCVData vo2 = data.toBaseCVData();
      BaseCVData minfo = this.selectDao.selectMediaInfo((ClickViewData)vo2);
      if (minfo != null && 
        StringUtils.isEmpty(data.getPlatform()))
        data.setPlatform(StringUtils.trimToNull2(minfo.getPlatform(), "m").toUpperCase()); 
    } 
  }
}
