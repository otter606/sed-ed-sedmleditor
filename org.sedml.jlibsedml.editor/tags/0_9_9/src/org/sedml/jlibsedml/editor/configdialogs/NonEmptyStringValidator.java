package org.sedml.jlibsedml.editor.configdialogs;


/**
 * Validates text field as  non-empty or <code>non-null</code>.
 * @author radams
 *
 */
public class NonEmptyStringValidator implements IValidator {
	
	/**
	 * 
	 * @param fName Name or label of text-b
	 */
	NonEmptyStringValidator(String fName) {
		super();
		this.fName = fName;
	}

	private String err;
	private String fName= "";
	public String getValidationFailureMessage() {
		// TODO Auto-generated method stub
		return err;
	}

	public boolean validate(String toValidate) {
		if (!(toValidate!=null && toValidate.length()>0 && !toValidate.matches("^\\s+$"))) {
			err="Please complete  field [" + fName + "].";
			return false;
		}
		return true;
	}

}
