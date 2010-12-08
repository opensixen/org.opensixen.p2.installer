/**
 * 
 */
package org.opensixen.p2.swt;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
public class InstallLocationPage extends WizardPage implements InstallerWizardPage,
		SelectionListener, ModifyListener {
	
	private Logger log = Logger.getLogger(getClass());
	
	private Text fClientPath;
	private Button bClient;
	private Text fServerPath;
	private Button bServer;
	
	private Text fDBPath;
	private Button bDB;

	public InstallLocationPage() {
		super(Messages.INSTALL_LOCATION);		
		setDescription(Messages.INSTALL_LOCATION_DESCRIPTION);
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
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		Label l = new Label(container, SWT.NONE);
		l.setText(Messages.CLIENT_PATH);
		Composite c = new Composite(container, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fClientPath = new Text(c, SWT.BORDER);
		fClientPath
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fClientPath.addModifyListener(this);
		bClient = new Button(c, SWT.PUSH);
		bClient.setText(Messages.SEARCH);
		bClient.addSelectionListener(this);

		// Server
		l = new Label(container, SWT.NONE);
		l.setText(Messages.SERVER_PATH);
		c = new Composite(container, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fServerPath = new Text(c, SWT.BORDER);
		fServerPath
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fServerPath.addModifyListener(this);
		bServer = new Button(c, SWT.PUSH);
		bServer.setText(Messages.SEARCH);
		bServer.addSelectionListener(this);		
		
		// Postgres
		l = new Label(container, SWT.NONE);
		l.setText("DB_PATH");
		c = new Composite(container, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBPath = new Text(c, SWT.BORDER);
		fDBPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBPath.addModifyListener(this);
		bDB = new Button(c, SWT.PUSH);
		bDB.setText(Messages.SEARCH);
		bDB.addSelectionListener(this);		

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		

		return true;
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
		if (e.getSource().equals(bClient)) {
			DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
			dd.setText(Messages.OPEN);

			String dir = dd.open();
			if (dir != null) {
				fClientPath.setText(dir);
			}

		}

		else if (e.getSource().equals(bServer)) {
			DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
			dd.setText(Messages.OPEN);

			String dir = dd.open();
			if (dir != null) {
				fServerPath.setText(dir);
			}
		}
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

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		InstallJob job = InstallJob.getInstance();
		for (InstallableApplication app : job.getInstallableApplications())	{
			if (app.getIu().equals(LiteApplication.IU_LITE)
					|| app.getIu().equals(ClientApplication.IU_CLIENT))	{
				app.setPath(fClientPath.getText());				
			}
			
			if (app.getIu().equals(ServerApplication.IU_SERVER)) {
				app.setPath(fServerPath.getText());
			}
			
			if (app.getIu().equals(PostgresApplication.IU_POSTGRES)) {
				app.setPath(fDBPath.getText());
			}									
		}
		
		return true;		
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		InstallJob job = InstallJob.getInstance();
		
		fClientPath.setEnabled(false);
		bClient.setEnabled(false);
		fServerPath.setEnabled(false);
		bServer.setEnabled(false);
		fDBPath.setEnabled(false);
		bDB.setEnabled(false);
		
		for (InstallableApplication app : job.getInstallableApplications())	{
			if (app.getIu().equals(LiteApplication.IU_LITE)
					|| app.getIu().equals(ClientApplication.IU_CLIENT))	{
				fClientPath.setEnabled(true);
				bClient.setEnabled(true);
			}
			
			if (app.getIu().equals(ServerApplication.IU_SERVER)) {
				fServerPath.setEnabled(true);
				bServer.setEnabled(true);
			}
			
			if (app.getIu().equals(PostgresApplication.IU_POSTGRES)) {
				fDBPath.setEnabled(true);
				bDB.setEnabled(true);
			}									
		}			
	}


}
