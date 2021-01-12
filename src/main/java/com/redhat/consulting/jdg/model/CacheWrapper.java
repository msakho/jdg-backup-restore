/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Meissa
 */

@SuppressWarnings("hiding")
public class CacheWrapper<CacheItem> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -30880237043570029L;
	
	private List<CacheItem> data;

	/**
	 * 
	 */
	public CacheWrapper() {
		
	}

	/**
	 * @param data
	 */
	public CacheWrapper(List<CacheItem> data) {
		
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public List<CacheItem> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<CacheItem> data) {
		this.data = data;
	}
	
	

}
