package org.sedml.jlibsedml.editor.configdialogs;

public class DoubleValidator implements IValidator {
	private String err;
	public String getValidationFailureMessage() {
		return err;
	}

	public boolean validate(String toValidate) {
		try {
			Double.parseDouble(toValidate);
		}catch(NumberFormatException exc){
			err="Field [" + getTruncatedErrStr(toValidate)+ "] must be a  number";
			return false;
		}
		
		return true;
	}
	
	String getTruncatedErrStr(String toVal){
		if(toVal.length() < 20){
			return toVal;
		}else {
			return toVal.substring(0, 20) + "...";
		}
	}

}
