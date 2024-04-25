package quarkus.bank.stimulator.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import org.jboss.resteasy.reactive.ResponseHeader;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Path("/test")
public class TestResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    public String allParams(@RestQuery String type,
                            @RestQuery String age) {
        return type + "/" + age;
    }

    @GET
    @Path("/response")
    public RestResponse<String> helloResponse() {
        // HTTP OK status with text/plain content type
        return ResponseBuilder.ok("Hello, World!", MediaType.TEXT_PLAIN_TYPE)
                // set a response header
                .header("X-Cheese", "Camembert")
                // set the Expires response header to two days from now
                .expires(Date.from(Instant.now().plus(Duration.ofDays(2))))
                // send a new cookie
                .cookie(new NewCookie("Flavour", "chocolate"))
                // end of builder API
                .build();
    }

    @ResponseStatus(201)
    @ResponseHeader(name = "X-Cheese", value = "Camembert")
    @GET
    @Path("/annotation")
    public String helloAnnotation() {
        return "Hello, World!";
    }


}

