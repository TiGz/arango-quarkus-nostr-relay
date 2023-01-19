package org.tigz.nostrelay.beans;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.Key;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {
 *   "id": <32-bytes sha256 of the the serialized event data>
 *   "pubkey": <32-bytes hex-encoded public key of the event creator>,
 *   "created_at": <unix timestamp in seconds>,
 *   "kind": <integer>,
 *   "tags": [
 *     ["e", <32-bytes hex of the id of another event>, <recommended relay URL>],
 *     ["p", <32-bytes hex of the key>, <recommended relay URL>],
 *     ... // other kinds of tags may be included later
 *   ],
 *   "content": <arbitrary string>,
 *   "sig": <64-bytes signature of the sha256 hash of the serialized event data, which is the same as the "id" field>
 * }
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NostrEvent {

    @Key
    @NotNull
    private String id;
    @NotNull
    private String pubkey;
    @JsonProperty("created_at")
    private Long createdAt;
    @NotNull
    private Integer kind;
    private List<Tag> tags;
    @NotNull
    private JsonNode content;
    @NotNull
    private String sig;

    @JsonIgnore
    private String originalJson;

    public List<Tag> getETags(){
        if ( this.tags == null ) return Collections.emptyList();
        return this.tags.stream().filter( tag -> tag.getName().equals("e") ).toList();
    }

    public List<Tag> getPTags(){
        if ( this.tags == null ) return Collections.emptyList();
        return this.tags.stream().filter( tag -> tag.getName().equals("p") ).toList();
    }

}
