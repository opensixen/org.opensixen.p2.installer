/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class InstallProgressBarDialog extends RunableProgressBarDialog {

	private InstallerWizard wizard;
	
	/**
	 * @param parent
	 * @param wizard
	 */
	public InstallProgressBarDialog(Shell parent, InstallerWizard wizard) {
		super(parent, wizard);
		this.wizard = wizard;
	}
	
	@Override
	public void create() {
		super.create();
		setTitle(Messages.INSTALLING_OPENSIXEN);
		setMessage(Messages.INSTALLING_OPENSIXEN_WAIT, IMessageProvider.INFORMATION);
	}
	
	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.RunableProgressBarDialog#getRunnable()
	 */
	@Override
	public ProgressBarRunnable getRunnable() {
		String installType = wizard.getInstallationTypePage().getInstallType();
		String clientPath = wizard.getInstallLocationPage().getClientInstallPath();
		String serverPath = wizard.getInstallLocationPage().getServerInstallPath();
		InstallWorker worker = new InstallWorker(installType, clientPath, serverPath, this);
		return worker;
	}

	public void install()	{
		run();
	}

	

	
}
