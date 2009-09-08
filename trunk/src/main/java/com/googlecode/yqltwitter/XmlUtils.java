/**
 * 
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
