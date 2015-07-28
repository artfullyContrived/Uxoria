package com.artfully.contrived.smpp.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.SMPPSession;

import com.google.common.collect.ComparisonChain;

public class ShortMessage implements Comparable<ShortMessage>, Serializable {

  private static final long serialVersionUID = 5278525042564918534L;
  private final String serviceType = "CMT";
  private TypeOfNumber sourceAddrTon;
  private NumberingPlanIndicator sourceAddrNpi;
  private String sourceAddr;
  private TypeOfNumber destAddrTon;
  private NumberingPlanIndicator destAddrNpi;
  private String destinationAddr;
  private transient final ESMClass esmClass = new ESMClass(
      MessageMode.STORE_AND_FORWARD,
      MessageType.DEFAULT,
      GSMSpecificFeature.DEFAULT);
  private byte priorityFlag;
  private byte protocolId;
  private String scheduleDeliveryTime;
  private String validityPeriod;
  private transient RegisteredDelivery registeredDelivery;
  private final byte replaceIfPresentFlag = 0;
  private DataCoding dataCoding;
  private final byte smDefaultMsgId = 0;
  private String shortMessage;
  private OptionalParameter optionalParameters;
  private transient SMPPSession session;

  public String getServiceType() {
    return serviceType;
  }

  public TypeOfNumber getSourceAddrTon() {
    return sourceAddrTon;
  }

  public void setSourceAddrTon(TypeOfNumber sourceAddrTon) {
    this.sourceAddrTon = sourceAddrTon;
  }

  public NumberingPlanIndicator getSourceAddrNpi() {
    return sourceAddrNpi;
  }

  public void setSourceAddrNpi(NumberingPlanIndicator sourceAddrNpi) {
    this.sourceAddrNpi = sourceAddrNpi;
  }

  public String getSourceAddr() {
    return sourceAddr;
  }

  public void setSourceAddr(String sourceAddr) {
    this.sourceAddr = sourceAddr;
  }

  public TypeOfNumber getDestAddrTon() {
    return destAddrTon;
  }

  public void setDestAddrTon(TypeOfNumber destAddrTon) {
    this.destAddrTon = destAddrTon;
  }

  public NumberingPlanIndicator getDestAddrNpi() {
    return destAddrNpi;
  }

  public void setDestAddrNpi(NumberingPlanIndicator destAddrNpi) {
    this.destAddrNpi = destAddrNpi;
  }

  public String getDestinationAddr() {
    return destinationAddr;
  }

  public void setDestinationAddr(String destinationAddr) {
    this.destinationAddr = destinationAddr;
  }

  public ESMClass getEsmClass() {
    return esmClass;
  }

  public byte getPriorityFlag() {
    return priorityFlag;
  }

  public void setPriorityFlag(byte priorityFlag) {
    this.priorityFlag = priorityFlag;
  }

  public byte getProtocolId() {
    return protocolId;
  }

  public void setProtocolId(byte protocolId) {
    this.protocolId = protocolId;
  }

  public String getScheduleDeliveryTime() {
    return scheduleDeliveryTime;
  }

  public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
    this.scheduleDeliveryTime = scheduleDeliveryTime;
  }

  public String getValidityPeriod() {
    return validityPeriod;
  }

  public void setValidityPeriod(String validityPeriod) {
    this.validityPeriod = validityPeriod;
  }

  public RegisteredDelivery getRegisteredDelivery() {
    return registeredDelivery;
  }

  public void setRegisteredDelivery(RegisteredDelivery registeredDelivery) {
    this.registeredDelivery = registeredDelivery;
  }

  public byte getReplaceIfPresentFlag() {
    return replaceIfPresentFlag;
  }

  public DataCoding getDataCoding() {
    return dataCoding;
  }

  public void setDataCoding(DataCoding dataCoding) {
    this.dataCoding = dataCoding;
  }

  public byte getSmDefaultMsgId() {
    return smDefaultMsgId;
  }

  public String getShortMessage() {
    return shortMessage;
  }

  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }

  public OptionalParameter getOptionalParameters() {
    return optionalParameters;
  }

  public void setOptionalParameters(OptionalParameter optionalParameters) {
    this.optionalParameters = optionalParameters;
  }

  public SMPPSession getSession() {
    return session;
  }

  public void setSession(SMPPSession session) {
    this.session = session;
  }

  @Override
  public int compareTo(ShortMessage that) {

    return ComparisonChain.start()
        .compare(this.priorityFlag, that.priorityFlag)
        .compare(this.scheduleDeliveryTime, that.scheduleDeliveryTime)
        .result();
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.SHORT_PREFIX_STYLE);
  }

  public static class Builder {

    private TypeOfNumber sourceAddrTon;
    private NumberingPlanIndicator sourceAddrNpi;
    private String sourceAddr;
    private TypeOfNumber destAddrTon;
    private NumberingPlanIndicator destAddrNpi;
    private String destinationAddr;
    private byte priorityFlag;
    private byte protocolId;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private RegisteredDelivery registeredDelivery;
    private DataCoding dataCoding;
    private String shortMessage;
    private OptionalParameter optionalParameters;
    private final SMPPSession session;

    public Builder(SMPPSession session) {
      this.session = session;
    }

    public Builder sourceAddrTon(TypeOfNumber sourceAddrTon) {
      this.sourceAddrTon = sourceAddrTon;
      return this;
    }

    public Builder sourceAddrNpi(NumberingPlanIndicator sourceAddrNpi) {
      this.sourceAddrNpi = sourceAddrNpi;
      return this;
    }

    public Builder sourceAddr(String sourceAddr) {
      this.sourceAddr = sourceAddr;
      return this;
    }

    public Builder destAddrTon(TypeOfNumber destAddrTon) {
      this.destAddrTon = destAddrTon;
      return this;
    }

    public Builder destinationAddr(String destinationAddr) {
      this.destinationAddr = destinationAddr;
      return this;
    }

    public Builder destAddrNpi(NumberingPlanIndicator destAddrNpi) {
      this.destAddrNpi = destAddrNpi;
      return this;
    }

    public Builder priorityFlag(byte priorityFlag) {
      this.priorityFlag = priorityFlag;
      return this;
    }

    public Builder protocolId(byte protocolId) {
      this.protocolId = protocolId;
      return this;
    }

    public Builder scheduleDeliveryTime(String scheduleDeliveryTime) {
      this.scheduleDeliveryTime = scheduleDeliveryTime;
      return this;
    }

    public Builder validityPeriod(String validityPeriod) {
      this.validityPeriod = validityPeriod;
      return this;
    }

    public Builder registeredDelivery(RegisteredDelivery registeredDelivery) {
      this.registeredDelivery = registeredDelivery;
      return this;
    }

    public Builder dataCoding(DataCoding dataCoding) {
      this.dataCoding = dataCoding;
      return this;
    }

    public Builder shortMessage(String shortMessage) {
      this.shortMessage = shortMessage;
      return this;
    }

    public Builder optionalParameters(OptionalParameter optionalParameters) {
      this.optionalParameters = optionalParameters;
      return this;
    }

    public ShortMessage build() {
      return new ShortMessage(this);
    }
  }

  private ShortMessage(Builder builder) {

    this.dataCoding = builder.dataCoding;
    this.destAddrNpi = builder.destAddrNpi;
    this.destAddrTon = builder.destAddrTon;
    this.destinationAddr = builder.destinationAddr;
    this.optionalParameters = builder.optionalParameters;
    this.protocolId = builder.protocolId;
    this.registeredDelivery = builder.registeredDelivery;
    this.scheduleDeliveryTime = builder.scheduleDeliveryTime;
    this.shortMessage = builder.shortMessage;
    this.sourceAddr = builder.sourceAddr;
    this.sourceAddrNpi = builder.sourceAddrNpi;
    this.sourceAddrTon = builder.sourceAddrTon;
    this.validityPeriod = builder.validityPeriod;
    this.priorityFlag = builder.priorityFlag;
    this.session = builder.session;
  }
}
