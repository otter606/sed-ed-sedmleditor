package org.sedml.jlibsedml.editor.configdialogs;


public class PositiveIntValidator implements IValidator {

	private static final String ERROR_MESSAGE = "Time points must be a non-negative integer";
	String error =null;
	public String getValidationFailureMessage() {
		return error;
	}

	public boolean validate(String toValidate) {
			Integer numPoints=null;
			try {
				numPoints=Integer.parseInt(toValidate);
				if(numPoints < 0){
					error=ERROR_MESSAGE;
					return false;
				}
			}catch(NumberFormatException exc){
				error =ERROR_MESSAGE;
				return false;
			}
			return true;
		}
	

}
