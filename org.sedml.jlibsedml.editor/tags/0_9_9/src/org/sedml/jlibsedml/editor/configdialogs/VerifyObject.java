package org.sedml.jlibsedml.editor.configdialogs;

public class VerifyObject {
	
	VerifyObject(String toVerify, IValidator validator) {
		super();
		this.toVerify = toVerify;
		this.validator = validator;
	}
	private String toVerify;
	private IValidator validator;
	
	boolean isValid(){
		return validator.validate(toVerify);
	}
	
	/**
	 * Gets failure message if <code>isValid()==false</code> or <code>null</code>.
	 * @return
	 */
	String getMessage(){
		return validator.getValidationFailureMessage();
	}

}
