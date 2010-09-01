/**
 * 
 */
package org.opensixen.p2.swt;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.opensixen.p2.common.Installer;
import org.opensixen.p2.common.ProductDescription;

/**
 * Very Ugly class..
 * 
 * Must be rewrited.
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class InstallWorker implements ProgressBarRunnable{

	private Logger log = Logger.getLogger(getClass());
	
	public ProgressBarRunnableMessage messages;
	public ProgressBarRunnableBarStatus bar;
	
	private RunableProgressBarDialog dialog;
	
	private String installType;
	private String clientPath;
	private String serverPath;

	public InstallWorker(String installType, String clientPath, String serverPath , RunableProgressBarDialog dialog)	{
		this.installType = installType;
		this.clientPath = clientPath;
		this.serverPath = serverPath;
		
		this.dialog = dialog;
		 messages = new ProgressBarRunnableMessage(dialog);
		 bar = new ProgressBarRunnableBarStatus(dialog);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		boolean ok = true;
		try {
			install();
		}
		catch (WorkerException e)	{
			log.error(e.getMessage(), e);
			bar.setSelection(100);
			messages.setText(e.getMessage());
			ok = false;
		}
		dialog.finishWork(ok);
	}
	
	/**
	 * Ejecuta la instalacion
	 * @return
	 */
	private  boolean install() throws WorkerException	{				
		boolean ret = false;
		if (installType.equals(ProductDescription.TYPE_LITE))	{
			messages.setText(Messages.INSTALLING_OPENSIXEN_LITE);
			bar.setSelection(25);
			ret = installLite();
			bar.setSelection(100);
			if (ret)	{
				messages.setText(Messages.INSTALL_EXIT_OK);
			}
			else {
				throw new WorkerException(Messages.INSTALL_LITE_EXIT_FAIL);
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_CLIENT))	{
			messages.setText(Messages.INSTALLING_OPENSIXEN_CLIENT);
			bar.setSelection(25);
			ret = installClient();
			bar.setSelection(100);
			if (ret)	{
				messages.setText(Messages.INSTALL_EXIT_OK);
			}
			else {
				throw new WorkerException(Messages.INSTALL_CLIENT_EXIT_FAIL);
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_SERVER))	{
			messages.setText(Messages.INSTALLING_OPENSIXEN_SERVER);
			bar.setSelection(25);
			if (installServer())	{
				bar.setSelection(50);
				messages.setText(Messages.CONFIGURING_OPENSIXEN_SERVER);
				if (configureServer())	{
					bar.setSelection(75);
					messages.setText(Messages.INSTALLING_OPENSIXEN_MANAGER);
					if (installServerManager())	{
						ret = true;
					}
					else {
						throw new WorkerException(Messages.INSTALL_MANAGER_EXIT_FAIL);
					}
				}
				else {
					throw new WorkerException(Messages.CONFIG_SERVER_EXIT_FAIL);
				}
			}
			else {
				throw new WorkerException(Messages.INSTALL_SERVER_EXIT_FAIL);
			}
			bar.setSelection(100);
			if (ret)	{
				messages.setText(Messages.INSTALL_SERVER_EXIT_OK);
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_FULL))	{
			
			messages.setText(Messages.INSTALLING_OPENSIXEN_SERVER);
			bar.setSelection(20);
			if (installServer())	{
				bar.setSelection(40);
				messages.setText(Messages.CONFIGURING_OPENSIXEN_SERVER);
				if (configureServer())	{
					bar.setSelection(60);
					messages.setText(Messages.INSTALLING_OPENSIXEN_MANAGER);
					if (installServerManager())	{
						bar.setSelection(80);
						messages.setText(Messages.INSTALLING_OPENSIXEN_CLIENT);
						if (installClient())	{
							ret = true;							
						}
						else {
							throw new WorkerException(Messages.INSTALL_CLIENT_EXIT_FAIL);
						}
					}
					else {
						throw new WorkerException(Messages.INSTALL_MANAGER_EXIT_FAIL);
					}
				}
				else {
					throw new WorkerException(Messages.CONFIGURING_SERVER_EXIT_FAIL);
				}
			}
			else {
				throw new WorkerException(Messages.INSTALL_SERVER_EXIT_FAIL);
			}
			bar.setSelection(100);
			if (ret)	{
				messages.setText(Messages.INSTALL_FULL_EXIT_OK);
			}
			return ret;

		}
		return false;
	}
	
	private boolean installClient()	{		
		Installer installer = new Installer();
		return installer.install(ProductDescription.TYPE_CLIENT, clientPath);
	}
	
	private boolean installServer()	{
		Installer installer = new Installer();
		return installer.install(ProductDescription.TYPE_SERVER, serverPath);
	}
	
	private boolean installLite()	{
		Installer installer = new Installer();
		return installer.install(ProductDescription.TYPE_LITE, clientPath);
	}			
	
	private boolean installServerManager()	{
		/*
		Installer installer = new Installer();
		return installer.install(ProductDescription.TYPE_MANAGER, serverPath);
		*/
		return true;
	}
	
	private boolean configureServer()	{
		return true;
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

	

}