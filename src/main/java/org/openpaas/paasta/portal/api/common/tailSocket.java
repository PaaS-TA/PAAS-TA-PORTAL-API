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
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by indra on 2018-05-09.
 */
@Component
public class tailSocket implements CommandLineRunner {


    private static final Logger LOGGER = LoggerFactory.getLogger(tailSocket.class);

    @Value("${tailsocket.port}")
    public Integer tailPort;

    @Autowired
	private AppController appController;

    @Autowired
	private AppService appService;

    @Override
    public void run(String...args) throws Exception {
        startServer();
    }


    public void startServer() {
        Configuration config = new Configuration();

        String hostName = "";
        try{
            LOGGER.debug("InetAddress.getLocalHost().getHostName()="+ InetAddress.getLocalHost().getHostName() );
            LOGGER.debug("InetAddress.getLocalHost().getHostAddress()="+  InetAddress.getLocalHost().getHostAddress() );
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch( UnknownHostException e ){
            e.printStackTrace();
        }

        config.setHostname(hostName);
        config.setPort(tailPort);
        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                LOGGER.debug("onConnected");

                String referer = client.getHandshakeData().getHttpHeaders().get("Referer");
                String appName = referer.substring(referer.indexOf("name=")+5, referer.indexOf("&org="));
                String orgName = referer.substring(referer.indexOf("org=")+4, referer.indexOf("&space="));
                String spaceName = referer.substring(referer.indexOf("space=")+6, referer.indexOf("&guid="));
                LOGGER.debug(appName);
                LOGGER.debug(spaceName);
                LOGGER.debug(orgName);

                appController.socketTailLogs(client, appName, orgName, spaceName);

//                client.sendEvent("message", new ChatObject("", "Welcome to the chat!"));
//				SocketIOClient client2 = appService.socketTailLogs(client, "");
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                LOGGER.debug("onDisconnected");
            }
        });
        server.addEventListener("send", ChatObject.class, new DataListener<ChatObject>() {

            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                LOGGER.debug("onSend: " + data.toString());
                server.getBroadcastOperations().sendEvent("message", data);
            }
        });
        LOGGER.debug("Starting server...");
        server.start();
        LOGGER.debug("Server started");
    }
}
