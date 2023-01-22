package org.tigz.nostrelay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tigz.nostrelay.beans.NostrEvent;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class NostrJsonTest {

    @Test
    public void testToAndFromJson() throws JsonProcessingException {

        String eventJson = """
{"id": "1910092961fe1ae6b1ce8eecc1f711a025d0c23986e4257a188bf57d296f313a","pubkey":"3b2e2252f113791ec8ec973b8285021a7c49e80d5c9f54d2b28b610b303b8425","created_at":1674391523,"kind":1,"tags":[],"content":"test","sig":"eb3cf16a2441243cd35e73642ade2b1a95a8b16c3c24e1e8501b0f54438e87f0e77271d0981ebe977f962794f2cf2291b48c8e4c4dee62909a9980bda7b57b53"}""";

        ObjectMapper mapper = new ObjectMapper();

        NostrEvent event = mapper.readValue(eventJson, NostrEvent.class);

        assert event.getCreatedAt() == 1674391523;
        assert event.getTags().isEmpty();

        String json = mapper.writeValueAsString(event);
        assert json.contains("_key");

    }

    @Test
    public void testReadEvent() throws JsonProcessingException {

        String message = """
["EVENT",{"id": "1910092961fe1ae6b1ce8eecc1f711a025d0c23986e4257a188bf57d296f313a","pubkey":"3b2e2252f113791ec8ec973b8285021a7c49e80d5c9f54d2b28b610b303b8425","created_at":1674391523,"kind":1,"tags":[],"content":"test","sig":"eb3cf16a2441243cd35e73642ade2b1a95a8b16c3c24e1e8501b0f54438e87f0e77271d0981ebe977f962794f2cf2291b48c8e4c4dee62909a9980bda7b57b53"}]""";

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(message);
        assert node.isArray();
        String command = node.get(0).asText();

        assert command.equals("EVENT");

        JsonNode eventNode = node.get(1);
        NostrEvent event = mapper.treeToValue(eventNode, NostrEvent.class);
        String eventString = mapper.writeValueAsString(event);
        event.setOriginalJson(eventString);

        assert event.getCreatedAt() == 1674391523;
        assert event.getTags().isEmpty();

    }


}