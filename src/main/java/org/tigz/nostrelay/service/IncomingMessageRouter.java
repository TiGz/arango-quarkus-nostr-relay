package org.tigz.nostrelay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.beans.NostrFilter;
import org.tigz.nostrelay.ws.NostrSession;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Responsible for processing incoming messages
 * Turning the JSON on the wire into beans
 * And passing on to the appropriate service for handling
 */

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class IncomingMessageRouter {



    private List<IncomingNostrEventListener> eventListeners;


}
