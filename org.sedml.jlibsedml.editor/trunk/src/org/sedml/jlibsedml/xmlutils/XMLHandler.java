package org.sedml.jlibsedml.xmlutils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.sedml.jlibsedml.editor.configdialogs.IValidator;

/**
 * Handles raw input to XML fields (e.g., notes, XML changes etc)
 * @author radams
 *
 */
public class XMLHandler implements IValidator {
	private String err=null;
	private SAXBuilder builder = new SAXBuilder();

	public String getValidationFailureMessage() {
		return err;
	}

	public boolean validate(String toValidate) {
		List<String> els = getXMLelements2(toValidate);
		for (int i = 0; i < els.size(); i++) {
			try {
				 builder.build(new StringReader(els.get(i)) );
			} catch (JDOMException e) {
				err= " Text is not well-formed XML  in element [" + i+ "] " + e.getMessage() ;
				return false;
			} catch (IOException e) {
				err= "Text is not well-formed XML  in element [" + i+ "] "  + e.getMessage();
				return false;
			}
			
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param toValidate
	 * @return non-null List<Element>
	 * @throws IllegalArgumentException if <code>validate(toValidate()) ==false</code>
	 */
	public List<Element> getXMLElements(String toValidate) {
		if(!validate(toValidate)){
			throw new IllegalArgumentException();
		}
		List<Element>rc = new ArrayList<Element>();
		List<String> els = getXMLelements2(toValidate);
		for (int i = 0; i < els.size(); i++) {
			try {
				 Document doc = builder.build(new StringReader(els.get(i)) );
				 Element el = doc.detachRootElement();
				 rc.add(el);
			} catch (JDOMException e) {
				err= " Text is not well-formed XML  in element [" + i+ "] " + e.getMessage() ;
				return rc;
			} catch (IOException e) {
				err= "Text is not well-formed XML  in element [" + i+ "] "  + e.getMessage();
				return rc;
			}
			
		}
		
		return rc;
	}
	
	class Token {
		Pattern nanmeInEl=Pattern.compile("</?\\s*(\\w+)");
		Token(int realSt, int realEnd, String token) {
			super();
			this.realSt = realSt;
			this.realEnd = realEnd;
			this.token = token;
		}
		int realSt, realEnd;
		String token;
		public String toString(){
			return token + "st:" +realSt + " end:"+realEnd;
		}
		
		boolean isEndToken(){
			return token.matches("^<\\s*/.+");
		}
		
		boolean isUniElement(){
			return token.matches("^<\\s*.+/>");
		}
		
		String  getName(){
			Matcher m = nanmeInEl.matcher(token);
			if(m.find()){
				return m.group(1);
			}else {
				return null;
			}
		}
		
	}
	
	List<String> getXMLelements2(String totest){
		 List<String>rc = new ArrayList<String>();
		 String toVal=totest.trim();
		
		List<Token> els = tokenize(toVal);
		
		if(els.isEmpty()){
			rc.add(toVal); 
			return rc ;
		}
		if(els.get(0).realSt!=0){
			rc.add(toVal.substring(0, els.get(0).realSt));
		}
		
		Token first=null; Token last=null;
		int level=0;
		for (int i =0;i<els.size();i++){
			System.err.println("Token is" + els.get(i));
			if(level==0){
				first = els.get(i);
			}
			Token curr = els.get(i);
			if(first.getName().equals(curr.getName()) &&
					 !curr.isEndToken()){
				level++;
				
			} if (first.getName().equals(curr.getName()) &&
			 (curr.isEndToken()|| curr.isUniElement())){
				 level--;
				 last=curr;
			 }
			if(level==0 && last!=null){
				String block=toVal.substring(first.realSt, last.realEnd);
				rc.add(block);
				
				last=null;
			}
		}
		Token asblast = els.get(els.size()-1);
		if(asblast.realEnd!=toVal.length()){
			rc.add(toVal.substring(asblast.realEnd,toVal.length()));
		}
		
		
		
		
		return rc;
	}
	
	
	
	List<Token> tokenize(String all) {
		Pattern elName = Pattern.compile("(<[^>]+>)");
		List<Token>rc = new ArrayList<Token>();
		Matcher m =elName.matcher(all);
		while(m.find()){
			rc.add(new Token(m.start(),m.end(),m.group()));
		}
		return rc;
	}
	
	
	
	

	/**
	 * Formats a list of jdom ELements to <code>String</code>, appending a new line after
	 *  each element
	 * @param xml A <code>List</code> of jdom Elements				
	 * @return A formateed STring
	 */
	public String getElementsAsString(List<Element> xml) {
		StringBuffer buff = new StringBuffer();
		for(Element el: xml){
			buff.append(getElementAsString(el));	
		}
		return buff.toString();
	}
	
	/**
	 * Formats a single  jdom ELements to <code>String</code>, appending a wew line after
	 *  each element
	 * @param xml A <code>List</code> of jdom Elements				
	 * @return A formateed STring
	 */
	public String getElementAsString(Element xml) {
		StringBuffer buff = new StringBuffer();
			XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());	
			buff.append(xmlOut.outputString(xml)).append("\n");	
		
		return buff.toString();
	}

}
