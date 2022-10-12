package com.mobon.billing.dao;

import java.util.List;

import com.mobon.billing.vo.DataBaseVo;

public interface DataRecoveryDao {

	Boolean setData(List<DataBaseVo> list, String dbTableName);

}
