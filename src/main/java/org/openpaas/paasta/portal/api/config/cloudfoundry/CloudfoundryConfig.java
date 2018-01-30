//package org.openpaas.paasta.portal.api.config.cloudfoundry;
//
//import org.cloudfoundry.reactor.DefaultConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
//import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
//import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
//import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
//import org.openpaas.paasta.portal.api.util.CfUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Created by mg on 2016-08-03.
// */
//
//@Configuration
//public class CloudfoundryConfig {
//
//    @Bean
//    DefaultConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiHost,
//                                               @Value("${cloudfoundry.sslSkipValidation}") Boolean sslSkipValidation) {
//        return CfUtils.connectionContext(apiHost, sslSkipValidation);
//    }
//
//
//    @Bean
//    PasswordGrantTokenProvider tokenProvider(@Value("${cloudfoundry.user.uaaClient.adminClientId}") String clientId,
//                                             @Value("${cloudfoundry.user.uaaClient.adminClientSecret}") String clientSecret,
//                                             @Value("${cloudfoundry.user.admin.username}") String username,
//                                             @Value("${cloudfoundry.user.admin.password}") String password) {
//        return CfUtils.tokenProvider(username, password, clientId, clientSecret);
//    }
//
//    @Bean
//    ReactorCloudFoundryClient cloudFoundryClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return CfUtils.cloudFoundryClient(connectionContext, tokenProvider);
//    }
//
//    @Bean
//    ReactorDopplerClient dopplerClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return CfUtils.dopplerClient(connectionContext, tokenProvider);
//    }
//
//    @Bean
//    ReactorUaaClient uaaClient(DefaultConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return CfUtils.uaaClient(connectionContext, tokenProvider);
//    }
//}
