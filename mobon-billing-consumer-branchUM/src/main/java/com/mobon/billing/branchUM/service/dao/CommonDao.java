package com.mobon.billing.branchUM.service.dao;

import com.mobon.billing.branchUM.service.dao.CommonDao;
import java.util.List;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao {
	private static final Logger logger = LoggerFactory.getLogger(CommonDao.class);

	@Resource(name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;

	public static final String NAMESPACE = "commonMapper";

	public List getAdvrtsTpCode() {
		return this.sqlSessionTemplateBilling
				.selectList(String.format("%s.%s", new Object[] { "commonMapper", "selectAdvrtsTpCode" }));
	}
}
