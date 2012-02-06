package org.sedml.jlibsedml.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.XMLContentDescriber;

public class SEDMLContentDescriber extends XMLContentDescriber {
	
	
		
		
		@Override
		public int describe(InputStream contents, IContentDescription description)
				throws IOException {
			if(super.describe(contents, description)==INVALID){
				return INVALID;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(contents));
			String testline=br.readLine();
			int linesToTest=10;
			int line=0;
			while(line < linesToTest && (testline=br.readLine())!=null){
				if(testline.contains("<sedML")){
					line++;
					return VALID;
				}
			}
			return INVALID;
		}
		
		@Override
		public int describe(Reader contents, IContentDescription description)
				throws IOException {
			return  INVALID;
		}

}
