/**
 * 
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
		// TODO Auto-generated method stub
		return super.updateStatus(status, inReplyToStatusId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status updateStatus(String status) throws TwitterException {
		return helper.updateStatus(status);
	}

}
