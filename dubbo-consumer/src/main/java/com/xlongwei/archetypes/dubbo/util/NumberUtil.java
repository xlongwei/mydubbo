package com.xlongwei.archetypes.dubbo.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 解析字符串值为基本类型：整数，长整数，浮点数，布尔值
 * @author hongwei
 */
public class NumberUtil {
	private static Logger logger = LoggerFactory.getLogger(NumberUtil.class);
	
	public static Integer parseInt(String number, Integer defValue) {
		try {
			if(StringUtil.hasLength(number) && number.matches("[+-]?\\d+$")) return Integer.parseInt(number);
		} catch (Exception e) {
			logger.warn("fail to parse int from number: "+number+", ex: "+e.getMessage());
		}
		return defValue;
	}

	public static Long parseLong(String number, Long defValue) {
		try {
			if(StringUtil.hasLength(number) && number.matches("[+-]?\\d+$")) return Long.parseLong(number);
		} catch (Exception e) {
			logger.warn("fail to parse int from number: "+number+", ex: "+e.getMessage());
		}
		return defValue;
	}

	public static Double parseDouble(String number, Double defValue) {
		try {
			if(StringUtil.hasLength(number) && number.matches("[+-]?\\d+(\\.\\d*)?")) return Double.parseDouble(number);
		} catch (Exception e) {
			logger.warn("fail to parse int from number: "+number+", ex: "+e.getMessage());
		}
		return defValue;
	}
	
	/**
	 * 字符串转换成BigDecimal，最好的方式就是new BigDecimal(string)
	 */
	public static BigDecimal parseBigDecimal(String number, BigDecimal defValue) {
		try {
			if(StringUtil.hasLength(number) && number.matches("[+-]?\\d+(\\.\\d*)?")) return new BigDecimal(number);
		} catch (Exception e) {
			logger.warn("fail to parse int from number: "+number+", ex: "+e.getMessage());
		}
		return defValue;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(String number, Class<T> type, T defValue) {
		if(type==Integer.class) {
			return (T) parseInt(number, (Integer)defValue);
		}else if(type==Long.class) {
			return (T) parseLong(number, (Long)defValue);
		}else if(type==Double.class) {
			return (T) parseDouble(number, (Double)defValue);
		}else if(type==Boolean.class) {
			return (T) parseBoolean(number, (Boolean)defValue);
		}else if(type==BigDecimal.class) {
			return (T) parseBigDecimal(number, (BigDecimal)defValue);
		}
		return null;
	}
	
    private static String[] trueStrings = {"true", "yes", "y", "on", "1"};
    private static String[] falseStrings = {"false", "no", "n", "off", "0"};
    public static Boolean parseBoolean(String value, Boolean defValue) {
		if(StringUtil.hasLength(value)) {
	        String stringValue = value.toString().toLowerCase();
	        for(int i=0; i<trueStrings.length; ++i) 
	            if (trueStrings[i].equals(stringValue)) return Boolean.TRUE;
	        for(int i=0; i<falseStrings.length; ++i) 
	            if (falseStrings[i].equals(stringValue)) return Boolean.FALSE;
		}
		return defValue;
    }
    
    /**
     * [0#.,%\u2030-'] 0显示0，#不显示0，.小数点，,分隔符，%百分比，\u2030千分比‰，-负号，'字符'<br>
     * #### 4位整数<br>
     * ,####.00 4位分隔，两位小数，支持格式化金额为：亿,万,元.分<br>
     * ,###.00 3位分隔，两位小数，支持西式风格显示<br>
     * #.##% 百分比，支持1%和1.01%自动忽略0.00%<br>
     * #% 百分比，取整数部分<br>
     * #.###‰ 千分比<br>
     */
    public static String format(Number number, String format) {
    	return new DecimalFormat(format).format(number);
    }
    
    /** 是否有效的主键值 */
    public static boolean validId(Long id) {
    	return id!=null && id>0;
    }
}
