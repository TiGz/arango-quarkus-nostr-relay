package org.tigz.nostrelay.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.db.ArangoService;
import org.tigz.nostrelay.service.NostrService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

/**
 * Websocker endpoint for Nostr on /
 */
@ServerEndpoint("/")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NostrWebsocket {

    private final NostrService nostrService;

    @OnOpen
    public void onOpen(Session session) {
        nostrService.createNewSession(session);
        log.debug("Session opened: {}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        nostrService.closeSession(session);
        log.debug("Session closed: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("Session error: {}", session.getId(), throwable);
        nostrService.closeSession(session);
    }

    @OnMessage
    public void onMessage(String message) {

    }

    private void processClose(String subscriptionId) {
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getWebsocketSession().getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }


}
