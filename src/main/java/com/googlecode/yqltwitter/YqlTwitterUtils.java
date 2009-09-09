/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sutra Zhou nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Sutra Zhou ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Sutra Zhou BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.googlecode.yqltwitter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Configuration;
import twitter4j.Status;

/**
 * Operations to assist YqlTwitter.
 * 
 * @author Sutra Zhou
 */
/* package */class YqlTwitterUtils {
	private static final boolean DEBUG = Configuration.getDebug();
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
