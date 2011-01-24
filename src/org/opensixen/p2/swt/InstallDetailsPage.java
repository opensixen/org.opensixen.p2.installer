 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.p2.swt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.InstallDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.p2.equinox.installer.InstallDescription;
import org.eclipse.p2.equinox.installer.InstallDescriptionParser;
import org.eclipse.p2.equinox.installer.InstallUpdateProductOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.applications.InstallableApplication;
import org.opensixen.p2.common.Installer;

/**
 * 
 * InstallDetailsPage 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class InstallDetailsPage extends WizardPage implements InstallerWizardPage, SelectionListener {

	private Text installDetails;
	
	private Button installBtn;

	public InstallDetailsPage() {
		super(Messages.INSTALL_CONFIRM);
		setDescription(Messages.INSTALL_CONFIRM_DESCRIPTION);
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

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout());
		main.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		installDetails = new Text(main,SWT.MULTI | SWT.BORDER);
		installDetails.setEditable(false);
		GridData installData = new GridData(SWT.FILL, SWT.FILL, true, true);
		installData.heightHint = 200;
		installDetails.setLayoutData(installData);
		
		
		// Button for install
		Composite btnComposite = new Composite(main, SWT.NONE);
		btnComposite.setLayout(new GridLayout(2, false));
		btnComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true));

		Label l = new Label(btnComposite, SWT.NONE);
		l.setText(Messages.PRESS_BUTTON_TO_INSTALL);

		installBtn = new Button(btnComposite, SWT.PUSH);
		installBtn.setText(Messages.INSTALL);
		installBtn.addSelectionListener(this);

		// Required to avoid an error in the system
		setControl(main);
		setPageComplete(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		InstallJob job = InstallJob.getInstance();		
		
		for (InstallableApplication app: job.getInstallableApplications())	{
			if (app.isInstallOk() == false)	{
				return false;
			}
		}
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
		if (e.getSource().equals(installBtn)) {			
			try {
				install();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	private void install() throws Exception	{
		
		InstallJob job = InstallJob.getInstance();
		InstallDialog dialog = new InstallDialog(getShell());
		
		for (InstallableApplication app: job.getInstallableApplications())	{
			InstallDescription description = InstallDescriptionParser.createDescription(app, null);							
			InstallUpdateProductOperation op = new InstallUpdateProductOperation(description);			
			IStatus status = dialog.run(op);	
			
			if (status.getSeverity() != IStatus.ERROR)	{
				// run custom app stuff
				app.afterInstall();
				
				// Set app install ok
				app.setInstallOk(true);
			}
			else {
				app.setInstallOk(false);
				MessageBox message = new MessageBox(getShell(), SWT.ERROR);
				message.setText(status.getMessage());
				if (status.getException() != null)	{
					message.setMessage(status.getException().getMessage());
				}
				else {
					message.setMessage(status.getMessage());
				}
				
				message.open();
			}			
		}
		
		dialog.close();		
		getContainer().updateButtons();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		InstallJob job = InstallJob.getInstance();
		StringBuffer buff = new StringBuffer();
		for (InstallableApplication app:job.getInstallableApplications())	{
			buff.append("Instalar: ").append(app.getIu()).append("\n");
			buff.append("Path: " ).append(app.getPath()).append("\n\n");			
		}
		
		installDetails.setText(buff.toString());
	}

}
