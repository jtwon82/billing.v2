package com.adgather.user.inclinations.cookielist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;

/**
 * String의 리스트 동기화 관리객체
 * @author yhlim
 *
 */
public class StringSyncList extends SyncList<String> {
	private static final long serialVersionUID = 7842337484460712168L;

	public StringSyncList(String delimeter) {
		super(delimeter);
	}
	
	@Override
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");
		
		clear();
		
		String[] stepOne = splitStepOne(cookieValue);
		String[] stepTwo = splitStepTwo(stepOne[1]);
		
		this.syncTime = NumberUtils.toLong(stepOne[0], 0);
		for (String valElement : stepTwo) {
			if (StringUtils.isEmpty(valElement))	continue;
			try {
				if(cookieDef.getCodeConverter() != null) {
					valElement = cookieDef.getCodeConverter().decode(valElement);
				}
				add(valElement);
			} catch(Exception e) {}
		}
	}
	
	@Override
	public String getCookieValue(final int maxLen, CookieDef cookieDef) throws Exception {
		final int cnt = size();
		if(cnt == 0)		return null;
		
		StringBuffer buf = new StringBuffer();
		if(CIFunctionController.isRefactFormat()) {
			buf.append(String.format("%d$", syncTime));
		} else  {
			buf.append(String.format("(%d)", syncTime));
		}
		
		for (int idx = 0; idx < cnt; idx++) {
			String obj = get(idx);
			if(obj == null)		continue;
			
			if(cookieDef.getCodeConverter() != null) {
				obj = cookieDef.getCodeConverter().encode(obj);
			}
			
			buf.append(obj).append(DELIMETER);
			if(maxLen > 0 && buf.length() > maxLen) break;
		}
		
		if(maxLen > 0 && buf.length() > maxLen) {
			final int delimiterLen = DELIMETER.length();
			int lastIdx = buf.lastIndexOf(DELIMETER, maxLen - delimiterLen);
			if (lastIdx > -1) {
				buf.setLength(lastIdx + delimiterLen);
			}
		}
		
		if(buf.length() == 0)		return null;
		
		return buf.toString();
	}
	
	@Override
	public void setMongoValue(Object mongoObj, final CookieDef cookieDef) throws Exception {
		if(!(mongoObj instanceof Document))	throw new Exception("Unsupport Object.[" + mongoObj + "]");
		if(cookieDef == null) throw new Exception("CookieDef Is Null.");
		
		clear();
		
		Document mongoDoc = (Document)mongoObj;
		if(mongoDoc.containsKey("syncTime")) {
			this.syncTime = mongoDoc.getDate("syncTime").getTime();
		}
		
		if(mongoDoc.containsKey("data")) {	
			List<String> list = mongoDoc.get("data", new ArrayList<String>().getClass());
			for (int idx = 0; list != null && idx < list.size(); idx++) {
				add(list.get(idx));
			}	
		}
	}
	
	@Override
	public Object getMongoValue() throws Exception {
		return new Document("syncTime", new Date(syncTime)).append("data", this);
	}
	
	@Override
	public StringSyncList clone() {
		StringSyncList newList = new StringSyncList(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		for (int idx = 0; idx < size(); idx++) {
			newList.add(get(idx));
		}
		
		return newList;
	}
	
	@Override
	public String getKey() {
		return null;
	}
	
	@Override
	public Map<String, Integer> getIdxMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int idx = 0; idx < size(); idx++) {
			String e = get(idx);
			if(e == null)	continue;
			
			map.put(get(idx), idx);
		}
		return map;
	}

	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		if(element == null)		return false;
		
		boolean bMod = applyMod(element, bAppendValue);
		if(!bMod) {
			add(0, (String)element);
		}
		setNeedUpdateAll(true);
		return true;
	}

	@Override
	public boolean applyDel(Object element) {
		if(element == null)		return false;
		
		boolean bRes = false;
		for (int idx = size() - 1; idx > -1 ; idx--) {
			if( element.equals(get(idx)) ) {
				remove(idx);
				bRes = true;
				break;
			}
		}
		setNeedUpdateAll(true);
		return bRes;
	}

	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if(element == null)		return false;
		
		boolean bRes = false;
		for (int idx = 0; idx < size(); idx++) {
			if( element.equals(get(idx)) ) {
				set(idx, (String)element);
				bRes = true;
				break;
			}
		}
		setNeedUpdateAll(true);
		return bRes;
	}

}
