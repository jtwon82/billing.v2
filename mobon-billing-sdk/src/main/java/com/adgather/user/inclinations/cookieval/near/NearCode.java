package com.adgather.user.inclinations.cookieval.near;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.user.inclinations.cookieval.near.ctr.NearCodeCtr;

/**
 * 지역코드 데이터
 * 
 * @author kwseo
 *
 */
public class NearCode implements CookieVal {
	/** values ****************************************/
	private int nearCnt; // 지역코드 횟수
	private String nearCode; // 지역코드 (행정동코드)
	private String updDate;

	/** create method **********************************/
	public NearCode() {
	}

	public NearCode(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}

	public NearCode(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}

	public NearCode(NearCode obj) {
		this.nearCode = obj.nearCode;
		this.nearCnt = obj.nearCnt;
		this.updDate = obj.updDate;
	}

	/** Implements method **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if (cookieValue instanceof String) {
			setCookieValue((String) cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}

	public int getNearCnt() {
		return nearCnt;
	}

	public void setNearCnt(int nearCnt) {
		this.nearCnt = nearCnt;
	}

	public String getNearCode() {
		return nearCode;
	}

	public void setNearCode(String nearCode) {
		this.nearCode = nearCode;
	}

	public String getUpdDate() {
		return updDate;
	}

	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}

	public void setCookieValue(String cookieValue) throws Exception {
		if (StringUtils.isEmpty(cookieValue))
			throw new Exception("Cookie Value Is Empty.");

		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if (strs == null || strs.length < 2)
			throw new Exception("Cookie Value Is Not Validate.");

		this.nearCnt = NumberUtils.toInt(strs[0], 1); 
		this.nearCode = strs[1];
		this.updDate = strs.length >= 3 ? strs[2] : NearCodeCtr.getUpdDate(); // (기존)없을 경우 현재 시간
	}

	@Override
	public Object getCookieValue() {
		return String.format("%d%s%s%s%s", nearCnt, DELIMETER, StringUtils.defaultString(nearCode), DELIMETER,
				StringUtils.defaultString(updDate));
	}

	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if (mongoDoc instanceof Document) {
			setMongoValue((Document) mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}

	public void setMongoValue(Document mongoDoc) throws Exception {
		if (mongoDoc == null)
			throw new Exception("Mongo Value Is Empty.");
		this.nearCnt = mongoDoc.getInteger("nearCnt", 0);
		this.nearCode = mongoDoc.getString("nearCode");

		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : NearCodeCtr.getUpdDate();

	}

	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("nearCnt", this.nearCnt);
		doc.put("nearCode", this.nearCode);
		if (this.updDate != null)
			doc.put("updDate", this.updDate);

		return doc;
	}

	@Override
	public NearCode clone() throws CloneNotSupportedException {
		return new NearCode(this);
	}

	@Override
	public String getKey() {
		return StringUtils.defaultString(nearCode);
	}

	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if (!(element instanceof NearCode))
			return;

		NearCode obj = (NearCode) element;
		if (bAppendValue) {
			this.nearCnt += obj.nearCnt;
		} else {
			this.nearCnt = obj.nearCnt;
		}
		this.updDate = NearCodeCtr.getUpdDate();

	}
}
