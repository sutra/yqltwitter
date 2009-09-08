/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 */
package com.googlecode.yqltwitter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Status;

/**
 * Operations to assist YqlTwitter.
 * 
 * @author Sutra Zhou
 */
/* package */class YqlTwitterUtils {
	private static final boolean DEBUG = System.getProperty("YqlTwitter.debug") != null;
	private static final String YQL = "https://query.yahooapis.com/v1/public/yql?q=";
	private static final String enc = "UTF-8";

	/* package */static void debug(String message) {
		if (DEBUG) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date())
					+ " " + message);
		}
	}

	/**
	 * Create the YQL query URL.
	 * 
	 * @param q
	 *            the query string
	 * @return URL strng
	 */
	/* package */static String createYqlUrl(final String q) {
		try {
			final String url = YQL + URLEncoder.encode(q, enc);
			return url;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/* package */static <T> T newInstance(Class<T> clazz,
			Class<?>[] parameterTypes, Object[] initargs)
			throws InvocationTargetException {
		try {
			Constructor<T> c = clazz.getDeclaredConstructor(parameterTypes);
			c.setAccessible(true);
			return c.newInstance(initargs);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	/* package */static <T, R> R invokeStaticMethod(Class<T> clazz,
			String name, Class<?>[] parameterTypes, Object[] args)
			throws InvocationTargetException {
		Method m;
		try {
			m = Status.class.getDeclaredMethod(name, parameterTypes);
			m.setAccessible(true);
			Object o = m.invoke(clazz, args);
			return (R) o;
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


}
