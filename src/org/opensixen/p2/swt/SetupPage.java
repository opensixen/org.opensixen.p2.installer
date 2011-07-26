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

import java.util.Properties;

import org.apache.log4j.Logger;
import org.compiere.util.Ini;
import org.compiere.util.SecureEngine;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opensixen.os.PlatformDetails;
import org.opensixen.os.PlatformProvider;
import org.opensixen.os.ProviderFactory;
import org.opensixen.p2.applications.ClientApplication;
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.applications.InstallableApplication;
import org.opensixen.p2.applications.LiteApplication;
import org.opensixen.p2.applications.PostgresApplication;
import org.opensixen.p2.applications.ServerApplication;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class SetupPage extends WizardPage implements InstallerWizardPage, SelectionListener, ModifyListener {

	private Logger log = Logger.getLogger(getClass());
	
	private PlatformProvider provider;
	
	private Combo fDbType;
	private Text fDbHost;
	private Text fDBuser;
	private Text fDBPasswd;
	private Text fDbPort;
	private Text fDbName;
	
	private Text fDBSystemuser;
	private Text fDBSystemPasswd;
	
	private boolean init = false;
	
	// Hardcode. From Database.DB_NAMES
	private String DB_POSTGRES="PostgreSQL";
	private String PORT_POSTGRES ="5432";			
	private String DB_ORACLE = "Oracle";
	private String PORT_ORACLE = "1521";
	
	private String[] dbNames = {DB_POSTGRES, DB_ORACLE}; //$NON-NLS-1$ //$NON-NLS-2$
	private Text fASHost;
	private Text fASPort;

	private Properties config;
	
	

	
	/**
	 * @param pageName
	 */
	protected SetupPage() {
		super(Messages.DATABASE_SETUP);
		setDescription(Messages.DATABASE_SETUP_DESCRIPTION);
		provider = ProviderFactory.getProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		
		Group setup = new Group(container, SWT.NONE);
		setup.setLayout(new GridLayout(2, false));
		setup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setup.setText(Messages.CONFIG);

		Label l = new Label(setup, SWT.NONE);
		l.setText(Messages.AS_HOST);
		fASHost = new Text(setup, SWT.BORDER);
		fASHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fASHost.addModifyListener(this);
		
		// By default, the hostname is the same as the localhost
		//fASHost.setText(PlatformDetails.getHostname());
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.AS_PORT);
		fASPort = new Text(setup, SWT.BORDER);
		fASPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));				

		fASPort.setText("8080"); //$NON-NLS-1$
		// Not configurable
		//fASPort.addModifyListener(this);
		fASPort.setEnabled(false);
		
				
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DATABASE_TYPE);
		fDbType = new Combo(setup, SWT.DROP_DOWN);
		fDbType.setItems(dbNames);
		fDbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbType.setText(DB_POSTGRES);
		fDbType.addSelectionListener(this);
		
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_PORT);
		fDbPort = new Text(setup, SWT.BORDER);
		fDbPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbPort.setText(PORT_POSTGRES);
		fDbPort.addModifyListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_HOST);
		fDbHost = new Text(setup, SWT.BORDER);
		fDbHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbHost.addModifyListener(this);
		
		// By default, the hostname is the same as the localhost
		//fDbHost.setText(PlatformDetails.getHostname());
		/*
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_USER_SYSTEM);
		
		fDBSystemuser = new Text(setup, SWT.BORDER);
		fDBSystemuser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBSystemuser.addModifyListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_PWD_SYSTEM);
		
		fDBSystemPasswd = new Text(setup, SWT.PASSWORD | SWT.BORDER);
		fDBSystemPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));	
		fDBSystemPasswd.addModifyListener(this);
		*/
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_NAME);
		fDbName = new Text(setup, SWT.BORDER);
		fDbName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbName.addModifyListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_USER);
		
		fDBuser = new Text(setup, SWT.BORDER);
		fDBuser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		// Not configurable
		fDBuser.setText("adempiere"); //$NON-NLS-1$
		//fDBuser.addModifyListener(this);
		fDBuser.setEnabled(false);
		
		
		
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_PASSWORD);
		
		fDBPasswd = new Text(setup, SWT.PASSWORD | SWT.BORDER);
		fDBPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBPasswd.addModifyListener(this);
				
		//enableTextFields();
		setPageComplete(false);
		setControl(container);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {	
						
		// If change combo, change default port
		if (e.getSource().equals(fDbType))		{		
			if (fDbType.getText().equals(DB_POSTGRES))	{ //$NON-NLS-1$
				fDbPort.setText(PORT_POSTGRES); //$NON-NLS-1$
			}
			else {
				fDbPort.setText(PORT_ORACLE); //$NON-NLS-1$
			}
		}				
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		getContainer().updateButtons();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.SetupContainer#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		
		Text[] fields = {fDbHost,fDBuser,fDBPasswd,fDbPort,fDbName};
		for (Text field: fields)	{
			if (field.getText().length() == 0)	{
				return false;
			}
		}
		return true;
	
	}
	

	
	

	

	

	public void bind()	{
	// Load current configuration
	Properties conf = new Properties();
	conf.put("name", fASHost.getText()); //$NON-NLS-1$
	conf.put("AppsHost", fASHost.getText()); //$NON-NLS-1$
	conf.put("AppsPort", fASPort.getText()); //$NON-NLS-1$
	conf.put("type", fDbType.getText()); //$NON-NLS-1$
	conf.put("DBhost",fDbHost.getText()); //$NON-NLS-1$
	conf.put("DBport", fDbPort.getText()); //$NON-NLS-1$
	conf.put("DBname", fDbName.getText()); //$NON-NLS-1$
	
	conf.put("BQ", "false"); //$NON-NLS-1$
	conf.put("FW", "false"); //$NON-NLS-1$
	conf.put("FWhost", ""); //$NON-NLS-1$ //$NON-NLS-2$
	conf.put("FWport", "");	 //$NON-NLS-1$ //$NON-NLS-2$
	
	conf.put("UID", fDBuser.getText()); //$NON-NLS-1$
	conf.put("PWD", fDBPasswd.getText()); //$NON-NLS-1$
	
	//conf.put("SystemUID", fDBSystemuser.getText()); //$NON-NLS-1$
	//conf.put("SystemPWD", fDBSystemPasswd.getText()); //$NON-NLS-1$
	
	conf.put("SystemUID", "postgres"); //$NON-NLS-1$
	conf.put("SystemPWD", "postgres"); //$NON-NLS-1$
	
	this.config = conf;		
	}
	
	private boolean createProperties(String path)	{
		// Setup as server and setup path as adempiere home
		Ini.setClient(false);
		Ini.setAdempiereHome(path);
		Ini.loadProperties(true);		
		Ini.setProperty (Ini.P_CONNECTION, getConnectionString());
		
		Ini.saveProperties(false);
		return true;
		
	}
	
	/**
	 *  String representation.
	 *  Used also for Instanciation
	 *  @return string representation
	 *	@see #setAttributes(String) setAttributes
	 */
	public String getConnectionString ()
	{				
		StringBuffer sb = new StringBuffer ("CConnection["); //$NON-NLS-1$
		sb.append ("name=").append (config.getProperty("name")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",AppsHost=").append (config.getProperty("AppsHost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",AppsPort=").append (config.getProperty("AppsPort")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",type=").append (config.getProperty("type")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBhost=").append (config.getProperty("DBhost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBport=").append (config.getProperty("DBport")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBname=").append (config.getProperty("DBname")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",BQ=").append (config.getProperty("BQ")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FW=").append (config.getProperty("FW")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FWhost=").append (config.getProperty("FWhost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FWport=").append (config.getProperty("FWport")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",UID=").append (config.getProperty("UID")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",PWD=").append (config.getProperty("PWD")) //$NON-NLS-1$ //$NON-NLS-2$
		  ;		//	the format is read by setAttributes
		sb.append ("]"); //$NON-NLS-1$
		return sb.toString ();
	}	//  toStringLong

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		bind();
		for (InstallableApplication app:InstallJob.getInstance().getInstallableApplications())	{
			if (app.getIu().equals(ServerApplication.IU_SERVER))	{
				ServerApplication server = (ServerApplication) app;
				boolean ok = createProperties(server.getRealPath());
				app.setConfigOk(ok);
				return true;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		InstallJob job = InstallJob.getInstance();
		PlatformDetails details = provider.getPlatformDetails();
		if (fASHost.getText().length() == 0)	{
			fASHost.setText(details.getHostname());
		}
		
		if (fDbHost.getText().length() == 0)	{
			fDbHost.setText(details.getHostname());
		}
		
	}

	

}
