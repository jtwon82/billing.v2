package com.adgather.user.inclinations.cookieval.inct;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * UM 도메인 로우 데이터
 * @author yhlim
 *
 */
public class InctCate implements CookieVal {
	/** values ****************************************/
	private String category;				// 도메인
	private String target;

	/** create method **********************************/
	public InctCate() {}
	
	public InctCate(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctCate(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctCate(InctCate obj) {
		this.category = obj.category;
		this.target = obj.target;
	}
	
	/** value get/set method **********************************/
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	/** Implements method  **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(cookieValue instanceof String) {
			setCookieValue((String)cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}

	public void setCookieValue(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length < 2)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.category = strs[0];
		this.target = strs[1];
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%s"
					, StringUtils.defaultString(category), DELIMETER
					, StringUtils.defaultString(target));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	/** Document 배열 형태의 몽고 값 **/
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.category = mongoDoc.getString("cate");
		this.target = mongoDoc.getString("target");;
	}	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("cate", this.category);
		doc.put("target", this.target);
		return doc;
	}
	
	@Override
	public InctCate clone() throws CloneNotSupportedException {
		return new InctCate(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(category);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctCate))		return;
		
		this.target = ((InctCate)element).target;
	}
}
