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
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.opensixen.p2.common.Installer;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *@deprecated
 */
public abstract class RunableProgressBarDialog extends TitleAreaDialog {

	private Logger log = Logger.getLogger(getClass());
	
	private Label messages;
	private ProgressBar bar;

	private boolean finished = true;

	private Composite container;

	private ProgressBarRunnable worker;

	private Display display;


	private boolean workOk;

	/**
	 * @param parent
	 */
	public RunableProgressBarDialog(Shell parent) {
		super(parent);
		this.display = parent.getDisplay();				
		setBlockOnOpen(false);		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		messages = new Label(container, SWT.NONE);
		messages.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
		bar = new ProgressBar(container, SWT.SMOOTH);
		//bar.setBounds (10, 10, 200, 32);
		bar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		
						
		return parent;
	}
				
	public abstract ProgressBarRunnable getRunnable();
	
	public void run()	{
		if (!finished)	{
			log.error(Messages.JOB_RUNNING_CANT_INITIATE);
			return;
		}
		log.info("Starting installer thread."); //$NON-NLS-1$
		
		worker = getRunnable();
		
		new Thread(worker).start();
		//getShell().getDisplay().asyncExec(worker);
		//worker.run();
	}
			
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createButtonBar(Composite parent) {
		// TODO Auto-generated method stub
		Control control =  super.createButtonBar(parent);
		// Disable cancel button
		getButton(CANCEL).setVisible(false);
		getButton(OK).setEnabled(false);
		
		return control;
	}
	
	/**
	 * Called from the thread InstallWorker
	 * outside the UI thread
	 * @param ok true if the worker exit ok
	 * 
	 * see http://www.eclipse.org/swt/faq.php#uithread
	 */
	public synchronized void finishWork(final boolean ok)	{
		log.info("Install thread end."); //$NON-NLS-1$
		finished = true;
		workOk = ok;
		
		display.syncExec(
				  new Runnable() {
				    public void run(){
				    	getButton(OK).setEnabled(true);				    					    					    	
				    	if (ok)	{
				    		//wizard.fireChange(this);
				    	}
				    	else {
				    		//wizard.fireErrorExit();
				    	}
				    }
				  });		
	}
	
	/**
	 * Called from the thread InstallWorker
	 * outside the UI thread
	 * see http://www.eclipse.org/swt/faq.php#uithread
	 */
	public synchronized void fireChange()	{		
		final String text = worker.getMessage().getText();
		display.syncExec(
				  new Runnable() {
				    public void run(){
						messages.setText(text);
				    }
				  });
		final int selection = worker.getBarStatus().getSelection();
		display.syncExec(
				  new Runnable() {
				    public void run(){
				    	bar.setSelection(selection);
				    }
				  });
				 

		
		
			
	}
	
	
	public boolean finished()	{
		return finished;
	}
	
	public boolean workOk()	{
		return workOk;
	}
	
	
	

}
