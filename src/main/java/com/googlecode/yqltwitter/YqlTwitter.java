/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 */
package com.googlecode.yqltwitter;


import java.util.List;

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

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

	/*
	private static final String API_KEY = "dj0yJmk9VXZBSzNjc0Vwcm1MJmQ9WVdrOVlVczBSVkJNTkdzbWNHbzlNVEV4TWpjNE5EZ3lNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Mg--";
	private static final String SHARED_SECRET = "ef9287704ac426cb3e1ea0ce3d47fba7a2d82eaf";
	private static final String APPLICATION_ID = "aK4EPL4k";
	*/

	private final YqlTwitterHelper helper;

	public YqlTwitter() {
		super();
		helper = new YqlTwitterHelper(this, http);

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
		helper = new YqlTwitterHelper(this, http);
	}

	/**
	 * @param id
	 * @param password
	 */
	public YqlTwitter(String id, String password) {
		super(id, password);
		helper = new YqlTwitterHelper(this, http);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Status> getFriendsTimeline() throws TwitterException {
		return helper.getFriendsTimeline();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Status> getPublicTimeline() throws TwitterException {
		return helper.getPublicTimeline();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RateLimitStatus rateLimitStatus() throws TwitterException {
		return helper.rateLimitStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status updateStatus(String status, long inReplyToStatusId)
			throws TwitterException {
		return helper.updateStatus(status, inReplyToStatusId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status updateStatus(String status) throws TwitterException {
		return helper.updateStatus(status);
	}

}
