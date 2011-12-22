package org.sedml.jlibsedml.editor.configdialogs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.sedml.jlibsedml.editor.gmodel.GModel;
import org.sedml.jlibsedml.editor.gmodel.LanguageHelper;
import org.sedml.jlibsedml.xmlUI.XMLPreviewer;
import org.sedml.modelsupport.SUPPORTED_LANGUAGE;

/**
 * Dialog for editing properties of a SEDML model element.
 * 
 * @author radams
 * 
 */
public class ModelConfigDialog extends BaseConfigDialog {
	private GModel m;
	private Text srcText;
	private Combo lCombo;
	private Button previewButton;
	private LanguageHelper langHelper = new LanguageHelper();
	private String oldID, oldName, OldSrc, OldLang;

	public ModelConfigDialog(Shell shell, GModel model) {
		super(shell);
		this.m = model;
		this.oldID = model.getId();
		this.oldName = model.getName();
		this.OldSrc = model.getSource();
		this.OldLang = model.getLanguage();
		// TODO Auto-generated constructor stub
	}

	protected Control createDialogArea(Composite parent) {
		Composite child = (Composite) super.createDialogArea(parent);
		getShell().setText("Add properties to model");
		// Composite child = new Composite(parent,SWT.NULL);
		child.setLayout(createGridLayout(3));

		new IDNameGroup(child, m);
		createSourceRow(child);
		createLanguageRow(child);
		createPreviewButton(child);
		createStatus(child, 3);
		setHelp(parent);
		setHelpAvailable(true);
		setInitialisationComplete(true);
		return child;

	}

	private void createPreviewButton(Composite child) {
		previewButton = new Button(child, SWT.PUSH);
		previewButton.setText("Preview model");
		calculatePreviewButtonEnabled();
		previewButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (m.canGetModel()) {

					updateModel(srcText.getText(), langHelper.getURNForLang(lCombo.getText()),m.getName());

					Document model = m.getModelDocument(true);
					Document unChangedModel = m.getModelDocument(false);
					XMLPreviewer viewer = new XMLPreviewer(getShell(), model,unChangedModel);
					viewer.open();
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	private void calculatePreviewButtonEnabled() {
		if (previewButton == null || previewButton.isDisposed()) {
		
			return;
		}
		if (srcText != null && lCombo != null && lCombo.getText() != null) {
			updateModel(srcText.getText(), langHelper.getURNForLang(lCombo.getText()), m.getName());
			if (m.canGetModel())
				previewButton.setEnabled(true);
			else 
				previewButton.setEnabled(false);
			return;

		}

		previewButton.setEnabled(false);
	}

	private void setHelp(Composite parent) {
		// setHelpAvailable(true);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				PLUGINID + ".model");

	}

	void resetOldValues() {
		m.setId(oldID);
		m.setName(oldName);
		m.setSource(OldSrc);
		m.setLanguage(OldLang);

	}

	private void createLanguageRow(Composite child) {
		new Label(child, SWT.NULL).setText("Language: ");
		lCombo = new Combo(child, SWT.DROP_DOWN);

		// chooose from dropdown list
		lCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				verifyAll(createVerifyObjects());
				calculatePreviewButtonEnabled();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				verifyAll(createVerifyObjects());
			}

		});

		// text can be modified

		lCombo.setItems(langHelper.getLanguagesAsStrings());
		langHelper.getURNForLang("");

		lCombo.addModifyListener(new VerifyingModifyListener() {
			public void modifyText(ModifyEvent e) {
				super.modifyText(e);
				calculatePreviewButtonEnabled();

			}
		});

		// set default if new object
		if (m.getLanguage() == null) {
			m.setLanguage(SUPPORTED_LANGUAGE.SBML_GENERIC.getURN());
			lCombo.select(langHelper
					.getIndexFor(SUPPORTED_LANGUAGE.SBML_GENERIC));
			// else if unknown, set text
		} else if (langHelper.getIndexForURN(m.getLanguage()) == -1) {
			lCombo.setText(m.getLanguage());
			// else set the selection
		} else {
			lCombo.select(langHelper.getIndexForURN(m.getLanguage()));
		}

		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lCombo.setLayoutData(gd);

	}

	private void createSourceRow(Composite child) {
		new Label(child, SWT.NULL).setText("Source: ");
		srcText = new Text(child, SWT.BORDER);
		srcText.addModifyListener(new VerifyingModifyListener() {
			public void modifyText(ModifyEvent e) {
				super.modifyText(e);
				calculatePreviewButtonEnabled();

			}
		});
		srcText.setEditable(true);
		srcText.setText(m.getSource() == null ? "" : m.getSource());
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);

		srcText.setLayoutData(gd);
		Button browse = new Button(child, SWT.PUSH);
		browse.setText("Browse...");
		browse.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell());
				String file = fd.open();
				if (file != null) {
					srcText.setText(file);

				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	@Override
	List<VerifyObject> createVerifyObjects() {
		if (isInitialising) {
			return Collections.EMPTY_LIST;
		}
		VerifyObject v1 = new VerifyObject(srcText.getText(),
				new NonEmptyStringValidator("Source"));
		VerifyObject v2 = new VerifyObject(lCombo.getText(),
				new NonEmptyStringValidator("Language"));
		return Arrays.asList(new VerifyObject[] { v1, v2 });
	}

	protected void okPressed() {
		final String src = srcText.getText();
		final String lang = langHelper.getURNForLang(lCombo.getText());
		final String name = m.getName();
		if (hasChanged(src, lang,name)) {

			execute(new ICommand() {

				public void undo() {
					resetOldValues();
				}

				public void redo() {
					updateModel(src, lang,name);

				}

				public void execute() {
					redo();

				}

				public String getLabel() {
					return "Edit Model";
				}
			});
		}

		super.okPressed();

	}

	private void updateModel(final String src, final String lang, String name) {
		m.setSource(src);
		m.setLanguage(lang);
		m.setName(name);
	}

	private boolean hasChanged(String src, String lang, String name) {
		return (!src.equals(OldSrc) || !lang.equals(OldLang) || !name.equals(oldName));
	}

}
