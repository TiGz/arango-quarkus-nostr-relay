package org.tigz.nostrelay.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.beans.NostrFilter;
import org.tigz.nostrelay.beans.NoticeReply;
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

    private static final ObjectMapper mapper = new ObjectMapper();

    private final Session websocketSession;
    private final NostrSessionService sessionService;

    // Subscription ID to Filters map
    private final Map<String, NostrFilter> filterMap = new ConcurrentHashMap<>();

    @Override
    public void onMessage(String message) {
        processIncomingMessage(message, this);
    }

    /**
     * Clients can send 3 types of messages, which must be JSON arrays, according to the following patterns:
     *
     * ["EVENT", <event JSON as defined above>], used to publish events.
     * ["REQ", <subscription_id>, <filters JSON>...], used to request events and subscribe to new updates.
     * ["CLOSE", <subscription_id>], used to stop previous subscriptions.
     */
    public void processIncomingMessage(String message, NostrSession session){
        log.debug("Received message: {}", message);
        try{
            JsonNode node = mapper.readTree(message);
            assert node.isArray();
            String command = node.get(0).asText();
            switch (command) {
                case "EVENT":
                    JsonNode eventNode = node.get(1);
                    NostrEvent event = mapper.treeToValue(eventNode, NostrEvent.class);
                    String eventString = mapper.writeValueAsString(eventNode);
                    event.setOriginalJson(eventString);
                    log.info("Processing event: {}", eventString);
                    sessionService.processEvent(event, session);
                    return;

                case "REQ":
                    String subscriptionId = node.get(1).asText();
                    NostrFilter filter = mapper.treeToValue(node.get(2), NostrFilter.class);
                    this.filterMap.put(subscriptionId, filter);
                    log.info("Added subscription: {} with filters: {} to session: {}", subscriptionId, filter, session.getWebsocketSession().getId());
                    return;

                case "CLOSE":
                    String closeSubscriptionId = node.get(1).asText();
                    this.filterMap.remove(closeSubscriptionId);
                    log.info("Closed subscription: {} for session: {}", closeSubscriptionId, session.getWebsocketSession().getId());
                    return;

                default:
                    log.warn("Invalid Message received: {}", message);
            }
        }
        catch(Exception e){
            log.warn("Error parsing message: {}", message, e);
            session.sendNoticeReply("Failed to parse NOSTR message due to: " + e.getMessage());
        }
    }

    /**
     * Return true if the event matches any of the filters for this session
     * @param event
     * @return
     */
    public boolean isSubscribedToEvent(NostrEvent event) {
        return filterMap.values().stream().anyMatch(filter -> filter.matches(event));
    }

    /**
     * Send a NOTICE reply to the client - usually this is some kind of error
     * @param message
     */
    public void sendNoticeReply(String message) {
        try{
            String nostrMessage = mapper.writeValueAsString(new NoticeReply(message));
            websocketSession.getAsyncRemote().sendText(nostrMessage);
        }
        catch(JsonProcessingException e){
            log.error("Error serializing message: {}", message, e);
        }
        catch(Exception e){
            log.error("Error sending message: {}", message, e);
        }
    }
}
