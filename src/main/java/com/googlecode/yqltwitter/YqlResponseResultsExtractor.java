/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 */
package com.googlecode.yqltwitter;

import java.lang.reflect.InvocationTargetException;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import twitter4j.TwitterException;
import twitter4j.http.Response;

/**
 * YQL response extractor.
 * 
 * @author Sutra Zhou
 */
public class YqlResponseResultsExtractor {

	/**
	 * Extract the results in the response of YQL.
	 * 
	 * @param response
	 *            the response of YQL
	 * @return the results
	 */
	public Response extract(Response response) {
		try {
			String format = "Extracting from:\n%1$s\n";
			YqlTwitterUtils.debug(String.format(format, response.asString()));
			return toResponse(response);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (TwitterException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Extract twitter response from YQL response.
	 * 
	 * @param document
	 *            the YQL response
	 * @return the twitter response
	 * @throws XPathExpressionException
	 *             indicate xml parsed error
	 * @throws TransformerException
	 *             indicate xml parsed error
	 */
	// for test purpose
	/* package */Response extract(Document document)
			throws XPathExpressionException, TransformerException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr;
		expr = xpath.compile("/query/results/*");
		Node results = (Node) expr.evaluate(document, XPathConstants.NODE);
		String s = XmlUtils.toString(results);
		// LOG.debug(s);
		Response ret;
		try {
			Class<?>[] parameterTypes = new Class<?>[] { String.class };
			Object[] initargs = new Object[] { s };
			ret = YqlTwitterUtils.newInstance(Response.class, parameterTypes,
					initargs);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
		return ret;
	}

	/**
	 * Extract the results which is the response from twitter in the response of
	 * YQL.
	 * 
	 * @param response
	 *            the response of YQL
	 * @return the results
	 */
	private Response toResponse(Response response) throws TwitterException,
			XPathExpressionException, TransformerException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Document document = response.asDocument();
		Response ret = extract(document);

		ResponseUtils.setCon(ret, ResponseUtils.getCon(response));
		ResponseUtils.setStatusCode(ret, response.getStatusCode());

		return ret;
	}
}
