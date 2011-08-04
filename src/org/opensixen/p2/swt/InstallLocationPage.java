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
 * InstallLocationPage 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class InstallLocationPage extends WizardPage implements InstallerWizardPage,
		SelectionListener, ModifyListener {
	
	private Logger log = Logger.getLogger(getClass());
	private PlatformProvider provider;
	
	private Text fClientPath;
	private Button bClient;
	private Text fServerPath;
	private Button bServer;
	
	private Text fDBPath;
	private Button bDB;

	public InstallLocationPage() {
		super(Messages.INSTALL_LOCATION);		
		setDescription(Messages.INSTALL_LOCATION_DESCRIPTION);
		provider = ProviderFactory.getProvider();
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
		l.setText(Messages.DB_PATH);
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
		InstallJob job = InstallJob.getInstance();
		for (InstallableApplication app : job.getInstallableApplications())	{
			if (app.getID().equals(LiteApplication.IU_LITE)
					|| app.getID().equals(ClientApplication.IU_CLIENT))	{
				if (fClientPath.getText().length() == 0)	{
					return false;
				}
			}
			
			if (app.getID().equals(ServerApplication.IU_SERVER)) {
				if (fServerPath.getText().length() == 0)	{
					return false;
				}
			}
			
			if (app.getID().equals(PostgresApplication.IU_POSTGRES)) {
				if (fDBPath.getText().length() == 0)	{
					return false;
				}
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
		if (e.getSource().equals(bClient)) {
			String dir = openFileChoser();
			if (dir != null) {
				fClientPath.setText(dir);
			}
		}

		else if (e.getSource().equals(bServer)) {
			String dir = openFileChoser();
			if (dir != null) {
				fServerPath.setText(dir);
			}
		}
		else if (e.getSource().equals(bDB)) {
			String dir = openFileChoser();			
			if (dir != null) {
				fDBPath.setText(dir);
			}
		}
	}

	private String openFileChoser()	{
		DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
		dd.setText(Messages.OPEN);
		String dir = dd.open();
		return dir;
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
		getContainer().updateButtons();
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		InstallJob job = InstallJob.getInstance();
		for (InstallableApplication app : job.getInstallableApplications())	{
			if (app.getID().equals(LiteApplication.IU_LITE)
					|| app.getID().equals(ClientApplication.IU_CLIENT))	{
				app.setPath(fClientPath.getText());				
			}
			
			if (app.getID().equals(ServerApplication.IU_SERVER)) {
				app.setPath(fServerPath.getText());
			}
			
			if (app.getID().equals(PostgresApplication.IU_POSTGRES)) {
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
			if (app.getID().equals(LiteApplication.IU_LITE)
					|| app.getID().equals(ClientApplication.IU_CLIENT))	{
				fClientPath.setEnabled(true);
				bClient.setEnabled(true);
				// Default Path
				if (fClientPath.getText().length() == 0)	{
					fClientPath.setText(provider.getPath(PlatformProvider.PATH_CLIENT_ROOT_DEFAULT));
				}
			}
			
			if (app.getID().equals(ServerApplication.IU_SERVER)) {
				fServerPath.setEnabled(true);
				bServer.setEnabled(true);
				// Default Path
				if (fServerPath.getText().length() == 0)	{
					fServerPath.setText(provider.getPath(PlatformProvider.PATH_SERVER_ROOT_DEFAULT));
				}
			}
			
			if (app.getID().equals(PostgresApplication.IU_POSTGRES)) {
				fDBPath.setEnabled(true);
				bDB.setEnabled(true);
				// Default Path
				if (fDBPath.getText().length() == 0)	{
					fDBPath.setText(provider.getPath(PlatformProvider.PATH_PGSQL_ROOT_DEFAULT));
				}
			}									
		}			
	}


}
