/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.backup;

/**
 * @author Meissa
 */
public class BackupConfig {
	
	/**
	 *  The folder wher the cache dump will be stored as json file.
	 */
	public static final String BACKUP_LOCATION_FOLDER=System.getProperty("jdg.backup.location.folder");
	
	/**
	 * The json file name that will contains the cache data
	 */
	public static final String BACKUP_FILE_NAME=System.getProperty("jdg.backup.file.name");
	
	/**
	 * time interval in days between sucesssif backup.
	 */
	public static final long TIME_INTERVAL = Long.parseLong( System.getProperty("jdg.backup.time.interval", "7") );
	
	/**
	 * the time in days to delay the first backup.
	 */
	public static final long INITIAL_DELAY = Long.parseLong( System.getProperty("jdg.backup.initial.delay", "0") );
	
	
	/**
	 * Whether to start the backup on this node
	 */
	public static final Boolean IS_ACTIVE = Boolean.parseBoolean( System.getProperty("jdg.backup.active","true" ));
	
	/**
	 * The cache name to backup. 
	 */
	public static final String BACKUP_CACHE_NAME=System.getProperty("jdg.backup.cache.name");
}
