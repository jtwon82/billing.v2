package com.adgather.user.inclinations.cookielist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.adgather.user.inclinations.CIFunctionController;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.user.inclinations.cookieval.inter.Waitable;

/**
 * 새로 구현된 객체(CookieValElement)의 리스트 동기화 관리객체
 * @author yhlim
 *
 * @param <E>
 */
public class ObjectSyncList<E> extends SyncList<E> {
	private static final long serialVersionUID = -8798383082078679102L;
	private static final Logger logger = Logger.getLogger(ObjectSyncList.class);

	public ObjectSyncList(String delimeter) {
		super(delimeter);
	}
	
	public ObjectSyncList(String delimeter, long curTime, long syncTime) {
		super(delimeter);
		this.curTime = curTime;
		this.syncTime = syncTime;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setCookieValue(String cookieValue, final CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");
		
		final Object emptyObj = cookieDef.newObj();
		if(emptyObj == null)		throw new Exception("EmptyObject Is Not Setting.");

		clear();
		
		String[] stepOne = splitStepOne(cookieValue);
		String[] stepTwo = splitStepTwo(stepOne[1]);
		
		this.syncTime = NumberUtils.toLong(stepOne[0], 0);
		for (String valElement : stepTwo) {
			try {
				if(cookieDef.getCodeConverter() != null) {
					valElement = cookieDef.getCodeConverter().decode(valElement);
				}
				
				Object newObj = cookieDef.newObj();
				if(!(newObj instanceof CookieVal))	throw new Exception("This EmptyObject Is Not Support.["+newObj+"]");
				
				((CookieVal)newObj).setCookieValue(valElement);
				add((E)newObj);
			} catch (Exception e) {
				e.printStackTrace();
				//logger.error(ErrorLog.getStack(e));
			}
		}
	}
	
	@Override
	public String getCookieValue(final int maxLen, CookieDef cookieDef) throws Exception {
		if(cookieDef == null)	throw new Exception("CookieDef Is Null.");
		
		final int cnt = size();
		if(cnt == 0)		return null;
		
		StringBuffer buf = new StringBuffer();
		if(CIFunctionController.isRefactFormat()) {
			buf.append(String.format("%d$", syncTime));
		} else  {
			buf.append(String.format("(%d)", syncTime));
		}
		for (int idx = 0; idx < cnt; idx++) {
			E obj = get(idx);
			if(obj == null)		continue;
			
			if(Waitable.isWaitData(obj, getCurTime())) {
				continue;
			}
			
			String valElement = (String)((CookieVal)obj).getCookieValue();
			if(cookieDef.getCodeConverter() != null) {
				valElement = cookieDef.getCodeConverter().encode(valElement);
			}
			
			buf.append(valElement).append(DELIMETER);
			if(maxLen > 0 && buf.length() > maxLen) break;
		}
		
		if(maxLen > 0 && buf.length() > maxLen) {
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
			List<Document> list = mongoDoc.get("data", new ArrayList<Document>().getClass());
			for (int idx = 0; list != null && idx < list.size(); idx++) {
				try {
					Object newObj = cookieDef.newObj();
					if(!(newObj instanceof CookieVal))	throw new Exception("This EmptyObject Is Not Support.["+newObj+"]");
					
					((CookieVal)newObj).setMongoValue(list.get(idx));
					add((E)newObj);
				}catch (Exception e) {}
			}
		}
	}
	
	@Override
	public Object getMongoValue() throws Exception {
		List<Object> list = new ArrayList<>();
		for (int idx = 0; idx < size(); idx++) {
			list.add(((CookieVal)get(idx)).getMongoValue());
		}
		Document doc = new Document("syncTime", new Date(syncTime)).append("data", list);
		
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ObjectSyncList<E> clone() {
		ObjectSyncList<E> newList = new ObjectSyncList<E>(DELIMETER);
		newList.setNeedUpdate(this);
		newList.setCurTime(this.curTime);
		newList.setSyncTime(this.syncTime);
		
		try {
			for (int idx = 0; idx < size(); idx++) {
				newList.add((E)((CookieVal)get(idx)).clone());
			}
		} catch (Exception e) {}
		
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
			E e = get(idx);
			if(e == null)	continue;
			
			map.put(((CookieVal)get(idx)).getKey(), idx);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean applyAdd(Object element, boolean bAppendValue, int maxCnt) {
		if(element == null)		return false;
		
		boolean bMod = applyMod(element, bAppendValue);
		if(!bMod) {
			add(0, (E)element);
		}
		setNeedUpdateAll(true);
		return true;
	}

	@Override
	public boolean applyDel(Object element) {
		if(element == null)		return false;
		
		boolean bRes = false;
		String key = ((CookieVal)element).getKey();
		for (int idx = size() - 1; idx > -1 ; idx--) {
			CookieVal obj = (CookieVal)get(idx);
			if( key.equals(obj.getKey()) ) {
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
		String key = ((CookieVal)element).getKey();
		for (int idx = 0; idx < size(); idx++) {
			CookieVal obj = (CookieVal)get(idx);
			if ( key.equals(obj.getKey()) ) {
				obj.modValue(element, bAppendValue);
				bRes = true;
				break;
			}
		}
		setNeedUpdateAll(true);
		return bRes;
	}
}