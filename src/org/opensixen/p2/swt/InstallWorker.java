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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.opensixen.p2.applications.InstallJob;
import org.opensixen.p2.common.Installer;

/**
 * Very Ugly class..
 * 
 * Must be rewrited.
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 * @deprecated
 */
public class InstallWorker implements ProgressBarRunnable{

	private Logger log = Logger.getLogger(getClass());
	
	public ProgressBarRunnableMessage messages;
	public ProgressBarRunnableBarStatus bar;
	
	private RunableProgressBarDialog dialog;
	
	private InstallJob job;

	public InstallWorker(InstallJob job , RunableProgressBarDialog dialog)	{
		this.job = job;
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
	/*
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
		*/
		return false;
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