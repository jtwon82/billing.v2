package com.adgather.util.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adgather.constants.old.GlobalConstants;
import com.adgather.reportmodel.old.AdConfigData;
import com.adgather.reportmodel.old.MediaScriptData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @date 2017. 3. 6.
 * @param 
 * @exception
 * @see
 */
public class StringUtils{
	
//	public static final void main(String [] ar){
//	    String PAY_COM_LIST = "tx.allatpay.com,pay.ssg.com,mup.mobilians.co.kr,pay.naver.com,cjmall.com:web[30]mob[30]";
//	    String sss = "dreamsearch.or.kr(20170228120613)|dreamsearch.or.kr(20170228120612)|dreamsearch.or.kr(20170227184604)|dreamsearch.or.kr(20170224115817)|dreamsearch.or.kr(20170224115815)";
//	    List list = StringUtils.patternMatch("[0-9]{14}", sss);
////	    System.out.println(list);
////	    
////	    System.out.println( StringUtils.getAttribute(PAY_COM_LIST.split(",")[3], "web"));
//	    
//	    System.out.println( Integer.parseInt("1") );
//	}
	
	public static String getValue(JSONObject org, String key) {
		try {
			return org.get(key).toString();
		}catch(Exception e) {
			return "";
		}
	}
	
	public static String trimToNull2(Object obj) {
		if( obj==null ) {
			return "";
		} else {
			return org.apache.commons.lang.StringUtils.trimToNull(obj.toString());
		}
	}

	public static String trimToNull2(Object obj, String defaultValue) {
		if( obj==null ) {
			return defaultValue;
		} else if ( "".equals(obj.toString().trim()) ) {
			return defaultValue;
		} else {
			return org.apache.commons.lang.StringUtils.trimToNull(obj.toString());
		}
	}


	public static String buldString(String ... ar){
		return String.join(GlobalConstants.GUBUN, ar);
	}
	
  /**
   * name[value]name2[value2] ????????? ?????????????????? name????????? ?????? ??????
   * 
   * @param str
   * @param name
   * @return String
   */
  public static String getAttribute(String str, String name) {
    String result = "";
    try {
      StringTokenizer token = new StringTokenizer(str, "]");

      while (token.hasMoreTokens()) {
        StringTokenizer token2 = new StringTokenizer(token.nextToken(), "[");
        while (token2.hasMoreTokens()) {
          try {
            String key = token2.nextToken();
            String next_value = token2.nextToken();
            if (key.indexOf(":") > -1) {
              key = key.split(":")[1];
            }
            if (name.equals(key)) {
              result = next_value;
              break;
            }
          } catch (NoSuchElementException e) {
          }
        }
      }
    } catch (Exception e) {
    }

    return result;
  }

  /**
   * ???????????????????????? List ?????????
   * 
   * @param pattern
   * @param compString
   * @return list
   */
  public static List<String> patternMatch(String pattern, String compString) {
    List<String> list = new ArrayList<String>();
    try {
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(compString);
      while (m.find()) {
        list.add(m.group(0));
      }
      if (list.size() <= 0) {
        list.add("");
      }
    } catch (Exception e) {
    }
    return list;
  }
	


	private static final Pattern patternSpecialCharater = Pattern.compile("[^\uAC00-\uD7A30-9a-zA-Z\\s]");
	/**
	 * ???????????? ?????? ??????
	 * @param str
	 * @return
	 */
	public static synchronized String specialCharacterReplace(String str){
		return patternSpecialCharater.matcher(str).replaceAll("\u0020"); // space
	} // specialCharacterReplace()

	public static String getSqlDtIn(String sDate,String eDate){
		String rtn="";
		sDate=sDate.replace("-","");
		eDate=eDate.replace("-","");
		String orgSdate=sDate;
		DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
		java.util.Date sdate=null;
		java.util.Date edate=null;
		try{
			sdate=dateFormat.parse(sDate);
			edate=dateFormat.parse(eDate);
			Calendar sCal=Calendar.getInstance();
			Calendar eCal=Calendar.getInstance();
			sCal.setTime(sdate);
			eCal.setTime(edate);
			int cnt=0;
			String inStr="(";
			while(sCal.getTimeInMillis()<=eCal.getTimeInMillis()){
				java.util.Date tmpdate=new Date();
				tmpdate.setTime(sCal.getTimeInMillis());
				inStr+="'"+dateFormat.format(tmpdate)+"',";
				sCal.add(Calendar.DAY_OF_MONTH,1);
				cnt++;
			}
			if(cnt>0){
				inStr=inStr.substring(0,inStr.length()-1)+")";
				if(cnt>100){
					inStr=" between '"+orgSdate+"' and '"+eDate+"'";
				}
			}else{
				inStr="";
			}
			rtn=inStr;
		}catch(Exception e){
		}
		return rtn;
	}

//	public static String getFreeMakerData(String dir, String file, Map<?, ?> map,String charset) throws Exception { // not used
//		charset = org.apache.commons.lang3.StringUtils.defaultIfEmpty(charset, "utf-8");
//		String content = "";
//		try	{
//			Configuration cfg = new Configuration();
//			cfg.setDirectoryForTemplateLoading(new File(dir));
//			cfg.setObjectWrapper(new DefaultObjectWrapper());
//			cfg.setDefaultEncoding(charset);
//			cfg.setEncoding(Locale.KOREAN, charset);
//			cfg.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:50, soft:250");
//			cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
//			Template temp = cfg.getTemplate(file);
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			Writer output = new OutputStreamWriter(os, charset);
//			try {
//				temp.process(map, output);
//			} catch (TemplateException e) {
//				logger.error("getFreeMakerData1:"+e);
//			}
//			output.flush();
//			output.close();
//			os.close();
//			map.clear();
//			content = os.toString(charset);
//		}catch (Exception e) {
//			logger.error("getFreeMakerData2:"+e);
//		}
//		return content;
//	}

//	public static String getFreeMakerData(Configuration cfg,String file, Map<String,Object> map,String charset) throws Exception { // not used
//		charset = org.apache.commons.lang3.StringUtils.defaultIfEmpty(charset, "utf-8");
//		String content = "";
//		try	{
//			cfg.setDefaultEncoding(charset);
//			cfg.setEncoding(Locale.KOREAN,charset);
//			Template temp = cfg.getTemplate(file);
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			Writer output = new OutputStreamWriter(os, charset);
//			try {
//				temp.process(map, output);
//			} catch (TemplateException e) {
//				logger.error("getFreeMakerData3:"+e);
//			}
//			output.flush();
//			output.close();
//			os.close();
//			map.clear();
//			content = os.toString(charset);
//		}catch (Exception e) {
//			logger.error("getFreeMakerData4:"+e);
//		}
//		return content;
//	}

	public static String getConvertString(HashMap<String,String> h, String div){
		String result="";

		for(String a :h.keySet() )
			result += a + div;

		return result;
	}

	public static String getReg(String reg, String str_in){
		String a = str_in;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(a);
		String f = "";
		int breakchk=0;
		while( m.find() ){
			f = ( m.group(0) );
			breakchk++;
			if(breakchk>10000) break;
		}
		return f;
	}

    public static String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot) { // ????????? ?????????
        String r_val = szText;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE); // ???????????? ??????
        if (isNotag) {
            r_val = p.matcher(r_val).replaceAll("");
        } // ?????? ??????
        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", ""); // ????????????
        try {
            byte[] bytes = r_val.getBytes("UTF-8"); // ???????????? ??????
            if (StringUtils.isNotEmpty(szKey)) {
                nLengthPrev = (r_val.indexOf(szKey) == -1) ? 0 : r_val.indexOf(szKey); // ?????? ????????????
                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length; // ????????????????????? byte??? ?????? ?????????
                nLengthPrev = (nLengthPrev - nPrev >= 0) ? nLengthPrev - nPrev : 0; // ??? ??????????????? ?????????????????????.
            }
            // x?????? y???????????? ????????????. ??????????????????.
            int j = 0;
            if (nLengthPrev > 0)
                while (j < bytes.length) {
                    if ((bytes[j] & 0x80) != 0) {
                        oF += 2;
                        rF += 3;
                        if (oF + 2 > nLengthPrev) {
                            break;
                        }
                        j += 3;
                    } else {
                        if (oF + 1 > nLengthPrev) {
                            break;
                        }
                        ++oF;
                        ++rF;
                        ++j;
                    }
                }
            j = rF;
            while (j < bytes.length) {
                if ((bytes[j] & 0x80) != 0) {
                    if (oL + 2 > nLength) {
                        break;
                    }
                    oL += 2;
                    rL += 3;
                    j += 3;
                } else {
                    if (oL + 1 > nLength) {
                        break;
                    }
                    ++oL;
                    ++rL;
                    ++j;
                }
            }
            r_val = new String(bytes, rF, rL, "UTF-8"); // charset ??????
            if (isAdddot && rF + rL + 3 <= bytes.length) {
                r_val += "...";
            } // ...??? ??????????????? ??????
        } catch (Exception e) {
        }
        return r_val;
    }

    public static Stack<String> gListStack(String o_list){
		Stack<String> stack=new Stack<String>();
		StringTokenizer st=new StringTokenizer(o_list,"|||",false);
		while(st.hasMoreElements()){
			String sel_o_list=st.nextElement().toString();
			stack.push(sel_o_list);
		}
		return stack;
	}

	public static int gAtCnt(String str1, String ar){
		StringTokenizer st= new StringTokenizer(str1, ar);
		int id= -1;
		while( st.hasMoreElements() ){
			st.nextToken();
			id++;
		}
		return id;
	}

	/** item ??? item ?????? ??? ????????? ????????????.
	 * item ????????? ?????? item??? ????????????, ?????? ????????? item??? ??? ???????????? ????????????.
	 *
	 * @param url_list
	 * @param item
	 * @param ar
	 * @return
	 */
	public static String gAtFirst(String url_list, String item, String ar){
		String rtval = "";
		String rt1 = "";
		if( url_list.indexOf(item)>-1 ){ // item ???????????? url_list ??? ????????????
			StringTokenizer st= new StringTokenizer(url_list, ar); // url_list??? ar ???????????? ????????????.
			while( st.hasMoreElements() ){  // item ??? ????????? ????????? url ???  [?????????]+[url] ???????????? ???????????????..
				String b= st.nextToken();
				if( b.equals(item) ){
				}else{
					rt1 += ar + b;
				}
			}
			rt1 = item + rt1;
			url_list = rt1;
		}else{
			rt1 = item + ar + url_list;
		}
		rtval= rt1;
		return rtval;
	}

	/** item ??? ?????? ??? ????????? ????????????.<p>
	 * ?????? ????????? item??? ?????? ????????????, ?????? ????????? item??? ??? ???????????? ????????????. (by yseun)<br>
	 * gAtFirst() ?????? : (1) ???????????? ???????????? ?????? ????????? ??????, (2) Item ?????? ????????? ??????, (3) ????????? ?????? ????????? ??????
	 * addFirst() ?????? : (1) ????????? ?????? ????????? ??????
	 *
	 * @param sItemList ?????? Item ????????? ?????????
	 * @param sItem ????????? Item
	 * @param sSep ItemList ?????? Item ???????????? ????????? (Separator)
	 * @author yseun (2015/11/20)
	 * @return ????????? Item ?????????
	 */
	public static String addFirst(String sItemList, String sItem, String sSep)
	{
		while (sItemList.startsWith(sSep)) { // ?????? Item List ?????? ???????????? ????????? ?????????.
			sItemList = sItemList.substring(sSep.length());
		} // while
		while (sItemList.endsWith(sSep)) { // ?????? Item List ?????? ???????????? ????????? ?????????.
			sItemList = sItemList.substring(0, sItemList.length() - sSep.length());
		} // while
		if (sItemList.indexOf(sItem) == -1) { // ????????? item ???????????? ???????????? ?????????, ??? ?????? item ???????????? ??????
			return sItemList.length() == 0 ? sItem : String.format("%s"+ sSep +"%s", sItem, sItemList);
		} else { // ????????? item ???????????? ????????????
			// ????????? item ????????? ??????????????? ???????????????
			final StringBuilder sb = new StringBuilder(sItemList.length() + sItem.length() + sSep.length() * 2);
			sb.append(sItem).append(sSep).append(sItemList).append(sSep);
			final String sItemToFind = String.format(sSep +"%s"+ sSep, sItem); // item ????????? ???????????? ????????? ??????
			final int idx = sb.indexOf(sItemToFind, sItem.length());
			if (idx == -1) { // ?????? ????????? ???????????? Item ??? ??????.
				return sb.substring(0, sb.length() - sSep.length());
			} else if (idx == sItem.length()) { // ?????? Item ????????? ??? ????????? ??????????????? Item ??????. (???????????? ??????)
				return sItemList;
			} else { // ?????? ????????? Item ??? ????????????.
				sb.delete(idx, idx + sItemToFind.length() - sSep.length()); // ?????? item ??????
				return sb.substring(0, sb.length() - sSep.length());
			} // if
		} // if
	} // addFirst()

	/**
	 * 2015 04 20 ?????????
	 * @param list
	 * @param cmp
	 * @param item ????????? ?????????
	 * @param divi  list, cmp ??? ?????????
	 * @param dfval ?????? ?????? ??? ?????????
	 * @return
	 */
	public static String gAtData(String list, String cmp, String item, String divi, String dfval){
		String rt= "";

		if (StringUtils.isEmpty(item)) {
			rt = dfval;
		} else {
			int t = StringUtils.gAtId(cmp, item, divi);
			if( t>-1 ){
				rt = StringUtils.gAt1(list, t, divi);
			}
		}

		if( StringUtils.isEmpty(rt) ) rt = dfval;

		return rt;
	}

	public static String gAtData(String list, String cmp, String at, String divi){
		String rt= "";

		int t = StringUtils.gAtId(cmp, at, divi);

		if( t>-1 ){
			rt = StringUtils.gAt1(list, t, divi);
		}

		return rt;
	}
	/** 2015 04 20 ?????????
	 * index ?????? ?????? , ?????? ?????? ????????? -1??? ?????????.
	 * ex) gAtId("a,b,c", "e", ",") -> -1
	 * ex) gAtId("a,b,c", "a", ",") -> 0
	 * ex) gAtId("a,b,c", "b", ",") -> 1
	 *
	 * for ??? ?????? ???????????? ????????? ?????? ?????? return ????????? ?????? by min
	 *
	 * @param list
	 * @param item
	 * @param ar
	 * @return
	 */
	public static int gAtId(String list, String item, String ar){
		StringTokenizer st= new StringTokenizer(list, ar);
		int id= -1;
		int a = id;
		while( st.hasMoreElements() ){
			id++;
			String tmp= st.nextToken();
			if(tmp.equals(item)){
				a = id;
				return a;
			}
		}
		return a;
	}

	/** 2015 04 20 ?????????
	 * ????????? ???????????? ?????? ????????? ?????? ????????? ?????? ???????????? ????????? ?????????.
	 * ex) gAt1("a,b,c", 0, ","); -> "a"
	 * ex) gAt1("a,b,c", 1, ","); -> "b"
	 * ex) gAt1("a,b,c", 2, ","); -> "c"
	 * @param str1 ?????????
	 * @param a ?????????
	 * @param ar ?????????
	 * @return
	 */
	public static String gAt1(String str1, int index, String str){
		StringTokenizer st= new StringTokenizer(str1, str);
		String rtval= "";
		int id= 0;
		while( st.hasMoreElements() ){
			rtval= st.nextToken();
			if( id++ == index)	break;
		}
		return rtval;
	}
	public static String[] getFixedSplit(String src, String delim, int splitSize){
		if(splitSize <= 0)		return  null;
		if(src == null)			src = "";
		StringTokenizer st= new StringTokenizer(src, delim);
		String[] strArray = new String[splitSize];
		int index = 0;
		while( st.hasMoreElements() ){
			strArray[index++]= st.nextToken();
			if(index >= splitSize)	break;
		}
		for (; index < splitSize; index++) {
			strArray[index] = "";
		}
		return strArray;
	}
	public static String gAtSet(String str1, int a, String target, String ar){
		StringTokenizer st= new StringTokenizer(str1, ar);
		String rtval= "";
		int id= 0;
		while( st.hasMoreElements() ){
			if( id++ == a){
				st.nextToken();
				rtval += ar + target;
			}else{
				rtval += ar + st.nextToken();
			}
		}
		if( str1.length() >0 )
			rtval= rtval.substring(ar.length());
		return rtval;
	}
	public static String getURLDecodeStrR(String str, String type, int depth){

		if( depth<1){
			// ????????? ?????? ?????? ?????? url ??? ?????? skip ?????? ????????? ?????? ????????? % -> ://
			// url ???????????? ?????? http:// -> http%3A%2F%2F ??????????????? ????????? ??????.
			// 20150427 ?????????
		} else if( str.indexOf("://") == -1 ){
			str = getURLDecodeStr(str, type);
			str = getURLDecodeStrR(str, type, --depth);
		}
		return str;
	}
	public static String getURLDecodeStr(String str,String type){
		String rtn="";
		try{
			if(str!=null && str.length()>0){
				try{
					if(str.endsWith("%")) str=str+"25";
					str=URLDecoder.decode(str,type);
				}catch(Exception e){
					str=str+"25";
					try{
						str=URLDecoder.decode(str,type);
					}catch(Exception ex){
						str="";
					}
				}
				str = str.replace("&amp;amp;", "&");
				str = str.replace("&amp;", "&");
			}else{
				str="";
			}
			rtn=str;
		}catch(Exception e){
		}
		return rtn;
	}
	public static int getRangeVal(int s, int e){
		ArrayList<Integer> a = StringUtils.getRandList(e);
		int r = a.get(0);
		return r+s;
	}

	public static ArrayList<Integer> getRandList(int cnt){
		ArrayList<Integer> list=new ArrayList<Integer>();
		ArrayList<Integer> rtnList=new ArrayList<Integer>();
		for(int i=0;i<cnt;i++){
			list.add(i);
		}
		Random trandom= new Random();
		while(list.size()>0){
			int tx=trandom.nextInt(list.size());
			if (tx == list.size()) tx = tx -1;
			rtnList.add(list.get(tx));
			list.remove(tx);
		}
		return rtnList;
	}
	public static String getPartIdKey(String ipKey){
		String result="";
		try{
			if( ipKey.length()<2 ){
				ipKey = "000"+ipKey;
			}
			String ipStr=ipKey.replace(".","");
			ipStr=ipStr.substring(ipStr.length()-2,ipStr.length());
			SimpleDateFormat mm = new SimpleDateFormat("MM");
			java.util.Date date=new java.util.Date();
			String m=mm.format(date);
			result=((Integer.parseInt(ipStr)%30)+10)+m;
		}catch(Exception e){
		}
		return result;
	}
	public static String getPrevPartIdKey(String ipKey){
		String result="";
		try{
			if( ipKey.length()<2 ){
				ipKey = "000"+ipKey;
			}
			String ipStr=ipKey.replace(".","");
			ipStr=ipStr.substring(ipStr.length()-2,ipStr.length());
			SimpleDateFormat mm = new SimpleDateFormat("MM");
			java.util.Date date=new java.util.Date();
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.MONTH,-1);
			date.setTime(cal.getTimeInMillis());
			String m=mm.format(date);
			result=((Integer.parseInt(ipStr)%30)+10)+m;
		}catch(Exception e){
		}
		return result;
	}
	public static String unescape(String inp) {
	     StringBuffer rtnStr = new StringBuffer();
	     char [] arrInp = inp.toCharArray();
	     int i;
	     for(i=0;i<arrInp.length;i++) {
	         if(arrInp[i] == '%') {
	             String hex;
	             if(arrInp[i+1] == 'u') {    //????????????.
	                 hex = inp.substring(i+2, i+6);
	                 i += 5;
	             } else {    //ascii
	                 hex = inp.substring(i+1, i+3);
	                 i += 2;
	             }
	             try{
	                 rtnStr.append(Character.toChars(Integer.parseInt(hex, 16)));
	             } catch(NumberFormatException e) {
	              rtnStr.append("%");
	                 i -= (hex.length()>2 ? 5 : 2);
	             }
	         } else {
	          rtnStr.append(arrInp[i]);
	         }
	     }

	     return rtnStr.toString();
	 }

	public static String getRefMass(String refURL){
		if(refURL==null) refURL = "";
		refURL = refURL.toLowerCase();
		String refMass = "";
		try{
			refMass = refURL.replace("http://", "");
			refMass = refMass.replace("https://", "");
			if(refMass.indexOf("/") > -1) {
				refMass = refMass.substring(0, refMass.indexOf("/"));
			} else if(refURL.indexOf("?") > -1) {
				refMass = refMass.substring(0, refMass.indexOf("?"));
			} else if(StringUtils.isEmpty(refMass)) {
				refMass = "bookmark";
			}
		}catch(Exception e){
			return refMass;
		}
		return refMass;
	}

	/**
	 * ????????? ?????? ?????? pjm
	 * True : 11st.com
	 * False : P
	 * True : Au.co.kr
	 * False : au
	 * False : m.com
	 * False : m.s
	 * True : m.me.co.kr
	 * True : http://www.auction.co.kr
	 * True : me.co.kr/?pid=hive-Mobion
	 * True : skyedu.com
	 * False : www
	 * False : www.
	 * True : www.com
	 * True : www.ca.ac.kr
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isDomain(String url) {
		Pattern p = Pattern.compile(	"((?:[a-z][a-z\\.\\d\\-]+)\\.(?:[a-z][a-z\\-]+))(?![\\w\\.])",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(url);
		return m.find();
	}
	/**
	 * ????????? ?????? ??????
	 * http://www.styleberry.co.kr/index.html -> styleberry.co.kr <br/>
	 *
	 * @param url
	 * @return
	 */
	public static String getDestDomain(String url){
		String destDomain=url;
		url=url.toLowerCase();
		try{
			if(url !=null && url.trim().length() !=0){
				if(url.indexOf("http://") != -1 ){
					destDomain= url.substring(7,url.length());
				}else if(url.indexOf("https://") != -1 ){
					destDomain= url.substring(8,url.length());
				}
				if(url.indexOf("www.") != -1 ){
					destDomain= destDomain.substring(4,destDomain.length());
				}
				if(destDomain.indexOf("/")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("/"));
				}
				if(destDomain.indexOf("?")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("?"));
				}

			}
		}catch(Exception e){
			return destDomain;
		}
		return destDomain;
	}

	public static String getDestDomainExwww(String url){
		String destDomain=url;
		url=url.toLowerCase();
		try{
			if(url !=null && url.trim().length() !=0){
				if(url.indexOf("http://") != -1 ){
					destDomain= url.substring(7,url.length());
				}else if(url.indexOf("https://") != -1 ){
					destDomain= url.substring(8,url.length());
				}
				if(destDomain.indexOf("/")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("/"));
				}
				if(destDomain.indexOf("?")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("?"));
				}

			}
		}catch(Exception e){
			return destDomain;
		}
		return destDomain;
	}

	public static String getParamName(Map<String, String> list, String refMass) {
		String tempParamName = "";
		for(int i = 0; i < list.size(); i++) {
			Iterator<String> it = list.keySet().iterator();
			while(it.hasNext()) {
				String temp = it.next();
				if(refMass.equals(temp)){
					tempParamName = list.get(temp);
					break;
				}
			}
		}
		return tempParamName;
	}

	public static String getParamValue(String url, String chooseName) {
		//String temp = "http://search.naver.com/search.nhn?one=1&two=2&three=3";
		String v = "noKeyword";
		if(StringUtils.isNotEmpty(chooseName)) {
			StringTokenizer st = new StringTokenizer(url.substring(url.indexOf("?")+1),"&");
			while(st.hasMoreTokens()) {
				String [] pair = st.nextToken().split("=");
				if(pair[0].equals(chooseName)) {
					v = pair[1];
				}
			}
		}
		return v;
	}

	/**
	 * null ??????
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
	}

	/**
	 * null ??????
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return org.apache.commons.lang3.StringUtils.isEmpty(str);
	}

	/**
	 * null ?????? + ????????????
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		return org.apache.commons.lang3.StringUtils.isBlank(str);
	}

	/**
	 * ???????????? ??????
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		return org.apache.commons.lang3.StringUtils.isNumeric(str);
	}

	/**
	 * ????????? ??????
	 * ????????? ????????? ?????? ??????
	 * @param str
	 * @return
	 */
	public static String numChk(String str) {
		String numeral = "";
		// ????????? ?????? ?????? ?????????.
		str = str.indexOf(".") != -1 ? str.split("[.]")[0] :  str;
		try {
		    if( str == null ) numeral = null;
		    else {
		    	String patternStr = "\\d"; //????????? ???????????? ??????
		    	Pattern pattern = Pattern.compile(patternStr);
		    	Matcher matcher = pattern.matcher(str);
		    	while(matcher.find()) {
		    		numeral += matcher.group(0); //????????? ????????? ???????????? numeral ????????? ?????????. ???????????? ??????!!
		    	}
		    }
		} catch (Exception e) {
			return "0";
		}
	    return numeral;
	}


	/**
	 * ???????????? ??????
	 * @param str
	 * @return
	 */
	public static String StringReplace(String str){
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str =str.replaceAll(match, " ");
		return str;
	}

	/**
	 * Null ?????? ??? ?????? return;
	 * @param str
	 * @return
	 */
	public static String nvl(String nvl){
		return nvl(nvl, "");
	}

	/**
	 * Null ?????? ??? ????????? return;
	 * @param str
	 * @param rep
	 * @return
	 */
	public static String nvl(String nvl, String rep){
		if (nvl != null) {
			return nvl.trim();
		} else {
			return rep;
		}
	}

	/**
	 * Null ?????? ??? ??????????????? ???????????? return;
	 * ??????????????? error ??? ????????? int ?????? ????????????.
	 * @param nvl
	 * @param error
	 * @return	int
	 */
	public static int nvlNumber(String nvl, int error){
		if (nvl != null) {
			try {
				return Integer.parseInt(nvl.trim());
			} catch (Exception e) {
				return error;
			}
		} else {
			return error;
		}
	}

	/**
	 * Null ?????? ??? ??????????????? ???????????? return;
	 * ??????????????? error ??? ????????? int ?????? ????????????.
	 * @param nvl
	 * @param error
	 * @return	int
	 */
	public static float nvlFloat(String nvl){
		if (nvl != null) {
			try {
				return Float.valueOf(nvl.trim());
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	// ??????3?????? ?????? ?????? ?????? ??????
	public static int removeString(String number,int index, String str) {
		number = StringUtils.gAt1(number, index, str);
		number = number.replaceAll("[^0-9]", "");
		try {
			if (number.length() > 3)
				number = number.substring(0, 3);
		    return Integer.parseInt(number);
		} catch (Exception e) {
			return 0;
		}
	}

	// ????????? ???????????? ????????? ????????? ?????????
	public static int onlyInt(String number,int count) {
		number = number.replaceAll("[^0-9]", "");
		try {
			if (number.length() > count)
				number = number.substring(0, count);
		    return Integer.parseInt(number);
		} catch (Exception e) {
			// logger.error(ErrorLog.getStack(e));
			return 0;
		}
	}

	/**
	 * Null ?????? ??? ?????? return;
	 * @param str
	 * @return
	 */
	public static long numberLong(String number){
		try {
			return Long.parseLong(number);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Null ?????? ??? ?????? return;
	 * @param str
	 * @return
	 */
	public static int number(String number){
		try {
			return Integer.parseInt(number);
		} catch (Exception e) {
			//logger.error(ErrorLog.getDebugStack(e));
			return 0;
		}
	}

	public static float tofloat(String number){
		try {
			return Float.parseFloat(number);
		} catch (Exception e) {
			return 0.1f;
		}
	}

	public static int intCast(Object value) {
		int intValue = 0;
		try {
			intValue = Integer.valueOf(String.valueOf(value));
		} catch (Exception e) {}
		return intValue;
	}

	public static float flaotCast(Object value) {
		float intValue = 0;
		try {
			intValue = Float.valueOf(String.valueOf(value));
		} catch (Exception e) {}
		return intValue;
	}

	public static long longCast(Object value) {
		long intValue = 0;
		try {
			intValue = Long.valueOf(String.valueOf(value));
		} catch (Exception e) {}
		return intValue;
	}

	/**
	 * ????????? ?????????????????? ???????????? ????????? ????????? ?????? ?????? ??????????????????.
	 *  [?????????????????? ???????????? KWD(?????????) ???????????????]
	 *  ????????? 0.
	 *  ????????? ?????? ?????? ?????? ????????? ??????
	 *  0. ???????????? ????????? ????????????
	 *  1. 360*50 (????????? ?????????)
	 *  2. 250*250 (???????????? ?????????)
	 *  3. 120*600
	 *  4. 728*90
	 *  5. 300*180
	 *  6. 800*1500
	 *  7. 300*65
	 *  8. 180*180 (?????? ?????????)
	 * @param imgCode
	 * @return
	 */
	public static String imgCodeChk(MediaScriptData ms, String imgType) {
		try{
			if ("0".equals(imgType)) {
				return GlobalConstants.IMG_NON;	// ???????????? ????????? ????????????
			} else if ("1".equals(imgType)){
				return GlobalConstants.MOBILEIMG_360_50;
			} else if ("2".equals(imgType)){
				return GlobalConstants.IMGNAME1_250_250;
			} else if ("3".equals(imgType)){
				return GlobalConstants.IMGNAME2_120_600;
			} else if ("4".equals(imgType)){
				return GlobalConstants.IMGNAME3_728_90;
			} else if ("5".equals(imgType)){
				return GlobalConstants.IMGNAME4_300_180;
			} else if ("6".equals(imgType)){
				return GlobalConstants.IMGNAME5_800_1500;
			} else if ("7".equals(imgType)){
				return GlobalConstants.IMGNAME8_300_65;
			} else if ("8".equals(imgType)){
				return GlobalConstants.IMGNAME8_300_65;
			} else return ms.getImgcode();
		}catch(Exception e) {
		}
		return "";
	}

	/**
	 *  ??????????????? ????????????
	 * @param imgCode
	 * @return
	 */
	public static String imgCodeChk(MediaScriptData ms) {
		try{
			return ms.getImgcode();
		}catch(Exception e) {
		}
		return "";
	}

	/**
	 * ???????????? ????????? bannerPath
	 * bannerpath1 = 360 * 50
	 * imgname = 250 * 250
	 * imgname2 = 120 * 600
	 * imgname3 = 728 * 90
	 * imgname4 = 300 * 180
	 * imgname5 = 800 * 1500
	 * imgname6 = ??????
	 * imgname7 = 160 * 300
	 * imgname8 = 300 * 65
	 * imgname9 : 850 * 800
	 * imgname10 : 960 * 100
	 * imgname11 : 720 * 1230
	 * imgname12 : 160 * 600
	 * imgname13 : 640 * 350
	 * imgname14 : 250 * 250 (????????????)
	 * @param cnf
	 * @param web:igb ,mobile:bntype, app:imgType
	 * @param type
	 * @return
	 */
	public static String getChangeIgb(String igb,String type) {
		String img = "";
		try {
			if(GlobalConstants.MOBILE.equals(type)){
				if("30".equals(igb)||"31".equals(igb)||"32".equals(igb)||"33".equals(igb)||"34".equals(igb)||"35".equals(igb)||"36".equals(igb)||"38".equals(igb)||"41".equals(igb)||"42".equals(igb) ||"51".equals(igb) ||"98".equals(igb) ||"52".equals(igb) ||"47".equals(igb)){
					img = GlobalConstants.IMGNAME1_250_250;
				}else if("44".equals(igb)||"45".equals(igb) ||"49".equals(igb) ||"99".equals(igb)){
					img = GlobalConstants.IMGNAME4_300_180;
				}else if ("56".equals(igb)||GlobalConstants.MOBILE_BRANDLINK.equals(igb)){
					img = GlobalConstants.IMGNAME5_800_1500;
				}else if("39".equals(igb)){
					img = GlobalConstants.IMGNAME6_EDGE;
				}else{
					img = GlobalConstants.MOBILEIMG_360_50;
				}
			}else if(GlobalConstants.APP.equals(type)){
				if("banner1".equals(igb)){
					img = GlobalConstants.MOBILEIMG_360_50;
				}else if("1".equals(igb)){
					img = GlobalConstants.IMGNAME1_250_250;
				}else if("4".equals(igb)){
					img = GlobalConstants.IMGNAME4_300_180;
				}else if("5".equals(igb)){
					img = GlobalConstants.IMGNAME5_800_1500;
				}else if("11".equals(igb)){// 11, 13, 14 ??? AdbnSdkApi ????????? ??????????????? ???????????? ???????????? ???????????? ?????? ???????????? ????????????.
					img = GlobalConstants.IMGNAME11_720_1230;
				}else if("13".equals(igb)){
					img = GlobalConstants.IMGNAME13_640_350;
				}else if("14".equals(igb)){
					img = GlobalConstants.IMGNAME14_250_250;
				}
			}else{
				if("1".equals(igb) || "71".equals(igb) || "74".equals(igb) || "80".equals(igb) || "81".equals(igb) || "61".equals(igb) || "85".equals(igb) || "86".equals(igb)){
					img = GlobalConstants.IMGNAME1_250_250;
				}else if("32".equals(igb)|| "76".equals(igb) || "64".equals(igb)){
					img = GlobalConstants.IMGNAME2_120_600;
				}else if("60".equals(igb) || "72".equals(igb) ||"75".equals(igb) ||"82".equals(igb) ||"83".equals(igb)){
					img = GlobalConstants.IMGNAME3_728_90;
				}else if("62".equals(igb) || "92".equals(igb) || "93".equals(igb)){
					img = GlobalConstants.IMGNAME4_300_180;
				}else if("5".equals(igb) || "90".equals(igb) || "91".equals(igb)){
					img = GlobalConstants.IMGNAME5_800_1500;
				}else if("7".equals(igb) || "70".equals(igb)) {
					img = GlobalConstants.IMGNAME6_EDGE;
				}else if("36".equals(igb) || "66".equals(igb) || "67".equals(igb) || "68".equals(igb) || "73".equals(igb)){
					img = GlobalConstants.IMGNAME7_160_300;
				}else if("65".equals(igb) || "63".equals(igb) || "82".equals(igb) || "94".equals(igb)){
					img =GlobalConstants.IMGNAME8_300_65;
				}else if("69".equals(igb)){
					img =GlobalConstants.IMGNAME12_160_600;
				}
			}

			if(StringUtils.isEmpty(img)) {		// ?????? ?????? ?????? igb??? ?????? 250x250 ???????????? ?????????.
				img = GlobalConstants.IMGNAME1_250_250;
			}
		}
		catch (Exception e) {
			img = "";
		}
		return img;
	}


	/**
	 * ???????????? ????????? bannerPath
	 * ?????? ???????????? ?????????. ????????? ???????????? ?????? ????????? ?????? ?????? ???????????????.
	 * igb ???????????? ???????????? ?????? imgCode ??????
	 * bannerpath1 = 360 * 50
	 * imgname = 250 * 250
	 * imgname2 = 120 * 600
	 * imgname3 = 728 * 90
	 * imgname4 = 300 * 180
	 * imgname5 = 800 * 1500
	 * imgname6 = ??????
	 * imgname7 = 160 * 300
	 * imgname8 = 300 * 65
	 * imgname9 : 850 * 800
	 * imgname10 : 960 * 100
	 * imgname11 : 720 * 1230
	 * imgname12 : 160 * 600
	 * imgname13 : 640 * 350
	 * imgname14 : 250 * 250 (????????????)
	 * @param cnf
	 * @param type <br/>
	 * ??? : web<br/>
	 * ????????? : mobile<br/>
	 * ????????? ??????????????? : mobile_sky<br/>
	 * ????????? ??? : app <br/>
	 * @param imgCode
	 * @return
	 */
	public static String getCommonImgPath(AdConfigData cnf, String type, String imgCode) {
		String img = "";
		try {
			if(GlobalConstants.MOBILE.equals(type)){
				if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}
				// ?????? ???????????? 850_800 ??? ?????? ?????? ????????? ???????????????. ????????? ????????? ?????????????????? 09??? ??????????????? ????????? 800_1500 ??? ??????????????? ????????? (?????? ??????)
				else if((GlobalConstants.IMGNAME5_800_1500.equals(imgCode) || GlobalConstants.IMGNAME9_850_800.equals(imgCode)) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}
				else if(GlobalConstants.IMGNAME6_EDGE.equals(imgCode) && cnf.getImgname6()!=null && cnf.getImgname6().length()>0){
					img = cnf.getImgname6();
				}
				else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}
				else if(GlobalConstants.IMGNAME17_300_250.equals(imgCode) && cnf.getImgname17()!=null && cnf.getImgname17().length()>0){
					img = cnf.getImgname17();
				}
				else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
				else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
				// ???????????? ????????? ?????? ??? ??????????????? ??????????????????, ??? ????????? ????????? ??????, ????????? ???????????? ????????????, ????????? ????????? ??????!!
				else if(cnf.getBanner_path1()!=null && cnf.getBanner_path1().length()>0){
					img = cnf.getBanner_path1();
				}
			}else if(GlobalConstants.APP.equals(type)){
				if( "banner1".equals(imgCode) && cnf.getBanner_path1()!=null && cnf.getBanner_path1().length()>0 ){
					img = cnf.getBanner_path1();
				}else if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}
				// 11, 13, 14 ??? AdbnSdkApi ????????? ??????????????? ???????????? ???????????? ???????????? ?????? ???????????? ????????????.
				else if(GlobalConstants.IMGNAME11_720_1230.equals(imgCode) && cnf.getImgname11()!=null && cnf.getImgname11().length()>0){
					img = cnf.getImgname11();
				}else if(GlobalConstants.IMGNAME13_640_350.equals(imgCode) && cnf.getImgname13()!=null && cnf.getImgname13().length()>0){
					img = cnf.getImgname13();
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}	else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				} else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
				// ???????????? ????????? ?????? ??? ??????????????? ??????????????????, ??? ????????? ????????? ??????, ????????? ???????????? ????????????, ????????? ????????? ??????!!
				else if(cnf.getBanner_path1()!=null && cnf.getBanner_path1().length()>0){
					img = cnf.getBanner_path1();
				}
			}else{
				if("01".equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME2_120_600.equals(imgCode) && cnf.getImgname2()!=null && cnf.getImgname2().length()>0){
					img = cnf.getImgname2();
				}else if(GlobalConstants.IMGNAME3_728_90.equals(imgCode) && cnf.getImgname3()!=null && cnf.getImgname3().length()>0){
					img = cnf.getImgname3();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}else if(GlobalConstants.IMGNAME6_EDGE.equals(imgCode) && cnf.getImgname6()!=null && cnf.getImgname6().length()>0){
					img = cnf.getImgname6();
				}else if(GlobalConstants.IMGNAME7_160_300.equals(imgCode) && cnf.getImgname7()!=null && cnf.getImgname7().length()>0){
					img = cnf.getImgname7();
				}else if(GlobalConstants.IMGNAME8_300_65.equals(imgCode) && cnf.getImgname8()!=null && cnf.getImgname8().length()>0){
					img = cnf.getImgname8();
				}else if(GlobalConstants.IMGNAME12_160_600.equals(imgCode) && cnf.getImgname12()!=null && cnf.getImgname12().length()>0){
					img = cnf.getImgname12();
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}else if(GlobalConstants.IMGNAME17_300_250.equals(imgCode) && cnf.getImgname17()!=null && cnf.getImgname17().length()>0){
					img = cnf.getImgname17();
				}else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
				// ???????????? ????????? ?????? ??? ??????????????? ??????????????????, ??? ????????? ????????? ??????, ????????? ???????????? ????????????, ????????? ????????? ??????!!
				else if(cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}
			}
		}
		catch (Exception e) {
			img = "";
		}
		return img;
	}
	
	/**
	 * ????????? RTB?????? ????????? ?????? 
	 * ????????? RTB??? ?????? ?????? ???????????? ?????? ?????? ????????? ???????????? ???????????? ?????? 
	 * ???????????? ????????? ?????????.
	 * bannerpath1 = 360 * 50
	 * imgname = 250 * 250
	 * imgname2 = 120 * 600
	 * imgname3 = 728 * 90
	 * imgname4 = 300 * 180
	 * imgname5 = 800 * 1500
	 * imgname6 = ??????
	 * imgname7 = 160 * 300
	 * imgname8 = 300 * 65
	 * imgname9 : 850 * 800
	 * imgname10 : 960 * 100
	 * imgname11 : 720 * 1230
	 * imgname12 : 160 * 600
	 * imgname13 : 640 * 350
	 * imgname14 : 250 * 250 (????????????)
	 * @param cnf
	 * @param type <br/>
	 * ??? : web<br/>
	 * ????????? : mobile<br/>
	 * ????????? ??????????????? : mobile_sky<br/>
	 * ????????? ??? : app <br/>
	 * @param imgCode
	 * @return
	 */
	public static String getFrameRTBImgPath(AdConfigData cnf, String type, String imgCode) {
		String img = "";
		try {
			if(GlobalConstants.MOBILE.equals(type)){
				if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}
				// ?????? ???????????? 850_800 ??? ?????? ?????? ????????? ???????????????. ????????? ????????? ?????????????????? 09??? ??????????????? ????????? 800_1500 ??? ??????????????? ????????? (?????? ??????)
				else if((GlobalConstants.IMGNAME5_800_1500.equals(imgCode) || GlobalConstants.IMGNAME9_850_800.equals(imgCode)) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}
				else if(GlobalConstants.IMGNAME6_EDGE.equals(imgCode) && cnf.getImgname6()!=null && cnf.getImgname6().length()>0){
					img = cnf.getImgname6();
				}
				else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}
				else if(GlobalConstants.IMGNAME17_300_250.equals(imgCode) && cnf.getImgname17()!=null && cnf.getImgname17().length()>0){
					img = cnf.getImgname17();
				}
				else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
				else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
			}else if(GlobalConstants.APP.equals(type)){
				if( "banner1".equals(imgCode) && cnf.getBanner_path1()!=null && cnf.getBanner_path1().length()>0 ){
					img = cnf.getBanner_path1();
				}else if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}
				// 11, 13, 14 ??? AdbnSdkApi ????????? ??????????????? ???????????? ???????????? ???????????? ?????? ???????????? ????????????.
				else if(GlobalConstants.IMGNAME11_720_1230.equals(imgCode) && cnf.getImgname11()!=null && cnf.getImgname11().length()>0){
					img = cnf.getImgname11();
				}else if(GlobalConstants.IMGNAME13_640_350.equals(imgCode) && cnf.getImgname13()!=null && cnf.getImgname13().length()>0){
					img = cnf.getImgname13();
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}	else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				} else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
			}else{
				if("01".equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					img = cnf.getImgname();
				}else if(GlobalConstants.IMGNAME2_120_600.equals(imgCode) && cnf.getImgname2()!=null && cnf.getImgname2().length()>0){
					img = cnf.getImgname2();
				}else if(GlobalConstants.IMGNAME3_728_90.equals(imgCode) && cnf.getImgname3()!=null && cnf.getImgname3().length()>0){
					img = cnf.getImgname3();
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					img = cnf.getImgname4();
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					img = cnf.getImgname5();
				}else if(GlobalConstants.IMGNAME6_EDGE.equals(imgCode) && cnf.getImgname6()!=null && cnf.getImgname6().length()>0){
					img = cnf.getImgname6();
				}else if(GlobalConstants.IMGNAME7_160_300.equals(imgCode) && cnf.getImgname7()!=null && cnf.getImgname7().length()>0){
					img = cnf.getImgname7();
				}else if(GlobalConstants.IMGNAME8_300_65.equals(imgCode) && cnf.getImgname8()!=null && cnf.getImgname8().length()>0){
					img = cnf.getImgname8();
				}else if(GlobalConstants.IMGNAME12_160_600.equals(imgCode) && cnf.getImgname12()!=null && cnf.getImgname12().length()>0){
					img = cnf.getImgname12();
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					img = cnf.getImgname14();
				}else if(GlobalConstants.IMGNAME17_300_250.equals(imgCode) && cnf.getImgname17()!=null && cnf.getImgname17().length()>0){
					img = cnf.getImgname17();
				}else if(GlobalConstants.IMGNAME22_320_100.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}else if(GlobalConstants.MOBILEIMG_360_50.equals(imgCode) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
					img = cnf.getImageInfo();
				}
			}
		}
		catch (Exception e) {
			img = "";
		}
		return img;
	}
	/**
	 * ????????? ???????????? ????????? ??????
	 * @param cnf
	 * @param web:igb ,mobile:bntype, app:imgType
	 * @param type
	 * @return
	 */
	public static boolean checkImgpath(AdConfigData cnf,String imgCode,String type) {
		try {
			// ???????????? ????????? ???????????? ????????? ?????? true??? ????????????.
			if(GlobalConstants.IMG_NON.equals(imgCode)) return true;
			/**
			 * ????????? ??????????????? ???????????? ????????? ???????????????, ?????????????????? ???????????? ????????? ?????????????????? ????????? ?????? ????????????????????? ?????? ??????,
			 * ???????????? ????????? ??????
			 */
			if(GlobalConstants.MOBILE_BRANDLINK.equals(type) || GlobalConstants.SKY.equals(type)){
				if(cnf.getImgname5().length()>0 && cnf.getImgname5()!=null){
					return true;
				}else{
					return false;
				}
			}else if(GlobalConstants.APP.equals(type) || GlobalConstants.MOBILE.equals(type)){
				if(("banner1".equals(imgCode) || GlobalConstants.MOBILEIMG_360_50.equals(imgCode)) && cnf.getBanner_path1()!=null && cnf.getBanner_path1().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME9_850_800.equals(imgCode) && cnf.getImgname9()!=null && cnf.getImgname9().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME11_720_1230.equals(imgCode) && cnf.getImgname11()!=null && cnf.getImgname11().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME13_640_350.equals(imgCode) && cnf.getImgname13()!=null && cnf.getImgname13().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					return true;
				}else{
					return false;
				}
			}else{
				if(GlobalConstants.IMGNAME1_250_250.equals(imgCode) && cnf.getImgname()!=null && cnf.getImgname().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME2_120_600.equals(imgCode) && cnf.getImgname2()!=null && cnf.getImgname2().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME3_728_90.equals(imgCode) && cnf.getImgname3()!=null && cnf.getImgname3().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME4_300_180.equals(imgCode) && cnf.getImgname4()!=null && cnf.getImgname4().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME5_800_1500.equals(imgCode) && cnf.getImgname5()!=null && cnf.getImgname5().length()>0 ){
					return true;
				}else if(GlobalConstants.IMGNAME6_EDGE.equals(imgCode) && cnf.getImgname6()!=null && cnf.getImgname6().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME7_160_300.equals(imgCode) && cnf.getImgname7()!=null && cnf.getImgname7().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME8_300_65.equals(imgCode) && cnf.getImgname8()!=null && cnf.getImgname8().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME9_850_800.equals(imgCode) && cnf.getImgname9()!=null && cnf.getImgname9().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME12_160_600.equals(imgCode) && cnf.getImgname12()!=null && cnf.getImgname12().length()>0){
					return true;
				}else if(GlobalConstants.IMGNAME14_250_250.equals(imgCode) && cnf.getImgname14()!=null && cnf.getImgname14().length()>0){
					return true;
				}else if((GlobalConstants.MOBILEIMG_360_50.equals(imgCode)) && cnf.getImageInfo()!=null && cnf.getImageInfo().length()>0){
          return true;
        }else{
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ?????? base ????????? HTML ??????
	 */
	public static void bastHtml(StringBuffer htmlStr,String logoChk,int wvalue,int hvalue) {

		htmlStr.append("<!DOCTYPE html>\n");
		htmlStr.append(" <html xmlns='http://www.w3.org/1999/xhtml'>                                \n");
		htmlStr.append(" <head>                                                                     \n");
		htmlStr.append(" <title>????????????</title>                                                    \n");
		htmlStr.append(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=euc-kr\" /> \n");
		htmlStr.append(" <script type='text/javascript' src='http://"+GlobalConstants.IMG_DOMAIN+"/newAd/js/jquery-1.9.1.min.js'></script> \n");
		htmlStr.append(" <style>                                                                    \n");
		htmlStr.append("#mobon_right_target_banner {width:118px; height:598px; border:1px solid #CCCCCC;}\n");
		htmlStr.append("#mobon_right_target_banner .mobon_right_shop_logo {height:30px; overflow:hidden; padding:15px 10px; border-bottom:1px solid #ccc; cursor:pointer;}\n");
		htmlStr.append("#mobon_right_target_banner .mobon_right_shop_logo img {width:100%;}\n");
		htmlStr.append("#mobon_right_target_banner dl {margin:0; padding:0; padding:3px; margin-top:15px; border:2px solid #fff; cursor:pointer;}\n");
		htmlStr.append("#mobon_right_target_banner dl:hover {border:2px solid #ce1212;}\n");
		htmlStr.append("#mobon_right_target_banner dl dt { margin:0; padding:0; width:108px; height:108px;overflow:hidden;}\n");
		htmlStr.append("#mobon_right_target_banner dl dt img {width:100%;}\n");
		htmlStr.append("#mobon_right_target_banner dl dd {text-align:center; margin:0; padding:0; font-size : 12px ; font-family : '??????' ;}\n");
		htmlStr.append("#mobon_right_target_banner dl dd.desc { height:15px; line-height:140%; overflow:hidden; padding-top:10px;}\n");
		htmlStr.append("#mobon_right_target_banner dl dd.price {color:#ce1212; }\n");
		htmlStr.append("#mobon_bottom_target_banner * {margin:0; padding:0; font-family:'??????', dotum;}\n");
		htmlStr.append("#mobon_bottom_target_banner {margin:0; padding:0; width:");
		htmlStr.append(wvalue-2);
		htmlStr.append("px; height:");
		htmlStr.append(hvalue-2);
		htmlStr.append("px; border:1px solid #ddd; position:relative;}\n");
		htmlStr.append("#mobon_bottom_target_banner .mobon_bottom_shop_logo {position:absolute; top:50%; left:10px;}\n");
		htmlStr.append("#mobon_bottom_target_banner .mobon_bottom_shop_logo img {height:30px; }\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box {margin-left:110px;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box:after {content:''; display:block; clear:both;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl {float:left; border: 2px solid #fff; width: 32%;  position:relative;   display: inline; cursor: pointer;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl:hover {border: 2px solid #ce1212;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl dt {position: absolute;  top: 0;  left: 0; }\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl dt img {width:100%;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl dd.desc {text-align: center;overflow: hidden;  font-size: 12px;  font-family: '??????';}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl dd.desc span{display: block; overflow: hidden;  height:15px; line-height:130%;}\n");
		htmlStr.append("#mobon_bottom_target_banner .item_box dl dd.desc strong{color: #ce1212;  display: block;}\n");
		htmlStr.append("#resize-banner-box{width: 248px; height: 248px; border:1px solid #dbdbdb; margin:0; padding:0; position:relative; overflow: hidden;} \n");
        htmlStr.append("#resize-banner-box > .mobon-logo{position: absolute; top: 0; right: 0;} \n");
        htmlStr.append("#resize-banner-box > .target-img img{width: 100%; margin:0; padding:0; border:0;} \n");
        htmlStr.append("#resize-banner-box > .info-box{background:#000; position: absolute; bottom: 0; left: 0; width: 100%; margin:0; padding:0; height: 50px; cursor:pointer;} \n");
        htmlStr.append("#resize-banner-box > .info-box .shop-logo-box{width:95px; max-height:41px;  position: absolute; top: 4px; left: 5px; padding: 8px 0;background:#fff; text-align:center; font-size:0;} \n");
        htmlStr.append("#resize-banner-box > .info-box .shop-logo-box img{width: 80%;  margin:0; padding:0; } \n");
        htmlStr.append("#resize-banner-box > .info-box .target-text{ color: #fff; margin-left:110px;} \n");
        htmlStr.append("#resize-banner-box > .info-box .target-text .name{white-space: nowrap; overflow: hidden; text-overflow: ellipsis; padding:7px 0 0 0 ; font-size:12px; margin:0; line-height:120%;} \n");
        htmlStr.append("#resize-banner-box > .info-box .target-text .price{letter-spacing: -1px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; color: #FFFF00; font-weight: bold; padding:5px 0 0 0;  font-size:12px; margin:0; line-height:120%;} \n");
		htmlStr.append(" body {                                                                     \n");
		htmlStr.append("   color : #9A9A9A ;                                                        \n");
		htmlStr.append("   font-size : 9pt ;                                                        \n");
		htmlStr.append("   font-family : \"??????\" ;                                                 \n");
		htmlStr.append("   background-color : #ffffff ;                                             \n");
		htmlStr.append("   margin : 0px 0px 0px 0px ;                                               \n");
		htmlStr.append("   scrollbar-face-color: #fffffff;                                          \n");
		htmlStr.append("   scrollbar-highlight-color:#ffffff;                                       \n");
		htmlStr.append("   scrollbar-3dlight-color: #fffffff;                                       \n");
		htmlStr.append("   scrollbar-shadow-color: #fffffff;                                        \n");
		htmlStr.append("   scrollbar-darkshadow-color: #ffffff;                                     \n");
		htmlStr.append("   scrollbar-track-color: #fffffff;                                         \n");
		htmlStr.append("   scrollbar-arrow-color: #000000                                           \n");
		htmlStr.append(" }                                                                          \n");
		htmlStr.append("   A:link { text-decoration:none; color: 00448B;}                           \n");
		htmlStr.append("   A:visited { text-decoration:none; color: green;}                         \n");
		htmlStr.append("   A:active { text-decoration:none; color:red ;}                            \n");
		htmlStr.append("   A:hover {color: black; text-decoration:none;}                            \n");
		htmlStr.append(" </style>                                                                   \n");
		htmlStr.append(" <style type='text/css'>                                                    \n");
		htmlStr.append(" #ad2 {                                                                     \n");
		htmlStr.append("     BORDER-BOTTOM: #CCCCCC 2px solid;                                      \n");
		htmlStr.append("     BORDER-LEFT: #CCCCCC 2px solid;                                        \n");
		htmlStr.append("     DISPLAY: block;                                                        \n");
		htmlStr.append("     BORDER-TOP: #CCCCCC 2px solid;                                         \n");
		htmlStr.append("     BORDER-RIGHT: #CCCCCC 2px solid                                        \n");
		htmlStr.append("                                                                            \n");
		htmlStr.append(" }                                                                          \n");
		htmlStr.append(" #pname {                                                                   \n");
		htmlStr.append("     color:#FFFFFF;                                                         \n");
		htmlStr.append(" 	font-size:12px;                                                         \n");
		htmlStr.append(" 	font-weight:bold;                                                       \n");
		htmlStr.append(" }                                                                          \n");
		htmlStr.append(" </style>                                                                   \n");
		htmlStr.append(" <script>                                                                   \n");
		htmlStr.append(" function chk(){                                                            \n");
		htmlStr.append("   alert(screen.width);                                                     \n");
		htmlStr.append(" }                                                                          \n");
		htmlStr.append(" function imgcheck(imgObj, bool){                                           \n");
		htmlStr.append("   var w = screen.width;                                                    \n");
		htmlStr.append(" 	imgObj.width = w;                                            			\n");
		htmlStr.append(" 	//document.getElementById('divtxt').style.width=w;                      \n");
		htmlStr.append(" }                                                                          \n");
		if (!logoChk.equals("0")) {
			htmlStr.append("$(document).ready(function() {								 			\n");
			htmlStr.append(" $('#divimg').hide();													\n");
			htmlStr.append(" });		 															\n");
		}
		htmlStr.append(" $(window).load(function(){    \n");
		htmlStr.append("	var shop_logo = $('#mobon_bottom_target_banner .mobon_bottom_shop_logo img').height(); \n");
		htmlStr.append("	var img_height = $('#mobon_bottom_target_banner').height()-4; \n");
		htmlStr.append("	var desc_position = img_height / 2 - 15 \n");
		htmlStr.append("	$('#mobon_bottom_target_banner .mobon_bottom_shop_logo') \n");
		htmlStr.append("		.css('top','50%') \n");
		htmlStr.append("		.css('margin-top',-shop_logo/2); \n");
		htmlStr.append("	$('#mobon_bottom_target_banner dl dt') \n");
		htmlStr.append("		.css('width',img_height) \n");
		htmlStr.append("		.css('height',img_height); \n");
		htmlStr.append("	$('#mobon_bottom_target_banner .item_box dl dd.desc') \n");
		htmlStr.append("		.css('margin-left',img_height+3) \n");
		htmlStr.append("		.css('height',img_height); \n");
		htmlStr.append("	$('#mobon_bottom_target_banner .item_box dl dd.desc span') \n");
		htmlStr.append("		.css('padding-top',desc_position); \n");
		htmlStr.append(" });         \n");
		htmlStr.append(" </script>                                                                  \n");
		htmlStr.append(" </head>                                                                    \n");
		htmlStr.append(" <body >                                                                    \n");
		htmlStr.append(" <div id='container'   ");
		htmlStr.append(" style='width:");
		htmlStr.append(wvalue);
		htmlStr.append("px; height:");
		htmlStr.append(hvalue);
		htmlStr.append("px'> \n");
	}


	/**
	 *	????????? ???????????? Sevlet ??? ?????? (????????? /sevlet/drc ??? ??????)
	 * @param no
	 * @param kno
	 * @param s				??????????????????ID
	 * @param adgubun		???????????????
	 * @param sc			???????????????
	 * @param mc			??????????????????ID
	 * @param mcgb			????????????????????? ?????? ?????????
	 * @param userid		??????ID
	 * @param product
	 * @param slink			????????? ????????? ??????
	 * @param clk_param
	 * @param pcode			????????????
	 * @param clktracking	????????? ????????? ??????
	 * @param rtb_param		RTB ?????? ?????????
	 * @param mobonlinkcate		?????????????????? ?????? api???
	 * @param pb_gubun
	 * @param freqLog
	 * @return
	 */
	public static String drcHtml(
            String no,
            String kno,
            String s,
            String adgubun,
            String sc,
            String mc,
            String mcgb,
            String userid,
            String product,
            String slink,
            String clk_param,
            String pcode,
            String clktracking,
            String keyIp,
            String rtb_param,
            String mobonlinkcate,
            String pb_gubun,
            int freqLog,
            Set<String> abtests) {
		return drcHtml(no,kno,s,adgubun,sc,mc,mcgb,userid,product,slink,clk_param,pcode,clktracking,keyIp,rtb_param,mobonlinkcate,pb_gubun,freqLog,"","","","", abtests);
	}

	/**
	 *	????????? ???????????? Sevlet ??? ?????? (????????? /sevlet/drc ??? ??????)
	 * @param no
	 * @param kno
	 * @param s				??????????????????ID
	 * @param adgubun		???????????????
	 * @param sc			???????????????
	 * @param mc			??????????????????ID
	 * @param mcgb			????????????????????? ?????? ?????????
	 * @param userid		??????ID
	 * @param product
	 * @param slink			????????? ????????? ??????
	 * @param clk_param
	 * @param pcode			????????????
	 * @param clktracking	????????? ????????? ??????
	 * @param rtb_param		RTB ?????? ?????????
	 * @param mobonlinkcate		?????????????????? ?????? api???
	 * @param turl				app?????? url or twodepth url
	 * @param tgubun			??? url or ????????? url ?????????
	 * @return ????????????
	 */
	public static String drcHtml(
	                             String no,
	                             String kno,
	                             String s,
	                             String adgubun,
	                             String sc,
	                             String mc,
	                             String mcgb,
	                             String userid,
	                             String product,
	                             String slink,
	                             String clk_param,
	                             String pcode,
	                             String clktracking,
	                             String keyIp,
	                             String rtb_param,
	                             String mobonlinkcate,
	                             String pb_gubun,
	                             int freqLog,
	                             String shortCut,
	                             String scriptId,
	                             String turl,
	                             String tgubun,
	                             Set<String> abtests) {

		if(StringUtils.isEmpty(mc) || StringUtils.isEmpty(s)){
			StringBuilder sb = new StringBuilder();
			StackTraceElement[] ste = new Throwable().getStackTrace();
			for (StackTraceElement ele : ste) {
				sb.append(ele.getClassName());
				sb.append(":");
				sb.append(ele.getMethodName());
				sb.append("  LINE>>>>");
				sb.append(ele.getLineNumber());
				sb.append("\r\n");
			}
			String parameter = String.format("no=%s,kno=%s,s=%s,adGubun=%s,sc=%s,mc=%s,mcgb=%s,userid=%s,product=%s,slink=%s, clk_param=%s, pcode=%s, clktracking=%s"+
			"keyIp=%s, rtb_param=%s, mobonlinkcate=%s, pb_gubun=%s, freqLog=%s, shortCut=%s,scriptId=%s,turl=%s,tgubun=%s ",
			no,kno, s, adgubun, sc, mc, mcgb, userid,product, slink, clk_param, pcode, clktracking, keyIp, rtb_param, mobonlinkcate, pb_gubun, freqLog, shortCut, scriptId, turl, tgubun);
			//RedisUtil.gatheringLog("drc-mc-null-"+DateUtils.getDate("yyyyMMdd")+":", "trace info:" + sb.toString() +"parameters:"+parameter);
		}

		StringBuffer drc = new StringBuffer();
		// click randing url
		if((GlobalConstants.SKY + GlobalConstants.GUBUN + GlobalConstants.LOWER_M).equals(product) ||
			GlobalConstants.SKY.equals(product)) drc.append("/servlet/sdrc?no=");
		else drc.append("/servlet/drc?no=");
		// shop number!!
		if(StringUtils.isNotEmpty(no)) drc.append(no);
		else drc.append("0");
		// non shop number!!
		if(StringUtils.isNotEmpty(kno)) drc.append("&kno="+kno);
		else drc.append("&kno=0");
		// media script number
		if(StringUtils.isNotEmpty(s)) drc.append("&s="+s);
		// shortCut parameter
		if(StringUtils.isNotEmpty(shortCut)) drc.append("&shortCut="+shortCut);
		if(StringUtils.isNotEmpty(adgubun)) drc.append("&adgubun="+adgubun);
		if(StringUtils.isNotEmpty(adgubun)) drc.append("&gb="+adgubun);
		if(StringUtils.isNotEmpty(sc)) drc.append("&sc="+sc);
		if(StringUtils.isNotEmpty(mc)) drc.append("&mc="+mc);
		if(StringUtils.isNotEmpty(mcgb)) drc.append("&mcgb="+mcgb);
		if(StringUtils.isNotEmpty(userid)) drc.append("&userid="+userid);
		if(StringUtils.isNotEmpty(scriptId)) drc.append("&scriptId="+scriptId);
		if(StringUtils.isNotEmpty(userid)) drc.append("&u="+userid);
		if(StringUtils.isNotEmpty(product)) drc.append("&product="+product);
		if(StringUtils.isNotEmpty(slink)) drc.append("&slink="+slink);
		if(StringUtils.isNotEmpty(clk_param) && clk_param.startsWith("http")) drc.append("&clk_param="+clk_param);
		if(StringUtils.isNotEmpty(pcode)) drc.append("&pCode="+pcode);
		if(StringUtils.isNotEmpty(clktracking)) drc.append("&clktracking="+clktracking);
		// RTB ???????????? ??????
		if(StringUtils.isNotEmpty(rtb_param)) drc.append("&rtb_param="+rtb_param);
		// linkcate ?????????????????? ?????? api??? ??????
		if(StringUtils.isNotEmpty(mobonlinkcate)) drc.append("&mobonlinkcate="+mobonlinkcate);
		// ???????????? ??????
		if(StringUtils.isNotEmpty(pb_gubun)) drc.append("&pb_gubun="+pb_gubun);
		// ???????????? ??????
//		if(freqLog > FreqConstants.FREQUENCY_UNAVAILABLE) drc.append("&freqLog=").append(freqLog);
		// ??????????????? url  or 2depth ?????? url ??? ?????? ??????
		if(StringUtils.isNotEmpty(turl)) drc.append("&turl="+turl);
		// ????????? ?????? ????????? ???????????? ????????????
		if(StringUtils.isNotEmpty(tgubun)) drc.append("&tgubun="+tgubun);
		// ????????? ????????????
		boolean check = false;
//		if(check) RedisUtil.getClickKey(s, keyIp);
		// ????????????
//		else if("PUSH".equals(keyIp)) drc.append("&viewTime="+RedisUtil.getClickKey(s, keyIp, 1 * 60 * 60 * 10)); //8??????
		// ????????? ?????? ??????
//		else if("NO_CHECK".equals(keyIp)) drc.append("&viewTime="+keyIp+GlobalConstants.GUBUN+s);
//		// ?????? default expire
//		else drc.append("&viewTime="+RedisUtil.getClickKey(s, keyIp));

		// HOST ID (IP??? ????????? ?????????)
//		drc.append("&hId=").append(ConfigServlet.hostId);

		// ABTest case
//		if(abtests != null && abtests.size() > 0) 	drc.append("&abt=").append(Joiner.on(ABTester.TYPES_DELIMITER).join(abtests));

		return drc.toString();
	}







	
 /**
 * @method convertToHex
 * @see
 * @param data
 * @return String
 */
private static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) { 
        int halfbyte = (data[i] >>> 4) & 0x0F;
        int two_halfs = 0;
        do { 
            if ((0 <= halfbyte) && (halfbyte <= 9)) 
                buf.append((char) ('0' + halfbyte));
            else 
                buf.append((char) ('a' + (halfbyte - 10)));
            halfbyte = data[i] & 0x0F;
        } while(two_halfs++ < 1);
    } 
    return buf.toString();
	} 

  /**
   * @method sha1
   * @see
   * @param text
   * @return
   * @throws NoSuchAlgorithmException
   * @throws UnsupportedEncodingException String
   */
  public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
    MessageDigest md;
    md = MessageDigest.getInstance("SHA-1");
    byte[] sha1hash = new byte[40];
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    sha1hash = md.digest();
    return convertToHex(sha1hash);
  } 
	
  
  public static void sendPostData(JSONObject obj , String url) {
    HttpURLConnection conn = null;
    try {
      URL sendUrl = new URL(url);
      conn = (HttpURLConnection)sendUrl.openConnection();
      conn.setConnectTimeout(500); 
      conn.setReadTimeout(500);
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      
      String param = obj.toString();
      
      OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      
      //??????????????? osw ??? ?????? flush
      osw.write(param);
      osw.flush();

      StringBuilder sb = new StringBuilder();  
      int HttpResult =conn.getResponseCode(); 
      if(HttpResult ==HttpURLConnection.HTTP_OK){
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));  
          String line = null;  
          while ((line = br.readLine()) != null) {  
           sb.append(line + "\n");  
          }  
          br.close();  
      }else{
        long start = System.currentTimeMillis();
      }
      // ??????
      osw.close();
    } catch (MalformedURLException e) {
    }  catch (ProtocolException e) {
    } catch (UnsupportedEncodingException e) {
    } catch (IOException e) {
    }finally{
      if(conn != null){
        conn.disconnect();
      }
    }
  }
  /**
   * IP ??????
   * @method ipVerification
   * @see
   * @param ipAddress
   * @return String
   * @author HaeSungJu
   */
  public static boolean ipVerification(String ipAddress) {
    String pattern = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
    List<String> patternIps = null;
    try {
      patternIps = patternMatch(pattern, ipAddress);
      if (StringUtils.isEmpty(patternIps.get(0)))
        throw new Exception("PATTERN ERROR : " + ipAddress);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  

  /**
   * IP?????? ?????? ??????
   * MYSQL INET_ATON
   * @method ipToLong
   * @see
   * @param ipAddress
   * @return long
   * @author HaeSungJu
   */
  public static long getInetAton(String ipAddress) {
    // ex) ip : 127.0.0.1 / result : 2130706433
    if (isEmpty(ipAddress)) return 0;
    String[] ipAddressInArray = ipAddress.split("\\.");
    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {
      int power = 3 - i;
      int ip = Integer.parseInt(ipAddressInArray[i]);
      result += ip * Math.pow(256, power);
    }
    return result;
  }

  /**
   * ?????? IP?????? ??????
   * MYSQL INET_NTOA
   * @method longToIp
   * @see
   * @param ip
   * @return String
   * @author HaeSungJu
   */
  public static String getInetNtoa(long ipAddress) {
    // ex) ip : 2130706433 / result : 127.0.0.1
    if (ipAddress == 0) return "";
    StringBuilder result = new StringBuilder(15);
    for (int i = 0; i < 4; i++) {
      result.insert(0, Long.toString(ipAddress & 0xff));
      if (i < 3)  result.insert(0, '.');
      ipAddress = ipAddress >> 8;
    }
    return result.toString();
  }

  /**
   * removeEmoji
   * ??????????????? ???????????? String ??????
   * Emoji ?????? ???????????? ??????
   *
   * @author  : sjpark3
   * @since   : 2022-5-18
   */
  public static String removeEmoji(String str) {
	  if (str == null || "".equals(str)) {
		  return "";
	  } else {
		  return str.replaceAll("[\\x{10000}-\\x{10ffff}\ud800-\udfff]", "");
	  }
  }
  
}