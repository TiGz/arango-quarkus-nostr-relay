package org.tigz.nostrelay.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Bean for dealing with tag arrays like:
 *
 * ["e", <32-bytes hex of the id of another event>, <recommended relay URL>],
 * or
 * ["p", <32-bytes hex of the key>, <recommended relay URL>],
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"name", "id", "url" } )
public class Tag {

    // "e" for event, "p" for pubkey
    @NotNull
    @JsonProperty
    private String name;

    @NotNull
    // 32-bytes hex of the id of another event or pubkey
    @JsonProperty
    private String id;

    // recommended relay URL
    @JsonProperty
    private String url;
}
