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
 * Integer의 리스트 동기화 관리객체
 * (intger 리스트는 고정 크키와 추가/변경/삭제에 position을 이용한다, position 이용을 위해 IntegerEntry객체를 사용한다.) 
 * @author yhlim
 *
 */
public class IntegerSyncList extends SyncList<Integer> {
	private static final long serialVersionUID = 241307448393838520L;

	public IntegerSyncList(String delimeter) {
		super(delimeter);
	}
	
	public IntegerSyncList(String delimeter, long curTime, long syncTime) {
		super(delimeter);
		this.curTime = curTime;
		this.syncTime = syncTime;
	}
	
	
	@Override
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception {
		clear();
		
		String[] stepOne = splitStepOne(cookieValue);
		String[] stepTwo = null;
		
		if(stepOne.length == 1) {		/* syncTime이 없는 경우*/
			stepTwo = splitStepTwo(stepOne[0]);
			this.syncTime = 0;
		} else {
			stepTwo = splitStepTwo(stepOne[1]);
			this.syncTime = NumberUtils.toLong(stepOne[0], 0);
		}
		
		for (String valElement : stepTwo) {
			if (StringUtils.isEmpty(valElement))	continue;
			add(NumberUtils.toInt(valElement, 0)); 
		}
	}
	
	@Override
	public String getCookieValue(final int maxLen, final CookieDef cookieDef) throws Exception {
		final int cnt = size();
		if(cnt == 0)		return null;
		
		StringBuffer buf = new StringBuffer();
		if(CIFunctionController.isRefactFormat()) {
			buf.append(String.format("%d$", syncTime));
		} else  {
			buf.append(String.format("(%d)", syncTime));
		}
		for (int idx = 0; idx < cnt; idx++) {
			Integer obj = get(idx);
			if(obj == null)		continue;
			
			buf.append(String.valueOf(obj)).append(DELIMETER);
			if(maxLen > 0 && buf.length() > maxLen) break;
		}
		
		if (maxLen > 0 && buf.length() > maxLen) {
			int lastIdx = buf.lastIndexOf(DELIMETER, maxLen);
			if (lastIdx > -1) {
				buf.setLength(lastIdx);
			}
		}
		
		if(buf.length() == 0)		return null;
		
		return buf.toString();
	}
	
	@Override
	public void setMongoValue(Object mongoObj, final CookieDef cookieDef) throws Exception {
		if(!(mongoObj instanceof Document))	throw new Exception("Unsupported Object.[" + mongoObj + "]");

		if(cookieDef == null) throw new Exception("CookieDef Is Null.");
		
		clear();

		Document mongoDoc = (Document) mongoObj;
		if(mongoDoc.containsKey("syncTime")) {
			this.syncTime = mongoDoc.getDate("syncTime").getTime();
		}
		
		if(mongoDoc.containsKey("data")) {	
			@SuppressWarnings("unchecked")
			List<Integer> list = mongoDoc.get("data", new ArrayList<Integer>().getClass());
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
	public IntegerSyncList clone() {
		IntegerSyncList newList = new IntegerSyncList(DELIMETER);
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
			Integer e = get(idx);
			if(e == null)	continue;
			
			map.put(String.valueOf(get(idx)), idx);
		}
		return map;
	}

	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
//		if(!(element instanceof IntegerEntry))	return false;
		
		return applyMod(element, bAppendValue);
	}

	@Override
	public boolean applyDel(Object element) {
		if(!(element instanceof IntegerEntry))	return false;
		
		IntegerEntry entry = (IntegerEntry) element;
		initValues(entry.getMaxLength());
		
		if(entry.getPosition() >= size())	return false;
		
		set(entry.getPosition(), 0);
		setNeedUpdateAll(true);
		return true;
	}

	@Override
	public boolean applyMod(Object element, boolean bAppendValue) {
		if (element instanceof IntegerEntry) {
			return applyMod((IntegerEntry)element, bAppendValue);
		} else if (element instanceof int[]) {
			return applyMod((int[])element, bAppendValue);
		}
		return false;
	}
	
	private boolean applyMod(IntegerEntry entry, boolean bAppendValue) {
		initValues(entry.getMaxLength());

		if(entry.getPosition() >= size())	return false;
		
		if(bAppendValue) {
			set(entry.getPosition(), entry.getValue() + get(entry.getPosition()) );
				
		} else {
			set(entry.getPosition(), entry.getValue());
		}

		setNeedUpdateAll(true);
		return true;
	}
	
	private boolean applyMod(int[] values, boolean bAppendValue) {
		if(values == null)	return false;
		
		initValues(values.length);
		
		for(int idx = 0; idx < values.length; idx++) {
			if(bAppendValue) {
				set(idx, values[idx] + get(idx) );
			} else {
				set(idx, values[idx]);
			}
		}

		setNeedUpdateAll(true);
		return true;
	}
	
	private void initValues(int max) {
		if(size() == max) return;
		
		if(size() < max) {
			for(int idx = size(); idx < max; idx++) {
				add(new Integer(0));
			}
		} else if (size() > max) {
			for(int idx = size()-1; idx > max; idx--) {
				remove(idx);
			}
		}
	}
}
