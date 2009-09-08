/**
 * 
 */
package com.googlecode.yqltwitter;

import java.lang.reflect.Field;
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
