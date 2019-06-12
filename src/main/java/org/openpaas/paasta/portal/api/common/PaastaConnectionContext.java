package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;

import java.util.Date;

public class PaastaConnectionContext {

    DefaultConnectionContext connectionContext;
    Date create_time;

    public PaastaConnectionContext(DefaultConnectionContext connectionContext, Date create_time){
        this.connectionContext = connectionContext;
        this.create_time = create_time;
    }

    public DefaultConnectionContext getConnectionContext() {
        return connectionContext;
    }

    public Date getCreate_time() {
        return create_time;
    }
}
