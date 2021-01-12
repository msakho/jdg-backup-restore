/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Meissa
 */
public class PacsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7592663808598539616L;
	
	

	private String msgId;
	private String transactionId;
	private String instructionId;
	boolean timeoutManagement;
	private BigDecimal amount;
	

	/**
	 * @param msgId
	 * @param transactionId
	 * @param instructionId
	 * @param timeoutManagement
	 * @param amount
	 */
	public PacsBean(String msgId, String transactionId, String instructionId, boolean timeoutManagement,
			BigDecimal amount) {
		super();
		this.msgId = msgId;
		this.transactionId = transactionId;
		this.instructionId = instructionId;
		this.timeoutManagement = timeoutManagement;
		this.amount = amount;
	}

	/**
	 * 
	 */
	public PacsBean() {
		
	}

	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the instructionId
	 */
	public String getInstructionId() {
		return instructionId;
	}

	/**
	 * @param instructionId the instructionId to set
	 */
	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
	}

	/**
	 * @return the timeoutManagement
	 */
	public boolean isTimeoutManagement() {
		return timeoutManagement;
	}

	/**
	 * @param timeoutManagement the timeoutManagement to set
	 */
	public void setTimeoutManagement(boolean timeoutManagement) {
		this.timeoutManagement = timeoutManagement;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "PacsBean [msgId=" + msgId + ", transactionId=" + transactionId + ", instructionId=" + instructionId
				+ ", timeoutManagement=" + timeoutManagement + ", amount=" + amount + "]";
	}
	

}
