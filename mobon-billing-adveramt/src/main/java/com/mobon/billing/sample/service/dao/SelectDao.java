package com.mobon.billing.sample.service.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mobon.billing.sample.model.SampleVo;
import com.mobon.billing.sample.model.UseridVo;

@Repository
public class SelectDao {

	private static final Logger	logger				= LoggerFactory.getLogger(SelectDao.class);

	@Resource(name="sqlSessionTemplateBilling")
	private SqlSessionTemplate	sqlSessionTemplateBilling;

	public static final String	NAMESPACE	= "selectMapper";

	public SampleVo selectNow(SampleVo data) {
		
		SampleVo result = sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectNow"), data);
		
		return result;
	}
	
	public ArrayList<SampleVo> selectGroupKey(){
		return (ArrayList) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectGroupKey"));
	}
	
	public List getAdvrtsTpCode() {
		return (List) sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "selectAdvrtsTpCode"));
	}
	
	public Map<String,String> selectMedia(UseridVo useridVo) {
		return sqlSessionTemplateBilling.selectOne(String.format("%s.%s", NAMESPACE, "selectMedia"), useridVo);
	}

}
