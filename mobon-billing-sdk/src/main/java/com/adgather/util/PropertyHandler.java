package com.adgather.util;

import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.exception.common.BooleanFormatException;
import com.adgather.resource.loader.PropertyLoader;

/**
 * 1. Logger.getLogger 에서 LoggerFactory.getLogger 로 변경
 * 2. 프로퍼티값이 null 일 경우 default 값으로 넘어가도록 변경
 * 
 * @date 2017. 3. 5.
 * @param 
 * @exception
 * @see
 */
public class PropertyHandler {
  
	private static final Logger logger = LoggerFactory.getLogger(PropertyHandler.class);

	public static Properties properties = new Properties();
	
	/**
	 * 프로퍼티 값 반환
	 * @method getProperty
	 * @see
	 * @param paramString
	 * @return String
	 */
	public static String getProperty(String paramString) {
		String str = null;//ConfigServlet.property.get(paramString);
		
		if (StringUtils.isNotEmpty(str)) {
		  return str;
		}
		
		PropertyLoader localPropertyLoader = PropertyLoader.getInstance();

		str = properties.getProperty(paramString);
		if (str == null) {
//			logger.error("[PropertyHandler] Could not find property '{}'.",paramString);
			return null;
		}
		return str; // 로딩 때, Property 값은 trim된 상태로 보관함
	}

	/**
	 * String 값이 넘어왔을 경우에 제대로 된 데이터가 아닐 경우, default로 넘어온 값으로 처리
	 * @method getProperty
	 * @see
	 * @param paramString
	 * @param defaultVal
	 * @return String
	 */
	public static String getProperty(String paramString, String defaultVal) {
		String str = null;//ConfigServlet.property.get(paramString);
		if (StringUtils.isNotEmpty(str)) {
			return str;
		}

		PropertyLoader localPropertyLoader = PropertyLoader.getInstance();
		str = properties.getProperty(paramString);
		if (str == null) {
			str = defaultVal;
		}
		return str; // 로딩 때, Property 값은 trim된 상태로 보관함
	}
	
	/**
	 * 프로퍼티 문자열 반환
	 * @method getString
	 * @see
	 * @param paramString
	 * @return String
	 */
	static public String getString(String paramString) {
		return getProperty(paramString);
	}
	
	/**
	 * 프로퍼티 문자열 반환(defualt 값 처리) 
	 * @method getString
	 * @see
	 * @param paramString
	 * @param defaultVal
	 * @return String
	 */
	static public String getString(String paramString, String defaultVal) {
		return getProperty(paramString, defaultVal);
	}

	/**
	 * 프로퍼티 Int형 반환
	 * @method getInt
	 * @see
	 * @param paramString
	 * @return
	 * @throws NumberFormatException int
	 */
	static public int getInt(String paramString) throws NumberFormatException {
		String val = getProperty(paramString);
		if(StringUtils.isEmpty(val) || !NumberUtils.isNumber(val)) {
			 throw new NumberFormatException(String.format("Property %s is not number format[%s].", paramString, val));
		}
		return Integer.parseInt(val);
	}

	/**
	 * int 값이 넘어왔을 경우에 제대로 된 데이터가 아닐 경우, default로 넘어온 값으로 처리
	 * @method getInt
	 * @see
	 * @param paramString
	 * @param defaultVal
	 * @return int
	 */
	static public int getInt(String paramString, int defaultVal) {
		String val = getProperty(paramString, null);
		if(StringUtils.isEmpty(val)) {
			return defaultVal;
		} else if (NumberUtils.isNumber(val)) {
			return Integer.parseInt(val);
		} else {
			logger.error(String.format("Property %s is not number format[%s]. Set default value : %d", paramString, val, defaultVal));
			return defaultVal;
		}
	}
	
	static public float getFloat(String paramString, float defaultVal) {
		String val = getProperty(paramString, null);
		if(StringUtils.isEmpty(val)) {
			return defaultVal;
		} else if (NumberUtils.isNumber(val)) {
			return Float.parseFloat(val);
		} else {
			logger.error(String.format("Property %s is not float format[%s]. Set default value : %f", paramString, val, defaultVal));
			return defaultVal;
		}
	}
	
	static public boolean getBoolean(String paramString)  {
		String val = getProperty(paramString);
		if (StringUtils.isEmpty(val) || (!val.equalsIgnoreCase(Boolean.TRUE.toString()) && !val.equalsIgnoreCase(Boolean.FALSE.toString())) ) {
			throw new BooleanFormatException(String.format("Property %s is not boolean[%s].", paramString, val));
		}
		return Boolean.valueOf(val);
	}
	
	/**
	 * boolean 값이 넘어왔을 경우에 제대로 된 데이터가 아닐 경우, default로 넘어온 값으로 처리
	 * @method getBoolean
	 * @see
	 * @param paramString
	 * @param defaultVal
	 * @return boolean
	 */
	static public boolean getBoolean(String paramString, boolean defaultVal)  {
		try {
			String val = getProperty(paramString, null);
			if (StringUtils.isEmpty(val) ) {
				return defaultVal;
			} else if(val.equalsIgnoreCase(Boolean.TRUE.toString()) || val.equalsIgnoreCase(Boolean.FALSE.toString())) {
				return Boolean.valueOf(val);
			} else {
				logger.error(String.format("Property %s is not boolean format[%s]. Set default value : %s", paramString, val, defaultVal));
				return defaultVal;
			}
		} catch (Exception e) {
		    return defaultVal;
	    }
	}
	
	/**
	 * 프로퍼티 true여부 확인
	 * @method isTrue
	 * @see
	 * @param paramString
	 * @return boolean
	 */
	static public boolean isTrue(String paramString) {
	  try {
	    return getBoolean(paramString);
	  } catch (BooleanFormatException e) {
	    return false;
    }
	}
	
	/**
	 * 프로퍼티 false여부 확인
	 * @method isFalse
	 * @see
	 * @param paramString
	 * @return boolean
	 */
	static public boolean isFalse(String paramString) {
		return !getBoolean(paramString);
	}
	
	/**
	 * 프로퍼티 문자열 배열 반환
	 * @method getSplitString
	 * @see
	 * @param paramString
	 * @param sepRegex
	 * @return String[]
	 */
	static public String[] getSplitString(String paramString, String sepRegex) {
		String val = getProperty(paramString);
		if(StringUtils.isEmpty(val)) {
			logger.error("Property {} is Empty '{}'.", paramString, val);
			return null;
		}
		return val.split(sepRegex);
	}
	
	/**
	 * 프로퍼티 문자열 배열 반환(defualt 값 처리) 
	 * @method getSplitString
	 * @see
	 * @param paramString
	 * @param sepRegex
	 * @param defaultVal
	 * @return String[]
	 */
	static public String[] getSplitString(String paramString, String sepRegex, String[] defaultVal) {
		String val = getProperty(paramString, null);
		if(StringUtils.isEmpty(val)) {
			return defaultVal;
		} else {
			return val.split(sepRegex);
		}
	}
	
	
	/**
	 * 프로퍼티 문자열 포함 여부 확인
	 * @method containString
	 * @see
	 * @param paramString
	 * @param checkString
	 * @return boolean
	 */
	static public boolean containString(String paramString, String checkString) {
		String val = getProperty(paramString);
		if(val == null) 
			return false;
		
		if(checkString == null || checkString.length() == 0) {
			return false;
		}
		
		return val.indexOf(checkString) > -1;
	}
	
	/**
	 * 프로퍼티 문자열 배열 특정문자열 포함 여부 확인
	 * @method contain
	 * @see
	 * @param paramString
	 * @param sepRegex
	 * @param checkString
	 * @return boolean
	 */
	static public boolean contain(String paramString, String sepRegex, String checkString) {
		String val = getProperty(paramString);
		if(val == null) {
			return false;
			
		} else if (checkString == null || "".equals(checkString)) {
			return val.equals(checkString);
			
		} else {
			String[]  vals = getSplitString(paramString, sepRegex);
			if(vals == null)		return false;
			
			for (String s : vals) {
				if(s.equals(checkString) ) {
					return true;
				}	
			}
			return false;
		}
	}
	
	static public String getContainString(String paramPrefixString, String sepRegex, String checkString) {
		String val = getProperty(paramPrefixString);
		if(val == null) {
			return null;
			
		} else if (checkString == null || "".equals(checkString)) {
			return val;
			
		} else {
			String[]  vals = getSplitString(paramPrefixString, sepRegex);
			if(vals == null)		return null;
			
			for (String s : vals) {
				if(s.startsWith(checkString) ) {
					return s;
				}	
			}
			return null;
		}
	}
}