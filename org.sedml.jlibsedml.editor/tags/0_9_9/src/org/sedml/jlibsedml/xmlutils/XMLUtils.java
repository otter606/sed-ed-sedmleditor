package org.sedml.jlibsedml.xmlutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



public Map<Namespace,Element>  getElementNamespaces(Document doc) {
	
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
	Map<Namespace,Element> n2El = getElementNamespaces(doc);
	Map<Namespace,String> rc =new HashMap<Namespace,String>();
	for (Namespace ns:n2El.keySet()){
		String s = null;
		// ideally we use the existing prefix in the model element
		if(ns.getPrefix().length()>0){
			rc.put(ns, ns.getPrefix());
		// else we use the namespace to generate a prefix
		}else if(( s =getNCNameFromNSURL(ns))!=null) {
			rc.put(ns, s);
		// else a last resort we sjust use the element name as a prefix and 
		// hope this will get matched to a namespace in the model.
		}else {
			rc.put(ns, n2El.get(ns).getName());
		}
	}
	return rc;
}
 
 // this method will generate a sensible ( but long winded) prefix based on 
 // the URL, translating to an XML NcNAME suitable for use as an XPath prefix.
 private String  getNCNameFromNSURL(Namespace ns){
	 String uri = ns.getURI();
	 Pattern p = Pattern.compile("^(.+://)?([^:\\? ]+)");
	 Matcher m = p.matcher(uri);
	 if(m.find()){
		 return m.group(2).replaceAll("/", "_");
	 }else {
		 return null;
	 }
 }

public  String getXPathFor(Element el, Document doc) {
	Map<Namespace,String> ns2Prefix= getNamespacesWithPRefixes(doc);
	Stack<Element> s = buildElementStack(el);
	StringBuffer out = new StringBuffer();

	while(!s.isEmpty()){
		Element el2 = s.pop();
		out.append("/").append(ns2Prefix.get(el2.getNamespace()) + ":"+el2.getName());
	
		if(s.size()==0){
			
			addIdentifiersToXPath(out,  el2,ns2Prefix);
			
		}else {
			addIdentifiersToXPath(out,  el2,ns2Prefix);
		}
	}
	

	return out.toString();
	
}

/**
 * An 
 * @param el An {@link Element} that is attached to its hierarchy. 
 * @param attribute A
 * @return
 * @throws IOException 
 * @throws JDOMException 
 */
public  String getXPathForElementIdentifiedByAttribute(Element el, Document doc,  Attribute selected)  {
	Map<Namespace,String> ns2Prefix= getNamespacesWithPRefixes(doc);
	Stack<Element> s = buildElementStack(el);
	StringBuffer out = new StringBuffer();
	
	while(!s.isEmpty()){
		Element el2 = s.pop();
		out.append("/").append(ns2Prefix.get(el2.getNamespace()) + ":"+el2.getName());
		if(s.size()==0){
			out.append("[@").append(getAttStrForAtt(selected)).append("='").append(selected.getValue()).append("']");
		
		}else {
			addIdentifiersToXPath(out, el2,ns2Prefix);
		}
	}

	return out.toString();
	
	}

// adds predicates to an element in the location path to enable unambigous identification 
void addIdentifiersToXPath(StringBuffer out, 
		Element el2, Map<Namespace, String> ns2Prefix) {
	AttributeUniqenessAnalyser anal = new AttributeUniqenessAnalyser();
	AttDataObj uniqueAttr = anal.getUniqueAttributeForElement(el2);
	if(uniqueAttr!=null){
		out.append("[@");
		out.append(getAttStrForAtt(uniqueAttr.att)).append("='");
		String val = el2.getAttribute(uniqueAttr.name, uniqueAttr.ns).getValue();
		out.append(escape(val)).append("']");
		
	}else {
		int indx =anal.getIndexForElementAmongstSiblings(el2);
		if(indx!=-1)
		out.append("[").append(indx)
		.append("]");
	}
}

private Object escape(String val) {
	return val.replaceAll("'", "&apos;");
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
			
			out.append("[@").append( getAttStrForAtt(identifier)).append("='")
				.append(identifier.getValue()).append("']")
				.append("/@").append(getAttStrForAtt(toIdentify));
		}else {
			addIdentifiersToXPath(out,  el2,ns2Prefix);
		}
		
	}

	return out.toString();
	
}
// generates attname with or without a prefix depending on if it has a namespace
String getAttStrForAtt(Attribute att ){
	if(!att.getNamespace().equals(Namespace.NO_NAMESPACE)){
		return att.getNamespacePrefix() + ":"+att.getName();
	}else {
		return att.getName();
	}
	
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
 * Gets concatenated list of name=value pairs of attributes.
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
