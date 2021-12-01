package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;

import java.util.Date;

public class PaastaConnectionContext extends PaastaContextInterface {

    DefaultConnectionContext connectionContext;
    Date create_time;

    public PaastaConnectionContext(DefaultConnectionContext connectionContext, Date create_time){
        this.connectionContext = connectionContext;
        this.create_time = create_time == null ? null : new Date(create_time.getTime());
    }

    public DefaultConnectionContext getConnectionContext() {
        return connectionContext;
    }

    @Override
    public Date getCreate_time() {
        return create_time == null ? null : new Date(create_time.getTime());
    }
}
