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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Operations to assist XML.
 * 
 * @author Sutra Zhou
 */
/* package */class XmlUtils {
	/**
	 * Document builder.
	 */
	private static ThreadLocal<DocumentBuilder> builders = new ThreadLocal<DocumentBuilder>() {
		@Override
		protected DocumentBuilder initialValue() {
			try {
				return DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				throw new ExceptionInInitializerError(ex);
			}
		}
	};

	/**
	 * XML transformer.
	 */
	private static ThreadLocal<Transformer> transformers = new ThreadLocal<Transformer>() {
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Transformer initialValue() {
			try {
				return TransformerFactory.newInstance().newTransformer();
			} catch (TransformerConfigurationException ex) {
				throw new ExceptionInInitializerError(ex);
			}
		}

	};

	/**
	 * Transform string to XML document.
	 * 
	 * @param str
	 *            the string to transform
	 * @return XML document transformed from the string
	 * @throws UnsupportedEncodingException
	 *             encoding "UTF-8" does not support on the platform
	 * @throws SAXException
	 *             indicate SAX error
	 * @throws IOException
	 *             indicate IO error
	 */
	public static Document toDocument(String str)
			throws UnsupportedEncodingException, SAXException, IOException {
		return builders.get().parse(
				new ByteArrayInputStream(str.getBytes("UTF-8")));
	}

	/**
	 * Transform input stream to XML document.
	 * 
	 * @param is
	 *            the input stream to transform
	 * @return XML document transformed from the input stream
	 * @throws SAXException
	 *             indicate SAX error
	 * @throws IOException
	 *             indicate IO error
	 */
	public static Document toDocument(InputStream is) throws SAXException,
			IOException {
		return builders.get().parse(is);
	}

	/**
	 * Transform to XML document from the URL.
	 * 
	 * @param url
	 *            the url to transform
	 * @return XML document transformed from the URL.
	 * @throws SAXException
	 *             indicate SAX error
	 * @throws IOException
	 *             indicate IO error
	 * @throws URISyntaxException
	 *             indicate the URL syntax error
	 */
	public static Document toDocument(URL url) throws SAXException,
			IOException, URISyntaxException {
		return builders.get().parse(url.toURI().toString());
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
	public static String toString(Node node) throws TransformerException {
		DOMSource source = new DOMSource(node);
		Writer w = new StringWriter();
		StreamResult result = new StreamResult(w);
		transformers.get().transform(source, result);
		return w.toString();
	}

}
