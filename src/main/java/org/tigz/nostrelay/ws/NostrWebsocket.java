package org.tigz.nostrelay.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.service.NostrSessionService;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
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

    private final NostrSessionService nostrSessionService;

    @OnOpen
    public void onOpen(Session session) {
        nostrSessionService.createNewSession(session);
        log.debug("Session opened: {}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        nostrSessionService.closeSession(session);
        log.debug("Session closed: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("Session error: {}", session.getId(), throwable);
        nostrSessionService.closeSession(session);
    }

}
