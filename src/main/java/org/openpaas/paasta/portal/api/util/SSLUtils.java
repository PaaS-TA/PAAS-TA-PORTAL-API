package org.openpaas.paasta.portal.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;
/**
 * Created by Dojun on 2016-10-05.
 */

/*
    resttemplate로 api 요청시 SSL 유효성 체크를 건너뜀
 */
public final class SSLUtils {
    //private static final Logger LOGGER = LoggerFactory.getLogger(SSLUtils.class);
    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
            new javax.net.ssl.HostnameVerifier(){

                public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                    if (hostname.equals("localhost")) {
                        return true;
                    }
                    return false;
                }
            });
    }

    private static final TrustManager[] UNQUESTIONING_TRUST_MANAGER = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers(){
                return null;
            }
            public void checkClientTrusted( X509Certificate[] certs, String authType ){}
            public void checkServerTrusted( X509Certificate[] certs, String authType ){}
        }
    };

    public  static void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init( null, UNQUESTIONING_TRUST_MANAGER, null );
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
        // Return it to the initial state (discovered by reflection, now hardcoded)
        SSLContext.getInstance("SSL").init( null, null, null );
    }

    private SSLUtils(){
        throw new UnsupportedOperationException( "Do not instantiate libraries.");
    }
}