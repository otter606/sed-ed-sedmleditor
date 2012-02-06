package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jdom.Element;
import org.sedml.Notes;
import org.sedml.jlibsedml.editor.gmodel.GElement;
import org.sedml.jlibsedml.xmlutils.XMLHandler;

/**
 * Handles creation of AddNotes dialog.
 * 
 * @author radams
 * 
 */
public class AddNoteDialog extends BaseConfigDialog {
	private StyledText text;
	private Browser browser;
	private GElement sedmlEl;
	private XMLHandler handler;
	private Notes oldNotes;
	private Button clearNotesBtn;
	private boolean textIsCleared = false;

	public AddNoteDialog(Shell shell, GElement model) {
		super(shell);
		this.sedmlEl = model;
		this.oldNotes = model.getNotes();
		this.handler = new XMLHandler();

	}

	protected Control createDialogArea(Composite parent) {
		Composite child = (Composite) super.createDialogArea(parent);
		getShell().setText("Add annotation");
		// Composite child = new Composite(parent,SWT.NULL);

		child.setLayout(new GridLayout(2, true));
		createTextEntryField(child);

		createBrowser(child);
		createClearNotesBttn(child);
		text.addModifyListener(new VerifyingModifyListener());
		text.addFocusListener(new VerifyingFocusListener());
		if (sedmlEl.getNotes() != null) {
			Element el = sedmlEl.getNotes().getNotesElement();
			text.setText(handler.getElementsAsString(Arrays
					.asList(new Element[] { el })));
		}else {
			text.setText("<p>\n\n</p>");
			text.setCaretOffset(4); // sets cursor between p tags
		}
		
		createStatus(child, 2);
		setHelp(parent);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		// verifyAll(createVerifyObjects());

		return child;
	}

	private void createClearNotesBttn(Composite child) {
		clearNotesBtn = new Button(child, SWT.PUSH);
		clearNotesBtn.setText("Remove Note");
		clearNotesBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				text.setText("");
				enableOK();
				textIsCleared = true;

			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	private void setHelp(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".notes");

	}

	private void createBrowser(Composite child) {
		try {
			browser = new Browser(child, SWT.NULL);
		} catch (SWTError e) {
			MessageDialog.openError(getShell(), "Error opening browser",
					"Sorry, on this platform  an embedded browser couldn't be opened -"
							+ "Please edit the text field with HTML input");
		}
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		gd2.minimumHeight = 200;
		gd2.minimumWidth = 200;
		browser.setLayoutData(gd2);
	}

	private void createTextEntryField(Composite child) {
		text = new StyledText(child, SWT.BORDER | SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 200;
		gd.minimumWidth = 200;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				browser.setText(text.getText());
				textIsCleared = false;

			}
		});

	}

	protected void okPressed() {
		final String textStr = text.getText();
		if (hasChanged(textStr)) {
			execute(new ICommand() {
				public void undo() {
					resetOldValues();
				}

				public void redo() {
					if (textIsCleared) {
						sedmlEl.setNotes(null);
					} else {
						sedmlEl.setNotes(new Notes(handler.getXMLElements(
								textStr).get(0)));
					}
				}

				public void execute() {
					redo();

				}

				public String getLabel() {
					return "Edit Note";
				}
			});
		}

		super.okPressed();

	}

	private boolean hasChanged(String textStr) {
		if (oldNotes == null) {
			return true;
		}
		return !textStr.equals(handler.getElementAsString(oldNotes
				.getNotesElement()));
	}

	@Override
	List<VerifyObject> createVerifyObjects() {
		VerifyObject v1 = new VerifyObject(text.getText(), handler);
		return Arrays.asList(new VerifyObject[] { v1 });
	}

	@Override
	void resetOldValues() {
		sedmlEl.setNotes(oldNotes);

	}

}
