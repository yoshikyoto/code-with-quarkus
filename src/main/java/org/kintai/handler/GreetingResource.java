package org.kintai.handler;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestPath;
import org.kintai.entity.SampleEntity;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

@Path("/hello")
public class GreetingResource {

    Logger logger = Logger.getLogger(GreetingResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/samples/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SampleEntityResponse get(Long id) throws IOException {
        logger.info("GET /samples/" + id);
        ObjectifyService.init();
        ObjectifyService.register(SampleEntity.class);
        SampleEntity sampleEntity;
        try (Closeable closeable = ObjectifyService.begin()) {
            sampleEntity = ObjectifyService.ofy().load().type(SampleEntity.class).id(id).now();
        }
        SampleEntityResponse response = new SampleEntityResponse(
                sampleEntity.getId(),
                sampleEntity.getName()
        );
        return response;
    }

    @POST
    @Path("/samples")
    @Consumes(MediaType.APPLICATION_JSON) // request の content-type
    @Produces(MediaType.APPLICATION_JSON) // response の content-type
    // jackson をインストールしているとこれだけで勝手に request にパラメータを突っ込んでくれる
    public SampleEntityResponse create(SampleEntityCreateRequest request) throws IOException {
        logger.info("POST /samples name:" + request.name);
        ObjectifyService.init();
        ObjectifyService.register(SampleEntity.class);
        SampleEntity sampleEntity;
        try (Closeable closeable = ObjectifyService.begin()) {
            Long id = ObjectifyService.factory().allocateId(SampleEntity.class).getId();
            sampleEntity = new SampleEntity(id, request.name);
            ObjectifyService.ofy().save().entity(sampleEntity).now();
        }
        SampleEntityResponse response = new SampleEntityResponse(
                sampleEntity.getId(),
                sampleEntity.getName()
        );
        return response;
    }

    public static class SampleEntityCreateRequest {
        public String name;

        public SampleEntityCreateRequest() {
        }
    }

    public static class SampleEntityResponse {
        public Long id;
        public String name;

        public SampleEntityResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
