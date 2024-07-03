package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.kafka.quarkus.Movie;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/movies")
public class MoviesResource {

    private static final Logger log = LoggerFactory.getLogger(MoviesResource.class);

    @Channel("movies")
    Emitter<Movie> emitter;

    @Channel("movies-from-log")
    Multi<KafkaRecord<String, Movie>> movies;

    @POST
    public Response enqueueMovie(Movie movie) {
        log.info("Sending movie {} to Kafka", movie.getTitle());
        var record = KafkaRecord
                .of(movie.getTitle(), movie);
        emitter.send(record);
        return Response.accepted().build();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    public Multi<String> stream() {
        return movies
                .map(KafkaRecord::getPayload)
                .map(movie -> String.format("'%s' from %s", movie.getTitle(), movie.getYear()));
    }


}
