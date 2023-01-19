package org.tigz.nostrelay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.ws.NostrSession;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Nostr Session Service is responsible for holding all current
 * sessions and routing messages from the websocket to the correct
 * place via the incoming message router
 *
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NostrSessionService {

    private final ObjectMapper mapper;

    final Map<String, NostrSession> sessions = new ConcurrentHashMap<>();

    public NostrSession createNewSession(Session websocketSession){
        NostrSession nostrSession = new NostrSession(websocketSession, incomingMessageRouter);
        sessions.put(websocketSession.getId(), nostrSession);
        return nostrSession;
    }

    public void closeSession(Session websocketSession){
        sessions.remove(websocketSession.getId());
    }

    public void sendEventToSubscribedSessions(NostrEvent event){
        sessions.values().forEach(session -> {
            if(session.isSubscribedToEvent(event)){
                try {
                    session.getWebsocketSession().getAsyncRemote().sendText(event.getOriginalJson());
                } catch (Exception e) {
                    log.error("Error sending event to session: {}", session.getWebsocketSession().getId(), e);
                }
            }
        });
    }
}
