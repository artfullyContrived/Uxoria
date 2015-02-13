package com.artfully.contrived.smpp.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MessageQueue {

	private int id;
	private int smppId;
	private String recipient;
	private byte priority;
	private String message;
	private Date timestamp;

	public MessageQueue(ResultSet resultSet) throws SQLException {
		this.id= resultSet.getInt("id");
		this.smppId = resultSet.getInt("smppId");
		this.recipient = resultSet.getString("recipient");
		this.priority = resultSet.getByte("priority");
		this.message = resultSet.getString("message");
		this.timestamp = resultSet.getDate("timestamp");
	}

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

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}