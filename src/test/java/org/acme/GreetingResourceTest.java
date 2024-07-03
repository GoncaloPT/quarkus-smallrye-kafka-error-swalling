package org.acme;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
        /*@QuarkusTestResource(KafkaCompanionResource.class)*/
class GreetingResourceTest {
    @TestHTTPResource("/movies")
    URI movies;

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

    /*@Test
    void testProducing() {

        companion
                .produce(Serdes.String(), new AvroSerde<>())
                .withProp(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
                .withProp("schema.registry.url", "http://localhost:8081")
                .usingGenerator(i -> new ProducerRecord<>("test-topic", "key" + i, new Movie("movie-"+i,i)), 5);

        var consumerTask = companion.consume(Serdes.String(), new AvroSerde<>())
                .fromTopics("test-topic", 5);

        consumerTask.awaitCompletion(Duration.ofSeconds(3));
        assertEquals(5, consumerTask.count());


    }*/



}
