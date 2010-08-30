/**
 * 
 */
package org.opensixen.p2.swt;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public interface ProgressBarRunnable extends Runnable {
	
	public ProgressBarRunnableMessage getMessage();
	public ProgressBarRunnableBarStatus getBarStatus();
	

}
