package org.tigz.nostrelay;

import com.arangodb.entity.ArangoDBVersion;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.tigz.nostrelay.db.ArangoService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/version")
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    private final ArangoService arangoService;

    @Inject
    public GreetingResource(ArangoService arangoService) {
        this.arangoService = arangoService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArangoDBVersion getVersion() {
        LOG.info("Getting version");
        return arangoService.getVersion();
    }
}
