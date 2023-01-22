package org.tigz.nostrelay.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.tigz.nostrelay.service.NostrSessionService;

import javax.annotation.PostConstruct;
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
@ServerEndpoint("/ws")
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NostrWebsocket {

    private static final Logger LOG = Logger.getLogger(NostrWebsocket.class);

    private final NostrSessionService nostrSessionService;

    @PostConstruct
    public void init(){
        LOG.info("NostrWebsocket initialized");
        log.info("NostrWebsocket initialized");
    }

    @OnOpen
    public void onOpen(Session session) {
        nostrSessionService.createNewSession(session);
        log.info("Session opened: {}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        nostrSessionService.closeSession(session);
        log.info("Session closed: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.warn("Session error: {}", session.getId(), throwable);
        nostrSessionService.closeSession(session);
    }

}
