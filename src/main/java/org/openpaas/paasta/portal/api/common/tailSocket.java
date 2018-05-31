package org.openpaas.paasta.portal.api.common;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.openpaas.paasta.portal.api.controller.AppController;
import org.openpaas.paasta.portal.api.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by indra on 2018-05-09.
 */
@Component
public class tailSocket implements CommandLineRunner {


    private static final Logger LOGGER = LoggerFactory.getLogger(tailSocket.class);

    @Autowired
    public Environment env;


    @Value("${tailsocket.port}")
    public Integer tailPort;

    @Autowired
    private AppController appController;

    @Autowired
    private AppService appService;

    @Override
    public void run(String... args) throws Exception {
        startServer();
    }


    public void startServer() {
        String active = "";
        String hostName = "";

        Configuration config = new Configuration();
        try {
            for (String str : env.getActiveProfiles()) {
                active = str;
            }

            if (active.equals("local")) {
                hostName = "localhost";
            } else if (active.equals("dev")) {
                LOGGER.info("InetAddress.getLocalHost().getHostName()=" + InetAddress.getLocalHost().getHostName());
                LOGGER.info("InetAddress.getLocalHost().getHostAddress()=" + InetAddress.getLocalHost().getHostAddress());
                hostName = InetAddress.getLocalHost().getHostAddress();
            } else {
                hostName = "localhost";
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostName = "localhost";
        }

        LOGGER.info("Host ::: " + hostName);

        config.setHostname(hostName);
        config.setPort(tailPort);
        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {

                LOGGER.info("onConnected");
                String referer = client.getHandshakeData().getHttpHeaders().get("Referer");
                LOGGER.info(referer);
                String appName = referer.substring(referer.indexOf("name=") + 5, referer.indexOf("&org="));
                String orgName = referer.substring(referer.indexOf("org=") + 4, referer.indexOf("&space="));
                String spaceName = referer.substring(referer.indexOf("space=") + 6, referer.indexOf("&guid="));

                LOGGER.info(appName);
                LOGGER.info(spaceName);
                LOGGER.info(orgName);

                appController.socketTailLogs(client, appName, orgName, spaceName);

//                client.sendEvent("message", new ChatObject("", "Welcome to the chat!"));
//				SocketIOClient client2 = appService.socketTailLogs(client, "");
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                LOGGER.info("onDisconnected");
            }
        });
        server.addEventListener("send", ChatObject.class, new DataListener<ChatObject>() {

            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                LOGGER.info("onSend: " + data.toString());
                server.getBroadcastOperations().sendEvent("message", data);
            }
        });
        LOGGER.info("Starting server...");
        server.start();
        LOGGER.info("Server started");
    }
}
