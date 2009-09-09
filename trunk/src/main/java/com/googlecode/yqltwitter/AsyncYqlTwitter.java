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

import twitter4j.AsyncTwitter;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * AsyncTwitter that use YQL to access the twitter API.
 * 
 * @author Sutra Zhou
 */
public class AsyncYqlTwitter extends AsyncTwitter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8197227302630017804L;

	private final YqlTwitterHelper helper;

	/**
	 * @param id
	 * @param password
	 * @param baseURL
	 */
	public AsyncYqlTwitter(String id, String password, String baseURL) {
		super(id, password, baseURL);
		helper = new YqlTwitterHelper(this, http);
	}

	/**
	 * @param id
	 * @param password
	 */
	public AsyncYqlTwitter(String id, String password) {
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
