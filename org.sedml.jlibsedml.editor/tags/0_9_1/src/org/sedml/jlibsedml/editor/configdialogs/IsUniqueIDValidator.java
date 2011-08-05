package org.sedml.jlibsedml.editor.configdialogs;

import org.sedml.jlibsedml.editor.gmodel.GElement;

public class IsUniqueIDValidator implements IValidator {
	private GElement el;
	public IsUniqueIDValidator(GElement el) {
		super();
		this.el = el;
	}

	private String err;
	public String getValidationFailureMessage() {
		return err;
	}

	public boolean validate(String toValidate) {
		if(el.getParent()==null){
			throw new IllegalStateException();
		} 
		if(toValidate == null || toValidate.length() == 0){
			err = "Id can't be empty";
			return false;
		}else if (! (toValidate.matches("^(_|[a-z]|[A-Z])(_|[a-z]|[A-Z]|[0-9])*$"))){
			err = "Please enter an alphanumeric ID";
			return false;
		}
		else {
			for (GElement child: el.getParent().getChildren()){
				if(child!=el && toValidate.equalsIgnoreCase(child.getId())){
						err= toValidate + " is not a unique identifier, please modify it.";
						return false;
					}
				} 
			}
		
		return true;
	}

}
