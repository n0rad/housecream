package org.apache.camel.component.cxf.jaxrs.testbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;

/**
 * 
 * @version
 */
@Component
@Path("/customerservice/")
public class CustomerService {
    long currentId = 123;
    Map<Long, Customer> customers = new HashMap<Long, Customer>();
    Map<Long, Order> orders = new HashMap<Long, Order>();

    public CustomerService() {
        init();
    }

    @GET
    @Path("/customers/{id}/")
    public Customer getCustomer(@PathParam("id") String id) {
        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);
        return c;
    }

    @GET
    @Path("/customers")
    public Customer getCustomerByQueryParam(@QueryParam("id") String id) {
        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);
        return c;
    }

    @GET
    @Path("/customers/")
    @Produces("application/xml")
    public List<Customer> getCustomers() {
        List<Customer> l = new ArrayList<Customer>(customers.values());
        return l;
    }

    @PUT
    @Path("/customers/")
    public Response updateCustomer(Customer customer) {
        Customer c = customers.get(customer.getId());
        Response r;
        if (c != null) {
            customers.put(customer.getId(), customer);
            r = Response.ok().build();
        } else {
            r = Response.notModified().build();
        }

        return r;
    }

    @POST
    @Path("/customers/")
    public Response addCustomer(Customer customer) {
        customer.setId(++currentId);

        customers.put(customer.getId(), customer);

        return Response.ok(customer).build();
    }

    @POST
    @Path("/customersUniqueResponseCode/")
    public Response addCustomerUniqueResponseCode(Customer customer) {
        customer.setId(++currentId);

        customers.put(customer.getId(), customer);

        return Response.status(201).entity(customer).build();
    }

    @DELETE
    @Path("/customers/{id}/")
    public Response deleteCustomer(@PathParam("id") String id) {
        long idNumber = Long.parseLong(id);
        Customer c = customers.get(idNumber);

        Response r;
        if (c != null) {
            r = Response.ok().build();
            customers.remove(idNumber);
        } else {
            r = Response.notModified().build();
        }
        if (idNumber == currentId) {
            --currentId;
        }
        return r;
    }

    @Path("/orders/{orderId}/")
    public Order getOrder(@PathParam("orderId") String orderId) {
        long idNumber = Long.parseLong(orderId);
        Order c = orders.get(idNumber);
        return c;
    }

    final void init() {
        Customer c = new Customer();
        c.setName("John");
        c.setId(123);
        customers.put(c.getId(), c);

        c = new Customer();
        c.setName("Dan");
        c.setId(113);
        customers.put(c.getId(), c);

        Order o = new Order();
        o.setDescription("order 223");
        o.setId(223);
        orders.put(o.getId(), o);
    }

}
