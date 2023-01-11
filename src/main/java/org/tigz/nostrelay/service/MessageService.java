package org.tigz.nostrelay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.Filters;
import org.tigz.nostrelay.ws.NostrSession;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final ObjectMapper mapper;

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

    private void processEvent(String eventJson, NostrSession session) {

    }

    private void processReq(String subscriptionId, String filtersJson, NostrSession session) throws JsonProcessingException {
        Filters filters = mapper.readValue(filtersJson, Filters.class);
        session.subscribe(subscriptionId, filters);
    }

    private void processClose(String subscriptionId, NostrSession session) {
        session.unsubscribe(subscriptionId);
    }

}
