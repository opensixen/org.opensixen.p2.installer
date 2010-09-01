/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.opensixen.p2.common.Installer;
import org.opensixen.p2.common.ProductDescription;

/**
 * @author harlock
 *
 */
public class InstallerWizard extends Wizard {

	private InstallationTypePage installationTypePage;
	private InstallLocationPage installLocationPage;
	private InstallDetailsPage installDetailsPage;
	private SetupPage setupPage;
	
	
	private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
	private Shell shell;
	private WizardDialog dialog;
	
	private boolean canFinish = false;
	private SetupDetailsPage setupDetailsPage;
	
	public  InstallerWizard(Shell shell)	{
		super();
		this.shell = shell;
		setNeedsProgressMonitor(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		installationTypePage = new InstallationTypePage(this);
		installLocationPage = new InstallLocationPage(this);
		installDetailsPage = new InstallDetailsPage(this);
		
		addPage(installationTypePage);
		listeners.add(installationTypePage);
		
		addPage(installLocationPage);
		listeners.add(installLocationPage);
		
		
		addPage(installDetailsPage);
		listeners.add(installDetailsPage);
			
	}
	private boolean postInstallAdded = false;
	public void addPostInstallPages()	{
		setupPage = new SetupPage(this);
		addPage(setupPage);
		listeners.add(setupPage);
		
		
		setupDetailsPage = new SetupDetailsPage(this);
		addPage(setupDetailsPage);
		listeners.add(setupDetailsPage);
		
		postInstallAdded = true;
	}
 
	
	public void fireChange(Object caller)	{
		boolean ok = true;
			
		for (ChangeListener listener:listeners)	{
			listener.changePerformed();

			// If some listener cat't flit to next page, we can't finish
			if (!listener.canFlipToNextPage())	{
				ok = false;
			}
		}
		
		
		// If is called from the InstallDialog, add the setup
		// page because can't change the install type after this
		// stage.
		
		if (caller.getClass().equals(InstallProgressBarDialog.class) && !postInstallAdded)	{
			addPostInstallPages();
			ok = false;
		}
		
		
		
		if (ok)	{
			canFinish = true;
		}
		else {
			canFinish = false;
		}
				
		dialog.updateButtons();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
	}
	

	public void fireErrorExit()	{		
		canFinish = true;
		dialog.updateButtons();		
	}
	

	/**
	 * @return the installationTypePage
	 */
	public InstallationTypePage getInstallationTypePage() {
		return installationTypePage;
	}

	/**
	 * @return the installLocationPage
	 */
	public InstallLocationPage getInstallLocationPage() {
		return installLocationPage;
	}
	
	
	/**
	 * @return the setupPage
	 */
	public SetupPage getSetupPage() {
		return setupPage;
	}

	/**
	 * @return the setupDetailsPage
	 */
	public SetupDetailsPage getSetupDetailsPage() {
		return setupDetailsPage;
	}

	public void open()	{
		dialog = new WizardDialog(shell, this);
		dialog.open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return canFinish;
	}
	
	
	
	
}
