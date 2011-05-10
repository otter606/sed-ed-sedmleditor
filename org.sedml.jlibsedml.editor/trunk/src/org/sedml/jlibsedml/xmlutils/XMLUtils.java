package org.sedml.jlibsedml.xmlutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

public class XMLUtils {




public Document readDoc(InputStream is) throws JDOMException, IOException {
	SAXBuilder builder = new SAXBuilder();
	
	Document doc = builder.build(is);
	return doc;
	
}

public Map<Namespace,Element>  getNamespaces(Document doc) {
	
	Iterator it = doc.getDescendants(new ElementFilter());
	Map<Namespace,Element> nss = new HashMap<Namespace,Element>();
	while (it.hasNext()) {
		Element el = (Element) it.next();
		Namespace ns = el.getNamespace();

		if (ns != null && nss.get(ns)==null ){
			nss.put(ns,el);

		}
	}
	return nss;
}

 Map<Namespace,String>  getNamespacesWithPRefixes(Document doc){
	Map<Namespace,Element> n2El = getNamespaces(doc);
	Map<Namespace,String> rc =new HashMap<Namespace,String>();
	for (Namespace ns:n2El.keySet()){
		if(ns.getPrefix().length()>0){
			rc.put(ns, ns.getPrefix());
		}else {
			rc.put(ns, n2El.get(ns).getName());
		}
	}
	return rc;
}

public  String getXPathFor(Element el, Document doc) {
	Map<Namespace,String> ns2Prefix= getNamespacesWithPRefixes(doc);
	Stack<Element> s = buildElementStack(el);
	StringBuffer out = new StringBuffer();

	while(!s.isEmpty()){
		Element el2 = s.pop();
		out.append("/").append(ns2Prefix.get(el2.getNamespace()) + ":"+el2.getName());
		if(s.size()==0){
			

			
		}
	}
	

	return out.toString();
	
}

/**
 * An 
 * @param el An {@link Element} that is attached to its hierarchy. 
 * @param attName A
 * @return
 * @throws IOException 
 * @throws JDOMException 
 */
public  String getXPathForElementIdentifiedByAttribute(Element el, Document doc,  String attName)  {
	Map<Namespace,String> ns2Prefix= getNamespacesWithPRefixes(doc);
	Stack<Element> s = buildElementStack(el);
	StringBuffer out = new StringBuffer();

	while(!s.isEmpty()){
		Element el2 = s.pop();
		out.append("/").append(ns2Prefix.get(el2.getNamespace()) + ":"+el2.getName());
		if(s.size()==0){
		 Attribute atts = el2.getAttribute(attName);
		
		if(atts!=null){
			out.append("[@").append(atts.getName()).append("='").append(atts.getValue()).append("']");
		}
		}
	}

	return out.toString();
	
	}

/**
 * An 
 * @param el An {@link Element} that is attached to its hierarchy. 
 * @param attName A
 * @return
 * @throws IOException 
 * @throws JDOMException 
 */
public  String getXPathForAttributeIdentifiedByAttribute(Element el, Attribute toIdentify, Document doc,  Attribute identifier) throws JDOMException, IOException {
	Map<Namespace,String> ns2Prefix= getNamespacesWithPRefixes(doc);
	Stack<Element> s = buildElementStack(el);
	StringBuffer out = new StringBuffer();

	while(!s.isEmpty()){
		Element el2 = s.pop();
		out.append("/").append(ns2Prefix.get(el2.getNamespace()) + ":"+el2.getName());
		if(s.size()==0){
			out.append("[@").append( identifier.getName()).append("='")
				.append(identifier.getValue()).append("']")
				.append("/@").append(toIdentify.getName());
		}
		
	}

	return out.toString();
	
}
private Stack<Element> buildElementStack(Element el) {
	Stack<Element> s = new Stack<Element>();
	s.push(el);
	while ((el =el.getParentElement())!=null){
		s.push(el);
	}
	return s;
}

/**
 * Gets concatenated list of name=vale pairs of attributes.
 * @param element
 * @return
 */
public String getAttributesAsString(Element element) {
	StringBuffer sb = new StringBuffer();
	
	List attrs = element.getAttributes();
	if(attrs.size() >0){
		sb.append(" [");;
	}else {
		return "";
	}
	for (int i = 0; i< element.getAttributes().size();i++){
		sb.append( ((Attribute)attrs.get(i)).getName() + "="+ ((Attribute)attrs.get(i)).getValue()+",");
	}
	String rc = sb.toString();
	if(rc.charAt(rc.length()-1)==','){
		rc=rc.replaceAll(",$", "]");
	}
	return rc;
}




}
