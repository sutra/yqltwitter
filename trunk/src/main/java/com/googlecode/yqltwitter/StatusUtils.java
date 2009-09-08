/**
 * 
 */
package com.googlecode.yqltwitter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.Response;

/**
 * Operations to assist {@link twitter4j.Status}.
 * 
 * @author Sutra Zhou
 */
/* package */class StatusUtils {
	/**
	 * Construct status from the <code>response</code>.
	 * 
	 * @param response
	 *            the response
	 * @param twitter
	 *            the twitter instance
	 * @return a status
	 * @throws TwitterException
	 *             indicate constract failed
	 */
	public static Status toStatus(Response response, Twitter twitter)
			throws TwitterException {
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

	/**
	 * Construct statuses from the response.
	 * 
	 * @param response
	 *            the response from twitter API
	 * @param twitter
	 *            the twitter object
	 * @return statuses list
	 * @throws TwitterException
	 *             if the response is malformed
	 */
	public static List<Status> constructStatuses(Response response,
			Twitter twitter) throws TwitterException {
		String method = "constructStatuses";
		Class<?>[] parameterTypes = new Class<?>[] { Response.class,
				Twitter.class };
		Object[] args = new Object[] { response, twitter };
		try {
			return YqlTwitterUtils.invokeStaticMethod(Status.class, method,
					parameterTypes, args);
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
}
