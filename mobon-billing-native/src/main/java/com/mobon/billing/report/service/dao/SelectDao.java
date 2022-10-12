package com.mobon.billing.report.service.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;

@Repository
public class SelectDao {

	private static final Logger	logger				= LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "selectMapper";

	public BaseCVData selectMediaInfo(ClickViewData data) {
		
		logger.debug("basecv - {}", data);
		
		BaseCVData result = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectMediaInfo"), data);
		
		// AD_TYPE 조회가 안됨 나중에 방법을 찾아서 변경하기
		if( result!=null ) {
			result.setAD_TYPE(result.getType());
			result.setType("");	// 이동시켰으니 삭제
			
			if((null !=result.getPlatform())&&("".equals(result.getPlatform()))) {
				data.setPlatform( result.getPlatform() );
			}
		}
		
		return result;
	}

	public HashMap<Integer, String> selectItlInfo() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<BaseCVData> result = sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectItlInfo"));
		
		for( BaseCVData row : result ) {
			map.put(row.getScriptNo(), row.getScriptUserId());
		}
		
		return map;
	}

	public Map mangoStyle(BaseCVData vo) {
		Map map = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "mangoStyle"), vo);
		return map;
	}
	
	public List<Map> selectExternalUserInfo() {
		return this.sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectExternalUserInfo"));
	}

	public BaseCVData selectRtbInfo(BaseCVData data) {
		BaseCVData result = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectRtbInfo"), data);
		return result;
	}
	
	public BaseCVData selectDaylySucc(BaseCVData data) {
		BaseCVData result = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectDaylySucc"), data);
		return result;
	}

	public ArrayList selectGroupKey() {
		return (ArrayList) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdgubunKey"));
	}

}
