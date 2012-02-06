package org.sedml.jlibsedml.editor.configdialogs;

import org.jmathml.ASTNode;
import org.sedml.Libsedml;

/**
 * Validates a String a Math expression
 * @author radams
 *
 */
public class MathsValidator implements IValidator {
	private String err;
	public String getValidationFailureMessage() {
		return err;
	}

	/**
	 * REturns ttrue if mathematical expression can be parsed into an {@link ASTNode}.
	 * Returns <code>false</code> if :
	 *  <ul>
	 *   <li> REturned node is <code>null</code>
	 *   <li> Parentheses are unbalanced
	 *   <li> Syntax causes the math parser to throw an exception
	 */
	public boolean validate(String toValidate) {
		ASTNode math=null;
		if(toValidate==null){
			err="No maths to validate!";
			return false;
		}
		if (unBalancedBrackets(toValidate)){
			err= " Unbalanced parentheses. ";
			return false;
		}
		try {
		  math = Libsedml.parseFormulaString(toValidate);
		  math.getIdentifiers();
		} catch (RuntimeException e){
			err= " Maths field is invalid syntax : " + e.getMessage();
			return false;
		}
		if(math==null){
			err= " Maths field is empty, please add an expression.";
			return false;
		}
		return true;
	}

	private boolean unBalancedBrackets(String toValidate) {
		 int lhc=0;
		 int rhc = 0;
		 for (char c : toValidate.toCharArray()){
			 if (c == '('){
				 lhc++;
			 }else if (c ==')'){
				 rhc++;
			 }
		 }
		 return lhc !=rhc;
	}

}
