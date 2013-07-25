/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.service;

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
