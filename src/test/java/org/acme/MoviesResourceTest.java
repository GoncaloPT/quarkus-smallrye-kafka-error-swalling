package org.acme;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import org.hamcrest.Matchers;
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
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class MoviesResourceTest {
    @TestHTTPResource("/movies")
    URI movies;


    @Test
    void testConsumption() throws InterruptedException {
        // create a client for `ConsumedMovieResource` and collect the consumed resources in a list
        try (Client client = ClientBuilder.newClient()
        ) {
            WebTarget target = client.target(movies);
            List<String> received = new CopyOnWriteArrayList<>();
            SseEventSource source = SseEventSource.target(target).build();
            source.register(inboundSseEvent -> received.add(inboundSseEvent.readData()));
            var movieSender = movieSender();

            source.open();

            // check if, after at most 5 seconds, we have at least 2 items collected, and they are what we expect
            await().atMost(5, SECONDS).until(() -> received.size() >= 2);
            assertThat(received, Matchers.hasItems("'The Shawshank Redemption' from 1994",
                    "'12 Angry Men' from 1957"));
            source.close();

            movieSender.shutdownNow();
            movieSender.awaitTermination(5, SECONDS);


        }


    }

    private ExecutorService movieSender() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            while (true) {
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"title\":\"The Shawshank Redemption\",\"year\":1994}")
                        .when()
                        .post("/movies")
                        .then()
                        .statusCode(202);

                given()
                        .contentType(ContentType.JSON)
                        .body("{\"title\":\"12 Angry Men\",\"year\":1957}")
                        .when()
                        .post("/movies")
                        .then()
                        .statusCode(202);

                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        return executorService;
    }


}
