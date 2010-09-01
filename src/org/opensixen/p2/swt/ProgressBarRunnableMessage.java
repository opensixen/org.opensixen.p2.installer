/**
 * 
 */
package org.opensixen.p2.swt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

class ProgressBarRunnableMessage	{
	
	private String text;
	private RunableProgressBarDialog dialog;		

	public ProgressBarRunnableMessage(RunableProgressBarDialog dialog) {
		super();
		this.dialog = dialog;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		if (text != null)
			return text;
		return ""; //$NON-NLS-1$
	
		
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		dialog.fireChange();
	}

	
}