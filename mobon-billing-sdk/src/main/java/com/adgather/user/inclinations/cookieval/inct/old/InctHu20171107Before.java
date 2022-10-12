package com.adgather.user.inclinations.cookieval.inct.old;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.InctHu;


/**
 * HU 방문횟수(성향) 로우 데이터
 * @author yhlim
 *
 */
public class InctHu20171107Before extends InctHu {
	/** create method **********************************/
	public InctHu20171107Before() {}
	
	public InctHu20171107Before(String cookieValue) throws Exception {
		super(cookieValue);
	}
	
	public InctHu20171107Before(Document mongoDoc) throws Exception {
		super(mongoDoc);
	}
	
	public InctHu20171107Before(InctHu obj) {
		super(obj);
	}

	/** Implements method  **********************************/
	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s"
					, getVisitCnt(), DELIMETER
					, StringUtils.defaultString(getDomain()));
	}

	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("visitCnt", getVisitCnt());
		doc.put("domain", getDomain());
		return doc;
	}
	
	@Override
	public InctHu20171107Before clone() throws CloneNotSupportedException {
		return new InctHu20171107Before(this);
	}
}
