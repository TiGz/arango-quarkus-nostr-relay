package org.tigz.nostrelay.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * To obtain the event.id, we sha256 the serialized event. The serialization is done over the UTF-8 JSON-serialized string (with no white space or line breaks) of the following structure:
 *
 * [
 *   0,
 *   <pubkey, as a (lowercase) hex string>,
 *   <created_at, as a number>,
 *   <kind, as a number>,
 *   <tags, as an array of arrays of non-null strings>,
 *   <content, as a string>
 * ]
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"zero", "pubkey", "createAt", "kind", "tags", "content" } )
public class EventSchnorr {


    @JsonProperty
    private Integer zero = 0;



}
