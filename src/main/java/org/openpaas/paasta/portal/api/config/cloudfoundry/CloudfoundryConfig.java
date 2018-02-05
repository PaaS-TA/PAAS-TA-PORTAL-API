package org.openpaas.paasta.portal.api.config.cloudfoundry;

import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mg on 2016-08-03.
 */

@Configuration
public class CloudfoundryConfig {

    @Bean
    DefaultConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiHost,
                                               @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        return Common.connectionContext(apiHost, sslSkipValidation);
    }


    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cloudfoundry.user.admin.username}") String username,
                                             @Value("${cloudfoundry.user.admin.password}") String password) {
        return Common.tokenProvider(username, password);
    }

    @Bean
    ReactorCloudFoundryClient cloudFoundryClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
        return Common.cloudFoundryClient(connectionContext, tokenProvider);
    }

    @Bean
    ReactorDopplerClient dopplerClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
        return Common.dopplerClient(connectionContext, tokenProvider);
    }

    @Bean
    ReactorUaaClient uaaClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
        return Common.uaaClient(connectionContext, tokenProvider);
    }

}
