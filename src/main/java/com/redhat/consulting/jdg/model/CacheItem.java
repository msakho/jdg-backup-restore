/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.model;

import java.io.Serializable;

/**
 * @author Meissa
 */
public class CacheItem<K,V> implements Serializable {

	
	private static final long serialVersionUID = -166132672403456494L;
	private K key;
	private V entry;
	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}
	/**
	 * @return the entry
	 */
	public V getEntry() {
		return entry;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setEntry(V entry) {
		this.entry = entry;
	}
	/**
	 * @param key
	 * @param entry
	 */
	public CacheItem(K key, V entry) {
		super();
		this.key = key;
		this.entry = entry;
	}
	/**
	 * 
	 */
	public CacheItem() {
		
	}
	
	@Override
	public String toString() {
		return "CacheItem [key=" + key + ", entry=" + entry + "]";
	}

	

}
