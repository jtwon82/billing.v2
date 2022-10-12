package com.mobon.billing.core.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.sun.javafx.binding.StringFormatter;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;

@Repository
public class SelectDao {

	private static final Logger	logger				= LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBillingSIMPLE")
	private SqlSessionTemplate	sqlSessionTemplateBillingSIMPLE;

	public static final String	NAMESPACE	= "selectMapper";

	public Map selectNow() {
		Map result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectNow"));
		return result;
	}
	
	public BaseCVData selectMediaInfo(ClickViewData data) {
		BaseCVData result = null;
		try {
			result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectMediaInfo"), data);
			
			// AD_TYPE 조회가 안됨 나중에 방법을 찾아서 변경하기
			if( result!=null ) {
				result.setAD_TYPE(result.getType());
				result.setType("");	// 이동시켰으니 삭제
				
				if((null !=result.getPlatform())&&("".equals(result.getPlatform()))) {
					data.setPlatform( result.getPlatform() );
				}
			}
		}catch(Exception e) {
			logger.error("err selectMediaInfo scriptNo - {}", data.getScriptNo(), e);
		}
		return result;
	}
	
	public BaseCVData selectMediaScriptChrgInfo(ClickViewData data) {
		BaseCVData result = null;
		
		try {
			result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectMediaScriptChrgInfo"), data);
		}catch(Exception e) {
			logger.error("err selectMediaScriptChrgInfo scriptNo - {}", data.getScriptNo(), e);
		}
		
		return result;
	}

	public HashMap<Integer, String> selectItlInfo() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<BaseCVData> result = sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectItlInfo"));
		
		for( BaseCVData row : result ) {
			map.put(row.getScriptNo(), row.getScriptUserId());
		}
		
		return map;
	}

	public Map mangoStyle(BaseCVData vo) {
		Map map = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "mangoStyle"), vo);
		return map;
	}
	
	public List<Map> selectExternalUserInfo() {
		return this.sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectExternalUserInfo"));
	}

	public BaseCVData selectRtbInfo(BaseCVData data) {
		BaseCVData result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectRtbInfo"), data);
		return result;
	}

	public BaseCVData selectOpenRtbHIRNK_NO(BaseCVData data) {
		BaseCVData result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectOpenRtbHIRNK_NO"), data);
		return result;
	}
	
	public BaseCVData selectDaylySucc(BaseCVData data) {
		BaseCVData result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE, "selectDaylySucc"), data);
		return result;
	}

	public ArrayList selectAdgubunKey() {
		return (ArrayList) sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey"));
	}

	public ArrayList selectAdgubunKey2() {
		return (ArrayList) sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey2"));
	}

	public ArrayList selectAdgubunKey2_EX() {
		return (ArrayList) sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey2_EX"));
	}
	
	public Map<String, String> selectMobonComCode(String CODE_TP_ID) {
		Map<String, String> result = new HashMap();
		try {
			Map param = new HashMap();
			param.put("CODE_TP_ID", CODE_TP_ID);
			
			List<Map> list = this.sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectMobonComCode"), param);
			
			logger.debug("list - {}", list);
			
			for( Map<String, String> row : list ) {
				result.put(row.get("CODE_VAL"), row.get("CODE_ID"));
			}
		}catch(Exception e) {
			logger.error("err selectMobonComCode", e);
		}
		return result;
	}

	public String convertAdvrtsPrdtCodeX(String product) {
		String result = "";
		
		try {
			Map<String,String> advrtsPrdtCode = selectMobonComCode("ADVRTS_PRDT_CODE");
			
			Iterator<Entry<String, String>> it = advrtsPrdtCode.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, String> item = it.next();
				String key = item.getKey();
				if( key.equals(product) ) {
					result = item.getValue();
					break;
				}
			}
		}catch(Exception e) {
			logger.error("err convertAdvrtsPrdtCode product - {}", product, e);
		}
		
		return result;
	}

	public String convertAdvrtsTpCode(String adGubun) {
		String result = "";
		
		try {
			Map<String,String> advrtsTpCode = selectMobonComCode("ADVRTS_TP_CODE");
			
			Iterator<Entry<String, String>> it = advrtsTpCode.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, String> item = it.next();
				String key = item.getKey();
				if( key.equals(adGubun) ) {
					result = item.getValue();
					break;
				}
			}
		}catch(Exception e) {
			logger.error("err convertAdvrtsTpCode product - {}", adGubun, e);
		}
		
		return result;
	}

	public String selectMobonComCodeAdvrtsPrdtCode(String product) {
		String result = "";
		
		try {
			Map<String, String> advrtsPrdtCode = new HashMap();
			List<Map> list = this.sqlSessionTemplateBillingSIMPLE.selectList(String.format("%s.%s", NAMESPACE, "selectMobonComCodeAdvrtsPrdtCode"));
			
			for( Map<String, String> row : list ) {
				advrtsPrdtCode.put(row.get("CODE_VAL"), row.get("CODE_ID"));
			}
			
			Iterator<Entry<String, String>> it = advrtsPrdtCode.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, String> item = it.next();
				String key = item.getKey();
				if( key.equals(product) ) {
					result = item.getValue();
					break;
				}
			}
		}catch(Exception e) {
			logger.error("err selectMobonComCodeAdvrtsPrdtCode product - {}", product, e);
		}
		
		return result;
	}

	//미디어 tp code 리턴
    public String selectMediaTpCode(String scriptNo) {
		String result = "";

		Map<String, Object> param = new HashMap<String , Object>();
		param.put("scriptNo", scriptNo);
		result = sqlSessionTemplateBillingSIMPLE.selectOne(String.format("%s.%s", NAMESPACE,"selectMediaTpCode"), param);

		return result;
    }
}
