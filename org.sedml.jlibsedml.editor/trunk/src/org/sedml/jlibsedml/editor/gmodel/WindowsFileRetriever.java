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

import org.jlibsedml.execution.IModelResolver;
import org.w3c.dom.Document;

/**
 * Specific file resolver for handling Windows filepath/URI conversions
 * 
 * @author otter606
 * 
 */
public class WindowsFileRetriever implements IModelResolver {

	/**
	 * Boolean test for whether this OS is windows.
	 * 
	 * @return <code>true </code> if the OS running this code is Windows, <code>false</code>otherwise.
	 */
	public static boolean isWindows() {

		return System.getProperty("os.name").contains("win")
				|| System.getProperty("os.name").contains("Win");

	}
	/**
	 * <ul>
	 * <li> Converts spaces to %20
	 * <li> Converts \ to /
	 * @param path A Windows absolute filepath
	 * @return A String of the filepath suitable  for inclusion as a URI object.
	 */
	public static String convertAbsoluteFilePathToURI(String path) {
		String noSpaces = path.replaceAll(" ", "%20");
		return noSpaces.replaceAll("\\\\", "/");
	}

	public String getModelXMLFor(URI modelURI) {
		String asStr = modelURI.toString();
		if (asStr == null) {
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
