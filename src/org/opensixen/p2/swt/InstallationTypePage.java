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
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.applications.InstallableApplication;
import org.opensixen.p2.installer.apps.ClientApplication;
import org.opensixen.p2.installer.apps.LiteApplication;
import org.opensixen.p2.installer.apps.PostgresApplication;
import org.opensixen.p2.installer.apps.ServerApplication;

/**
 * 
 * InstallationTypePage 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class InstallationTypePage extends WizardPage implements InstallerWizardPage, SelectionListener {

	private Logger log = Logger.getLogger(getClass());
	
	private PlatformProvider provider;
	
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
		provider = ProviderFactory.getProvider();
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
		
		// if unix, disable postrgres
		if (provider.isUnix())	{
			serverDBBtn.setEnabled(false);
			liteDBBtn.setEnabled(false);
		}				
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(optionLite))	{
			optionStd.setSelection(false);			
		}
		else if (e.getSource().equals(optionStd))	{
			optionLite.setSelection(false);
		}				
		updateButtons();
		// Update wizard buttons
		getContainer().updateButtons();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		updateButtons();		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		if (optionLite.getSelection())	{
			return true;
		}
		else {
			if (serverBtn.getSelection() || clientBtn.getSelection() || serverDBBtn.getSelection())	{
				return true;
			}
		}
		return false;
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
		
		
	}
	
	
}

