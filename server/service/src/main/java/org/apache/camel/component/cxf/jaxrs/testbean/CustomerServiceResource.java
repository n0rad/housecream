
package org.apache.camel.component.cxf.jaxrs.testbean;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @version
 */
// START SNIPPET: example
@Path("/customerservice/")
public class CustomerServiceResource {

    public CustomerServiceResource() {
    }

    @GET
    @Path("/customers/{id}/")
    public Customer getCustomer(@PathParam("id") String id) {
        return null;
    }

    @PUT
    @Path("/customers/")
    public Response updateCustomer(Customer customer) {
        return null;
    }
}
// END SNIPPET: example