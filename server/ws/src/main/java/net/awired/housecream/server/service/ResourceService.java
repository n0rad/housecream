package net.awired.housecream.server.service;

import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.stereotype.Component;
import com.google.common.io.CharStreams;

@Component
@Path("/resources")
public class ResourceService {

    boolean b = false;

    @GET
    @Path("Validator.js")
    @Produces("application/x-javascript")
    public String getValidator() {

        //        File f = new File("/tmp/file.doc");
        //
        //        ResponseBuilder response = Response.ok((Object) f);
        //        response.type(contentType);
        //        response.header("Content-Disposition", "attachment; filename=\"file.doc\"");
        //        return response.build();

        try {
            return CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/Validator.js"),
                    "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
