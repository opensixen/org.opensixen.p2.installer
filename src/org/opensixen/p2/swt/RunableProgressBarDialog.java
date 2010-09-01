/**
 * 
 */
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
import org.opensixen.p2.common.ProductDescription;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public abstract class RunableProgressBarDialog extends TitleAreaDialog {

	private Logger log = Logger.getLogger(getClass());
	
	private Label messages;
	private ProgressBar bar;

	private boolean finished = true;

	private Composite container;

	private ProgressBarRunnable worker;

	private Display display;

	private InstallerWizard wizard;

	private boolean workOk;

	/**
	 * @param parent
	 */
	public RunableProgressBarDialog(Shell parent, InstallerWizard wizard) {
		super(parent);
		this.display = parent.getDisplay();
		this.wizard = wizard;
		
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
				    		wizard.fireChange(this);
				    	}
				    	else {
				    		wizard.fireErrorExit();
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
