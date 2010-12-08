/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.Properties;

import org.opensixen.p2.installer.Ini;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class ConfigWorker implements ProgressBarRunnable {

	public ProgressBarRunnableMessage messages;
	public ProgressBarRunnableBarStatus bar;
	
	private ConfigProgressBarDialog dialog;
	private Properties configuration;
	
	
	public ConfigWorker(Properties configuration, ConfigProgressBarDialog dialog)	{
		this.configuration = configuration;
		this.dialog = dialog;
		
		 messages = new ProgressBarRunnableMessage(dialog);
		 bar = new ProgressBarRunnableBarStatus(dialog);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.ProgressBarRunnable#getMessage()
	 */
	@Override
	public ProgressBarRunnableMessage getMessage() {
		return messages;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.ProgressBarRunnable#getBarStatus()
	 */
	@Override
	public ProgressBarRunnableBarStatus getBarStatus() {
		return bar;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Create Properties
		messages.setText(Messages.ConfigWorker_CREATING_PROPERTIES);
		bar.setSelection(20);
		createProperties();
		bar.setSelection(100);
		
		dialog.finishWork(true);
	}
	
	private boolean createProperties()	{
		/*
		// If server is not selected, then need client conf.
		String installType = configuration.getProperty("InstallType");
		if (!ProductDescription.TYPE_SERVER.equals(installType))	{
			createProperties(configuration.getProperty("ClientPath"));
		}
		
		if (ProductDescription.TYPE_SERVER.equals(installType) || ProductDescription.TYPE_FULL.equals(installType))	{
			createProperties(configuration.getProperty("ServerPath"));
		}
		*/
		return true;
	}

	
	private boolean createProperties(String path)	{
		// Setup as server and setup path as adempiere home
		Ini.setClient(false);
		Ini.setAdempiereHome(path);
		Ini.loadProperties(true);
		Ini.setProperty (Ini.P_CONNECTION, configuration.getProperty(Ini.P_CONNECTION));
		Ini.saveProperties(false);
		return true;
		
	}
}
