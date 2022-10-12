package com.adgather.user.inclinations.memory.simulator;

import org.apache.log4j.Logger;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctShopsCtr;
import com.adgather.user.inclinations.cookieval.inct.old.OldInctShopLog;
import com.adgather.user.inclinations.memory.MemoryObj;

/**
 * CW 샵로그 대행자
 * @author yhlim
 *
 */
public class InctCwSimul extends InctShopsSimul {
	private static final Logger logger = Logger.getLogger(InctCwSimul.class);
	
	public InctCwSimul(MemoryObj<OldInctShopLog> refactMemoryObject) {
		super(refactMemoryObject);
	}
	
	@Override
	protected boolean isTargetShopLog(OldInctShopLog obj) {
		if(obj == null)		return false;
		
		return InctShopsCtr.isCwShopLog(obj);
	}
}
