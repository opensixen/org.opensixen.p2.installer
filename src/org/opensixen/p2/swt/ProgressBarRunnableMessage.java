/**
 * 
 */
package org.opensixen.p2.swt;

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
		return "";
	
		
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		dialog.fireChange();
	}
	
	
}