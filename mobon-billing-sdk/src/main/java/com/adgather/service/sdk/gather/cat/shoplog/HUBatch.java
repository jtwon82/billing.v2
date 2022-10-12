package com.adgather.service.sdk.gather.cat.shoplog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.service.sdk.gather.batch.IBatch;
import com.adgather.service.sdk.gather.batch.SourceBean;
import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.minct.MInctHU;
import com.adgather.user.inclinations.cookieval.minct.ctr.MInctHUCtr;

public class HUBatch
					implements
					IBatch {

	protected final static Logger logger = LoggerFactory.getLogger(HUBatch.class);
	
	private IBatch batch = null;

	public HUBatch() {
		// TODO Auto-generated constructor stub
	}

	public HUBatch(
		IBatch batch) {
		// TODO Auto-generated constructor stub
		this.batch = batch;
	}

	public void setBatch(IBatch batch) {
		// TODO Auto-generated constructor stub
		this.batch = batch;
	}

	@Override
	public void batch(Object bean) {
		SourceBean _bean = (SourceBean) bean; 	
		List<String> data = (List<String>) _bean.getData();
		int count = (data.size() > 20) ? 20 : data.size();
		for(int i=0; i<count; i++) {	
			String[] row = data.get(i).split("_");
			MInctHU mhu = new MInctHU();
//			System.out.println(data.toString());
			if(row.length == 2) {
				mhu.setPkg(row[1]);
				mhu.setUpdDate(row[0]);
			} else {
				mhu.setPkg(data.get(i));
				mhu.setUpdDate(MInctHUCtr.getUpdDate());
			}
			
//			if(row.length == 2) {
//				mhu.setPkg(row[1]);
//				String _date = row[0];
//				int size = 12 - _date.length();
//				if(size > 0) {
//					for(int k=0; k<size; k++) {
//						_date += "0";
//					}
//				}
//				mhu.setUpdDate(_date);
//				
//			} else {
//				mhu.setPkg(data.get(i));
//				mhu.setUpdDate(MInctHUCtr.getUpdDate());
//			}
//			mhu.setFromApp("Y");
			_bean.getCi().addCookie(CookieDefRepository.M_HU, mhu, false);
			
			logger.debug(data.get(i) + " ============= " + MInctHUCtr.getUpdDate());
		}
//		_bean.getCi().add
		
		if (this.batch != null) {
			this.batch.batch(bean);
		}
	}
}




