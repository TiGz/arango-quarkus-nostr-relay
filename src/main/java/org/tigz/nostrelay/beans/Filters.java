package org.tigz.nostrelay.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <filters> is a JSON object that determines what events will be sent in that subscription, it can have the following attributes:
 *
 * {
 *   "ids": <a list of event ids or prefixes>,
 *   "authors": <a list of pubkeys or prefixes, the pubkey of an event must be one of these>,
 *   "kinds": <a list of a kind numbers>,
 *   "#e": <a list of event ids that are referenced in an "e" tag>,
 *   "#p": <a list of pubkeys that are referenced in a "p" tag>,
 *   "since": <a timestamp, events must be newer than this to pass>,
 *   "until": <a timestamp, events must be older than this to pass>,
 *   "limit": <maximum number of events to be returned in the initial query>
 * }
 * 
 */
@Data
@Builder
public class Filters {
    @JsonIgnore
    private String subscriptionId;
    private List<String> ids;
    private List<String> authors;
    private List<String> kinds;
    @JsonProperty("#e")
    private List<String> referencedEventIds;
    @JsonProperty("#p")
    private List<String> referencedPubkeys;
    private Long since;
    private Long until;
    private Integer limit;
    
}
