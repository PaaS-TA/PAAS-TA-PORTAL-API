package org.openpaas.paasta.portal.api.config;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.assertj.core.api.Fail.fail;


@ActiveProfiles("dev")
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*", "org.openpaas.paasta.portal.api.common.*", "org.openpaas.paasta.portal.api.config.*"})
@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml","eureka.client.enabled=false"}) // Push 용
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class TestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfig.class);
    protected static final ConnectionContext CONNECTION_CONTEXT = DefaultConnectionContext.builder()
            .apiHost("localhost")
            .secure(false)
            .problemHandler(new FailingDeserializationProblemHandler())  // Test-only problem handler
            .build();

    protected static final TokenProvider TOKEN_PROVIDER = connectionContext -> Mono.just("test-authorization");

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    protected final Mono<String> root;

    final MockWebServer mockWebServer;

//    private MultipleRequestDispatcher multipleRequestDispatcher = new MultipleRequestDispatcher();

    protected TestConfig() {
        this.mockWebServer = new MockWebServer();
//        this.mockWebServer.setDispatcher(this.multipleRequestDispatcher);

        this.root = Mono.just(this.mockWebServer.url("/").uri().toString());
    }

    @After
    public final void shutdown() throws IOException {
        this.mockWebServer.shutdown();
    }


    private static final class FailingDeserializationProblemHandler extends DeserializationProblemHandler {

        @Override
        public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) {
            fail(String.format("Found unexpected property %s in payload for %s", propertyName, beanOrClass.getClass().getName()));
            return false;
        }

    }


}


