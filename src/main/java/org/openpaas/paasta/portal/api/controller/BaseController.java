//package org.openpaas.paasta.portal.api.controller;
//
//import org.cloudfoundry.client.CloudFoundryClient;
//import org.cloudfoundry.doppler.DopplerClient;
//import org.cloudfoundry.operations.CloudFoundryOperations;
//import org.cloudfoundry.reactor.ConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.uaa.UaaClient;
//import org.openpaas.paasta.portal.api.util.CfUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
///**
// * Created by mg on 2016-10-21.
// */
//public class BaseController {
//    protected static final String AUTHORIZATION_HEADER_KEY="cf-Authorization";
//
//    @Autowired
//    @Qualifier("connectionContext")
//    ConnectionContext connectionContext;
//    @Autowired
//    @Qualifier("tokenProvider")
//    TokenProvider adminTokenProvider;
//    @Autowired
//    @Qualifier("dopplerClient")
//    DopplerClient adminDopplerClient;
//    @Autowired
//    @Qualifier("uaaClient")
//    UaaClient adminUaaClient;
//    @Autowired
//    @Qualifier("cloudFoundryClient")
//    CloudFoundryClient adminCloudFoundryClient;
//
//    protected CloudFoundryOperations getAdminCloudFoundryOperations(String organization, String space) {
//        return CfUtils.cloudFoundryOperations(adminCloudFoundryClient, adminDopplerClient, adminUaaClient, organization, space);
//    }
//
//    protected TokenProvider getTokenProvider(String token) {
//        return CfUtils.tokenProvider(token);
//    }
//}
//package org.openpaas.paasta.portal.api.controller;
//
//import org.cloudfoundry.client.CloudFoundryClient;
//import org.cloudfoundry.doppler.DopplerClient;
//import org.cloudfoundry.operations.CloudFoundryOperations;
//import org.cloudfoundry.reactor.ConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.uaa.UaaClient;
//import org.openpaas.paasta.portal.api.util.CfUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
///**
// * Created by mg on 2016-10-21.
// */
//public class BaseController {
//    protected static final String AUTHORIZATION_HEADER_KEY="cf-Authorization";
//
//    @Autowired
//    @Qualifier("connectionContext")
//    ConnectionContext connectionContext;
//    @Autowired
//    @Qualifier("tokenProvider")
//    TokenProvider adminTokenProvider;
//    @Autowired
//    @Qualifier("dopplerClient")
//    DopplerClient adminDopplerClient;
//    @Autowired
//    @Qualifier("uaaClient")
//    UaaClient adminUaaClient;
//    @Autowired
//    @Qualifier("cloudFoundryClient")
//    CloudFoundryClient adminCloudFoundryClient;
//
//    protected CloudFoundryOperations getAdminCloudFoundryOperations(String organization, String space) {
//        return CfUtils.cloudFoundryOperations(adminCloudFoundryClient, adminDopplerClient, adminUaaClient, organization, space);
//    }
//
//    protected TokenProvider getTokenProvider(String token) {
//        return CfUtils.tokenProvider(token);
//    }
//}
