package org.sedml.jlibsedml.editor.configdialogs;

public interface IValidator {

	
	boolean validate (String toValidate);
	
	String getValidationFailureMessage();
}
