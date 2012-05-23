package net.awired.housecream.server.core.service;

import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouter2 extends RouteBuilder {

    private static final String CXF_RS_ENDPOINT_URI = "cxfrs:/rest?resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerServiceResource";

    @Override
    public void configure() throws Exception {
        errorHandler(new NoErrorHandlerBuilder());

        from(CXF_RS_ENDPOINT_URI).to("log:com.mycompany.order?level=WARN");

        //                from("cxfrs:/rest?resourceClasses=net.awired.housecream.server.core.service.HccResource").process(
        //                        new Processor() {
        //                            @Override
        //                            public void process(Exchange exchange) throws Exception {
        //                                //                        Message inMessage = exchange.getIn();
        //                                exchange.getOut().setBody("salut");
        //                            }
        //                        });

        //        from(CXF_RS_ENDPOINT_URI).to("cxfrs://bean://rsClient");
        //        if (true || true) {
        //            return;
        //        }

        //
        //        from(CXF_RS_ENDPOINT_URI).process(new Processor() {
        //            @Override
        //            public void process(Exchange exchange) throws Exception {
        //                Message inMessage = exchange.getIn();
        //
        //                // Get the operation name from in message
        //                String operationName = inMessage.getHeader(CxfConstants.OPERATION_NAME, String.class);
        //                if ("getCustomer".equals(operationName)) {
        //                    String httpMethod = inMessage.getHeader(Exchange.HTTP_METHOD, String.class);
        //                    //assertEquals("Get a wrong http method", "GET", httpMethod);
        //                    String path = inMessage.getHeader(Exchange.HTTP_PATH, String.class);
        //                    // The parameter of the invocation is stored in the body of in message
        //                    String id = inMessage.getBody(String.class);
        //                    if ("/customerservice/customers/126".equals(path)) {
        //                        Customer customer = new Customer();
        //                        customer.setId(Long.parseLong(id));
        //                        customer.setName("Willem");
        //                        // We just put the response Object into the out message body
        //                        exchange.getOut().setBody(customer);
        //                    } else {
        //                        if ("/customerservice/customers/400".equals(path)) {
        //                            // We return the remote client IP address this time
        //                            org.apache.cxf.message.Message cxfMessage = inMessage.getHeader(
        //                                    CxfConstants.CAMEL_CXF_MESSAGE, org.apache.cxf.message.Message.class);
        //                            ServletRequest request = (ServletRequest) cxfMessage.get("HTTP.REQUEST");
        //                            String remoteAddress = request.getRemoteAddr();
        //                            Response r = Response.status(200).entity("The remoteAddress is " + remoteAddress).build();
        //                            exchange.getOut().setBody(r);
        //                            return;
        //                        }
        //                        if ("/customerservice/customers/123".equals(path)) {
        //                            // send a customer response back
        //                            Response r = Response.status(200).entity("customer response back!").build();
        //                            exchange.getOut().setBody(r);
        //                            return;
        //                        }
        //                        if ("/customerservice/customers/456".equals(path)) {
        //                            Response r = Response.status(404).entity("Can't found the customer with uri " + path)
        //                                    .build();
        //                            throw new WebApplicationException(r);
        //                        } else {
        //                            throw new RuntimeCamelException("Can't found the customer with uri " + path);
        //                        }
        //                    }
        //                }
        //                if ("updateCustomer".equals(operationName)) {
        //                    //                    assertEquals("Get a wrong customer message header", "header1;header2",
        //                    //                            inMessage.getHeader("test"));
        //                    String httpMethod = inMessage.getHeader(Exchange.HTTP_METHOD, String.class);
        //                    //                    assertEquals("Get a wrong http method", "PUT", httpMethod);
        //                    Customer customer = inMessage.getBody(Customer.class);
        //                    //                    assertNotNull("The customer should not be null.", customer);
        //                    // Now you can do what you want on the customer object
        //                    //                    assertEquals("Get a wrong customer name.", "Mary", customer.getName());
        //                    // set the response back
        //                    exchange.getOut().setBody(Response.ok().build());
        //                }
        //
        //            }
        //
        //        });
    }

}
