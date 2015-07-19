package com.artfully.contrived.smpp.receiver.module;

import com.artfully.contrived.smpp.common.SessionBinder;
import com.artfully.contrived.smpp.model.SMPP;

public interface SessionBinderFactory {
    SessionBinder create(SMPP smpp);
}