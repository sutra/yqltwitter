/**
 * 
 */
package com.googlecode.yqltwitter;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
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
	}

	@Test
	public void testHttp() {
		con = createMock(HttpURLConnection.class);
		con.getHeaderField("X-RateLimit-Limit");
		replay(con);
		reset(con);
		con.getHeaderField("X-RateLimit-Limit");
		verify(con);
	}

	@Test
	public void testUpdateStatusSuccess() throws XPathExpressionException,
			TransformerException, SAXException, IOException, URISyntaxException {
		Response response = toResponse("updateStatus.success.xml");
		try {
			Status status = StatusUtils.toStatus(response, twitter);
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
		Status status = StatusUtils.toStatus(response, twitter);
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
