package com.artfully.contrived.smpp.model;

import java.io.Serializable;
import java.util.Date;
//TODO possible divide onto MT and MO tables
public class MessageLog implements Serializable {

  private static final long serialVersionUID = 1L;

  private int id;
  private int smppId;
  private int type;
  private String message;
  private String recepient;
  private boolean requestConfirmation;
  private Date timestamp;
  private String messageId;
  private boolean delivered;
  private Date deliveryTime;
  private int priority;
  private String received;
  private int status;
  private int serviceId;
  private boolean isCharged;
  private int numberOfSms;
  private double price;
  private boolean isSubscription;
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

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public boolean isDelivered() {
    return delivered;
  }

  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }

  public Date getDeliveryTime() {
    return deliveryTime;
  }

  public void setDeliveryTime(Date deliveryTime) {
    this.deliveryTime = deliveryTime;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getReceived() {
    return received;
  }

  public void setReceived(String received) {
    this.received = received;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean isSubscription() {
    return isSubscription;
  }

  public void setSubscription(boolean isSubscription) {
    this.isSubscription = isSubscription;
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