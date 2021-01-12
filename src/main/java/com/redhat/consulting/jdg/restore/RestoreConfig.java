/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.restore;

/**
 * @author Meissa
 */
public class RestoreConfig {
	/**
	 *  Define the restore jso file that will be used to restore the cache. 
	 *  This must be a complete path including the filename.
	 */
	public static final String RESTORE_LOCATION_FILE=System.getProperty("jdg.restore.location.file");
	
	/**
	 * Whether to start the restore on this node
	 */
	public static final Boolean IS_ACTIVE = Boolean.parseBoolean( System.getProperty("jdg.restore.active","true" ));
	
	/**
	 * the time in seconds from now to execute the restore.
	 */
	public static final long INITIAL_DELAY = Long.parseLong( System.getProperty("jdg.restore.initial.delay", "10") );
	
	
	
	public static final String RESTORE_CACHE_NAME=System.getProperty("jdg.restore.cache.name");

}
