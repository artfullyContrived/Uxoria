package com.artfully.contrived.smpp.model;

import java.util.HashMap;
import java.util.Map;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.MessageType;

public class MyDeliverSM extends SMPP {

  private static final long serialVersionUID = 1L;
  private SMPP smpp;
  private DeliverSm deliverSm;

  public MyDeliverSM(SMPP smpp, DeliverSm deliverSm) {
    this.smpp = smpp;
    this.deliverSm = deliverSm;
  }

  public SMPP getSmpp() {
    return smpp;
  }

  public void setSmpp(SMPP smpp) {
    this.smpp = smpp;
  }

  public DeliverSm getDeliverSm() {
    return deliverSm;
  }

  public void setDeliverSm(DeliverSm deliverSm) {
    this.deliverSm = deliverSm;
  }

  public String getServiceType() {
    return deliverSm.getServiceType();
  }

  public byte[] getShortMessage() {
    return deliverSm.getShortMessage();
  }

  public String getSourceAddr() {
    return deliverSm.getSourceAddr();
  }

  public boolean isSmeDeliveryAckRequested() {
    return deliverSm.isSmeDeliveryAckRequested();
  }

  public int getSequenceNumber() {
    return deliverSm.getSequenceNumber();
  }

  public String getDestAddress() {
    return deliverSm.getDestAddress();
  }

  public MessageType getEsmClass() {
    return getMessageType(deliverSm.getEsmClass());
  }

  private MessageType getMessageType(byte bite) {
    Map<Byte, MessageType> map = new HashMap();
    for (MessageType x : MessageType.values()) {
      map.put(x.value(), x);
    }
    return map.get(bite);
  }
}
