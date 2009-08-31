/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 */
package com.googlecode.yqltwitter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.HttpClient;
import twitter4j.http.Response;

/**
 * Helper for YqlTwitter.
 * 
 * @author Sutra Zhou
 */
/* package */class YqlTwitterHelper {
	private static final String TABLE_BASE_URL = "http://yqltwitter.googlecode.com/svn/trunk/src/main/table/";

	private final YqlResponseResultsExtractor extractor = new YqlResponseResultsExtractor();
	private final Twitter twitter;
	private final HttpClient http;

	/**
	 * @param extractor
	 * @param http
	 */
	public YqlTwitterHelper(Twitter twitter, HttpClient http) {
		this.twitter = twitter;
		this.http = http;
	}

	public List<Status> getFriendsTimeline() throws TwitterException {
		String format = "use '%1$stwitter.friends_timeline.xml' as ft;"
				+ " select * from ft where username='%2$s' and password='%3$s'";
		String q = String.format(format, TABLE_BASE_URL, twitter.getUserId(),
				twitter.getPassword());
		Response response = get(q, true);
		try {
			return YqlTwitterUtils.constructStatuses(response, twitter);
		} catch (TwitterException te) {
			System.err.println(String.format("q: %1$s, results: %2$s", q,
					response.asString()));
			throw te;
		}
	}

	public List<Status> getPublicTimeline() throws TwitterException {
		String format = "use '%1$stwitter.public_timeline.xml' as pt;"
				+ " select * from pt";
		String q = String.format(format, TABLE_BASE_URL);
		Response response = get(q, false);
		return YqlTwitterUtils.constructStatuses(response, twitter);
	}

	public RateLimitStatus rateLimitStatus() throws TwitterException {
		String format = "use '%1$srate_limit_status.xml' as rls;"
				+ "select * from rls";
		String q = String.format(format, TABLE_BASE_URL);
		Response response = get(q, null != twitter.getUserId()
				&& null != twitter.getPassword());
		try {
			return YqlTwitterUtils.newInstance(RateLimitStatus.class,
					new Class<?>[] { Response.class },
					new Object[] { response });
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof TwitterException) {
				throw (TwitterException) cause;
			} else if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
	}

	public Status updateStatus(String status) throws TwitterException {
		String format = "use '%1$stwitter.status.xml' as s;"
				+ " insert into s(status,username,password,source) values ('%2$s','%3$s','%4$s','%5$s')";
		String q = String.format(format, TABLE_BASE_URL, status, twitter
				.getUserId(), twitter.getPassword(), twitter.getSource());
		Response response = post(q, true);
		try {
			Class<?>[] parameterTypes = new Class<?>[] { Response.class,
					Twitter.class };
			Object[] initargs = new Object[] { response, twitter };
			return YqlTwitterUtils.newInstance(Status.class, parameterTypes,
					initargs);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof TwitterException) {
				throw (TwitterException) cause;
			} else if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
	}

	private Response get(String q, boolean authenticate)
			throws TwitterException {
		String url = YqlTwitterUtils.createYqlUrl(q);
		YqlTwitterUtils.debug("Get: " + url);
		Response response = http.get(url, authenticate);
		return extractor.extract(response);
	}

	private Response post(String q, boolean authenticate)
			throws TwitterException {
		String url = YqlTwitterUtils.createYqlUrl(q);
		YqlTwitterUtils.debug("Post: " + url);
		Response response = http.post(url, authenticate);
		return extractor.extract(response);
	}

}
