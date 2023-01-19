package org.tigz.nostrelay.service;

import org.tigz.nostrelay.beans.NostrEvent;
import org.tigz.nostrelay.ws.NostrSession;

public interface IncomingNostrEventListener {
    void onIncomingNostrEvent(NostrEvent nostrEvent, NostrSession session);

}
