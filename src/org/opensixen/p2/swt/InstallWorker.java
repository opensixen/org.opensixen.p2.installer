/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.opensixen.p2.common.Installer;
import org.opensixen.p2.common.ProductDescription;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class InstallWorker implements ProgressBarRunnable{

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
		install();
		dialog.finishWork();
	}
	
	/**
	 * Ejecuta la instalacion
	 * @return
	 */
	private  boolean install()	{				
		boolean ret = false;
		if (installType.equals(ProductDescription.TYPE_LITE))	{
			messages.setText("Instalando Opensixen Lite");
			bar.setSelection(25);
			ret = installLite();
			bar.setSelection(100);
			if (ret)	{
				messages.setText("Instalacion completada con exito.");
			}
			else {
				messages.setText("Fallo al instalar Opensixen Lite.");
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_CLIENT))	{
			messages.setText("Instalando cliente Opensixen");
			bar.setSelection(25);
			ret = installLite();
			bar.setSelection(100);
			if (ret)	{
				messages.setText("Instalacion completada con exito.");
			}
			else {
				messages.setText("Fallo al instalar el cliente.");
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_SERVER))	{
			messages.setText("Instalando servidor Opensixen");
			bar.setSelection(25);
			if (installServer())	{
				bar.setSelection(50);
				messages.setText("Configurando el servidor Opensixen");
				if (configureServer())	{
					bar.setSelection(75);
					messages.setText("Instalando Server Manager");
					if (installServerManager())	{
						ret = true;
					}
					else {
						messages.setText("Fallo al instalar el Server Manager.");
					}
				}
				else {
					messages.setText("Fallo al configurar el servidor.");
				}
			}
			else {
				messages.setText("Fallo al instalar el servidor.");
			}
			bar.setSelection(100);
			if (ret)	{
				messages.setText("Instalacion completada con exito.");
			}
			return ret;
		}
		else if (installType.equals(ProductDescription.TYPE_FULL))	{
			
			messages.setText("Instalando servidor Opensixen");
			bar.setSelection(20);
			if (installServer())	{
				bar.setSelection(40);
				messages.setText("Configurando el servidor Opensixen");
				if (configureServer())	{
					bar.setSelection(60);
					messages.setText("Instalando Server Manager");
					if (installServerManager())	{
						bar.setSelection(80);
						messages.setText("Instalando cliente Opensixen");
						if (installClient())	{
							ret = true;							
						}
						else {
							messages.setText("Fallo al instalar el cliente Opensixen.");
						}
					}
					else {
						messages.setText("Fallo al instalar el Server Manager.");
					}
				}
				else {
					messages.setText("Fallo al configurar el servidor.");
				}
			}
			else {
				messages.setText("Fallo al instalar el servidor.");
			}
			bar.setSelection(100);
			if (ret)	{
				messages.setText("Instalacion completada con exito.");
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