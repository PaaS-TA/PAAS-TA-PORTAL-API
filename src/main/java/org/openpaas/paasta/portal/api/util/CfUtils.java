//package org.openpaas.paasta.portal.api.util;
//
//import org.cloudfoundry.client.CloudFoundryClient;
//import org.cloudfoundry.doppler.DopplerClient;
//import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
//import org.cloudfoundry.reactor.ConnectionContext;
//import org.cloudfoundry.reactor.DefaultConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
//import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
//import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
//import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
//import org.cloudfoundry.uaa.UaaClient;
//import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
//
///**
// * CF client 를 생성해주는 Util 클레스
// *
// * Created by mg on 2016-10-13.
// */
//public class CfUtils {
//    /**
//     * 기본적인 연결에 대한 설정값을 갖는 클레스
//     *
//     * @param apiHost
//     * @param skipSslValidation
//     * @return
//     */
//    public static DefaultConnectionContext connectionContext(String apiHost, Boolean skipSslValidation) {
//        return DefaultConnectionContext.builder()
//                .apiHost(apiHost)
//                .skipSslValidation(skipSslValidation)
//                .build();
//    }
//
//    /**
//     * token을 제공하는 클레스 기본적으로 'cloudfoundry' client를 사용하며
//     * user token을 얻는 것을 주 목적으로 사용한다.
//     *
//     * @param username
//     * @param password
//     * @return
//     */
//    public static PasswordGrantTokenProvider tokenProvider(String username,
//                                                    String password) {
//        return PasswordGrantTokenProvider.builder()
//                .clientId("cf")
//                .clientSecret("")
//                .password(password)
//                .username(username)
//                .build();
//    }
//
//    /**
//     * token을 제공하는 클레스 사용자 임의의 clientId를 사용하며,
//     * user token, client token을 모두 얻을 수 있다.
//     *
//     * @param clientId
//     * @param clientSecret
//     * @param username
//     * @param password
//     * @return
//     */
//    public static PasswordGrantTokenProvider tokenProvider(String username,
//                                                                        String password,
//                                                                        String clientId,
//                                                                        String clientSecret) {
//        return PasswordGrantTokenProvider.builder()
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .password(password)
//                .username(username)
//                .build();
//    }
//
//    /**
//     * 이미 가지고 있는 token을 CF client에 제공하는 클래스
//     *
//     * @param token
//     * @return
//     */
//
//    public static TokenGrantTokenProvider tokenProvider(String token) {
//        return new TokenGrantTokenProvider(token);
//    }
//
//    /**
//     * CloudFoudry의 기능을 제공한다.
//     *
//     * @param connectionContext
//     * @param tokenProvider
//     * @return
//     */
//    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return ReactorCloudFoundryClient.builder()
//                .connectionContext(connectionContext)
//                .tokenProvider(tokenProvider)
//                .build();
//    }
//    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
//        return cloudFoundryClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
//    }
//    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String username, String password) {
//        return cloudFoundryClient(connectionContext, tokenProvider(username, password));
//    }
//    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String token) {
//        return cloudFoundryClient(connectionContext, tokenProvider(token));
//    }
//
//    /**
//     * Dropper의 기능을 제공한다.
//     *
//     * @param connectionContext
//     * @param tokenProvider
//     * @return
//     */
//    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return ReactorDopplerClient.builder()
//                .connectionContext(connectionContext)
//                .tokenProvider(tokenProvider)
//                .build();
//    }
//    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
//        return dopplerClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
//    }
//    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String username, String password) {
//        return dopplerClient(connectionContext, tokenProvider(username, password));
//    }
//    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String token) {
//        return dopplerClient(connectionContext, tokenProvider(token));
//    }
//
//    /**
//     * Uaa 의 기능을 제공한다.
//     *
//     * @param connectionContext
//     * @param tokenProvider
//     * @return
//     */
//    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return ReactorUaaClient.builder()
//                .connectionContext(connectionContext)
//                .tokenProvider(tokenProvider)
//                .build();
//    }
//    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
//        return uaaClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
//    }
//    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String username, String password) {
//        return uaaClient(connectionContext, tokenProvider(username, password));
//    }
//    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String token) {
//        return uaaClient(connectionContext, tokenProvider(token));
//    }
//
//    /**
//     * CloudFoundry, Dropper, Uaa 의 기능을 함께 사용하여 구현가능한 기능을 제공한다.
//     *
//     * @param cloudFoundryClient
//     * @param dopplerClient
//     * @param uaaClient
//     * @param organization
//     * @param space
//     * @return
//     */
//    public static DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
//                                                                       DopplerClient dopplerClient,
//                                                                       UaaClient uaaClient,
//                                                                       String organization,
//                                                                       String space) {
//        return DefaultCloudFoundryOperations.builder()
//                .cloudFoundryClient(cloudFoundryClient)
//                .dopplerClient(dopplerClient)
//                .uaaClient(uaaClient)
//                .organization(organization)
//                .space(space)
//                .build();
//    }
//    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext,
//                                                                       TokenProvider tokenProvider,
//                                                                       String organization,
//                                                                       String space) {
//        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider),
//                dopplerClient(connectionContext, tokenProvider),
//                uaaClient(connectionContext, tokenProvider),
//                organization,
//                space);
//    }
//    public static DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
//                                                                       DopplerClient dopplerClient,
//                                                                       UaaClient uaaClient) {
//        return DefaultCloudFoundryOperations.builder()
//                .cloudFoundryClient(cloudFoundryClient)
//                .dopplerClient(dopplerClient)
//                .uaaClient(uaaClient)
//                .build();
//    }
//    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext, TokenProvider tokenProvider) {
//        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider),
//                dopplerClient(connectionContext, tokenProvider),
//                uaaClient(connectionContext, tokenProvider));
//    }
//}
