/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public interface InstallerWizardPage extends IWizardPage {

	public boolean storeDialogSettings();
	
	public void refresh();
	
}
