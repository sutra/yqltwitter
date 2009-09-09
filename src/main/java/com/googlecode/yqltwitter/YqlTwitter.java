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
