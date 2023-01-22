package org.tigz.nostrelay.db;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.entity.ArangoDBVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tigz.nostrelay.beans.NostrEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * ArangoDB service
 */
@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ArangoService {

    private final ArangoDB arango;

    public ArangoDBVersion getVersion() {
        return arango.getVersion();
    }

    public void saveEvent(NostrEvent event) {
        ArangoCollection events = arango.db().collection("events");
        if (!events.exists()){
            arango.db().createCollection("events");
            events = arango.db().collection("events");
        }

        // insert document into DB
        events.insertDocument(event);
    }

    @PostConstruct
    public void init(){
        log.info("ArangoService initialized");
    }
}
