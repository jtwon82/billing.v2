package com.adgather.user.inclinations.cookieval.inct.old;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.InctUm;


/**
 * UM 도메인 로우 데이터
 * @author yhlim
 *
 */
public class InctUm20171107Before extends InctUm {

	/** create method **********************************/
	public InctUm20171107Before() {}
	
	public InctUm20171107Before(String cookieValue) throws Exception {
		super(cookieValue);
	}
	
	public InctUm20171107Before(Document mongoDoc) throws Exception {
		super(mongoDoc);
	}
	
	public InctUm20171107Before(InctUm obj) {
		super(obj);
	}
	
	/** Implements method  **********************************/

	@Override
	public Object getCookieValue() {
		return StringUtils.defaultString(getDomain());
	}

	/** Document 배열 형태의 몽고 값 **/
	@Override
	public Object getMongoValue() {
		return getDomain();
	}
	
	@Override
	public InctUm20171107Before clone() throws CloneNotSupportedException {
		return new InctUm20171107Before(this);
	}
}
