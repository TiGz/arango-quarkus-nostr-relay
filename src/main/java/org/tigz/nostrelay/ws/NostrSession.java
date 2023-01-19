package org.tigz.nostrelay.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.beans.NostrFilter;
import org.tigz.nostrelay.service.IncomingMessageRouter;
import org.tigz.nostrelay.service.NostrSessionService;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class NostrSession implements MessageHandler.Whole<String> {

    private final Session websocketSession;
    private final NostrSessionService sessionService;

    // Subscription ID to Filters map
    private final Map<String, NostrFilter> filterMap = new ConcurrentHashMap<>();

    @Override
    public void onMessage(String message) {
        incomingMessageRouter.processIncomingMessage(message, this);
    }


    public void processIncomingMessage(String message, NostrSession session){
        log.debug("Received message: {}", message);
        try{
            String[] nostrMessageArr = mapper.readValue(message, String[].class);
            switch (nostrMessageArr[0]) {
                case "EVENT":
                    processEvent(nostrMessageArr[1], session);
                    return;

                case "REQ":
                    processReq(nostrMessageArr[1], nostrMessageArr[2], session);
                    return;

                case "CLOSE":
                    processClose(nostrMessageArr[1], session);
                    return;

                default:
                    log.warn("Invalid Message received: {}", message);
            }
        }
        catch(Exception e){
            log.error("Error parsing message: {}", message, e);
        }
    }

    /**
     * Every incoming event needs to be:
     * a) written to the database
     * b) forwarded to any sessions that have an appropriate filter
     * @param eventJson
     * @param session
     */
    private void processEvent(String eventJson, NostrSession session) throws JsonProcessingException {
        log.debug("Processing event: {} for session with id: {}", eventJson, session.getWebsocketSession().getId());
        NostrEvent event = mapper.readValue(eventJson, NostrEvent.class);
        event.setOriginalJson(eventJson);
        log.debug("Event as Json: {}", );
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.onIncomingNostrEvent(event, session));
        }
    }

    private void processReq(String subscriptionId, String filtersJson, NostrSession session) throws JsonProcessingException {
        NostrFilter filters = mapper.readValue(filtersJson, NostrFilter.class);
        session.subscribe(subscriptionId, filters);
    }

    private void processClose(String subscriptionId, NostrSession session) {
        session.unsubscribe(subscriptionId);
    }

    public void subscribe(String subscriptionId, NostrFilter filters){
        filterMap.put(subscriptionId, filters);
    }

    public void unsubscribe(String subscriptionId) {
        filterMap.remove(subscriptionId);
        log.debug("Removed subscription: {}", subscriptionId);
    }

    /**
     * Return true if the event matches any of the filters for this session
     * @param event
     * @return
     */
    public boolean isSubscribedToEvent(NostrEvent event) {
        return filterMap.values().stream().anyMatch(filter -> filter.matches(event));
    }
}
