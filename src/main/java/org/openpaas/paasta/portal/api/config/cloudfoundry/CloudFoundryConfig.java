package org.openpaas.paasta.portal.api.config.cloudfoundry;


import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudFoundryConfig {
    @Bean
    DefaultConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiHost, @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        return Common.crateConnectionContext(apiHost, sslSkipValidation);
    }
}
