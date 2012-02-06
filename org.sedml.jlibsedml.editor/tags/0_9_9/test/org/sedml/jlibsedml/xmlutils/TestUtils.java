package org.sedml.jlibsedml.xmlutils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;

public class TestUtils {
	static Document getDoc(String in) throws JDOMException, IOException {
		return new XMLUtils().readDoc(new ByteArrayInputStream(in.getBytes()));
	}

}
