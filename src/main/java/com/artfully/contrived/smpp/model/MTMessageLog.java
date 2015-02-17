package com.artfully.contrived.smpp.model;

import java.io.Serializable;

import java.util.Date;

//TODO extends messageLog
public class MTMessageLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;
  private int smppId;
  private int type;
  private String message;
  private String sender;
  private boolean requestConfirmation;
  private Date timestamp;
  private String messageId;
  private int serviceId;
  private boolean isCharged;
  private int numberOfSms;
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

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
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

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public int getServiceId() {
    return serviceId;
  }

  public void setServiceId(int serviceId) {
    this.serviceId = serviceId;
  }

  public boolean isCharged() {
    return isCharged;
  }

  public void setCharged(boolean isCharged) {
    this.isCharged = isCharged;
  }

  public int getNumberOfSms() {
    return numberOfSms;
  }

  public void setNumberOfSms(int numberOfSms) {
    this.numberOfSms = numberOfSms;
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MOMessageLog [id=");
    builder.append(id);
    builder.append(", smppId=");
    builder.append(smppId);
    builder.append(", type=");
    builder.append(type);
    builder.append(", message=");
    builder.append(message);
    builder.append(", sender=");
    builder.append(sender);
    builder.append(", requestConfirmation=");
    builder.append(requestConfirmation);
    builder.append(", timestamp=");
    builder.append(timestamp);
    builder.append(", messageId=");
    builder.append(messageId);
    builder.append(", serviceId=");
    builder.append(serviceId);
    builder.append(", isCharged=");
    builder.append(isCharged);
    builder.append(", numberOfSms=");
    builder.append(numberOfSms);
    builder.append(", shortcode=");
    builder.append(shortcode);
    builder.append("]");
    return builder.toString();
  }

}