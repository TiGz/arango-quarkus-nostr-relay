package org.tigz.nostrelay.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Bean for responding with an error
 *
 * ["NOTICE", <message>], used to send human-readable error messages or other things to clients.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"type", "message" } )
public class NoticeReply {

    // "e" for event, "p" for pubkey
    @NotNull
    @JsonProperty
    private String type;

    @NotNull
    @JsonProperty
    private String message;

    public NoticeReply(String message) {
        this.type = "NOTICE";
        this.message = message;
    }
}
