package com.mobon.billing.branchUM.service.dao;

import com.mobon.billing.branchUM.service.dao.SelectDao;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SelectDao {
	private static final Logger logger = LoggerFactory.getLogger(SelectDao.class);

	@Resource(name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	public static final String NAMESPACE = "selectMapper";

	public Map selectNow() {
		Map result = (Map) this.sqlSessionTemplateBilling
				.selectOne(String.format("%s.%s", new Object[] { "selectMapper", "selectNow" }));
		return result;
	}

	public BaseCVData selectMediaInfo(ClickViewData data) {
		BaseCVData result = null;
		try {
			result = (BaseCVData) this.sqlSessionTemplateBilling
					.selectOne(String.format("%s.%s", new Object[] { "selectMapper", "selectMediaInfo" }), data);

			if (result != null) {
				result.setAD_TYPE(result.getType());
				result.setType("");

				if (null != result.getPlatform() && "".equals(result.getPlatform())) {
					data.setPlatform(result.getPlatform());
				}
			}
		} catch (Exception e) {
			logger.error("err selectMediaInfo scriptNo - {}", Integer.valueOf(data.getScriptNo()), e);
		}
		return result;
	}

	public BaseCVData selectMediaScriptChrgInfo(ClickViewData data) {
		BaseCVData result = null;

		try {
			result = (BaseCVData) this.sqlSessionTemplateBilling.selectOne(
					String.format("%s.%s", new Object[] { "selectMapper", "selectMediaScriptChrgInfo" }), data);
		} catch (Exception e) {
			logger.error("err selectMediaScriptChrgInfo scriptNo - {}", Integer.valueOf(data.getScriptNo()), e);
		}

		return result;
	}

	public HashMap<Integer, String> selectItlInfo() {
		HashMap<Integer, String> map = new HashMap<>();
		List<BaseCVData> result = this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectItlInfo" }));

		for (BaseCVData row : result) {
			map.put(Integer.valueOf(row.getScriptNo()), row.getScriptUserId());
		}

		return map;
	}

	public Map mangoStyle(BaseCVData vo) {
		Map map = (Map) this.sqlSessionTemplateBilling
				.selectOne(String.format("%s.%s", new Object[] { "selectMapper", "mangoStyle" }), vo);
		return map;
	}

	public List<Map> selectExternalUserInfo() {
		return this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectExternalUserInfo" }));
	}

	public BaseCVData selectRtbInfo(BaseCVData data) {
		BaseCVData result = (BaseCVData) this.sqlSessionTemplateBilling
				.selectOne(String.format("%s.%s", new Object[] { "selectMapper", "selectRtbInfo" }), data);
		return result;
	}

	public BaseCVData selectDaylySucc(BaseCVData data) {
		BaseCVData result = (BaseCVData) this.sqlSessionTemplateBilling
				.selectOne(String.format("%s.%s", new Object[] { "selectMapper", "selectDaylySucc" }), data);
		return result;
	}

	public ArrayList selectAdgubunKey() {
		return (ArrayList) this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectAdgubunKey" }));
	}

	public ArrayList selectAdgubunKey2() {
		return (ArrayList) this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectAdgubunKey2" }));
	}

	public ArrayList selectAdgubunKey2_EX() {
		return (ArrayList) this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectAdgubunKey2_EX" }));
	}

	public Map<String, String> selectMobonComCode(String CODE_TP_ID) {
		Map<String, String> result = new HashMap<>();
		try {
			Map<Object, Object> param = new HashMap<>();
			param.put("CODE_TP_ID", CODE_TP_ID);

			List<Map> list = this.sqlSessionTemplateBilling
					.selectList(String.format("%s.%s", new Object[] { "selectMapper", "selectMobonComCode" }), param);

			logger.debug("list - {}", list);

			for (Map<String, String> row : list) {
				result.put(row.get("CODE_VAL"), row.get("CODE_ID"));
			}
		} catch (Exception e) {
			logger.error("err selectMobonComCode", e);
		}
		return result;
	}

	public String convertAdvrtsPrdtCodeX(String product) {
		String result = "";

		try {
			Map<String, String> advrtsPrdtCode = selectMobonComCode("ADVRTS_PRDT_CODE");

			Iterator<Map.Entry<String, String>> it = advrtsPrdtCode.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> item = it.next();
				String key = item.getKey();
				if (key.equals(product)) {
					result = item.getValue();
					break;
				}
			}
		} catch (Exception e) {
			logger.error("err convertAdvrtsPrdtCode product - {}", product, e);
		}

		return result;
	}

	public String convertAdvrtsTpCode(String adGubun) {
		String result = "";

		try {
			Map<String, String> advrtsTpCode = selectMobonComCode("ADVRTS_TP_CODE");

			Iterator<Map.Entry<String, String>> it = advrtsTpCode.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> item = it.next();
				String key = item.getKey();
				if (key.equals(adGubun)) {
					result = item.getValue();
					break;
				}
			}
		} catch (Exception e) {
			logger.error("err convertAdvrtsTpCode product - {}", adGubun, e);
		}

		return result;
	}

	public String selectMobonComCodeAdvrtsPrdtCode(String product) {
		String result = "";

		try {
			Map<String, String> advrtsPrdtCode = new HashMap<>();
			List<Map> list = this.sqlSessionTemplateBilling.selectList(
					String.format("%s.%s", new Object[] { "selectMapper", "selectMobonComCodeAdvrtsPrdtCode" }));

			for (Map<String, String> row : list) {
				advrtsPrdtCode.put(row.get("CODE_VAL"), row.get("CODE_ID"));
			}

			Iterator<Map.Entry<String, String>> it = advrtsPrdtCode.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> item = it.next();
				String key = item.getKey();
				if (key.equals(product)) {
					result = item.getValue();
					break;
				}
			}
		} catch (Exception e) {
			logger.error("err selectMobonComCodeAdvrtsPrdtCode product - {}", product, e);
		}

		return result;
	}

	public Map<String,Object> selectKpiInfo(ClickViewData vo) {
		Map<String,Object> kpiInfoData = null;
		try {
			kpiInfoData = this.sqlSessionTemplateBilling.selectOne(String.format("%s.%s", new Object[] {"selectMapper","selectKpiStats"}), vo);
		} catch (Exception e) {
			logger.error("err selectKpiInfo produce - {}", vo , e);
		}
		return kpiInfoData;
	}

	public BaseCVData selectNewKpiInfo(BaseCVData vo) {
		try {
			BaseCVData kpiInfoData = this.sqlSessionTemplateBilling.selectOne(String.format("%s.%s", new Object[] {"selectMapper","selectNewKpiStats"}), vo);
//			BaseCVData mInfo= this.sqlSessionTemplateBilling.selectOne(String.format("%s.%s", new Object[] {"selectMapper","selectMediaInfo"}), vo);
			
			if(kpiInfoData!=null)
				vo.setKpiNo( kpiInfoData.getKpiNo() );
//			if(mInfo!=null)
//				vo.setItlTpCode( mInfo.getItlTpCode() );
			
		} catch (Exception e) {
		}
		return vo;
	}
}
