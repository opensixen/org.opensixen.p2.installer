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

	private InstallerWizard wizard;

	/**
	 * @param parent
	 */
	public ConfigProgressBarDialog(Shell parent,  InstallerWizard wizard) {
		super(parent);
		this.wizard = wizard;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Configurando Opensixen");
		setMessage("Se esta configurando Opensixen, por favor espere.", IMessageProvider.INFORMATION);
	}
	
	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.RunableProgressBarDialog#getRunnable()
	 */
	@Override
	public ProgressBarRunnable getRunnable() {
		SetupPage setupPage = wizard.getSetupPage();
		Properties prop = setupPage.getConfiguration();
		InstallLocationPage location = wizard.getInstallLocationPage();

		// Add some configuration
		prop.put("SetupType", setupPage.getConfigType());
		prop.put("ClientPath", location.getClientInstallPath());
		prop.put("ServerPath", location.getServerInstallPath());
		prop.put("InstallType", wizard.getInstallationTypePage().getInstallType());
		prop.put(Ini.P_CONNECTION, setupPage.toStringLong ());
		
		ConfigWorker worker = new ConfigWorker(prop, this);
		return worker;
		
	}
	
	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.RunableProgressBarDialog#finishWork()
	 */
	@Override
	public synchronized void finishWork() {
		// TODO Auto-generated method stub
		super.finishWork();
		wizard.fireChange(this);
	}

}
