package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;

import java.util.Date;

public class PaastaTokenContext extends PaastaContextInterface{

    PasswordGrantTokenProvider tokenProvider;
    Date create_time;

    public PaastaTokenContext(PasswordGrantTokenProvider tokenProvider, Date create_time){
        this.tokenProvider = tokenProvider;
        this.create_time = create_time;
    }

    public PasswordGrantTokenProvider tokenProvider() {
        return tokenProvider;
    }

    @Override
    public Date getCreate_time() {
        return create_time;
    }
}
