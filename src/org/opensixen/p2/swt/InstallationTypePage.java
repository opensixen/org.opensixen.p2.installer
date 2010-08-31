/**
 * 
 */
package org.opensixen.p2.swt;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.opensixen.p2.common.ProductDescription;

/**
 * @author harlock
 *
 */
public class InstallationTypePage extends WizardPage implements SelectionListener, ChangeListener {

	private Logger log = Logger.getLogger(getClass());
	
	private Button liteBtn;
	private Button clientBtn;
	private Button serverBtn;
	private Button optionLite;
	private Button optionStd;
	private Button fullBtn;
	private InstallerWizard wizard;

	/**
	 * @param pageName
	 */
	protected InstallationTypePage(InstallerWizard wizard) {
		super(Messages.INSTALL_TYPE);
		this.wizard = wizard;
		setDescription(Messages.INSTALL_TYPE_DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		
		optionLite = new Button(container, SWT.CHECK);
		optionLite.setText(Messages.LITE_VERSION);
		optionLite.addSelectionListener(this);
		
		
		// Por defecto, el instalador ofrecera inatalar la version Lite
		optionLite.setSelection(true);
		
		Group liteGroup = new Group(container, SWT.NONE);
		liteGroup.setLayout(new GridLayout(2, true));
		liteGroup.setText(ProductDescription.LABEL_LITE);
		liteGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		liteBtn = new Button(liteGroup, SWT.RADIO);
		liteBtn.setText(ProductDescription.LABEL_LITE);
		liteBtn.addSelectionListener(this);
		
		liteGroup.pack();
		
		optionStd = new Button(container, SWT.CHECK);
		optionStd.setText(Messages.STD_VERSION);
		optionStd.addSelectionListener(this);
		
		Group stdGroup = new Group(container, SWT.NONE);
		stdGroup.setLayout(new GridLayout(2, true));
		stdGroup.setText(Messages.OPENSIXEN);
		stdGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		clientBtn = new Button(stdGroup, SWT.RADIO);
		clientBtn.setText(ProductDescription.LABEL_CLIENT);
		clientBtn.addSelectionListener(this);
		
		
		serverBtn = new Button(stdGroup, SWT.RADIO);
		serverBtn.setText(ProductDescription.LABEL_SERVER);
		serverBtn.addSelectionListener(this);
		
		fullBtn = new Button(stdGroup, SWT.RADIO);
		fullBtn.setText(ProductDescription.LABEL_FULL);
		fullBtn.addSelectionListener(this);
			
		stdGroup.pack();
		
		updateButtons();
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);


	}
	
	private void updateButtons()	{
		if (optionLite.getSelection())	{
			optionStd.setSelection(false);
			clientBtn.setSelection(false);
			clientBtn.setEnabled(false);
			serverBtn.setSelection(false);
			serverBtn.setEnabled(false);
			fullBtn.setSelection(false);
			fullBtn.setEnabled(false);
			liteBtn.setSelection(true);
			liteBtn.setEnabled(true);
			
			log.trace(Messages.LITE_SELECTED);
			
		}
		else  {
			optionLite.setSelection(false);
			clientBtn.setSelection(true);
			clientBtn.setEnabled(true);
			serverBtn.setSelection(false);
			serverBtn.setEnabled(true);
			fullBtn.setSelection(false);
			fullBtn.setEnabled(true);
			
			liteBtn.setSelection(false);
			liteBtn.setEnabled(false);
			
			log.trace(Messages.STD_SELECTED);
		}
		
	}
	
	public String getInstallType()	{
		if (optionStd.getSelection())	{
			if (fullBtn.getSelection())	{
				return ProductDescription.TYPE_FULL;
			}
			else if (clientBtn.getSelection())	{
				return ProductDescription.TYPE_CLIENT;
			}
			else if (serverBtn.getSelection()){
				return ProductDescription.TYPE_SERVER;
			}
		}
		
		return ProductDescription.TYPE_LITE;		
	}
	
	public String getInstallTypeLabel()	{
		if (optionStd.getSelection())	{
			if (fullBtn.getSelection())	{
				return ProductDescription.LABEL_FULL;
			}
			else if (clientBtn.getSelection())	{
				return ProductDescription.LABEL_CLIENT;
			}
			else if (serverBtn.getSelection()){
				return ProductDescription.LABEL_SERVER;
			}
		}
		
		return ProductDescription.LABEL_LITE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(optionLite))	{
			optionStd.setSelection(false);
			updateButtons();
		}
		else if (e.getSource().equals(optionStd))	{
			optionLite.setSelection(false);
			updateButtons();
		}				
		
		// Call changeListeners
		wizard.fireChange(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.opensixen.p2.swt.ChangeListener#changePerformed()
	 */
	@Override
	public void changePerformed() {
		// TODO Auto-generated method stub
		
	}	
}

