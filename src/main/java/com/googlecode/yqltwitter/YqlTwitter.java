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
import java.util.List;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.Response;

/**
 * Twitter that use YQL to access the twitter API.
 * 
 * @author Sutra Zhou
 */
public class YqlTwitter extends Twitter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4545368679112332540L;
	private static final boolean DEBUG = true;//System.getProperty("YqlTwitter.debug") != null;

	/*
	private static final String API_KEY = "dj0yJmk9VXZBSzNjc0Vwcm1MJmQ9WVdrOVlVczBSVkJNTkdzbWNHbzlNVEV4TWpjNE5EZ3lNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Mg--";
	private static final String SHARED_SECRET = "ef9287704ac426cb3e1ea0ce3d47fba7a2d82eaf";
	private static final String APPLICATION_ID = "aK4EPL4k";
	*/

	private static final String YQL = "https://query.yahooapis.com/v1/public/yql?q=";
	private static final String enc = "UTF-8";
	private static final String TABLE_BASE_URL = "http://yqltwitter.googlecode.com/svn/trunk/src/main/table/";

	private final YqlResponseResultsExtractor extractor = new YqlResponseResultsExtractor();

	public YqlTwitter() {
		super();
		/*
		long now = System.currentTimeMillis();
		String reqTokenUrl = "https://api.login.yahoo.com/oauth/v2/"
				+ "get_request_token?oauth_nonce="
				+ DigestUtils.md5Hex(String.valueOf(now)) + "&oauth_timestamp="
				+ now + "&oauth_consumer_key=" + SHARED_SECRET
				+ "&oauth_signature_method=plaintext"
				+ "&oauth_signature=abcdef" + "&oauth_version=1.0"
				+ "&xoauth_lang_pref=\"en-us\"" + "&oauth_callback=\"oob\"";
		try {
			Response res = http.get(reqTokenUrl);
			LOG.debug(res.asString());
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * @param id
	 * @param password
	 * @param baseURL
	 */
	public YqlTwitter(String id, String password, String baseURL) {
		super(id, password, baseURL);
	}

	/**
	 * @param id
	 * @param password
	 */
	public YqlTwitter(String id, String password) {
		super(id, password);
	}

	protected Response get(String q, boolean authenticate)
			throws TwitterException {
		String url = createYqlUrl(q);
		debug("Get: " + url);
		Response response = http.get(url, authenticate);
		return extractor.extract(response);
	}

	protected Response post(String q, boolean authenticate)
			throws TwitterException {
		String url = createYqlUrl(q);
		debug("Post: " + url);
		Response response = http.post(url, authenticate);
		return extractor.extract(response);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Status> getFriendsTimeline() throws TwitterException {
		String format = "use '%1$stwitter.friends_timeline.xml' as ft;"
				+ " select * from ft where username='%2$s' and password='%3$s'";
		String q = String.format(format, TABLE_BASE_URL, getUserId(), getPassword());
		Response response = get(q, true);
		try {
			return constructStatuses(response);
		} catch (TwitterException te) {
			System.err.println(String.format("q: %1$s, results: %2$s", q,
					response.asString()));
			throw te;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Status> getPublicTimeline() throws TwitterException {
		String format = "use '%1$stwitter.public_timeline.xml' as pt;"
				+ " select * from pt";
		String q = String.format(format, TABLE_BASE_URL);
		Response response = get(q, false);
		return constructStatuses(response);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RateLimitStatus rateLimitStatus() throws TwitterException {
		String format = "use '%1$srate_limit_status.xml' as rls;"
				+ "select * from rls";
		String q = String.format(format, TABLE_BASE_URL);
		Response response = get(q, null != getUserId() && null != getPassword());
		try {
			return newInstance(RateLimitStatus.class,
					new Class<?>[] { Response.class }, new Object[] { response });
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof TwitterException) {
				throw (TwitterException) cause;
			} else if (cause instanceof RuntimeException){
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status updateStatus(String status, long inReplyToStatusId)
			throws TwitterException {
		// TODO Auto-generated method stub
		return super.updateStatus(status, inReplyToStatusId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status updateStatus(String status) throws TwitterException {
		String format = "use '%1$stwitter.status.xml' as s;"
				+ " insert into s(status,username,password,source) values ('%2$s','%3$s','%4$s','%5$s')";
		String q = String.format(format, TABLE_BASE_URL, status, getUserId(),
				getPassword(), source);
		Response response = post(q, true);
		try {
			return newInstance(Status.class, new Class<?>[] { Response.class,
					Twitter.class }, new Object[] { response, this });
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof TwitterException) {
				throw (TwitterException) cause;
			} else if (cause instanceof RuntimeException){
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
	}

	/**
	 * Create the YQL query URL.
	 * 
	 * @param q
	 *            the query string
	 * @return URL strng
	 */
	private String createYqlUrl(final String q) {
		try {
			final String url = YQL + URLEncoder.encode(q, enc);
			return url;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Status> constructStatuses(Response response)
			throws TwitterException {
		Class<?>[] parameterTypes = new Class<?>[] { Response.class,
				Twitter.class };
		Object[] args = new Object[] { response, this };
		try {
			return invokeStaticMethod(Status.class, "constructStatuses",
					parameterTypes, args);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof TwitterException) {
				throw (TwitterException) cause;
			} else if (cause instanceof RuntimeException){
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
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

	/* package */static void debug(String message) {
		if (DEBUG) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date())
					+ " " + message);
		}
	}
}
