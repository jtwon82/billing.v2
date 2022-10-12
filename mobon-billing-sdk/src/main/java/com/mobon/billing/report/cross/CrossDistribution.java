package com.mobon.billing.report.cross;

import java.util.List;

import net.sf.json.JSONObject;


public class CrossDistribution {

	private ADIDMongo adid = new ADIDMongo();
	private ShopLogMongo shopLog = new ShopLogMongo();
	
	/**
	 * 
	 * 
	 * @param req
	 */
	public void distribution(JSONObject req) {
		
		// adid와 로그인스크립트의 au_id를 수집 조정한다. 
		/**
		 * 
		 * 쿠키 au_id 
		 * 로그인스크립트 아이디 
		 * 
		 */
		List<String> adids = adid.getAUIDs("");  
		shopLog.save(adids, shopLog.merge(adids));   
		
	}
}
