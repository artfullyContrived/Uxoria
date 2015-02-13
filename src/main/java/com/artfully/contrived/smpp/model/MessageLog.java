package com.artfully.contrived.smpp.model;

import java.io.Serializable;
import java.util.Date;

public class MessageLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int smppId;
	private int type;
	private String message;
	private String recepient;
	private boolean requestConfirmation;
	private Date timestamp;
	private int shortcode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSmppId() {
		return smppId;
	}

	public void setSmppId(int smppId) {
		this.smppId = smppId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRecepient() {
		return recepient;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}

	public boolean isRequestConfirmation() {
		return requestConfirmation;
	}

	public void setRequestConfirmation(boolean requestConfirmation) {
		this.requestConfirmation = requestConfirmation;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getShortcode() {
		return shortcode;
	}

	public void setShortcode(int shortcode) {
		this.shortcode = shortcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
