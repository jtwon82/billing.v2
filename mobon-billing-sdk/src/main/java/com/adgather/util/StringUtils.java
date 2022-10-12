package com.adgather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.adgather.constants.GlobalConstants;
import com.adgather.user.inclinations.convert.Base64Converter;

/**
 * @param
 * @date 2017. 3. 6.
 * @exception
 * @see
 */
public class StringUtils {
    private static Logger logger = Logger.getLogger(StringUtils.class);

    public static final void main(String[] ar) {
        String PAY_COM_LIST = "tx.allatpay.com,pay.ssg.com,mup.mobilians.co.kr,pay.naver.com,cjmall.com:web[30]mob[30]";
        String sss = "dreamsearch.or.kr(20170228120613)|dreamsearch.or.kr(20170228120612)|dreamsearch.or.kr(20170227184604)|dreamsearch.or.kr(20170224115817)|dreamsearch.or.kr(20170224115815)";
        List<String> list = StringUtils.patternMatch("[0-9]{14}", sss);
        System.out.println(list);

        System.out.println(StringUtils.getAttribute(PAY_COM_LIST.split(",")[3], "web"));
    }

    /**
     * name[value]name2[value2] 형식의 스트링값에서 name속성의 값을 리턴
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
                        logger.error(String.format("err %s => not exists str:web[N]mob[N] (str=%s, name=%s)", e, str, name));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(String.format("err %s (str=%s, name=%s)", e, str, name));
        }

        return result;
    }

    /**
     * 패턴매칭된녀석을 List 로리턴
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
            logger.error(String.format("err %s", e));
        }
        return list;
    }




    private static final Pattern patternSpecialCharater = Pattern.compile("[^\uAC00-\uD7A30-9a-zA-Z\\s]");

    /**
     * 특수문자 제거 로직
     *
     * @param str
     * @return
     */
    public static synchronized String specialCharacterReplace(String str) {
        return patternSpecialCharater.matcher(str).replaceAll("\u0020"); // space
    } // specialCharacterReplace()

    public static String getSqlDtIn(String sDate, String eDate) {
        String rtn = "";
        sDate = sDate.replace("-", "");
        eDate = eDate.replace("-", "");
        String orgSdate = sDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        java.util.Date sdate = null;
        java.util.Date edate = null;
        try {
            sdate = dateFormat.parse(sDate);
            edate = dateFormat.parse(eDate);
            Calendar sCal = Calendar.getInstance();
            Calendar eCal = Calendar.getInstance();
            sCal.setTime(sdate);
            eCal.setTime(edate);
            int cnt = 0;
            String inStr = "(";
            while (sCal.getTimeInMillis() <= eCal.getTimeInMillis()) {
                java.util.Date tmpdate = new Date();
                tmpdate.setTime(sCal.getTimeInMillis());
                inStr += "'" + dateFormat.format(tmpdate) + "',";
                sCal.add(Calendar.DAY_OF_MONTH, 1);
                cnt++;
            }
            if (cnt > 0) {
                inStr = inStr.substring(0, inStr.length() - 1) + ")";
                if (cnt > 100) {
                    inStr = " between '" + orgSdate + "' and '" + eDate + "'";
                }
            } else {
                inStr = "";
            }
            rtn = inStr;
        } catch (Exception e) {
            logger.error("getSqlDtCon:" + e);
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

    public static String getConvertString(HashMap<String, String> h, String div) {
        String result = "";

        for (String a : h.keySet())
            result += a + div;

        return result;
    }

    public static String getReg(String reg, String str_in) {
        String a = str_in;
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(a);
        String f = "";
        int breakchk = 0;
        while (m.find()) {
            f = (m.group(0));
            breakchk++;
            if (breakchk > 10000) break;
        }
        return f;
    }

    public static String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot) { // 문자열 자르기
        String r_val = szText;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE); // 태그제거 패턴
        if (isNotag) {
            r_val = p.matcher(r_val).replaceAll("");
        } // 태그 제거
        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", ""); // 공백제거
        try {
            byte[] bytes = r_val.getBytes("UTF-8"); // 바이트로 보관
            if (StringUtils.isNotEmpty(szKey)) {
                nLengthPrev = (r_val.indexOf(szKey) == -1) ? 0 : r_val.indexOf(szKey); // 일단 위치찾고
                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length; // 위치까지길이를 byte로 다시 구한다
                nLengthPrev = (nLengthPrev - nPrev >= 0) ? nLengthPrev - nPrev : 0; // 좀 앞부분부터 가져오도록한다.
            }
            // x부터 y길이만큼 잘라낸다. 한글안깨지게.
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
            r_val = new String(bytes, rF, rL, "UTF-8"); // charset 옵션
            if (isAdddot && rF + rL + 3 <= bytes.length) {
                r_val += "...";
            } // ...을 붙일지말지 옵션
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
        }
        return r_val;
    }

    public static Stack<String> gListStack(String o_list) {
        Stack<String> stack = new Stack<String>();
        StringTokenizer st = new StringTokenizer(o_list, "|||", false);
        while (st.hasMoreElements()) {
            String sel_o_list = st.nextElement().toString();
            stack.push(sel_o_list);
        }
        return stack;
    }

    public static int gAtCnt(String str1, String ar) {
        StringTokenizer st = new StringTokenizer(str1, ar);
        int id = -1;
        while (st.hasMoreElements()) {
            st.nextToken();
            id++;
        }
        return id;
    }

    /**
     * item 을 item 목록 맨 처음에 추가한다.
     * item 목록에 이미 item이 존재하면, 해당 위치의 item을 맨 처음으로 이동한다.
     *
     * @param url_list
     * @param item
     * @param ar
     * @return
     */
    public static String gAtFirst(String url_list, String item, String ar) {
        String rtval = "";
        String rt1 = "";
        if (url_list.indexOf(item) > -1) { // item 문자열이 url_list 에 존재하면
            StringTokenizer st = new StringTokenizer(url_list, ar); // url_list를 ar 구분자로 분할한다.
            while (st.hasMoreElements()) {  // item 을 제외한 나머지 url 을  [구분자]+[url] 형식으로 붙여나가고..
                String b = st.nextToken();
                if (b.equals(item)) {
                } else {
                    rt1 += ar + b;
                }
            }
            rt1 = item + rt1;
            url_list = rt1;
        } else {
            rt1 = item + ar + url_list;
        }
        rtval = rt1;
        return rtval;
    }

    /**
     * item 을 목록 맨 처음에 추가한다.<p>
     * 기존 목록에 item이 이미 존재하면, 해당 위치의 item을 맨 처음으로 이동한다. (by yseun)<br>
     * gAtFirst() 경우 : (1) 마지막에 구분자가 붙는 경우가 존재, (2) Item 단위 비교가 아님, (3) 리스트 크기 제한이 불가
     * addFirst() 경우 : (1) 리스트 크기 제한이 불가
     *
     * @param sItemList 기존 Item 리스트 문자열
     * @param sItem     추가할 Item
     * @param sSep      ItemList 에서 Item 구분하는 구분자 (Separator)
     * @return 변경된 Item 리스트
     * @author yseun (2015/11/20)
     */
    public static String addFirst(String sItemList, String sItem, String sSep) {
        while (sItemList.startsWith(sSep)) { // 기존 Item List 앞에 구분자가 있으면 지운다.
            sItemList = sItemList.substring(sSep.length());
        } // while
        while (sItemList.endsWith(sSep)) { // 기존 Item List 뒤에 구분자가 있으면 지운다.
            sItemList = sItemList.substring(0, sItemList.length() - sSep.length());
        } // while
        if (sItemList.indexOf(sItem) == -1) { // 추가할 item 문자열이 존재하지 않으며, 맨 앞에 item 추가하고 리턴
            return sItemList.length() == 0 ? sItem : String.format("%s" + sSep + "%s", sItem, sItemList);
        } else { // 추가할 item 문자열이 존재하면
            // 정확히 item 단위로 일치하는지 재확인하자
            final StringBuilder sb = new StringBuilder(sItemList.length() + sItem.length() + sSep.length() * 2);
            sb.append(sItem).append(sSep).append(sItemList).append(sSep);
            final String sItemToFind = String.format(sSep + "%s" + sSep, sItem); // item 앞뒤로 구분자를 붙여서 검색
            final int idx = sb.indexOf(sItemToFind, sItem.length());
            if (idx == -1) { // 기존 목록에 추가하는 Item 이 없다.
                return sb.substring(0, sb.length() - sSep.length());
            } else if (idx == sItem.length()) { // 기존 Item 목록의 첫 항목이 추가하려는 Item 이다. (수정사항 없음)
                return sItemList;
            } else { // 기존 목록에 Item 이 존재한다.
                sb.delete(idx, idx + sItemToFind.length() - sSep.length()); // 기존 item 삭제
                return sb.substring(0, sb.length() - sSep.length());
            } // if
        } // if
    } // addFirst()

    /**
     * 2015 04 20 한종상
     *
     * @param list
     * @param cmp
     * @param item  검색할 문자열
     * @param divi  list, cmp 의 구분자
     * @param dfval 값이 없을 때 기본값
     * @return
     */
    public static String gAtData(String list, String cmp, String item, String divi, String dfval) {
        String rt = "";

        if (StringUtils.isEmpty(item)) {
            rt = dfval;
        } else {
            int t = StringUtils.gAtId(cmp, item, divi);
            if (t > -1) {
                rt = StringUtils.gAt1(list, t, divi);
            }
        }

        if (StringUtils.isEmpty(rt)) rt = dfval;

        return rt;
    }

    public static String gAtData(String list, String cmp, String at, String divi) {
        String rt = "";

        int t = StringUtils.gAtId(cmp, at, divi);

        if (t > -1) {
            rt = StringUtils.gAt1(list, t, divi);
        }

        return rt;
    }

    /**
     * 2015 04 20 한종상
     * index 값을 리턴 , 찾는 값이 없을땐 -1를 리턴함.
     * ex) gAtId("a,b,c", "e", ",") -> -1
     * ex) gAtId("a,b,c", "a", ",") -> 0
     * ex) gAtId("a,b,c", "b", ",") -> 1
     * <p>
     * for 문 중에 데이터를 찾으면 값을 바로 return 하도록 변경 by min
     *
     * @param list
     * @param item
     * @param ar
     * @return
     */
    public static int gAtId(String list, String item, String ar) {
        StringTokenizer st = new StringTokenizer(list, ar);
        int id = -1;
        int a = id;
        while (st.hasMoreElements()) {
            id++;
            String tmp = st.nextToken();
            if (tmp.equals(item)) {
                a = id;
                return a;
            }
        }
        return a;
    }

    /**
     * 2015 04 20 한종상
     * 문자열 구분자로 분리 시킨뒤 해당 인덱스 값에 일치하는 문자를 리턴함.
     * ex) gAt1("a,b,c", 0, ","); -> "a"
     * ex) gAt1("a,b,c", 1, ","); -> "b"
     * ex) gAt1("a,b,c", 2, ","); -> "c"
     *
     * @param str1  문자열
     * @param index 인덱스
     * @param str   구분자
     * @return
     */
    public static String gAt1(String str1, int index, String str) {
        StringTokenizer st = new StringTokenizer(str1, str);
        String rtval = "";
        int id = 0;
        while (st.hasMoreElements()) {
            rtval = st.nextToken();
            if (id++ == index) break;
        }
        return rtval;
    }

    public static String[] getFixedSplit(String src, String delim, int splitSize) {
        if (splitSize <= 0) return null;
        if (src == null) src = "";
        StringTokenizer st = new StringTokenizer(src, delim);
        String[] strArray = new String[splitSize];
        int index = 0;
        while (st.hasMoreElements()) {
            strArray[index++] = st.nextToken();
            if (index >= splitSize) break;
        }
        for (; index < splitSize; index++) {
            strArray[index] = "";
        }
        return strArray;
    }

    public static String gAtSet(String str1, int a, String target, String ar) {
        StringTokenizer st = new StringTokenizer(str1, ar);
        String rtval = "";
        int id = 0;
        while (st.hasMoreElements()) {
            if (id++ == a) {
                st.nextToken();
                rtval += ar + target;
            } else {
                rtval += ar + st.nextToken();
            }
        }
        if (str1.length() > 0)
            rtval = rtval.substring(ar.length());
        return rtval;
    }

    public static String getURLDecodeStrR(String str, String type, int depth) {

        if (depth < 1) {
            // 인코딩 되어 있지 않은 url 의 경우 skip 하기 위해서 조건 변경함 % -> ://
            // url 인코딩에 경우 http:// -> http%3A%2F%2F 이런식으로 인코딩 된다.
            // 20150427 한종상
        } else if (str.indexOf("://") == -1) {
            str = getURLDecodeStr(str, type);
            str = getURLDecodeStrR(str, type, --depth);
        }
        return str;
    }

    public static String getURLDecodeStr(String str, String type) {
        String rtn = "";
        try {
            if (str != null && str.length() > 0) {
                try {
                    if (str.endsWith("%")) str = str + "25";
                    str = URLDecoder.decode(str, type);
                } catch (Exception e) {
                    str = str + "25";
                    try {
                        str = URLDecoder.decode(str, type);
                    } catch (Exception ex) {
                        str = "";
                    }
                }
                str = str.replace("&amp;amp;", "&");
                str = str.replace("&amp;", "&");
            } else {
                str = "";
            }
            rtn = str;
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
        }
        return rtn;
    }

    public static int getRangeVal(int s, int e) {
        ArrayList<Integer> a = StringUtils.getRandList(e);
        int r = a.get(0);
        return r + s;
    }

    public static ArrayList<Integer> getRandList(int cnt) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> rtnList = new ArrayList<Integer>();
        for (int i = 0; i < cnt; i++) {
            list.add(i);
        }
        Random trandom = new Random();
        while (list.size() > 0) {
            int tx = trandom.nextInt(list.size());
            if (tx == list.size()) tx = tx - 1;
            rtnList.add(list.get(tx));
            list.remove(tx);
        }
        return rtnList;
    }

    public static String getPartIdKey(String ipKey) {
        String result = "";
        try {
            if (ipKey.length() < 2) {
                ipKey = "000" + ipKey;
            }
            String ipStr = ipKey.replace(".", "");
            ipStr = ipStr.substring(ipStr.length() - 2, ipStr.length());
            SimpleDateFormat mm = new SimpleDateFormat("MM");
            java.util.Date date = new java.util.Date();
            String m = mm.format(date);
            result = ((Integer.parseInt(ipStr) % 30) + 10) + m;
        } catch (Exception e) {
            logger.error("getPartIdKey:" + e);
        }
        return result;
    }

    public static String getPrevPartIdKey(String ipKey) {
        String result = "";
        try {
            if (ipKey.length() < 2) {
                ipKey = "000" + ipKey;
            }
            String ipStr = ipKey.replace(".", "");
            ipStr = ipStr.substring(ipStr.length() - 2, ipStr.length());
            SimpleDateFormat mm = new SimpleDateFormat("MM");
            java.util.Date date = new java.util.Date();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            date.setTime(cal.getTimeInMillis());
            String m = mm.format(date);
            result = ((Integer.parseInt(ipStr) % 30) + 10) + m;
        } catch (Exception e) {
            logger.error("getPrevPartIdKey:" + e);
        }
        return result;
    }

    public static String unescape(String inp) {
        StringBuffer rtnStr = new StringBuffer();
        char[] arrInp = inp.toCharArray();
        int i;
        for (i = 0; i < arrInp.length; i++) {
            if (arrInp[i] == '%') {
                String hex;
                if (arrInp[i + 1] == 'u') {    //유니코드.
                    hex = inp.substring(i + 2, i + 6);
                    i += 5;
                } else {    //ascii
                    hex = inp.substring(i + 1, i + 3);
                    i += 2;
                }
                try {
                    rtnStr.append(Character.toChars(Integer.parseInt(hex, 16)));
                } catch (NumberFormatException e) {
                    rtnStr.append("%");
                    i -= (hex.length() > 2 ? 5 : 2);
                }
            } else {
                rtnStr.append(arrInp[i]);
            }
        }

        return rtnStr.toString();
    }

    public static String getRefMass(String refURL) {
        if (refURL == null) refURL = "";
        refURL = refURL.toLowerCase();
        String refMass = "";
        try {
            refMass = refURL.replace("http://", "");
            refMass = refMass.replace("https://", "");
            if (refMass.indexOf("/") > -1) {
                refMass = refMass.substring(0, refMass.indexOf("/"));
            } else if (refURL.indexOf("?") > -1) {
                refMass = refMass.substring(0, refMass.indexOf("?"));
            } else if (StringUtils.isEmpty(refMass)) {
                refMass = "bookmark";
            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return refMass;
        }
        return refMass;
    }

    /**
     * 도메인 체크 함수 pjm
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
        Pattern p = Pattern.compile("((?:[a-z][a-z\\.\\d\\-]+)\\.(?:[a-z][a-z\\-]+))(?![\\w\\.])", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(url);
        return m.find();
    }

    /**
     * 도메인 가공 함수
     * http://www.styleberry.co.kr/index.html -> styleberry.co.kr <br/>
     *
     * @param url
     * @return
     */
    public static String getDestDomain(String url) {
        String destDomain = url;
        url = url.toLowerCase();
        try {
            if (url != null && url.trim().length() != 0) {
                if (url.indexOf("http://") != -1) {
                    destDomain = url.substring(7, url.length());
                } else if (url.indexOf("https://") != -1) {
                    destDomain = url.substring(8, url.length());
                }
                if (url.indexOf("www.") != -1) {
                    destDomain = destDomain.substring(4, destDomain.length());
                }
                if (destDomain.indexOf("/") > -1) {
                    destDomain = destDomain.substring(0, destDomain.indexOf("/"));
                }
                if (destDomain.indexOf("?") > -1) {
                    destDomain = destDomain.substring(0, destDomain.indexOf("?"));
                }

            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return destDomain;
        }
        return destDomain;
    }

    public static String getDestDomainExwww(String url) {
        String destDomain = url;
        url = url.toLowerCase();
        try {
            if (url != null && url.trim().length() != 0) {
                if (url.indexOf("http://") != -1) {
                    destDomain = url.substring(7, url.length());
                } else if (url.indexOf("https://") != -1) {
                    destDomain = url.substring(8, url.length());
                }
                if (destDomain.indexOf("/") > -1) {
                    destDomain = destDomain.substring(0, destDomain.indexOf("/"));
                }
                if (destDomain.indexOf("?") > -1) {
                    destDomain = destDomain.substring(0, destDomain.indexOf("?"));
                }

            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return destDomain;
        }
        return destDomain;
    }

    public static String getParamName(Map<String, String> list, String refMass) {
        String tempParamName = "";
        for (int i = 0; i < list.size(); i++) {
            Iterator<String> it = list.keySet().iterator();
            while (it.hasNext()) {
                String temp = it.next();
                if (refMass.equals(temp)) {
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
        if (StringUtils.isNotEmpty(chooseName)) {
            StringTokenizer st = new StringTokenizer(url.substring(url.indexOf("?") + 1), "&");
            while (st.hasMoreTokens()) {
                String[] pair = st.nextToken().split("=");
                if (pair[0].equals(chooseName)) {
                    v = pair[1];
                }
            }
        }
        return v;
    }
    
    public static String getParamValue2(String url, String chooseName) {
        //String temp = "http://search.naver.com/search.nhn?one=1&two=2&three=3";
        String v = "noKeyword";
        try {
            if (StringUtils.isNotEmpty(chooseName)) {
                StringTokenizer st = new StringTokenizer(url.substring(url.indexOf("?") + 1), "&");
                while (st.hasMoreTokens()) {
                    String[] pair = st.nextToken().split("=");
                    if (pair[0].equals(chooseName)) {
                        v = pair[1];
                    }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return v;
    }

    /**
     * null 체크
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
    }

    /**
     * null 체크
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.isEmpty(str);
    }

    /**
     * 숫자인지 체크
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return org.apache.commons.lang3.StringUtils.isNumeric(str);
    }

    /**
     * 소스점 제거
     * 문자열 숫자만 체크 리턴
     *
     * @param str
     * @return
     */
    public static String numChk(String str) {
        String numeral = "";
        // 소스점 이하 버림 제거함.
        str = str.indexOf(".") != -1 ? str.split("[.]")[0] : str;
        try {
            if (str == null) numeral = null;
            else {
                String patternStr = "\\d"; //숫자를 패턴으로 지정
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    numeral += matcher.group(0); //지정된 패턴과 매칭되면 numeral 변수에 넣는다. 여기서는 숫자!!
                }
            }
        } catch (Exception e) {
            return "0";
        }
        return numeral;
    }





    /**
     * 특수문자 제거
     *
     * @param str
     * @return
     */
    public static String StringReplace(String str) {
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str = str.replaceAll(match, " ");
        return str;
    }

    /**
     * Null 체크 후 빈값 return;
     *
     * @param nvl
     * @return
     */
    public static String nvl(String nvl) {
        return nvl(nvl, "");
    }

    /**
     * Null 체크 후 대체값 return;
     *
     * @param nvl
     * @param rep
     * @return
     */
    public static String nvl(String nvl, String rep) {
        if (nvl != null) {
            return nvl.trim();
        } else {
            return rep;
        }
    }

    /**
     * Null 체크 후 숫자형태로 변화하여 return;
     * 에러시에는 error 시 대응할 int 값을 리턴시킴.
     *
     * @param nvl
     * @param error
     * @return int
     */
    public static int nvlNumber(String nvl, int error) {
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
     * Null 체크 후 숫자형태로 변화하여 return;
     * 에러시에는 error 시 대응할 int 값을 리턴시킴.
     *
     * @param nvl
     * @return int
     */
    public static float nvlFloat(String nvl) {
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

    // 앞에3자리 숫자 빼고 전부 삭제
    public static int removeString(String number, int index, String str) {
        number = StringUtils.gAt1(number, index, str);
        number = number.replaceAll("[^0-9]", "");
        try {
            if (number.length() > 3)
                number = number.substring(0, 3);
            return Integer.parseInt(number);
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return 0;
        }
    }

    // 넘어온 숫자만큼 쪼개서 넘어온 카운트
    public static int onlyInt(String number, int count) {
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
     * Null 체크 후 빈값 return;
     *
     * @param number
     * @return
     */
    public static long numberLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            logger.error(ErrorLog.getDebugStack(e));
            return 0;
        }
    }

    /**
     * Null 체크 후 빈값 return;
     *
     * @param number
     * @return
     */
    public static int number(String number) {
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            //logger.error(ErrorLog.getDebugStack(e));
            return 0;
        }
    }

    public static float tofloat(String number) {
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return 0.1f;
        }
    }

    public static int intCast(Object value) {
        int intValue = 0;
        try {
            intValue = Integer.valueOf(String.valueOf(value));
        } catch (Exception e) {
        }
        return intValue;
    }

    public static float flaotCast(Object value) {
        float intValue = 0;
        try {
            intValue = Float.valueOf(String.valueOf(value));
        } catch (Exception e) {
        }
        return intValue;
    }

    public static long longCast(Object value) {
        long intValue = 0;
        try {
            intValue = Long.valueOf(String.valueOf(value));
        } catch (Exception e) {
        }
        return intValue;
    }


    /**
     * String substring start TO end
     *
     * @param str
     * @param startLen
     * @param endLen
     * @return
     * @throws Exception
     */
    public static String stringSplit(String str, int startLen, int endLen) throws Exception {
        String strResult = nvl(str);
        if (str != null && str.length() == endLen) {
            strResult = strResult.substring(startLen, endLen);
        } else if (str != null && str.length() >= endLen) {
            strResult = strResult.substring(startLen, endLen) + "...";
        }
        return strResult;
    }

    /**
     * String url http:// 존제여부 체크하여 http://+원문 리턴
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String stringHttpPlus(String str) throws Exception {
        String strResult = nvl(str);
        if (str != null && str.length() > 4) {
            if (strResult.substring(0, 4).indexOf("http") > -1) {
            } else {
                strResult = "http://" + strResult;
            }
        }
        return strResult;
    }


    /**
     * 이미지코드 예외처리
     *
     * @param ms
     * @return
     */
//    public static String imgCodeChk(MediaScriptData ms) {
//        try {
//            return ms.getImgcode();
//        } catch (Exception e) {
//            logger.error(ErrorLog.getStack(e));
//        }
//        return "";
//    }

    /**
     * 테이블에 저장된 bannerPath
     * bannerpath1 = 360 * 50
     * imgname = 250 * 250
     * imgname2 = 120 * 600
     * imgname3 = 728 * 90
     * imgname4 = 300 * 180
     * imgname5 = 800 * 1500
     * imgname6 = 엣지
     * imgname7 = 160 * 300
     * imgname8 = 300 * 65
     * imgname9 : 850 * 800
     * imgname10 : 960 * 100
     * imgname11 : 720 * 1230
     * imgname12 : 160 * 600
     * imgname13 : 640 * 350
     * imgname14 : 250 * 250 (쇼플전용)
     *
     * @param igb  web:igb ,mobile:bntype, app:imgType
     * @param type
     * @return
     */
    public static String getChangeIgb(String igb, String type) {
        String img = "";
        try {
            if (GlobalConstants.MOBILE.equals(type)) {
                if ("30".equals(igb) || "31".equals(igb) || "32".equals(igb) || "33".equals(igb) || "34".equals(igb) || "35".equals(igb) || "36".equals(igb) || "38".equals(igb) || "41".equals(igb) || "42".equals(igb) || "43".equals(igb) || "51".equals(igb) || "98".equals(igb) || "52".equals(igb) || "47".equals(igb)) {
                    img = GlobalConstants.IMGNAME1_250_250;
                } else if ("44".equals(igb) || "45".equals(igb) || "49".equals(igb) || "99".equals(igb)) {
                    img = GlobalConstants.IMGNAME4_300_180;
                } else if ("56".equals(igb) || GlobalConstants.MOBILE_BRANDLINK.equals(igb)) {
                    img = GlobalConstants.IMGNAME5_800_1500;
                } else if ("39".equals(igb)) {
                    img = GlobalConstants.IMGNAME6_EDGE;
                } else {
                    img = GlobalConstants.MOBILEIMG_360_50;
                }
            } else if (GlobalConstants.APP.equals(type)) {
                if ("banner1".equals(igb)) {
                    img = GlobalConstants.MOBILEIMG_360_50;
                } else if ("1".equals(igb)) {
                    img = GlobalConstants.IMGNAME1_250_250;
                } else if ("4".equals(igb)) {
                    img = GlobalConstants.IMGNAME4_300_180;
                } else if ("5".equals(igb)) {
                    img = GlobalConstants.IMGNAME5_800_1500;
                } else if ("11".equals(igb)) {// 11, 13, 14 는 AdbnSdkApi 쪽에서 독립적으로 구현되어 돌아가고 있으므로 현재 사용하지 않습니다.
                    img = GlobalConstants.IMGNAME11_720_1230;
                } else if ("13".equals(igb)) {
                    img = GlobalConstants.IMGNAME13_640_350;
                } else if ("14".equals(igb)) {
                    img = GlobalConstants.IMGNAME14_250_250;
                }
            } else {
                if ("1".equals(igb) || "71".equals(igb) || "74".equals(igb) || "80".equals(igb) || "81".equals(igb) || "61".equals(igb) || "85".equals(igb) || "86".equals(igb)) {
                    img = GlobalConstants.IMGNAME1_250_250;
                } else if ("32".equals(igb) || "76".equals(igb) || "64".equals(igb)) {
                    img = GlobalConstants.IMGNAME2_120_600;
                } else if ("60".equals(igb) || "72".equals(igb) || "75".equals(igb) || "82".equals(igb) || "83".equals(igb)) {
                    img = GlobalConstants.IMGNAME3_728_90;
                } else if ("62".equals(igb) || "92".equals(igb) || "93".equals(igb)) {
                    img = GlobalConstants.IMGNAME4_300_180;
                } else if ("5".equals(igb) || "90".equals(igb) || "91".equals(igb)) {
                    img = GlobalConstants.IMGNAME5_800_1500;
                } else if ("7".equals(igb) || "70".equals(igb)) {
                    img = GlobalConstants.IMGNAME6_EDGE;
                } else if ("36".equals(igb) || "66".equals(igb) || "67".equals(igb) || "68".equals(igb) || "73".equals(igb)) {
                    img = GlobalConstants.IMGNAME7_160_300;
                } else if ("65".equals(igb) || "63".equals(igb) || "82".equals(igb) || "94".equals(igb)) {
                    img = GlobalConstants.IMGNAME8_300_65;
                } else if ("69".equals(igb)) {
                    img = GlobalConstants.IMGNAME12_160_600;
                }
            }

            if (StringUtils.isEmpty(img)) {        // 정의 되지 않은 igb인 경우 250x250 사이즈를 넣는다.
                img = GlobalConstants.IMGNAME1_250_250;
            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            img = "";
        }
        return img;
    }


    
    /**
     * @param scfg        광고주 정보(info)
     * @param adgubun     광고구분
     * @param ms          매체정보(s값)
     * @param types       nor, mbb, mbw, mba, mbe 등등의 클릭유형
     * @param clk_param   클릭시 이미지 url로 해당 주소 호출
     * @param target      모바일, 웹에 대한 구분(WEB, MOBILE)
     * @param clktracking 클릭시 클릭된 곳으로 렌딩
     * @param keyIp       IP정보를 입력
     * @param rtbParam    클릭시 RTB 데이터 클릭수 증가(몽고에)
     * @param freqLog
     * @return 클릭할 렌딩 주소값
     */
//    public static String setJsonObjAndReturnNtlink(
//            AdConfigData scfg,
//            String adgubun,
//            MediaScriptData ms,
//            String types,
//            String clk_param,
//            String target,
//            String clktracking,
//            String keyIp,
//            String rtbParam,
//            int freqLog,
//            String shortCut,
//            String scriptId) {
//        try {
//            String purl = randingPurl(scfg, adgubun, target);
//            // 키워드 관련 유일 key값
//            String keylog_no = scfg.getKno();
//            if (keylog_no == null) keylog_no = "";
//
//            // 상품관련 유일key값, DB 컬럼이 int로되어있어서, 인트로 변환 후, 다시 스트링으로 변환하여 처리함.
//            String shop_logno = scfg.getNo();
//            if (StringUtils.isEmpty(shop_logno) || "0".equals(shop_logno)) shop_logno = scfg.getPcode();
//            if (StringUtils.isNotEmpty(shop_logno)) {
//                shop_logno = shop_logno.replaceAll("[^0-9]", "");
//                if (shop_logno.length() > 9)
//                    shop_logno = shop_logno.substring(shop_logno.length() - 9, shop_logno.length());
//                if ("".equals(shop_logno)) shop_logno = "0";
//                else shop_logno = String.valueOf(StringUtils.numChk(shop_logno));
//            }
//            //투뎁스, 앱설치 url 인코딩 처리
//            String twoDepthUrl = "";
//            if (StringUtils.isNotEmpty(scfg.getTurl())) {
//                twoDepthUrl = changeHttpsImgPath(scfg.getTurl(), ms.getNo());
//                if (!(twoDepthUrl.indexOf(("%3A%2F%2F")) > -1)) {
//                    try {
//                        twoDepthUrl = URLEncoder.encode(twoDepthUrl, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                    } // 이 예외는 절대 발생안함
//                }
//            }
//            //클릭링크를 리턴합니다.
//            return drcHtml(
//                    shop_logno,
//                    keylog_no,
//                    ms.getNo(),
//                    adgubun,
//                    scfg.getSite_code(),
//                    ms.getNo(),
//                    scfg.getMcgb(),
//                    scfg.getUserid(),
//                    GlobalConstants.MOBILE.equals(target) ? types : GlobalConstants.PNT,
//                    purl != null ? URLEncoder.encode(URLEncoder.encode(purl, "UTF-8"), "UTF-8") : "",
//                    isNotEmpty(clk_param) ? URLEncoder.encode(clk_param, "euc-kr") : "",
//                    scfg.getPcode(),
//                    clktracking,
//                    keyIp,
//                    rtbParam,
//                    scfg.getPb_gubun(),
//                    freqLog,
//                    shortCut,
//                    scriptId,
//                    GlobalConstants.MOBILE.equals(target) ? twoDepthUrl : "",
//                    GlobalConstants.MOBILE.equals(target) ? scfg.getTgubun() : "",
//                    scfg.getAbtest(), scfg.getSubGubun(), scfg.getUserNearCode(), scfg.getFromApp());
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e); // 이 예외는 절대 발생안함
//        }
//    }

  

    /**
     * 이미지 확장자 체크
     *
     * @param img
     * @return true
     */
    public static boolean imgChk(String img) {
        try {
//		 		String imgName = img.toLowerCase(); // 무효한 코드라 지움
            String imgName = img.substring(img.lastIndexOf(".") + 1, img.length()).trim();
            final String[] imgPattern = {"jpg", "jpeg", "png", "gif", "bmp", "tif"};
            int len = imgPattern.length;
            for (int i = 0; i < len; i++) {
                if (imgName.equalsIgnoreCase(imgPattern[i])) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return false;
        }
        return false;
    }

    /**
     * 이미지 확장자 체크, 만약 이미지가 / 뒤에 아무런값이 없을 경우에는 해당 이미지가 잘못된 이미지로 판단한다.
     * 특정한 엑박이미지의 경우에도 잘못된 이미지로 판단하고 팅겨낸다.
     *
     * @param img
     * @return true
     */
    public static boolean shopDataChk(String img) {
        try {
            String imgName = img.toLowerCase().trim();
            imgName = imgName.substring(imgName.lastIndexOf("/") + 1, imgName.length());
            if (StringUtils.isEmpty(imgName)) return false;
            else if (imgName.indexOf("img_product_big.gif") != -1 || imgName.indexOf("img_product_medium.gif") != -1 || imgName.indexOf("img_product_small.gif") != -1)
                return false;
            else return true;
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return false;
        }
    }

    /**
     * 특수문자 표현
     *
     * @param srcString
     * @return String
     * @throws Exception
     */
    public static String getLinkStrReplace(String srcString) {
        String tmpString = srcString;
        try {
            if (StringUtils.isNotEmpty(tmpString)) {
                tmpString = tmpString.replaceAll("&lt;", "<");
                tmpString = tmpString.replaceAll("&gt;", ">");
                tmpString = tmpString.replaceAll("&amp;", "&");
                tmpString = tmpString.replaceAll("&nbsp;", " ");
                tmpString = tmpString.replaceAll("&apos;", "\'");
                tmpString = tmpString.replaceAll("&quot;", "\"");
            }
        } catch (Exception e) {
//            logger.error(ErrorLog.getStack(e));
        }
        return tmpString;
    }

    /**
     * @param productName
     * @return
     */
    public static boolean filterString(String productName) {
        productName = productName.toLowerCase();
        String[] filterPnameArr = PropertyHandler.getString("FILTER_SHOP_DATA_PNAME").split(",");
        for (String pname : filterPnameArr) {
            if (productName.indexOf(pname) >= 0) return true;
        }
        return false;
    }

    /**
     * 기존배너 상품갯수
     *
     * @param types
     * @param adgubun
     * @param cnt
     * @return
     */
    public static int getMaxCnt(String types, String igb, String adgubun, String cnt) {
        int max = 0;
        if (StringUtils.isNotEmpty(cnt)) {
            int tmp = Integer.valueOf(cnt);
            max = tmp == 1 || tmp == 0 ? max++ : tmp - 1;
        } else {
            max = 2;
        }
        return max;
    }

//    public static String makePlayLinkPurl(String sKno, String sS, String sGb, String sSc, String sMc, String sU, String sSlink, String sMcgb, String sPcode, String product, String sKeyIp, String rtbParam, int freqLog, String abTest) {
//        if (GlobalConstants.PLAY_LINK.equalsIgnoreCase(product) || GlobalConstants.PLAY_LINK_MOBILE.equalsIgnoreCase(product)) {
//            return makePurl("http://www.dreamsearch.or.kr/servlet/pldrc", sKno, sS, sGb, sSc, sMc, sU, sSlink, sMcgb, sPcode, product, sKeyIp, rtbParam, freqLog, abTest, null);
//        }
//        return makePurl("http://www.dreamsearch.or.kr/servlet/sdrc", sKno, sS, sGb, sSc, sMc, sU, sSlink, sMcgb, sPcode, product, sKeyIp, rtbParam, freqLog, abTest, null);
//    }
//
//    public static String makePlayLinkPurl(String sKno, String sS, String sGb, String sSc, String sMc, String sU, String sSlink, String sMcgb, String sPcode, String product, String sKeyIp, String rtbParam, int freqLog, String abTest, String userNearCode) {
//        if (GlobalConstants.PLAY_LINK.equalsIgnoreCase(product) || GlobalConstants.PLAY_LINK_MOBILE.equalsIgnoreCase(product)) {
//            return makePurl("http://www.dreamsearch.or.kr/servlet/pldrc", sKno, sS, sGb, sSc, sMc, sU, sSlink, sMcgb, sPcode, product, sKeyIp, rtbParam, freqLog, abTest, userNearCode);
//        }
//        return makePurl("http://www.dreamsearch.or.kr/servlet/sdrc", sKno, sS, sGb, sSc, sMc, sU, sSlink, sMcgb, sPcode, product, sKeyIp, rtbParam, freqLog, abTest, userNearCode);
//    }

   

    /**
     * encoding error modify
     *
     * @param purl
     * @return
     */
    public static boolean chkIndibrandUrl(String purl) {
        if (!(purl.indexOf("indibrand") > 0))
            return true;
        else {
            if (purl.indexOf("shop2.indibrand") > 0) {
                return false;
            } else if (purl.indexOf("shop3.indibrand") > 0) {
                return false;
            } else if (purl.indexOf("shop4.indibrand") > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * encoding error modify
     * 유입시 다른 광고솔류션의 파라미터 및 인코딩이 깨진 한글검색어를 제거한 후 상품을 수집하는 로직
     * 공통로직화로 인한 프로퍼티로 제거 (shop_log 쿠키에서 처리)
     * 하.. 진짜 노답 메소드.. 여기서 에러 겁나 남 ㅡㅡ
     * 이거 뜯어 고칠 예정...
     *
     * @param purl
     * @return
     */
    @Deprecated
    public static String returnKeywordDelUrl(String purl) {
        if (isEmpty(purl) || !purl.startsWith("http")) {
            return "";
        }
//		logger.info("purl>>>>>>>>>>>>" + purl);
        String[] tempList = purl.split("&");
        String temp = "";
        StringBuffer url = new StringBuffer();


        String[] rfShopList = (
                PropertyHandler.getProperty("RFSHOP_EXCEPTION_PARAMETER") +
                        PropertyHandler.getProperty("RFSHOP_EXCEPTION_PARAMETER1") +
                        PropertyHandler.getProperty("RFSHOP_EXCEPTION_PARAMETER2")).split(",");

        for (int i = 0; i < tempList.length; i++) {
            temp = tempList[i];
            if (i == 0 && temp.startsWith("http") && temp.indexOf("?") > 0) {
                String[] tempUrl = temp.split("\\?");
                url.append(tempUrl[0]).append("?");
                temp = "";
                for (int j = 1; j < tempUrl.length; j++) {
                    temp += tempUrl[j];
                }
            }
//			logger.info("after loop >>>>>>>>>>>>" + url);
//			logger.info("after loop temp>>>>>>>>>>>>" + temp);

            boolean chk = true;
            for (int j = 0; j < rfShopList.length; j++) {
                String[] tempParameter = null;
                if (temp.indexOf("=") > 0) {
                    tempParameter = temp.split("=");
                }
                if (tempParameter != null) {
                    if (tempParameter[0].equals(rfShopList[j])) {
                        chk = false;
                        break;
                    }
                }
            }
            if (chk) {
                if (i != 0) url.append("&");
                url.append(temp);
            }
        }

        temp = url.toString();

        try {
//			temp = new String(temp.getBytes("8859_1"), "UTF-8");
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e, "CLASS:returnKeywordDelUrl", temp));
        }
//		logger.info("end process >>>>>>>>>>>>" + url);


        return temp;
    }


    private static String readAll(Reader rd) {
        StringBuilder sb = new StringBuilder();
        try {
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
        }
        return sb.toString();
    }

 
   

    /**
     * 리얼클릭 노출 오차로 인해서 만든 임시성 함수
     * 해당 프로퍼티로 on/off 할 수 있다.
     *
     * @param scriptNo
     * @return
     */
    @Deprecated
    public static boolean isUsedServerToServer(String scriptNo) {
        boolean beUserdSeverToServer = PropertyHandler.getBoolean("TEMP_USE_SERVER_TO_SERVER");
        if (!beUserdSeverToServer) return false;
        String scriptNumbers = PropertyHandler.getString("TEMP_USER_REALCLICK_S_NO");
        return scriptNumbers.indexOf(scriptNo) != -1;
    }

    

    /**
     * @param adgubun
     * @param sc
     * @param userid
     * @param product
     * @param coup_no
     * @param media_no
     * @param script_no
     * @param payflag
     * @param viewLink
     * @return
     */
    public static String drcCoupon(
            String adgubun,
            String sc,
            String userid,
            String product,
            String coup_no,
            String media_no,
            String script_no,
            String payflag,
            boolean viewLink) {
        StringBuffer drc = new StringBuffer();

        if (viewLink) {
            drc.append("/servlet/CouponViewApi?adgubun=");

            if (StringUtils.isNotEmpty(adgubun)) {
                drc.append(adgubun);
            }
            if (StringUtils.isNotEmpty(adgubun)) {
                drc.append("&gb=").append(adgubun);
            }
            if (StringUtils.isNotEmpty(sc)) {
                drc.append("&sc=").append(sc);
            }

            if (StringUtils.isNotEmpty(userid)) {
                drc.append("&userid=").append(userid);
            }
            if (StringUtils.isNotEmpty(userid)) {
                drc.append("&u=").append(userid);
            }
            if (StringUtils.isNotEmpty(media_no)) {
                drc.append("&media_no=").append(media_no);
            }
            if (StringUtils.isNotEmpty(product)) {
                drc.append("&product=").append(product);
            }
            if (StringUtils.isNotEmpty(coup_no)) {
                drc.append("&coup_no=").append(coup_no);
            }
            if (StringUtils.isNotEmpty(script_no)) {
                drc.append("&script_no=").append(script_no);
            }
            if (StringUtils.isNotEmpty(payflag)) {
                drc.append("&payflag=").append(payflag);
            }

            return drc.toString();
        } else {
            if (payflag.equals("1")) {
                drc.append("/servlet/CouponFreeDownApi?adgubun=");

                if (StringUtils.isNotEmpty(adgubun)) {
                    drc.append(adgubun);
                }
                if (StringUtils.isNotEmpty(adgubun)) {
                    drc.append("&gb=").append(adgubun);
                }
                if (StringUtils.isNotEmpty(sc)) {
                    drc.append("&sc=").append(sc);
                }

                if (StringUtils.isNotEmpty(userid)) {
                    drc.append("&userid=").append(userid);
                }
                if (StringUtils.isNotEmpty(userid)) {
                    drc.append("&u=").append(userid);
                }
                if (StringUtils.isNotEmpty(media_no)) {
                    drc.append("&media_no=").append(media_no);
                }
                if (StringUtils.isNotEmpty(product)) {
                    drc.append("&product=").append(product);
                }
                if (StringUtils.isNotEmpty(coup_no)) {
                    drc.append("&coup_no=").append(coup_no);
                }
                if (StringUtils.isNotEmpty(script_no)) {
                    drc.append("&script_no=").append(script_no);
                }
                if (StringUtils.isNotEmpty(payflag)) {
                    drc.append("&payflag=").append(payflag);
                }

                return drc.toString();
            } else {
                drc.append("/servlet/CouponPayDownApi?adgubun=");

                if (StringUtils.isNotEmpty(adgubun)) {
                    drc.append(adgubun);
                }
                if (StringUtils.isNotEmpty(adgubun)) {
                    drc.append("&gb=").append(adgubun);
                }
                if (StringUtils.isNotEmpty(sc)) {
                    drc.append("&sc=").append(sc);
                }
                if (StringUtils.isNotEmpty(script_no)) {
                    drc.append("&script_no=").append(script_no);
                }
                drc.append("&tryChk=T");
//					return drc.toString();
            }
        }

        return drc.toString();
    }

   

    /**
     * 퀴즈형 웹, 모바일 배너 특정 매체지면 스크립트번호 확인
     * 작성자: 조규홍
     * 작성일자: 2016-06-14
     *
     * @param s
     * @return
     */
    public static boolean checkQuizBanner(String s) {
        try {
            if (PropertyHandler.getProperty("WEB_QUIZ_NO").indexOf(s + ",") >= 0
                    || PropertyHandler.getProperty("MOBILE_QUIZ_NO").indexOf(s + ",") >= 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 3번정도 (0,1,2) 상품을 쪼개면서 최대한 shop_log복구시킴.
     * 빈값일 경우 어차피 몽고에서 상품을 뒤질테니까!!!!!
     *
     * @param s
     * @return
     */
    public static String returnShopLog(String shopLog, int cnt) {
        try {
            if (cnt > 2) return "";
            shopLog = shopLog.substring(0, shopLog.lastIndexOf("},")) + "}]}";
            System.out.println(shopLog);
            shopLog = URLDecoder.decode(shopLog, "utf-8");
            System.out.println(shopLog.length());
            if (shopLog.length() < 32) return ""; // site_code의 길이는 32byte로 그 이하면 shop_log로서의 의미가 없음.
            return shopLog;
        } catch (Exception e) {
            return returnShopLog(shopLog, ++cnt);
        }
    }

    /**
     * 문자열을 구분자로 구분하여 잘라 리스트에 담기 구분
     * StringTokenizer 클래스: 생성자에 단 한 문자 구분자만 사용하여 토큰으로 분리한다.(여러개의 구분자는 사용가능하다)
     * 작성자: 조규홍
     * 작성일자 : 2016-06-16
     *
     * @param ktype
     * @param pattern
     * @return
     */
    public static ArrayList<String> splitFunction(String ktype, String pattern) {
        StringTokenizer st = new StringTokenizer(ktype, pattern);
        ArrayList<String> userList = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            userList.add(st.nextToken());
        }
        return userList;
    }

    private static final Pattern PATTERN_WHITE_SPACE = Pattern.compile("\\s+");

    /**
     * 모든 공백 제거
     */
    public static String removeWhiteSpace(CharSequence cs) {
        return PATTERN_WHITE_SPACE.matcher(cs).replaceAll("");
    } // removeWhiteSpace()


    /**
     * @param data
     * @return String
     * @method convertToHex
     * @see
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
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException String
     * @method sha1
     * @see
     */
    public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    /**
     * @param request
     * @return String
     * @method deviceIp
     * @see
     */
    public static String deviceIp(HttpServletRequest request) {
        String deviceIp = request.getHeader("X-Forwarded-For");
        if (deviceIp == null || deviceIp.length() == 0 || "unknown".equalsIgnoreCase(deviceIp)) {
            deviceIp = request.getHeader("Proxy-Client-deviceIp");
        }
        if (deviceIp == null || deviceIp.length() == 0 || "unknown".equalsIgnoreCase(deviceIp)) {
            deviceIp = request.getHeader("WL-Proxy-Client-deviceIp");
        }
        if (deviceIp == null || deviceIp.length() == 0 || "unknown".equalsIgnoreCase(deviceIp)) {
            deviceIp = request.getHeader("HTTP_CLIENT_deviceIp");
        }
        if (deviceIp == null || deviceIp.length() == 0 || "unknown".equalsIgnoreCase(deviceIp)) {
            deviceIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (deviceIp == null || deviceIp.length() == 0 || "unknown".equalsIgnoreCase(deviceIp)) {
            deviceIp = request.getRemoteAddr();
        }
        return deviceIp;
    }

    public static void sendPostData(JSONObject obj, String url) {
        HttpURLConnection conn = null;
        try {
            URL sendUrl = new URL(url);
            conn = (HttpURLConnection) sendUrl.openConnection();
            conn.setConnectTimeout(500);
            conn.setReadTimeout(500);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String param = obj.toString();

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            //파라미터를 osw 에 넣고 flush
            osw.write(param);
            osw.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
            } else {
                long start = System.currentTimeMillis();
                logger.info("Url Do not Send info : GET_URL : " + url + "startTime :" + start);
            }
            // 닫기
            osw.close();
        } catch (MalformedURLException e) {
//            logger.error(ErrorLog.getStack(e));
        } catch (ProtocolException e) {
//            logger.error(ErrorLog.getStack(e));
        } catch (UnsupportedEncodingException e) {
//            logger.error(ErrorLog.getStack(e));
        } catch (IOException e) {
//            logger.error(ErrorLog.getStack(e));
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * IP 검증
     * IPv6 체크 로직 추가
     *
     * @param ipAddress
     * @return String
     * @method ipVerification
     * @author HaeSungJu
     * @see
     */
    public static boolean ipVerification(String ipAddress) {
        // IP
        String pattern = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
        List<String> patternIps = null;
        try {
            // IPv6인 경우 별도 error를 찍지 않는다. 20170809 hsju
//      if (checkIPv6(ipAddress)) throw new Exception("IPv6 ERROR : " + ipAddress);
            if (checkIPv6(ipAddress)) return false;
            patternIps = patternMatch(pattern, ipAddress);

//      if (StringUtils.isEmpty(patternIps.get(0))) throw new Exception("PATTERN ERROR : " + ipAddress);
            if (StringUtils.isEmpty(patternIps.get(0))) return false;
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return false;
        }
        return true;
    }

    /**
     * IP split
     * xxx.xxx.xxx.xxx.xxxxxx 인경우 뒤에 .xxxxxx 자른 후 리턴
     *
     * @param ipAddress
     * @return String
     * @method ipSplit
     * @author HaeSungJu
     * @see
     */
    public static String[] ipSplit(String ipAddress) {
        String[] ips = new String[2];
        try {
            if (StringUtils.isEmpty(ipAddress)) return null;
            int count = countOccurrencesOf(ipAddress, ".");
            if (count > 3) {
                ips[0] = ipAddress.substring(0, ipAddress.lastIndexOf("."));
                ips[1] = ipAddress.substring(ipAddress.lastIndexOf("."));
            } else {
                ips[0] = ipAddress;
                ips[1] = "0";
            }
        } catch (Exception e) {
            logger.error(ErrorLog.getStack(e));
            return null;
        }
        return ips;
    }

    /**
     * IP주소 정수 변환
     * MYSQL INET_ATON
     *
     * @param ipAddress
     * @return long
     * @method ipToLong
     * @author HaeSungJu
     * @see
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
     * 정수 IP주소 변환
     * MYSQL INET_NTOA
     *
     * @param ipAddress
     * @return String
     * @method longToIp
     * @author HaeSungJu
     * @see
     */
    public static String getInetNtoa(long ipAddress) {
        // ex) ip : 2130706433 / result : 127.0.0.1
        if (ipAddress == 0) return "";
        StringBuilder result = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            result.insert(0, Long.toString(ipAddress & 0xff));
            if (i < 3) result.insert(0, '.');
            ipAddress = ipAddress >> 8;
        }
        return result.toString();
    }

    /**
     * 특정 문장열 개수 찾기
     *
     * @param str
     * @param pattern
     * @return int
     * @method countOccurrencesOf
     * @author HaeSungJu
     * @see
     */
    public static int countOccurrencesOf(String str, String pattern) {
        int cnt = 0;
        try {
            cnt = org.springframework.util.StringUtils.countOccurrencesOf(str, pattern);
        } catch (Exception e) {
            return cnt;
        }
        return cnt;
    }

    /**
     * IPv6 체크
     *
     * @param ip
     * @return boolean
     * @method checkIPv6
     * @author HaeSungJu
     * @see
     */
    public static boolean checkIPv6(String ip) {
        String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
        Pattern pattern = Pattern.compile(regexIPv6);
        return pattern.matcher(ip).matches();
    }

    /**
     * 웹/모바일 접속 구분
     *
     * @param request
     * @return boolean
     * @method
     * @author hmkim
     * @see
     */
    public static boolean deviceContact(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        boolean mobile1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
        boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
        if (mobile1 || mobile2) {
            return false;
        }
        return true;
    }

    public static String changeHttpsImgPath(String imgPath, String mediaNo) {
        String mediaList = PropertyHandler.getProperty("HTTPS_PROXY_USE_MEDIA");
        if (!"".equals(mediaList)) {
            ArrayList<String> useMediaList = StringUtils.splitFunction(mediaList, ",");
            if (useMediaList == null) return imgPath;
            for (String no : useMediaList) {
                if (no.equals(mediaNo)) {
                    if (imgPath.startsWith(GlobalConstants.HTTP)) {
                        imgPath = imgPath.replaceAll("//", "/");
                        if (imgPath.indexOf("/") < 10 && imgPath.indexOf("/") != -1) {
                            imgPath = GlobalConstants.HTTPS_PROXY_DOMAIN + GlobalConstants.HTTP + imgPath.substring(imgPath.indexOf("/") + 1, imgPath.length());
                        } else {
                            imgPath = GlobalConstants.HTTPS_PROXY_DOMAIN + GlobalConstants.HTTP + imgPath;
                        } //if
                        // 이미지 url 내 http가 두개 이상일 경우 "/" 유실 현상 대응
                        if (imgPath.contains("http:/")) {
                            imgPath = imgPath.replaceAll("http:/", "http://");
                            imgPath = imgPath.replaceAll("///", "//");
                        }
                    } else {
                        imgPath = GlobalConstants.HTTPS_PROXY_DOMAIN + imgPath;
                    } //if
                }

            }
        }
        return imgPath;
    }

    public static String sdkChangeUrl(String url, String fromUrl) {
        final String[] sdkReplaceUrl = {"mitem.gmarket.co.kr", "gmarket.co.kr", "mobile.gmarket.co.kr", "11st.co.kr", "m.11st.co.kr", "m.coupang.com"};
        final String WEMAKE_URL = "m.wemakeprice.com";
        if (StringUtils.isNotEmpty(fromUrl) && fromUrl.indexOf("sdkScripts.html") != -1) {
            for (String replaceUrl : sdkReplaceUrl) {
                // 도메인 변경하기
                if (url.indexOf(replaceUrl) != -1) {
                    return WEMAKE_URL;
                }
            }
        }
        return url;
    }

    

    public static String apiKeyEncoding(String value) {
        String date = DateUtils.getFormatDate("yyyyMMddHHmmSSS");
        return Base64Converter.getInstance().encode(Base64Converter.getInstance().encode(date + "_" + value));
    }

    public static String apiKeyDecoding(String value) {
        return Base64Converter.getInstance().decode(Base64Converter.getInstance().decode(value));
    }

    /**
     * 구카테고리 -> 신카테고리 변경
     *
     * @param category
     * @return
     */
    public static Set<String> transformCategory(String category) {
        Set<String> cate = new HashSet<>();
        if ("263".equals(category)) {
            cate.add("174");
        } else if ("264".equals(category)) {
            cate.add("196");
            cate.add("197");
            cate.add("198");
            cate.add("199");
            cate.add("200");
        } else if ("265".equals(category)) {
            cate.add("253");
            cate.add("254");
            cate.add("255");
            cate.add("256");
        } else if ("266".equals(category)) {
            cate.add("161");
        } else if ("267".equals(category)) {
            cate.add("289");
        } else if ("268".equals(category)) {
            cate.add("322");
            cate.add("327");
            cate.add("333");
        } else if ("269".equals(category)) {
            cate.add("226");
        } else if ("270".equals(category)) {
            cate.add("168");
            cate.add("169");
            cate.add("170");
            cate.add("171");
            cate.add("172");
        } else if ("271".equals(category)) {
            cate.add("181");
            cate.add("182");
            cate.add("183");
            cate.add("184");
            cate.add("185");
            cate.add("186");
            cate.add("187");
            cate.add("188");
            cate.add("189");
            cate.add("190");
            cate.add("191");
            cate.add("192");
            cate.add("193");
            cate.add("194");
            cate.add("195");
        } else if ("272".equals(category)) {
            cate.add("130");
        } else if ("273".equals(category)) {
            cate.add("243");
        } else if ("274".equals(category)) {
            cate.add("178");
            cate.add("179");
            cate.add("180");
        } else if ("275".equals(category)) {
            cate.add("244");
            cate.add("301");
            cate.add("302");
            cate.add("303");
        } else if ("276".equals(category)) {
            cate.add("288");
        } else if ("277".equals(category)) {
            cate.add("310");
        } else if ("278".equals(category)) {
            cate.add("311");
            cate.add("312");
            cate.add("313");
            cate.add("314");
            cate.add("315");
            cate.add("316");
        } else if ("279".equals(category)) {
            cate.add("294");
            cate.add("295");
            cate.add("296");
        } else if ("280".equals(category)) {
            cate.add("320");
        } else if ("281".equals(category)) {
            cate.add("201");
            cate.add("202");
            cate.add("203");
            cate.add("204");
            cate.add("205");
            cate.add("206");
            cate.add("207");
            cate.add("208");
            cate.add("209");
            cate.add("210");
            cate.add("211");
            cate.add("212");
            cate.add("213");
            cate.add("214");
            cate.add("215");
            cate.add("216");
            cate.add("217");
            cate.add("218");
            cate.add("219");
            cate.add("220");
        } else if ("282".equals(category)) {
            cate.add("137");
            cate.add("138");
            cate.add("139");
            cate.add("140");
            cate.add("141");
            cate.add("142");
            cate.add("143");
            cate.add("144");
            cate.add("145");
            cate.add("146");
        } else if ("283".equals(category)) {
            cate.add("124");
            cate.add("125");
            cate.add("257");
            cate.add("258");
            cate.add("259");
            cate.add("260");
            cate.add("261");
            cate.add("262");
            cate.add("263");
            cate.add("264");
            cate.add("265");
            cate.add("266");
            cate.add("267");
            cate.add("268");
            cate.add("269");
            cate.add("270");
            cate.add("271");
            cate.add("272");
            cate.add("331");
            cate.add("332");
            cate.add("333");
            cate.add("334");
            cate.add("335");
        } else if ("284".equals(category)) {
            cate.add("151");
            cate.add("152");
            cate.add("153");
            cate.add("154");
            cate.add("155");
            cate.add("156");
            cate.add("157");
        } else if ("285".equals(category)) {
            cate.add("160");
        } else if ("286".equals(category)) {
            cate.add("158");
            cate.add("159");
            cate.add("160");
            cate.add("161");
            cate.add("162");
            cate.add("163");
            cate.add("164");
            cate.add("165");
            cate.add("166");
            cate.add("167");
        } else if ("287".equals(category)) {
            cate.add("346");
        } else if ("288".equals(category)) {
            cate.add("229");
            cate.add("230");
            cate.add("231");
            cate.add("232");
            cate.add("233");
            cate.add("234");
            cate.add("235");
            cate.add("236");
            cate.add("237");
            cate.add("238");
            cate.add("239");
            cate.add("240");
            cate.add("241");
            cate.add("242");
            cate.add("243");
            cate.add("244");
            cate.add("245");
            cate.add("246");
            cate.add("247");
            cate.add("248");
            cate.add("249");
            cate.add("250");
        } else if ("289".equals(category)) {
            cate.add("276");
            cate.add("282");
        } else if ("290".equals(category)) {
            cate.add("118");
            cate.add("119");
            cate.add("120");
            cate.add("121");
            cate.add("122");
        } else if ("291".equals(category)) {
            cate.add("338");
            cate.add("339");
            cate.add("340");
            cate.add("341");
            cate.add("342");
            cate.add("343");
            cate.add("344");
            cate.add("345");
        } else if ("292".equals(category)) {
            cate.add("337");
        } else if ("293".equals(category)) {
            cate.add("106");
            cate.add("107");
            cate.add("108");
        } else if ("294".equals(category)) {
            cate.add("107");
            cate.add("109");
            cate.add("110");
            cate.add("111");
            cate.add("123");
            cate.add("126");
            cate.add("127");
            cate.add("128");
            cate.add("129");
            cate.add("130");
            cate.add("131");
            cate.add("132");
            cate.add("148");
            cate.add("161");
            cate.add("168");
            cate.add("169");
            cate.add("170");
            cate.add("171");
            cate.add("172");
            cate.add("173");
            cate.add("174");
            cate.add("175");
            cate.add("176");
            cate.add("177");
            cate.add("178");
            cate.add("179");
            cate.add("181");
            cate.add("182");
            cate.add("183");
            cate.add("184");
            cate.add("185");
            cate.add("186");
            cate.add("187");
            cate.add("188");
            cate.add("189");
            cate.add("190");
            cate.add("191");
            cate.add("192");
            cate.add("193");
            cate.add("196");
            cate.add("197");
            cate.add("198");
            cate.add("199");
            cate.add("200");
            cate.add("226");
            cate.add("229");
            cate.add("230");
            cate.add("231");
            cate.add("232");
            cate.add("233");
            cate.add("234");
            cate.add("235");
            cate.add("236");
            cate.add("237");
            cate.add("238");
            cate.add("239");
            cate.add("240");
            cate.add("241");
            cate.add("242");
            cate.add("243");
            cate.add("244");
            cate.add("245");
            cate.add("251");
            cate.add("252");
            cate.add("253");
            cate.add("254");
            cate.add("255");
            cate.add("256");
            cate.add("273");
            cate.add("274");
            cate.add("275");
            cate.add("288");
            cate.add("289");
            cate.add("290");
            cate.add("291");
            cate.add("292");
            cate.add("293");
            cate.add("294");
            cate.add("295");
            cate.add("296");
            cate.add("297");
            cate.add("298");
            cate.add("299");
            cate.add("300");
            cate.add("301");
            cate.add("302");
            cate.add("303");
            cate.add("304");
            cate.add("305");
            cate.add("306");
            cate.add("307");
            cate.add("308");
            cate.add("309");
            cate.add("310");
            cate.add("311");
            cate.add("312");
            cate.add("313");
            cate.add("314");
            cate.add("315");
            cate.add("316");
            cate.add("317");
            cate.add("318");
            cate.add("319");
            cate.add("320");
            cate.add("321");
            cate.add("322");
            cate.add("323");
            cate.add("324");
            cate.add("325");
            cate.add("326");
            cate.add("327");
            cate.add("328");
            cate.add("329");
            cate.add("338");
            cate.add("339");
            cate.add("341");
            cate.add("342");
            cate.add("343");
        } else if ("295".equals(category)) {
            cate.add("106");
            cate.add("108");
            cate.add("112");
            cate.add("113");
            cate.add("114");
            cate.add("115");
            cate.add("116");
            cate.add("117");
            cate.add("118");
            cate.add("119");
            cate.add("120");
            cate.add("121");
            cate.add("122");
            cate.add("124");
            cate.add("125");
            cate.add("133");
            cate.add("134");
            cate.add("135");
            cate.add("136");
            cate.add("137");
            cate.add("138");
            cate.add("139");
            cate.add("140");
            cate.add("141");
            cate.add("142");
            cate.add("143");
            cate.add("144");
            cate.add("145");
            cate.add("146");
            cate.add("147");
            cate.add("149");
            cate.add("150");
            cate.add("151");
            cate.add("152");
            cate.add("153");
            cate.add("154");
            cate.add("155");
            cate.add("156");
            cate.add("157");
            cate.add("158");
            cate.add("159");
            cate.add("160");
            cate.add("162");
            cate.add("163");
            cate.add("164");
            cate.add("165");
            cate.add("166");
            cate.add("167");
            cate.add("180");
            cate.add("194");
            cate.add("195");
            cate.add("201");
            cate.add("202");
            cate.add("203");
            cate.add("204");
            cate.add("205");
            cate.add("206");
            cate.add("207");
            cate.add("208");
            cate.add("209");
            cate.add("210");
            cate.add("211");
            cate.add("212");
            cate.add("213");
            cate.add("214");
            cate.add("215");
            cate.add("216");
            cate.add("217");
            cate.add("218");
            cate.add("219");
            cate.add("220");
            cate.add("221");
            cate.add("222");
            cate.add("223");
            cate.add("224");
            cate.add("225");
            cate.add("227");
            cate.add("228");
            cate.add("246");
            cate.add("247");
            cate.add("248");
            cate.add("249");
            cate.add("250");
            cate.add("257");
            cate.add("258");
            cate.add("259");
            cate.add("260");
            cate.add("261");
            cate.add("262");
            cate.add("263");
            cate.add("264");
            cate.add("265");
            cate.add("266");
            cate.add("267");
            cate.add("268");
            cate.add("269");
            cate.add("270");
            cate.add("271");
            cate.add("272");
            cate.add("276");
            cate.add("277");
            cate.add("278");
            cate.add("279");
            cate.add("280");
            cate.add("281");
            cate.add("282");
            cate.add("283");
            cate.add("284");
            cate.add("285");
            cate.add("286");
            cate.add("287");
            cate.add("330");
            cate.add("331");
            cate.add("332");
            cate.add("333");
            cate.add("334");
            cate.add("335");
            cate.add("336");
            cate.add("337");
            cate.add("340");
            cate.add("344");
            cate.add("345");
            cate.add("346");
            cate.add("347");
            cate.add("348");
            cate.add("349");
        } else if ("513".equals(category)) {
            cate.add("251");
            cate.add("252");
        } else if ("514".equals(category)) {
            cate.add("323");
        } else if ("515".equals(category)) {
            cate.add("133");
        } else if ("516".equals(category)) {
            cate.add("321");
            cate.add("328");
        } else if ("517".equals(category)) {
            cate.add("170");
        } else if ("518".equals(category)) {
            cate.add("255");
        } else if ("519".equals(category)) {
            cate.add("168");
            cate.add("169");
        } else if ("520".equals(category)) {
            cate.add("127");
            cate.add("128");
            cate.add("129");
        } else if ("521".equals(category)) {
            cate.add("227");
        } else if ("522".equals(category)) {
            cate.add("229");
            cate.add("230");
            cate.add("231");
            cate.add("232");
            cate.add("233");
            cate.add("234");
            cate.add("235");
            cate.add("236");
            cate.add("237");
            cate.add("238");
        } else if ("523".equals(category)) {
            cate.add("176");
        } else if ("524".equals(category)) {
            cate.add("254");
        } else if ("525".equals(category)) {
            cate.add("256");
        } else if ("526".equals(category)) {
            cate.add("300");
        } else if ("527".equals(category)) {
            cate.add("252");
        } else if ("528".equals(category)) {
            cate.add("315");
        } else if ("530".equals(category)) {
            cate.add("150");
            cate.add("134");
        } else if ("531".equals(category)) {
            cate.add("150");
        } else if ("532".equals(category)) {
            cate.add("269");
            cate.add("333");
        } else if ("533".equals(category)) {
            cate.add("141");
        } else if ("534".equals(category)) {
            cate.add("149");
        } else if ("535".equals(category)) {
            cate.add("159");
        } else if ("536".equals(category)) {
            cate.add("124");
            cate.add("125");
            cate.add("259");
            cate.add("260");
        } else if ("537".equals(category)) {
            cate.add("331");
            cate.add("332");
        } else if ("538".equals(category)) {
            cate.add("138");
            cate.add("139");
            cate.add("140");
        } else if ("539".equals(category)) {
            cate.add("267");
            cate.add("334");
        } else if ("540".equals(category)) {
            cate.add("330");
        } else if ("541".equals(category)) {
            cate.add("268");
        } else if ("542".equals(category)) {
            cate.add("264");
        } else if ("543".equals(category)) {
            cate.add("261");
        } else if ("545".equals(category)) {
            cate.add("265");
            cate.add("266");
        } else if ("546".equals(category)) {
            cate.add("135");
        } else if ("547".equals(category)) {
            cate.add("144");
        } else if ("548".equals(category)) {
            cate.add("137");
        } else if ("549".equals(category)) {
            cate.add("113");
            cate.add("114");
            cate.add("115");
            cate.add("116");
            cate.add("117");
        } else if ("550".equals(category)) {
            cate.add("311");
        } else if ("553".equals(category)) {
            cate.add("291");
            cate.add("292");
            cate.add("293");
        } else if ("554".equals(category)) {
            cate.add("291");
        } else if ("555".equals(category)) {
            cate.add("321");
        } else if ("556".equals(category)) {
            cate.add("294");
            cate.add("295");
            cate.add("296");
        } else if ("557".equals(category)) {
            cate.add("301");
        } else if ("558".equals(category)) {
            cate.add("302");
        } else if ("559".equals(category)) {
            cate.add("320");
            cate.add("321");
            cate.add("322");
            cate.add("323");
            cate.add("324");
            cate.add("325");
            cate.add("326");
            cate.add("327");
            cate.add("328");
            cate.add("329");
            cate.add("330");
            cate.add("331");
            cate.add("332");
            cate.add("333");
            cate.add("334");
            cate.add("335");
        } else if ("560".equals(category)) {
            cate.add("304");
            cate.add("305");
            cate.add("306");
        } else if ("561".equals(category)) {
            cate.add("291");
            cate.add("292");
            cate.add("293");
        } else if ("562".equals(category)) {
            cate.add("297");
            cate.add("298");
            cate.add("299");
        } else if ("563".equals(category)) {
            cate.add("168");
            cate.add("169");
            cate.add("170");
            cate.add("171");
            cate.add("172");
            cate.add("237");
            cate.add("273");
            cate.add("294");
            cate.add("295");
            cate.add("296");
            cate.add("319");
        } else if ("564".equals(category)) {
            cate.add("297");
            cate.add("298");
            cate.add("299");
        } else if ("565".equals(category)) {
            cate.add("290");
        } else if ("566".equals(category)) {
            cate.add("123");
        } else if ("567".equals(category)) {
            cate.add("325");
        } else if ("568".equals(category)) {
            cate.add("221");
            cate.add("222");
            cate.add("223");
            cate.add("224");
        } else if ("569".equals(category)) {
            cate.add("240");
        } else if ("572".equals(category)) {
            cate.add("297");
        } else if ("573".equals(category)) {
            cate.add("294");
        } else if ("574".equals(category)) {
            cate.add("294");
        } else {
            return null;
        }
        return cate;
    }



   
}