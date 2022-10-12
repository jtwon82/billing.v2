package com.adgather.user.inclinations.cookieval.inct.ctr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctUm;

public class InctUmCtr extends CommonCtr {
	
	public static InctUm createInctUm(String domain) {
		if(StringUtils.isEmpty(domain))		return null;
		
		InctUm obj = new InctUm();
		obj.setDomain(domain);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
	
	public static String[] toDoaminArray(List<InctUm> list) {
		if(list == null || list.size() == 0) 	return null;
		
		String[] domains = new String[list.size()];
		for (int idx = 0; idx < list.size(); idx++) {
			domains[idx] = list.get(idx).getDomain();
		}
		return domains;
	}
	
	public static List<String> toDoaminList(List<InctUm> list) {
		if(list == null || list.size() == 0) 	return null;
		
		List<String> domains = new ArrayList<String>();
		for (int idx = 0; idx < list.size(); idx++) {
			domains.add(list.get(idx).getDomain());
		}
		return domains;
	}
	
	public static String getFromApp(String url, List<InctUm> list) {
		String result = "";
		
		if(list != null && list.size() > 0) {
			for(InctUm inctUm: list) {
				if(inctUm.getDomain().equals(url)) {
					if(inctUm.getFromApp() != null && !inctUm.getFromApp().equals("")) {
						result = inctUm.getFromApp();
					}
					break;
				}
			}
		}
		
		return result;
	}
}
