/**
 * Copyright (c) 2009, Sutra Zhou
 * All rights reserved.
 */
package com.googlecode.yqltwitter;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
			YqlTwitterUtils.debug("Extract from: " + response.asString());
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

	// for test purpose
	/* package */Response toResponse(Document document)
			throws XPathExpressionException, TransformerException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr;
		expr = xpath.compile("/query/results/*");
		Node results = (Node) expr.evaluate(document, XPathConstants.NODE);
		String s = toString(results);
		// LOG.debug(s);
		Response ret;
		try {
			Class<?>[] parameterTypes = new Class<?>[] { String.class };
			Object[] initargs = new Object[] { s };
			ret = YqlTwitterUtils.newInstance(Response.class, parameterTypes,
					initargs);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException){
				throw (RuntimeException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		}
		return ret;
	}

	private Response toResponse(Response response) throws TwitterException,
			XPathExpressionException, TransformerException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Document document = response.asDocument();
		Response ret = toResponse(document);
		Field f = Response.class.getDeclaredField("con");
		f.setAccessible(true);
		f.set(ret, f.get(response));

		f = Response.class.getDeclaredField("statusCode");
		f.setAccessible(true);
		f.set(ret, f.get(response));
		return ret;
	}

	/**
	 * Write node to a string.
	 * 
	 * @param node
	 *            the node to write
	 * @return string
	 * @throws TransformerException
	 *             if write failed
	 */
	private static String toString(Node node) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(node);
		Writer w = new StringWriter();
		StreamResult result = new StreamResult(w);
		transformer.transform(source, result);
		return w.toString();
	}

}
