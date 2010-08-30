/**
 * 
 */
package org.opensixen.p2.swt;

import java.util.Properties;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class Prueba {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties conf = new Properties();
		conf.put("name", "ASHost");
		conf.put("AppsHost", "ASHost");
		conf.put("AppsPort", "ASPort");
		conf.put("type", "DbType");
		conf.put("DBhost", "DbHost");
		conf.put("DBport", "DbPort");
		conf.put("DBname", "DbName");
		
		conf.put("BQ", false);
		conf.put("FW", false);
		conf.put("FWhost", "");
		conf.put("FWport", "");	
		
		conf.put("UID", "DBuser");
		conf.put("PWD", "DBPasswd");
		conf.put("SystemUID", "DBSystemuser");
		conf.put("SystemPWD", "DBSystemPasswd");

	}

}
