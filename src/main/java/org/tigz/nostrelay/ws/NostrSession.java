package org.tigz.nostrelay.ws;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.Filters;
import org.tigz.nostrelay.service.NostrService;

import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class NostrSession implements MessageHandler.Whole<String> {

    private final Session websocketSession;
    private final NostrService nostrService;

    // Subscription ID to Filters map
    private final Map<String,Filters> filterMap = new ConcurrentHashMap<>();

    @Override
    public void onMessage(String message) {
        nostrService.processIncomingMessage(message, this);
    }

    public void subscribe(String subscriptionId, Filters filters){
        filterMap.put(subscriptionId, filters);
    }

    public void unsubscribe(String subscriptionId) {
        filterMap.remove(subscriptionId);
        log.debug("Removed subscription: {}", subscriptionId);
    }
}
