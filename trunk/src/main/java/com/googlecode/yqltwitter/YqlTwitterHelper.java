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

import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

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
	private static final String DEFAULT_TABLE_BASE_URL = "http://yqltwitter.googlecode.com/svn/trunk/src/main/table/";
	private static final String TABLE_BASE_URL;
	static {
		String baseUrl = System.getProperty("yqltwitter.table.baseUrl");
		TABLE_BASE_URL = baseUrl == null ? DEFAULT_TABLE_BASE_URL : baseUrl;
	}

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
			return StatusUtils.constructStatuses(response, twitter);
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
		HttpURLConnection con = ResponseUtils.getCon(response);
		Map<String, List<String>> fs = con.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : fs.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print(": ");
			System.out.println(entry.getValue());
		}

		return StatusUtils.constructStatuses(response, twitter);
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
		String q = String.format(format, TABLE_BASE_URL, escape(status),
				escape(twitter.getUserId()), escape(twitter.getPassword()),
				escape(twitter.getSource()));
		Response response = post(q, true);
		return StatusUtils.toStatus(response, twitter);
	}

	public Status updateStatus(String status, long inReplyToStatusId)
			throws TwitterException {
		String format = "use '%1$stwitter.status.xml' as s;"
				+ " insert into s(status,username,password,in_reply_to_status_id,source) values ('%2$s','%3$s','%4$s',%5$s,'%6$s')";
		String q = String.format(format, TABLE_BASE_URL, escape(status),
				escape(twitter.getUserId()), escape(twitter.getPassword()),
				inReplyToStatusId, escape(twitter.getSource()));
		Response response = post(q, true);
		return StatusUtils.toStatus(response, twitter);
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

	/**
	 * Escape the quote for YQL.
	 * 
	 * @param value
	 *            the string to escape
	 * @return the escaped string
	 */
	private String escape(final String value) {
		return value == null ? null : value.replaceAll("\\'", "\\\\\'");
	}
}
