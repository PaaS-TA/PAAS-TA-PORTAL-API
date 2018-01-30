package org.openpaas.paasta.portal.api.config.cloudfoundry.provider;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import reactor.core.publisher.Mono;

/**
 * Created by mg on 2016-08-09.
 */
public class TokenGrantTokenProvider implements TokenProvider{

    private String token;

    public TokenGrantTokenProvider(String token) {
        this.token = token;
    }

    @Override
    public Mono<String> getToken(ConnectionContext connectionContext) {

        return Mono.just(token);
    }
}
