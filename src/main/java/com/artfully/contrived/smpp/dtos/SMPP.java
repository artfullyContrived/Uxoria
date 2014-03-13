/*
 * 
 */
package com.artfully.contrived.smpp.dtos;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;

/**
 * The Class SMPPBean.
 */

public class SMPP implements Serializable {

    private static final long serialVersionUID = -419536446569266465L;

    private int ID;

    /** The host. */
    private String SMPPServerIP;

    /** The port. */
    private int SMPPServerPort;

    /** The bind type. */
    private String bindType;;

    /** The system type. */
    private String systemType;

    /** The password. */
    private String password;

    /** The npi. */
    private byte NPI;

    /** The ton. */
    private byte TON;

    /** The system id. */
    private String systemID;

    /** The enquire link timer. */
    private int enquireLinkTimer;

    private boolean isShuttingDown;

    // dont know if its a good idea
    private transient SMPPSession session;

    private int dataEncoding;

    private String shortCode;

    private int tps;

    public int getID() {
	return ID;
    }

    public void setID(int iD) {
	ID = iD;
    }

    public String getSMPPServerIP() {
	return SMPPServerIP;
    }

    public void setSMPPServerIP(String sMPPServerIP) {
	SMPPServerIP = sMPPServerIP;
    }

    public int getSMPPServerPort() {
	return SMPPServerPort;
    }

    public void setSMPPServerPort(int sMPPServerPort) {
	SMPPServerPort = sMPPServerPort;
    }

    public String getSystemType() {
	return systemType;
    }

    public void setSystemType(String systemType) {
	this.systemType = systemType;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public byte getNPI() {
	return NPI;
    }

    public void setNPI(byte nPI) {
	NPI = nPI;
    }

    public byte getTON() {
	return TON;
    }

    public void setTON(byte tON) {
	TON = tON;
    }

    public String getSystemID() {
	return systemID;
    }

    public void setSystemID(String systemID) {
	this.systemID = systemID;
    }

    public int getEnquireLinkTimer() {
	return enquireLinkTimer;
    }

    public void setEnquireLinkTimer(int enquireLinkTimer) {
	this.enquireLinkTimer = enquireLinkTimer;
    }

    public SMPPSession getSession() {
	return session;
    }

    public void setSession(SMPPSession session) {
	this.session = session;
    }

    public int getDataEncoding() {
	return dataEncoding;
    }

    public void setDataEncoding(int dataEncoding) {
	this.dataEncoding = dataEncoding;
    }

    public String getShortCode() {
	return shortCode;
    }

    public void setShortCode(String shortCode) {
	this.shortCode = shortCode;
    }

    public void setTPS(int tps) {
	this.tps = tps;
    }

    public int getTPS() {
	return tps;
    }

    public String getBindType() {
	return bindType;
    }

    public void setBindType(String bindType) {
	this.bindType = bindType;
    }

    public BindParameter getBindParameters() {
	return new BindParameter(BindType.valueOf(bindType), systemID,
		password, systemType,
		TypeOfNumber.valueOf(TON), NumberingPlanIndicator.valueOf(NPI),
		"");
    }

    /**
     * @return the isShuttingDown
     */
    public boolean isShuttingDown() {
	return isShuttingDown;
    }

    /**
     * @param isShuttingDown
     *            the isShuttingDown to set
     */
    public void setShuttingDown(boolean isShuttingDown) {
	this.isShuttingDown = isShuttingDown;
    }

    @Override
    public String toString() {
	return ReflectionToStringBuilder.toString(this,
		ToStringStyle.SHORT_PREFIX_STYLE);

    }
}
