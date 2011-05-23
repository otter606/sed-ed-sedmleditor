package org.sedml.jlibsedml.editor.gmodel;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.sedml.execution.IModelResolver;
import org.w3c.dom.Document;

public class WindowsFileRetriever implements IModelResolver {

	public String getModelXMLFor(URI modelURI) {
		String asStr = modelURI.toString();
		if(asStr==null){
			return null;
		}
		File f = new File(asStr.replaceAll("%20", " "));
		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(f);
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);
			return result.getWriter().toString();

		} catch (Exception e) {
			return null;
		}
		
		
	}

}
