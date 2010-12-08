/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.opensixen.os.PlatformProvider;
import org.opensixen.os.ProviderFactory;
import org.opensixen.p2.applications.ClientApplication;
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.applications.InstallableApplication;
import org.opensixen.p2.applications.LiteApplication;
import org.opensixen.p2.applications.PostgresApplication;
import org.opensixen.p2.applications.ServerApplication;

/**
 * @author harlock
 *
 */
public class InstallationTypePage extends WizardPage implements InstallerWizardPage, SelectionListener {

	private Logger log = Logger.getLogger(getClass());
	
	private Button liteBtn;
	private Button liteDBBtn;
	private Button clientBtn;
	private Button serverBtn;
	private Button serverDBBtn;
	private Button optionLite;
	private Button optionStd;

	/**
	 * @param pageName
	 */
	protected InstallationTypePage() {
		super(Messages.INSTALL_TYPE);
		setDescription(Messages.INSTALL_TYPE_DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		
		optionLite = new Button(container, SWT.CHECK);
		optionLite.setText(Messages.LITE_VERSION);
		optionLite.addSelectionListener(this);
		
		
		// Por defecto, el instalador ofrecera inatalar la version Lite
		optionLite.setSelection(true);
		
		Group liteGroup = new Group(container, SWT.NONE);
		liteGroup.setLayout(new GridLayout(2, true));
		liteGroup.setText(Messages.OPENSIXEN_LITE);
		liteGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		liteBtn = new Button(liteGroup, SWT.RADIO);
		liteBtn.setText(Messages.OPENSIXEN_LITE);
		liteBtn.addSelectionListener(this);
		
		liteDBBtn = new Button(liteGroup, SWT.CHECK);
		liteDBBtn.setText(Messages.OPENSIXEN_DBSERVER);		
		liteDBBtn.addSelectionListener(this);
		
		liteGroup.pack();
		
		optionStd = new Button(container, SWT.CHECK);
		optionStd.setText(Messages.STD_VERSION);
		optionStd.addSelectionListener(this);
		
		Group stdGroup = new Group(container, SWT.NONE);
		stdGroup.setLayout(new GridLayout(2, true));
		stdGroup.setText(Messages.OPENSIXEN);
		stdGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		clientBtn = new Button(stdGroup, SWT.CHECK);
		clientBtn.setText(Messages.OPENSIXEN_CLIENT);
		clientBtn.addSelectionListener(this);
		
		
		serverBtn = new Button(stdGroup, SWT.CHECK);
		serverBtn.setText(Messages.OPENSIXEN_SERVER);
		serverBtn.addSelectionListener(this);
			
		serverDBBtn = new Button(stdGroup, SWT.CHECK);
		serverDBBtn.setText(Messages.OPENSIXEN_DBSERVER);
		serverDBBtn.addSelectionListener(this);
		stdGroup.pack();
		
		updateButtons();
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);


	}
	
	private void updateButtons()	{
		if (optionLite.getSelection())	{
			// Std
			optionStd.setSelection(false);
			//clientBtn.setSelection(false);
			clientBtn.setEnabled(false);
			//serverBtn.setSelection(false);
			serverBtn.setEnabled(false);
			serverDBBtn.setEnabled(false);
			// Lite
			liteBtn.setSelection(true);
			liteBtn.setEnabled(true);
			
			liteDBBtn.setEnabled(true);
			
			log.trace(Messages.LITE_SELECTED);
			
		}
		else  {
			// Std
			optionLite.setSelection(false);
			//clientBtn.setSelection(true);
			clientBtn.setEnabled(true);
			//serverBtn.setSelection(false);
			serverBtn.setEnabled(true);			
			serverDBBtn.setEnabled(true);
			
			// Lite
			//liteBtn.setSelection(false);
			liteBtn.setEnabled(false);			
			liteDBBtn.setEnabled(false);
			log.trace(Messages.STD_SELECTED);
		}		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(optionLite))	{
			optionStd.setSelection(false);
			updateButtons();
		}
		else if (e.getSource().equals(optionStd))	{
			optionLite.setSelection(false);
			updateButtons();
		}						
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		InstallJob job = InstallJob.getInstance();
		ArrayList<InstallableApplication> applications = new ArrayList<InstallableApplication>();
		
		if (optionLite.getSelection())	{
			applications.add(new LiteApplication());
			if (liteDBBtn.getSelection())	{
				applications.add(new PostgresApplication());
			}
		}
		else {
			if (clientBtn.getSelection())	{
				applications.add(new ClientApplication());
			}
			
			if (serverBtn.getSelection())	{
				applications.add(new ServerApplication());
			}
			if (serverDBBtn.getSelection())	{
				applications.add(new PostgresApplication());
			}
		}
		job.setInstallableApplications(applications);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		PlatformProvider platform = ProviderFactory.getProvider();
		if (platform.isUnix())	{
			
		}
		
	}
	
	
}

