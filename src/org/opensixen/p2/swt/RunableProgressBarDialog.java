/**
 * 
 */
package org.opensixen.p2.swt;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

	private boolean workEnd = true;

	private Composite container;

	private ProgressBarRunnable worker;

	/**
	 * @param parent
	 */
	public RunableProgressBarDialog(Shell parent) {
		super(parent);
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
		if (!workEnd)	{
			log.error("Trabajo corriendo actualmente. no se puede iniciar otra instancia.");
			return;
		}
		log.info("Starting installer thread.");
		
		worker = getRunnable();
		
		//new Thread(worker).start();
		getShell().getDisplay().asyncExec(worker);
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
	 */
	public synchronized void finishWork()	{
		log.info("Install thread end.");
		workEnd = true;
		getButton(OK).setEnabled(true);
	}
	
	/**
	 * Called from the thread InstallWorker
	 */
	public synchronized void fireChange()	{
		messages.setText(worker.getMessage().getText());
		bar.setSelection(worker.getBarStatus().getSelection());	
	}
	
	public boolean finished()	{
		return workEnd;
	}
	

}
