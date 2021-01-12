/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.restore;

import javax.management.MXBean;

/**
 * @author Meissa
 */
@MXBean
public interface RestoreCacheMBean {
	public void restoreCache(String cacheName);
	public void restoreCache(String cacheName, String jsonFileName);
	

}
