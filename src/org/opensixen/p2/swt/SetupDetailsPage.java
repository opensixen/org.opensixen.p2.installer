/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * @author harlock
 *
 */
public class SetupDetailsPage extends WizardPage implements ChangeListener, SelectionListener {

	private InstallerWizard wizard;

	
	private boolean configured = false;
	
	private Button configBtn;


	private Label installType;

	public SetupDetailsPage(InstallerWizard wizard)	{
		super(Messages.CONFIRM_SETUP);
		setDescription(Messages.CONFIRM_SETUP_DESCRIPTION);
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
			l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
			installType = new Label(container, SWT.NONE);
			installType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

			// Button for install
			Composite btnComposite = new Composite(main, SWT.NONE);
			btnComposite.setLayout(new GridLayout(2, false));
			btnComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));
			
			l = new Label(btnComposite, SWT.NONE);
			l.setText(Messages.PRESS_BUTTON_TO_CONFIGURE);
			
			configBtn = new Button(btnComposite, SWT.PUSH);
			configBtn.setText(Messages.CONFIG);
			configBtn.addSelectionListener(this);
						
			// Required to avoid an error in the system
			setPageComplete(false);
			setControl(main);

		
	}
		
	
	public void update()	{
		// Add all the properties in the configuration
		Properties prop = wizard.getSetupPage().getConfiguration();
		if (prop == null)	{
			return;
		}
		Enumeration<String> keys = (Enumeration<String>)prop.propertyNames();
		while (keys.hasMoreElements())	{
			String key = keys.nextElement();
		
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
		return configured;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(configBtn))	{
			ConfigProgressBarDialog worker = new ConfigProgressBarDialog(getShell(), wizard);
			worker.open();
			worker.run();
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
