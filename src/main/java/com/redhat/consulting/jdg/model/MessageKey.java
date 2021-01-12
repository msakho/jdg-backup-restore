/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.model;

import java.io.Serializable;

/**
 * @author Meissa
 */
public class MessageKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2743132420689984997L;
	
	private String id;
	private String bicCode;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the bicCode
	 */
	public String getBicCode() {
		return bicCode;
	}
	/**
	 * @param bicCode the bicCode to set
	 */
	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}
	/**
	 * @param id
	 * @param bicCode
	 */
	public MessageKey(String id, String bicCode) {
		super();
		this.id = id;
		this.bicCode = bicCode;
	}
	/**
	 * 
	 */
	public MessageKey() {
		
	}
	@Override
	public String toString() {
		return "MessageKey [id=" + id + ", bicCode=" + bicCode + "]";
	}
	
	

}
