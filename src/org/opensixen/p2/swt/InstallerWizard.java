/**
 * 
 */
package org.opensixen.p2.swt;


import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * @author harlock
 *
 */
public class InstallerWizard extends Wizard {
	
	private Shell shell;
	private WizardDialog dialog;
	
	
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
		addPage(new InstallationTypePage());		
		addPage(new InstallLocationPage());				
		addPage(new InstallDetailsPage());
		addPage(new SetupPage());				
		addPage(new SetupDetailsPage());			
	}
	
		
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return true;
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
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return processTransition(page);	
	}

	
	private IWizardPage processTransition(IWizardPage current)	{
		InstallerWizardPage pp = (InstallerWizardPage) current;
		pp.storeDialogSettings();
		IWizardPage next =  super.getNextPage(current);		
		InstallerWizardPage np = (InstallerWizardPage) next;
		np.refresh();
		return np;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		return processTransition(page);
	}
				
}
