/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opensixen.p2.common.Installer;
import org.opensixen.p2.common.ProductDescription;

/**
 * @author harlock
 *
 */
public class InstallDetailsPage extends WizardPage implements ChangeListener, SelectionListener {

	private InstallerWizard wizard;
	private Label installType;
	private Label clientPath;
	private Label serverPath;
	
	private boolean installed = false;
	private Button installBtn;
	private InstallProgressBarDialog worker;

	public InstallDetailsPage(InstallerWizard wizard)	{
		super(Messages.INSTALL_CONFIRM);
		setDescription(Messages.INSTALL_CONFIRM_DESCRIPTION);
		this.wizard = wizard;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout());
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite container = new Composite(main, SWT.NULL);
		GridLayout layout = new GridLayout(2,false);
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
						
			Label l = new Label(container, SWT.BOLD);
			l.setText(Messages.INSTALL_TYPE);
			installType = new Label(container, SWT.NONE);
			installType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
			l = new Label(container, SWT.BOLD);
			l.setText(Messages.CLIENT_PATH);
			clientPath = new Label(container, SWT.NONE);
			clientPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
			
			l = new Label(container, SWT.BOLD);
			l.setText(Messages.SERVER_PATH);
			serverPath = new Label(container, SWT.READ_ONLY);
			serverPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
			
			
			// Button for install
			Composite btnComposite = new Composite(main, SWT.NONE);
			btnComposite.setLayout(new GridLayout(2, false));
			btnComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
			
			l = new Label(btnComposite, SWT.NONE);
			l.setText(Messages.PRESS_BUTTON_TO_INSTALL);
			
			installBtn = new Button(btnComposite, SWT.PUSH);
			installBtn.setText(Messages.INSTALL);
			installBtn.addSelectionListener(this);
			
			
			update();
			// Required to avoid an error in the system
			setControl(main);
			setPageComplete(false);

	}
	
	private void update()		{
		installType.setText(wizard.getInstallationTypePage().getInstallTypeLabel());
		clientPath.setText(wizard.getInstallLocationPage().getClientInstallPath());
		
		if (wizard.getInstallationTypePage().getInstallType().equals(ProductDescription.TYPE_FULL)
				|| wizard.getInstallationTypePage().getInstallType().equals(ProductDescription.TYPE_SERVER))	{
			serverPath.setText(wizard.getInstallLocationPage().getServerInstallPath());
		}
		else {
			serverPath.setText(Messages.NOT_INSTALLED + ProductDescription.LABEL_SERVER);
		}
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.ChangeListener#changePerformed()
	 */
	@Override
	public void changePerformed() {
		update();	
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		if (worker == null)	{
			return false;
		}
		
		if (worker.finished())	{
			return worker.workOk();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(installBtn))	{
			worker = new InstallProgressBarDialog(getShell(), wizard);
			worker.open();
			worker.install();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
