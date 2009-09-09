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
