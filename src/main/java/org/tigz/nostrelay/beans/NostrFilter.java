package org.tigz.nostrelay.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.arc.All;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class NostrFilter {
    @JsonIgnore
    private String subscriptionId;
    private List<String> ids;
    private List<String> authors;
    private List<Integer> kinds;
    @JsonProperty("#e")
    private List<String> referencedEventIds;
    @JsonProperty("#p")
    private List<String> referencedPubkeys;
    private Long since;
    private Long until;
    private Integer limit;


    /**
     * Filter attributes containing lists (such as ids, kinds, or #e) are JSON arrays
     * with one or more values. At least one of the array's values must match the relevant
     * field in an event for the condition itself to be considered a match.
     * For scalar event attributes such as kind, the attribute from the event must be contained
     * in the filter list. For tag attributes such as #e, where an event may have multiple values,
     * the event and filter condition values must have at least one item in common.
     *
     * The ids and authors lists contain lowercase hexadecimal strings, which may either be
     * an exact 64-character match, or a prefix of the event value. A prefix match is when the filter
     * string is an exact string prefix of the event value. The use of prefixes allows for more compact
     * filters where a large number of values are queried, and can provide some privacy for clients that
     * may not want to disclose the exact authors or events they are searching for.
     *
     * All conditions of a filter that are specified must match for an event for it to pass the filter,
     * i.e., multiple conditions are interpreted as && conditions.
     *
     * @param event
     * @return
     */
    public boolean matches(NostrEvent event) {

        // first check the ids and authors lists
        if (ids != null && !ids.isEmpty()) {
            boolean idMatch = false;
            for (String id : ids) {
                if (id.length() == 64) {
                    if (id.equals(event.getId())) {
                        idMatch = true;
                        break;
                    }
                } else if (id.length() < 64) {
                    if (event.getId().startsWith(id)) {
                        idMatch = true;
                        break;
                    }
                }
            }
            if (!idMatch) {
                return false;
            }
        }

        // check authors
        if (authors != null && !authors.isEmpty()) {
            boolean authorMatch = false;
            for (String author : authors) {
                if (author.length() == 64) {
                    if (author.equals(event.getPubkey())) {
                        authorMatch = true;
                        break;
                    }
                } else if (author.length() < 64) {
                    if (event.getPubkey().startsWith(author)) {
                        authorMatch = true;
                        break;
                    }
                }
            }
            if (!authorMatch) {
                return false;
            }
        }

        // check kinds
        if (kinds != null && !kinds.isEmpty()) {
            if (!kinds.contains(event.getKind())) {
                return false;
            }
        }

        // check referenced event ids
        if (referencedEventIds != null && !referencedEventIds.isEmpty()) {
            boolean referencedEventIdMatch = false;
            for (String referencedEventId : referencedEventIds) {
                if (referencedEventId.length() == 64) {
                    if (event.getETags().stream().anyMatch(e -> e.getId().equals(referencedEventId))) {
                        referencedEventIdMatch = true;
                        break;
                    }
                } else if (referencedEventId.length() < 64) {
                    if (event.getETags().stream().anyMatch(e -> e.getId().startsWith(referencedEventId))) {
                        referencedEventIdMatch = true;
                        break;
                    }
                }
            }
            if (!referencedEventIdMatch) {
                return false;
            }
        }

        // check referenced pubkeys
        if (referencedPubkeys != null && !referencedPubkeys.isEmpty()) {
            boolean referencedPubkeyMatch = false;
            for (String referencedPubkey : referencedPubkeys) {
                if (referencedPubkey.length() == 64) {
                    if (event.getPTags().stream().anyMatch(p -> p.getId().equals(referencedPubkey))) {
                        referencedPubkeyMatch = true;
                        break;
                    }
                } else if (referencedPubkey.length() < 64) {
                    if (event.getPTags().stream().anyMatch(p -> p.getId().startsWith(referencedPubkey))) {
                        referencedPubkeyMatch = true;
                        break;
                    }
                }
            }
            if (!referencedPubkeyMatch) {
                return false;
            }
        }

        // check since
        if (since != null) {
            if (event.getCreatedAt() < since) {
                return false;
            }
        }

        // check until
        if (until != null) {
            if (event.getCreatedAt() > until) {
                return false;
            }
        }

        return true;
    }
}
