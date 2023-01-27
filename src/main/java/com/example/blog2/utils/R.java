package com.example.blog2.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */

public class R<T> extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	/**
	 * 是否成功
	 */
	private Boolean flag;
	/**
	 * 返回码
	 */
	private Integer code;
	/**
	 * 返回消息
	 */
	private String message;
	/**
	 * 返回数据
	 */
	private T data;

	public R() {
		put("code", 0);
		put("msg", "success");
	}

	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
