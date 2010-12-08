/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.Properties;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.widgets.Shell;
import org.opensixen.p2.installer.Ini;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class ConfigProgressBarDialog extends RunableProgressBarDialog {

	/**
	 * @param parent
	 */
	public ConfigProgressBarDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.CONFIGURING_OPENSIEN);
		setMessage(Messages.CONFIGURING_OPENSIXEN_DESCRIPTION, IMessageProvider.INFORMATION);
	}
	
	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.RunableProgressBarDialog#getRunnable()
	 */
	@Override
	public ProgressBarRunnable getRunnable() {
	/*
		SetupPage setupPage = wizard.getSetupPage();
		Properties prop = setupPage.getConfiguration();
		InstallLocationPage location = wizard.getInstallLocationPage();

		// Add some configuration
		prop.put("SetupType", setupPage.getConfigType()); //$NON-NLS-1$
		prop.put("ClientPath", location.getClientInstallPath()); //$NON-NLS-1$
		prop.put("ServerPath", location.getServerInstallPath()); //$NON-NLS-1$
		prop.put("InstallType", wizard.getInstallationTypePage().getInstallType()); //$NON-NLS-1$
		prop.put(Ini.P_CONNECTION, setupPage.toStringLong ());
		
		ConfigWorker worker = new ConfigWorker(prop, this);
		return worker;
		*/
		return null;
		
	}

}
