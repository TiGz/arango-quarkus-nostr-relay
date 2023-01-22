package org.tigz.nostrelay.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.db.ArangoService;
import org.tigz.nostrelay.ws.NostrSession;

import javax.annotation.PostConstruct;
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

    private final ArangoService arangoService;

    final Map<String, NostrSession> sessions = new ConcurrentHashMap<>();

    public NostrSession createNewSession(Session websocketSession){
        log.info("createNewSession called...");
        NostrSession nostrSession = new NostrSession(websocketSession, this);
        websocketSession.addMessageHandler(nostrSession);
        sessions.put(websocketSession.getId(), nostrSession);
        return nostrSession;
    }

    public void closeSession(Session websocketSession){
        sessions.remove(websocketSession.getId());
    }

    public void processEvent(NostrEvent event, NostrSession session){
        try{
            validateEvent(event);
            arangoService.saveEvent(event);
            sendEventToSubscribedSessions(event);
        }
        catch(Exception e){
            log.error("Error parsing event: {}", event, e);
            session.sendNoticeReply("Failed to process NOSTR event due to: " + e.getMessage());
        }
    }

    private void validateEvent(NostrEvent event) {
        //@TODO
    }

    private void sendEventToSubscribedSessions(NostrEvent event){
        sessions.values().forEach(session -> {
            if(session.isSubscribedToEvent(event)){
                try {
                    log.info("Sending event to session: {} - json: {}", session.getWebsocketSession().getId(), event.getOriginalJson());
                    session.getWebsocketSession().getAsyncRemote().sendText(event.getOriginalJson());
                } catch (Exception e) {
                    log.error("Error sending event to session: {}", session.getWebsocketSession().getId(), e);
                }
            }
        });
    }

    @PostConstruct
    public void init(){
        log.info("NostrSessionService initialized and arango service is: {}", arangoService);
    }
}
