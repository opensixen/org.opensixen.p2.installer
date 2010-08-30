package org.opensixen.p2.installer;


import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.opensixen.p2.swt.InstallerWizard;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private Logger log = Logger.getLogger(getClass());
	
	private Display display;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		display = PlatformUI.createDisplay();
		Shell shell = new Shell(display);
		
		//Installer installer = new Installer();
		//installer.list();
		//installer.install("/tmp/osx");
			
		// Activamos Log4j
		URL log4j = getClass().getClassLoader().getResource("log4j.properties");
		
		if (log4j != null)	{
			PropertyConfigurator.configure( log4j );
		}
		
		log.info("Starting Opensixen Installer.");

		
		InstallerWizard wizard = new InstallerWizard(shell);
		wizard.open();
		
		

		return IApplication.EXIT_OK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		/*
		 * if (!PlatformUI.isWorkbenchRunning()) return; final IWorkbench
		 * workbench = PlatformUI.getWorkbench(); final Display display =
		 * workbench.getDisplay(); display.syncExec(new Runnable() { public void
		 * run() { if (!display.isDisposed()) workbench.close(); } });
		 */
		display.dispose();
	}

}
