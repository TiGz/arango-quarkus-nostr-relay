package org.tigz.nostrelay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.ws.NostrSession;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NostrService {

    private final MessageService messageService;

    final Map<String, NostrSession> sessions = new ConcurrentHashMap<>();

    public NostrSession createNewSession(Session websocketSession){
        NostrSession nostrSession = new NostrSession(websocketSession, this);
        sessions.put(websocketSession.getId(), nostrSession);
        return nostrSession;
    }

    public void closeSession(Session websocketSession){
        sessions.remove(websocketSession.getId());
    }

    public void processIncomingMessage(String message, NostrSession nostrSession){
        messageService.processIncomingMessage(message, nostrSession);
    }
}
