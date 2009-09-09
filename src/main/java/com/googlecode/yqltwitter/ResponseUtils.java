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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;

import twitter4j.http.Response;

/**
 * Utility functions for {@link twitter4j.http.Response}.
 * 
 * @author Sutra Zhou
 */
/* package */class ResponseUtils {
	private static final Field STATUS_CODE_FIELD;
	private static final Field CON_FIELD;
	static {
		try {
			STATUS_CODE_FIELD = Response.class.getDeclaredField("statusCode");
			CON_FIELD = Response.class.getDeclaredField("con");
		} catch (SecurityException ex) {
			throw new ExceptionInInitializerError(ex);
		} catch (NoSuchFieldException ex) {
			throw new ExceptionInInitializerError(ex);
		}
		STATUS_CODE_FIELD.setAccessible(true);
		CON_FIELD.setAccessible(true);
	}

	/**
	 * Call the constructor of Response with parameter String.
	 * 
	 * @param s
	 *            the parameter
	 * @return a response instance
	 */
	public static Response newResponse(String s) {
		Response ret;
		try {
			Class<?>[] parameterTypes = new Class<?>[] { String.class };
			Object[] initargs = new Object[] { s };
			ret = YqlTwitterUtils.newInstance(Response.class, parameterTypes,
					initargs);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
		return ret;
	}

	/**
	 * Set the private filed <code>statusCode</code>.
	 * 
	 * @param response
	 *            the response to set
	 * @param statusCode
	 *            the statusCode to set
	 */
	public static void setStatusCode(Response response, int statusCode) {
		try {
			STATUS_CODE_FIELD.set(response, statusCode);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Set the private filed <code>con</code>.
	 * 
	 * @param response
	 *            the response to set
	 * @param con
	 *            the HttpURLConnection to set
	 */
	public static void setCon(Response response, HttpURLConnection con) {
		try {
			CON_FIELD.set(response, con);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the value of the private field <code>con</code>.
	 * 
	 * @param response
	 *            the response to get from
	 * @return the HttpURLConnection of the response
	 */
	public static HttpURLConnection getCon(Response response) {
		try {
			return ((HttpURLConnection) CON_FIELD.get(response));
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
