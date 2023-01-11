package org.tigz.nostrelay.db;

import com.arangodb.ArangoDB;
import com.arangodb.entity.ArangoDBVersion;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * ArangoDB service
 */
@ApplicationScoped
public class ArangoService {

    private final ArangoDB arango;

    @Inject
    public ArangoService(final ArangoDB arango) {
        this.arango = arango;
    }

    public ArangoDBVersion getVersion() {
        return arango.getVersion();
    }

}
