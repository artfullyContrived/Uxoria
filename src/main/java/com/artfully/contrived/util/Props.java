package com.artfully.contrived.util;

public enum Props {
    receiverPropertyFile("receiver.properties"),
    log4jPropertyFile("log4j.properties");

    String fileName;

    Props(String file) {
	this.fileName = file;
    }

    public String getFileName() {
	return this.fileName;
    }
}
