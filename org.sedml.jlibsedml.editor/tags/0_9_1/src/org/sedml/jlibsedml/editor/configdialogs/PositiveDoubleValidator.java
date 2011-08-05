package org.sedml.jlibsedml.editor.configdialogs;

public class PositiveDoubleValidator implements IValidator {
	private String err;
	public String getValidationFailureMessage() {
		return err;
	}

	public boolean validate(String toValidate) {
		Double rc=null;
		try {
			rc=Double.parseDouble(toValidate);
			if(rc < 0){
				rc=null;
				err="Field [" + toValidate + "] must be a non-negative number";
				return false;
			}
		}catch(NumberFormatException exc){
			err="Field [" + toValidate+ "] must be a non-negative number";
			return false;
		}
		
		return true;
	}

}
