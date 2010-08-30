/**
 * 
 */
package org.opensixen.p2.swt;

class ProgressBarRunnableBarStatus	{
	private int selection;
	private RunableProgressBarDialog dialog;
	
	

	public ProgressBarRunnableBarStatus(RunableProgressBarDialog dialog) {
		super();
		this.dialog = dialog;
	}

	/**
	 * @return the selection
	 */
	public int getSelection() {
		return selection;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setSelection(int selection) {
		this.selection = selection;
		dialog.fireChange();
	}		
}