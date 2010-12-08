/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.Properties;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class SetupPage extends WizardPage implements InstallerWizardPage, SelectionListener, ModifyListener {

	private Logger log = Logger.getLogger(getClass());
	
	private Button dbInstall;
	private Button dbCreateUser;
	private Button dbImportData;
	private Button dbNothing;
	
	public final String TYPE_INSTALL = "install"; //$NON-NLS-1$
	public final String TYPE_USER = "user"; //$NON-NLS-1$
	public final String TYPE_DATA = "data"; //$NON-NLS-1$
	public final String TYPE_NOTHING = "nothing"; //$NON-NLS-1$
	
	private Text[] systemFields;
	private Text[] userFields;
	private Text[] allFields;
	
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
	private String[] dbNames = {"PostgreSQL", "Oracle"}; //$NON-NLS-1$ //$NON-NLS-2$
	private String homePath;
	private Text fASHost;
	private Text fASPort;

	private Properties config;
	private String configType;
	
	

	
	/**
	 * @param pageName
	 */
	protected SetupPage() {
		super(Messages.DATABASE_SETUP);
		setDescription(Messages.DATABASE_SETUP_DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		Group operation = new Group(container, SWT.NONE);
		operation.setLayout(new GridLayout());
		operation.setText(Messages.DATABASE_OPTIONS);
		operation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		dbInstall = new Button(operation, SWT.RADIO);
		dbInstall.setText(Messages.NO_DATABASE);
		dbInstall.addSelectionListener(this);
		
		dbCreateUser = new Button(operation, SWT.RADIO);
		dbCreateUser.setText(Messages.DATABASE_WITHOUT_SETUP);
		dbCreateUser.addSelectionListener(this);
		
		dbImportData = new Button(operation, SWT.RADIO);
		dbImportData.setText(Messages.DATABASE_WITHOUT_DATA);
		dbImportData.addSelectionListener(this);
		dbCreateUser.setSelection(true); // Default option
		
		dbNothing = new Button(operation, SWT.RADIO);
		dbNothing.setText(Messages.DATABASE_WITH_DATA);		
		dbNothing.addSelectionListener(this);

		Group saSetup = new Group(container, SWT.NONE);
		saSetup.setLayout(new GridLayout(2, false));
		saSetup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		saSetup.setText(Messages.AS_CONFIG);		
		
		Label l = new Label(saSetup, SWT.NONE);
		l.setText(Messages.AS_HOST);
		fASHost = new Text(saSetup, SWT.BORDER);
		fASHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fASHost.addModifyListener(this);
		
		// By default, the hostname is the same as the localhost
		//fASHost.setText(PlatformDetails.getHostname());
		
		l = new Label(saSetup, SWT.NONE);
		l.setText(Messages.AS_PORT);
		fASPort = new Text(saSetup, SWT.BORDER);
		fASPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fASPort.addModifyListener(this);
		
		// Not configurable
		fASPort.setText("8080"); //$NON-NLS-1$
		fASPort.setEnabled(false);
		
		Group setup = new Group(container, SWT.NONE);
		setup.setLayout(new GridLayout(2, false));
		setup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setup.setText(Messages.CONFIG);

		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DATABASE_TYPE);
		fDbType = new Combo(setup, SWT.DROP_DOWN);
		fDbType.setItems(dbNames);
		fDbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbType.addSelectionListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_PORT);
		fDbPort = new Text(setup, SWT.BORDER);
		fDbPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbPort.addModifyListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_HOST);
		fDbHost = new Text(setup, SWT.BORDER);
		fDbHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbHost.addModifyListener(this);
		
		// By default, the hostname is the same as the localhost
		//fDbHost.setText(PlatformDetails.getHostname());
		
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
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_NAME);
		fDbName = new Text(setup, SWT.BORDER);
		fDbName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDbName.addModifyListener(this);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_USER);
		
		fDBuser = new Text(setup, SWT.BORDER);
		fDBuser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBuser.addModifyListener(this);
		
		// Not configurable
		fDBuser.setText("adempiere"); //$NON-NLS-1$
		fDBuser.setEnabled(false);
		
		l = new Label(setup, SWT.NONE);
		l.setText(Messages.DB_PASSWORD);
		
		fDBPasswd = new Text(setup, SWT.PASSWORD | SWT.BORDER);
		fDBPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fDBPasswd.addModifyListener(this);
		
		// Setup fields group
		setupFieldsGroup();
		enableTextFields();
		setPageComplete(false);
		setControl(container);
	}

	
	/**
	 * Create Text[] with fields required
	 * For disabled and check feature
	 */
	private void setupFieldsGroup()	{
		Text[] n = {fDbHost, fDbName, fDBuser, fDBPasswd, fDbPort};
		userFields = n;
		
		Text[] c = {fDbHost, fDbName, fDBuser, fDBPasswd, fDbPort, fDBSystemuser, fDBSystemPasswd};
		systemFields = c;								

		Text[] all ={fASHost, fASPort,fDbHost, fDbName, fDBuser, fDBPasswd, fDbPort, fDBSystemuser, fDBSystemPasswd};
		allFields = c;
		init=true;
	}
	
	/**
	 * Update enabled status for text fields
	 */
	private void enableTextFields() {
		if (dbNothing.getSelection())	{			
			enable(userFields);
			this.configType = TYPE_NOTHING;
		}
		
		else if (dbImportData.getSelection())	{
			enable(userFields);
			this.configType = TYPE_DATA;
		}
		
		else if (dbCreateUser.getSelection())	{
			enable(systemFields);
			this.configType = TYPE_USER;
		}
		
		else if (dbInstall.getSelection())	{
			enable(systemFields);
			this.configType = TYPE_INSTALL;
		}
		// Hardcoded
		fDBuser.setEnabled(false);
		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {	
		
		// change text fields configuration
		if (e.getSource().getClass().equals(Button.class))	{
			enableTextFields();
			return;
		}	
		
		// If change combo, change default port
		else if (e.getSource().equals(fDbType))		{
			
			if (fDbType.getText().equals("PostgreSQL"))	{ //$NON-NLS-1$
				fDbPort.setText("5432"); //$NON-NLS-1$
			}
			else {
				fDbPort.setText("1521"); //$NON-NLS-1$
			}
		}				
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		if (!init)	{
			return;
		}
		
		if (dataOK())	{
			//wizard.fireChange(this);			
			bind();
			Control control = (Control) e.getSource();
			control.setFocus();
		}
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
		if (!init)	{
			return false;
		}
		
		return dataOK();
	}
	

	
	public boolean dataOK() {

		if (dbNothing.getSelection())	{			
			return check(userFields);
		}
		
		else if (dbImportData.getSelection())	{
			return check(userFields);
		}
		
		else if (dbCreateUser.getSelection())	{
			return check(systemFields);
		}
		
		else if (dbInstall.getSelection())	{
			return check(systemFields);
		}
		
		return false;
	}
	
	

	private boolean check(Text[] fields)		{
		for (Text field:fields)	{
			if (field.getText().equals(null) ||field.getText().length() == 0)	{
				return false;
			}
		}
		return true;
	}
	
	private void enable(Text[] fields)		{
		// Disable all fields		
		for (Text field:systemFields)	{
			field.setEnabled(false);
		}
				
		for (Text field:fields)	{
			field.setEnabled(true);
		}
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
	
	conf.put("BQ", false); //$NON-NLS-1$
	conf.put("FW", false); //$NON-NLS-1$
	conf.put("FWhost", ""); //$NON-NLS-1$ //$NON-NLS-2$
	conf.put("FWport", "");	 //$NON-NLS-1$ //$NON-NLS-2$
	
	conf.put("UID", fDBuser.getText()); //$NON-NLS-1$
	conf.put("PWD", fDBPasswd.getText()); //$NON-NLS-1$
	conf.put("SystemUID", fDBSystemuser.getText()); //$NON-NLS-1$
	conf.put("SystemPWD", fDBSystemPasswd.getText()); //$NON-NLS-1$
	
	this.config = conf;
		
	}
	
	public Properties getConfiguration()	{
		if (!init)	{
			return null;
		}
		return config;
	}
	
	public String getConfigType()	{
		return configType;
	}
	
	/**
	 *  String representation.
	 *  Used also for Instanciation
	 *  @return string representation
	 *	@see #setAttributes(String) setAttributes
	 */
	public String toStringLong ()
	{
		Properties prop = getConfiguration();
		
		StringBuffer sb = new StringBuffer ("CConnection["); //$NON-NLS-1$
		sb.append ("name=").append (prop.getProperty("name")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",AppsHost=").append (prop.getProperty("AppsHost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",AppsPort=").append (prop.getProperty("AppsPort")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",type=").append (prop.getProperty("type")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBhost=").append (prop.getProperty("DBhost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBport=").append (prop.getProperty("DBport")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",DBname=").append (prop.getProperty("DBname")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",BQ=").append (prop.getProperty("BQ")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FW=").append (prop.getProperty("FW")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FWhost=").append (prop.getProperty("FWhost")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",FWport=").append (prop.getProperty("FWport")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",UID=").append (prop.getProperty("UID")) //$NON-NLS-1$ //$NON-NLS-2$
		  .append (",PWD=").append (prop.getProperty("PWD")) //$NON-NLS-1$ //$NON-NLS-2$
		  ;		//	the format is read by setAttributes
		sb.append ("]"); //$NON-NLS-1$
		return sb.toString ();
	}	//  toStringLong

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#storeDialogSettings()
	 */
	@Override
	public boolean storeDialogSettings() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.InstallerWizardPage#refresh()
	 */
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	

}
