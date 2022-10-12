package com.mobon.billing.report.disk;

import java.net.URLDecoder;
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
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * @param
 * @date 2017. 3. 6.
 * @exception
 * @see
 */
public class StringUtils {
    static Logger logger = Logger.getLogger(StringUtils.class);

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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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

}