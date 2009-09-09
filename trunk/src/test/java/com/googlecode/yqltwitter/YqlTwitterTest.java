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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.http.Response;

/**
 * @author Sutra Zhou
 * 
 */
public class YqlTwitterTest {
	private YqlResponseResultsExtractor extractor;
	private YqlTwitter twitter;
	private HttpURLConnection con;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("YqlTwitter.debug", "true");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		twitter = new YqlTwitter();
		extractor = new YqlResponseResultsExtractor();
		con = createMock(HttpURLConnection.class);
		expect(con.getHeaderField("X-RateLimit-Limit")).andReturn("150").times(
				2);
		expect(con.getHeaderField("X-RateLimit-Remaining")).andReturn("149")
				.times(2);
		expect(con.getHeaderField("X-RateLimit-Reset")).andReturn("59")
				.times(2);
		replay(con);
	}

	@Test
	public void testUpdateStatusSuccess() throws XPathExpressionException,
			TransformerException, SAXException, IOException, URISyntaxException {
		Response response = toResponse("updateStatus.success.xml");
		try {
			Status status = StatusUtils.toStatus(response, twitter);
			assertEquals(1234567890, status.getId());
		} catch (TwitterException ex) {
			ex.printStackTrace();
			fail("A twitter exception should not be thrown.");
		}
	}

	@Test
	public void testUpdateStatusUnknowError() throws XPathExpressionException,
			TransformerException, SAXException, IOException,
			URISyntaxException, TwitterException {
		Response response = toResponse("updateStatus.unknown-error.xml");
		try {
			StatusUtils.toStatus(response, twitter);
			fail("a TwitterException should be thrown.");
		} catch (TwitterException ex) {
			assertEquals(
					"Unexpected root node name:result. Expected:status. Check the availability of the Twitter API at http://status.twitter.com/.",
					ex.getMessage());
		}
	}

	/**
	 * Read the response by name, and construct to a response.
	 * 
	 * @param name
	 *            the resource name
	 * @return a response
	 * @throws XPathExpressionException
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private Response toResponse(String name) throws XPathExpressionException,
			TransformerException, SAXException, IOException, URISyntaxException {
		URL url = getClass().getResource(name);
		Response response = extractor.extract(XmlUtils.toDocument(url));
		ResponseUtils.setCon(response, con);
		return response;
	}
}
