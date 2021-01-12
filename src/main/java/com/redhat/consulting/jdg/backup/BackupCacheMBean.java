/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.backup;

import javax.management.MXBean;

/**
 * @author Meissa
 * This JMX interface exposes two methods to backup the cache.
 */
@MXBean
public interface BackupCacheMBean {
	public void backupCache(String cacheName);
	public void backupCache(String cacheName, String backupLocation);
	

}
