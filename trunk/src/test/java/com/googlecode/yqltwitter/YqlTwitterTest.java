/**
 * 
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
