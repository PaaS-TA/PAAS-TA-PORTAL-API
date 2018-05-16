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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by indra on 2018-05-09.
 */
@Component
public class tailSocket implements CommandLineRunner {

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
        config.setHostname("localhost");
        config.setPort(5555);
        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                System.out.println("onConnected");
//                System.out.println(client.getHandshakeData().getHttpHeaders().get("Referer"));
//                System.out.println(client.getHandshakeData().getUrl());
//                System.out.println(client.getHandshakeData().getUrlParams());

                String referer = client.getHandshakeData().getHttpHeaders().get("Referer");
                String appName = referer.substring(referer.indexOf("name=")+5, referer.indexOf("&org="));
                String orgName = referer.substring(referer.indexOf("org=")+4, referer.indexOf("&space="));
                String spaceName = referer.substring(referer.indexOf("space=")+6, referer.indexOf("&guid="));
                System.out.println(appName);
                System.out.println(orgName);
                System.out.println(spaceName);

                appController.socketTailLogs(client, appName, orgName, spaceName);

//                client.sendEvent("message", new ChatObject("", "Welcome to the chat!"));
//				SocketIOClient client2 = appService.socketTailLogs(client, "");
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("onDisconnected");
            }
        });
        server.addEventListener("send", ChatObject.class, new DataListener<ChatObject>() {

            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                System.out.println("onSend: " + data.toString());
                server.getBroadcastOperations().sendEvent("message", data);
            }
        });
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started");
    }
}
