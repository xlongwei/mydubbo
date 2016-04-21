package com.xlongwei.archetypes.dubbo.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StringUtil {

	/**
	 * @return true if string is not null or empty
	 */
	public static boolean hasLength(String string) {
		return string != null && string.length() > 0;
	}

	/**
	 * 判断空串
	 */
	public static boolean isBlank(String str) {
		if (!hasLength(str)) return true;
		for (int i = 0; i < str.length(); i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;
		return true;
	}

	public static String trim(String str) {
		if (!hasLength(str)) return str;
		int from = 0, to = str.length();
		while (from < to && Character.isWhitespace(str.charAt(from))) from++;
		while (to > from && Character.isWhitespace(str.charAt(to - 1))) to--;
		return str.substring(from, to);
	}

	public static String firstNotBlank(String... strings) {
		if (strings == null || strings.length == 0) return null;
		for (String string : strings)
			if (!StringUtil.isBlank(string))
				return string;
		return null;
	}

	/**
	 * 首字母大写
	 */
	public static String capitalize(String str) {
		if (!hasLength(str)) return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/** 遮掉部分字符，reverse=true时显示部分字符，len<0时从右边开始数 */
	public static String mask(String string, char c, int len, boolean reverse) {
		if (!hasLength(string)) return string;
		StringBuilder sb = new StringBuilder();
		int length = string.length();
		for (int i = 0; i < length; i++) {
			if (len > 0 && i < len)
				sb.append(reverse ? string.charAt(i) : c);
			else if (len < 0 && i - length >= len)
				sb.append(reverse ? string.charAt(i) : c);
			else
				sb.append(reverse ? c : string.charAt(i));
		}
		return sb.toString();
	}

	/** 是否包含数组中的某个 */
	public static boolean containsOneOf(String string, String... ones) {
		if (!hasLength(string)) return false;
		if (ones != null && ones.length > 0) {
			for (String one : ones) {
				if (string.contains(one))
					return true;
			}
		}
		return false;
	}

	/** 是否包含数组中的某个 */
	public static boolean containsOneOfIgnoreCase(String string, String... ones) {
		if (!hasLength(string))
			return false;
		string = string.toLowerCase();
		if (ones != null && ones.length > 0) {
			for (String one : ones) {
				if (string.contains(one.toLowerCase()))
					return true;
			}
		}
		return false;
	}

	/** 转换字符串为参数映射 */
	public static Map<String, String> params(String... param) {
		Map<String, String> params = new HashMap<>();
		if (param != null && param.length > 1) {
			for (int idx = 0, l = param.length - 1; idx < l; idx += 2) {
				params.put(param[idx], param[idx + 1]);
			}
		}
		return params;
	}

	public static String join(Collection<?> values, String front, String back, String seperator) {
		if (values == null || values.size() == 0) return null;
		StringBuilder join = new StringBuilder();
		for (Object obj : values) {
			if (front != null)
				join.append(front);
			if (obj != null)
				join.append(obj.toString());
			if (back != null)
				join.append(back);
			if (seperator != null)
				join.append(seperator);
		}
		if (seperator != null) join.delete(join.length() - seperator.length(), join.length());
		return join.toString();
	}

	/** 去掉sql参数里的特殊字符 */
	public static String sqlParam(String sqlParam) {
		return sqlParam.replaceAll("([';]+|(--)+)", "");
	}
}
