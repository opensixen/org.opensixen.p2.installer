/**
 * 
 */
package org.opensixen.p2.swt;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opensixen.p2.common.ProductDescription;

/**
 * @author harlock
 * 
 */
public class InstallLocationPage extends WizardPage implements ChangeListener,
		SelectionListener, ModifyListener {
	
	private Logger log = Logger.getLogger(getClass());
	
	private Text fClientPath;
	private Button bClient;
	private InstallerWizard wizard;
	private Text fServerPath;
	private Button bServer;

	public InstallLocationPage(InstallerWizard wizard) {
		super("Ubicacion de la instalacion");
		this.wizard = wizard;
		setDescription("Indique donde quiere instalar los componentes seleccionados");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		Label l = new Label(container, SWT.NONE);
		l.setText("Ruta del cliente");
		Composite c = new Composite(container, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fClientPath = new Text(c, SWT.BORDER);
		fClientPath
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fClientPath.addModifyListener(this);
		bClient = new Button(c, SWT.PUSH);
		bClient.setText("Buscar");
		bClient.addSelectionListener(this);

		l = new Label(container, SWT.NONE);
		l.setText("Ruta del servidor");
		c = new Composite(container, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fServerPath = new Text(c, SWT.BORDER);
		fServerPath
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		fServerPath.addModifyListener(this);
		bServer = new Button(c, SWT.PUSH);
		bServer.setText("Buscar");
		bServer.addSelectionListener(this);		
		
		update();

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public void update() {
		String installType = wizard.getInstallationTypePage().getInstallType();
		
		// If lite or client install, need only client path
		if (installType.equals(ProductDescription.TYPE_LITE) || installType.equals(ProductDescription.TYPE_CLIENT)) {
			fClientPath.setEnabled(true);
			bClient.setEnabled(true);
			fServerPath.setEnabled(false);
			bServer.setEnabled(false);
		}

		// if server only  need server path.
		else if (installType.equals(ProductDescription.TYPE_SERVER)) {
			fClientPath.setEnabled(false);
			bClient.setEnabled(false);
			fServerPath.setEnabled(true);
			bServer.setEnabled(true);
		}

		// if full, need booth
		else if (installType.equals(ProductDescription.TYPE_FULL)) {
			fClientPath.setEnabled(true);
			bClient.setEnabled(true);
			fServerPath.setEnabled(true);
			bServer.setEnabled(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		String installType = wizard.getInstallationTypePage().getInstallType();
		
		//StringBuffer infoBuff = new StringBuffer();
		
		// Si no es tipo servidor, pedimos la ruta del cliente
		if (installType.equals(ProductDescription.TYPE_LITE)
				|| installType.equals(ProductDescription.TYPE_FULL)
				|| installType.equals(ProductDescription.TYPE_CLIENT)) {
			
			if (fClientPath.getText() == null || fClientPath.getText().length() == 0) {
				log.trace("Client path null.");
				return false;				
			}

			/*
			File f = new File(getClientInstallPath());		
			if (!f.exists()) {
				infoBuff.append("No existe el directorio '"+fClientPath.getText()+"'. Se creara durante la instalación.");
			}
			else infoBuff.append("El directorio de cliente es valido.");
			infoBuff.append("\n");
			*/
		}

		// Si es servidor o completa, pedimos tambien la ruta al servidor.
		if (installType.equals(ProductDescription.TYPE_SERVER)) {
			if (fServerPath.getText() == null
					|| fServerPath.getText().length() == 0) {
				return false;
			}
			/*
			File f = new File(getServerInstallPath());
			if (!f.exists()) {
				infoBuff.append("No existe el directorio '"+fClientPath.getText()+"'. Se creara durante la instalación.");
			}
			else infoBuff.append("El directorio de cliente es valido.");
			infoBuff.append("\n");
			*/
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.p2.swt.ChangeListener#changePerformed()
	 */
	@Override
	public void changePerformed() {
		update();
	}

	public String getClientInstallPath() {
		return fClientPath.getText();
	}

	public String getServerInstallPath() {
		return fServerPath.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(bClient)) {
			DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
			dd.setText("Open");

			String dir = dd.open();
			if (dir != null) {
				fClientPath.setText(dir);
			}

		}

		else if (e.getSource().equals(bServer)) {
			DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
			dd.setText("Open");

			String dir = dd.open();
			if (dir != null) {
				fServerPath.setText(dir);
			}
		}

		wizard.fireChange(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
	 * .ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		if (e.getSource().equals(fClientPath)
				|| e.getSource().equals(fServerPath)) {
			wizard.fireChange(this);
		}
	}

}
