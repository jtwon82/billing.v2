package com.mobon.billing.core.service.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.adgather.constants.G;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.ConvData;

@Repository
public class ContributeConvDataDao {

	private static final Logger logger = LoggerFactory.getLogger(ContributeConvDataDao.class);

	public static final String	NAMESPACE	= "convDataMapper";

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateConvBilling;

	@Resource(name="sqlSessionTemplateConvDream")
	private SqlSessionTemplate	sqlSessionTemplateConvDream;

	@Autowired
	private TransactionTemplate transactionTemplate;

	public boolean transectionRuningV3(List<ConvData> aggregateList) {
		List <Map<String, Object>> actionLogList = new ArrayList<Map<String , Object>> ();
		boolean result = false;

		for (ConvData vo : aggregateList) {
			//ActionLog 조회 
			if (vo.isNoExposureYN()){
				//미노출 전환 데이터 인경우 미노출 action 로그 조회 
				actionLogList = sqlSessionTemplateConvDream.selectList(String.format("%s.%s", NAMESPACE, "selectUnExposureActionLogList"), vo);
			}  else {
				//기존 action_log 조회 
				actionLogList = sqlSessionTemplateConvDream.selectList(String.format("%s.%s", NAMESPACE, "selectActionLogList"), vo);				
			}
			if (actionLogList.size() == 0 && vo.getCrossLoginIp() != null) {
				try {
					ArrayList<String> ipList = new ArrayList<String>();
					if( vo.getCrossLoginIp().length() > 2 && vo.getCrossLoginIp().split(",").length > 0 ) {
						JSONArray jsonArray;
						jsonArray = (JSONArray) new JSONParser().parse(vo.getCrossLoginIp());
						for(int i=0;i<jsonArray.size();i++){
							Object jsonObj = jsonArray.get(i);
							ipList.add(jsonObj.toString());
						}
					}
					Map<String,Object> param = new HashMap <String, Object>();
					param.put("keyIp", ipList);
					param.put("advertiserId", vo.getAdvertiserId());
					param.put("partdt", vo.getPartdt());
					param.put("no", vo.getNo());
					actionLogList =  sqlSessionTemplateConvDream.selectList(String.format("%s.%s", NAMESPACE, "selectCrossIpActionLogList"), param);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			logger.info("actionLogList size - {}", actionLogList.size());
			for (Map<String, Object> param : actionLogList) {
				String adGubunCode = G.convertTP_CODE((String) param.get("ADGUBUN"));
				String productCode = G.convertPRDT_CODE((String) param.get("ADPRODUCT"));
				int mediaCode = Integer.parseInt((String) param.get("MEDIA_CODE"));
				String siteCode = (String) param.get("SITECODE");
				String mediaId = (String) param.get("MEDIA_ID");
				String contributeOrdCnt = String.format("%.5f", 1 / (double) actionLogList.size());
				String contributePrice = String.format("%.5f", Integer.parseInt(vo.getPrice()) / (double) actionLogList.size()); 

				vo.setAdGubun(adGubunCode);
				vo.setSiteCode(siteCode);
				vo.setPrdtTpCode(productCode);
				vo.setScriptNo(mediaCode);
				vo.setScriptUserId(mediaId);
				vo.setContributeOrdCnt(new BigDecimal(contributeOrdCnt));
				vo.setContributePrice(new BigDecimal(contributePrice));
				//상품 타겟팅 여부 확인용 메소드
				if (vo.getAdGubun() != null) {
					vo.setTargetYn(G.checkTargetYN(vo.getAdGubun()));
				}
				result = this.transectionRunning (vo);
			}
		}
		return result;
	}

	private boolean transectionRunning(ConvData vo) {
		boolean result = false;

		result = (Boolean) transactionTemplate.execute(new TransactionCallback<Object>() {
			boolean res  = false;
			public Object doInTransaction(TransactionStatus status) {
				long startTime = System.currentTimeMillis();
				if (vo != null) {
					sqlSessionTemplateConvBilling.update(String.format("%s.%s", NAMESPACE, "insertContribuetConvData"), vo);
					sqlSessionTemplateConvBilling.flushStatements();
					logger.info("TR Time (TBRT) Conversion : " + (System.currentTimeMillis() - startTime) + "(ms) ");
					res = true;
				}
				return res;
			}
		});
		return result;
	}
}
