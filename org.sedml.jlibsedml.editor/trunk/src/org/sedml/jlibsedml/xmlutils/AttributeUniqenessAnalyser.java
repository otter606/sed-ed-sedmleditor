package org.sedml.jlibsedml.xmlutils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;

public class AttributeUniqenessAnalyser {
	
	
	public String getMostUniqueAttribute(Element el) {
		List<Element> sibs  = getSiblings(el);
		Iterator it = sibs.iterator();
		Map<String, Integer> map = new TreeMap<String, Integer>();
		while (it.hasNext()){
			Element sib = (Element)it.next();
			List<Attribute> atts =sib.getAttributes();
			for (Attribute att:atts){
				// only consider atts that our element has
				if(el.getAttribute(att.getName())== null){
					continue;
				}
				String key = att.getName() + ">"+att.getValue();
				if(map.containsKey(key)){
					int incremented = map.get(key);
					incremented++;
					map.put(key, incremented);
				}else {
					map.put(key, 1);
				}
			}
		}
		int currMin = Integer.MAX_VALUE;
		String currAtt = null;
		for (String key: map.keySet()){
			if (map.get(key) < currMin){
				currAtt = key;
				currMin = map.get(key);
			}
		}
		
		String name = null;
		if (currAtt!=null){
			 name = currAtt.substring(0,currAtt.indexOf('>'));
		}
		return name;
	}

	/**
	 * Gets the index of an element amongst its siblings. This method is namespace aware and 
	 * thus the index will be for elements with the same element name and namespace.
	 * If this element cannot be found, the method returns -1. 
	 * @param el A non-null JDom Element that is part of a Document (i.e., not standalone ).
	 * @return A 1-based index, for insertion into an XPAth statement.
	 */
	public int getIndexForElementAmongstSiblings (Element el){
		List<Element> sibs = getSiblings ( el);
		if(sibs.size() == 1 && el==sibs.get(0)){
			return -1;
		}
		int indx = 0;
		for (int i = 0; i < sibs.size();i++){
			Element sib = sibs.get(i);
			if(sib.getNamespace()!=null &&sib.getNamespace().equals(el.getNamespace()) &&
					sib.getName().equals(el.getName())){
				indx++;
			}
			if(sib == el){
				return indx;
			}
		}
		return -1;
	}
	// gets sibling elements, regardless of namespace.
	List<Element> getSiblings (Element element) {
		if(element.getParentElement()!=null){
			Element parent = element.getParentElement();
			List siblings = parent.getContent(new ElementFilter());
			return siblings;
		}else {
			return Collections.EMPTY_LIST;
		}
		
	}
}
