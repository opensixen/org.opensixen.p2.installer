/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.InstallDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.p2.equinox.installer.InstallDescription;
import org.eclipse.p2.equinox.installer.InstallDescriptionParser;
import org.eclipse.p2.equinox.installer.InstallUpdateProductOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.applications.InstallableApplication;
import org.opensixen.p2.common.Installer;

/**
 * @author harlock
 * 
 */
public class InstallDetailsPage extends WizardPage implements InstallerWizardPage, SelectionListener {

	private Text installDetails;
	
	private Button installBtn;

	public InstallDetailsPage() {
		super(Messages.INSTALL_CONFIRM);
		setDescription(Messages.INSTALL_CONFIRM_DESCRIPTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout());
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		installDetails = new Text(main,SWT.MULTI | SWT.BORDER);
		installDetails.setEditable(false);
		GridData installData = new GridData(SWT.FILL, SWT.FILL, true, true);
		installData.heightHint = 200;
		installDetails.setLayoutData(installData);
		
		
		// Button for install
		Composite btnComposite = new Composite(main, SWT.NONE);
		btnComposite.setLayout(new GridLayout(2, false));
		btnComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));

		Label l = new Label(btnComposite, SWT.NONE);
		l.setText(Messages.PRESS_BUTTON_TO_INSTALL);

		installBtn = new Button(btnComposite, SWT.PUSH);
		installBtn.setText(Messages.INSTALL);
		installBtn.addSelectionListener(this);

		// Required to avoid an error in the system
		setControl(main);
		setPageComplete(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(installBtn)) {			
			try {
				install();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	private void install() throws Exception	{
		
		InstallJob job = InstallJob.getInstance();
		InstallDialog dialog = new InstallDialog(getShell());
		
		for (InstallableApplication app: job.getInstallableApplications())	{
			InstallDescription description = InstallDescriptionParser.createDescription(app, null);							
			InstallUpdateProductOperation op = new InstallUpdateProductOperation(description);			
			IStatus status = dialog.run(op);	
			
			if (status.getSeverity() != IStatus.ERROR)	{
				// run custom app stuff
				app.afterInstall();
				
				app.setInstallOk(true);
			}
			else {
				app.setInstallOk(false);
				MessageBox message = new MessageBox(getShell(), SWT.ERROR);
				message.setText(status.getMessage());
				if (status.getException() != null)	{
					message.setMessage(status.getException().getMessage());
				}
				else {
					message.setMessage(status.getMessage());
				}
				
				message.open();
			}			
		}
		
		dialog.close();
		
		//return op.getResult();		
		
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		InstallJob job = InstallJob.getInstance();
		StringBuffer buff = new StringBuffer();
		for (InstallableApplication app:job.getInstallableApplications())	{
			buff.append("Instalar: ").append(app.getIu()).append("\n");
			buff.append("Path: " ).append(app.getPath()).append("\n\n");			
		}
		
		installDetails.setText(buff.toString());
	}

}
