package org.sedml.jlibsedml.editor.gmodel;

import org.jlibsedml.modelsupport.SUPPORTED_LANGUAGE;

public class LanguageHelper {
	
	/**
	 * REturns a <code>URN</code> for specified {@link SUPPORTED_LANGUAGE} name,
	 *  or the name itself if a <code>URN</code> could not be found.
	 * @param langName A language name, can be empty or <code>null</code>.
	 * @return A <code>String</code> of a URN, if it can be found, else the argument is returned.
	 */
	public String getURNForLang(String langName){
		for (SUPPORTED_LANGUAGE lang: SUPPORTED_LANGUAGE.ALL_LANGUAGES){
			if(lang.getLanguage().equals(langName)){
				return lang.getURN();
			}
			
		}
		return langName;
	}
	
	public String [] getLanguagesAsStrings(){
	String [] languages = new String [SUPPORTED_LANGUAGE.ALL_LANGUAGES.size()];
	int i=0;
	for (SUPPORTED_LANGUAGE lang: SUPPORTED_LANGUAGE.ALL_LANGUAGES){
		languages[i++]=lang.getLanguage();
	
	}
	return languages;
	}
	
	public int getIndexFor(SUPPORTED_LANGUAGE language){
		int i=0;
		for (SUPPORTED_LANGUAGE lang: SUPPORTED_LANGUAGE.ALL_LANGUAGES){
			
			if(lang.equals(language)){
				return i;
			}
			i++;
		}
		return 0;
		
	}

	/**
	 * Given a language URn, returns an index in the {@link SUPPORTED_LANGUAGE} list, or -1 if not found.
	 * @param urn 
	 * @return  The index in the list of {@link SUPPORTED_LANGUAGE} or -1 if not found.
	 */
	public int getIndexForURN(String urn) {
		int i=0;
		for (SUPPORTED_LANGUAGE lang: SUPPORTED_LANGUAGE.ALL_LANGUAGES){
			
			if(lang.getURN().equals(urn)){
				return i;
			}
			i++;
		}
		return -1;
	}

}
