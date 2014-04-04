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
package org.housecream.server.it;

import static fr.norad.jaxrs.oauth2.api.spec.domain.GrantType.password;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorResponseExceptionMapper;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.client.server.rest.RestTokenProvider;
import fr.norad.jaxrs.junit.LoggingRule;
import fr.norad.jaxrs.oauth2.api.Client;
import fr.norad.jaxrs.oauth2.client.interception.toresource.AuthorizationInterceptor;
import fr.norad.jaxrs.oauth2.client.interception.toresource.TokenProvider;

public class ItServer extends LoggingRule {

    private RestBuilder context = new RestBuilder()
            .inheritHeaders(true)
            .addProvider(new ErrorExceptionMapper())
            .addProvider(new JacksonJaxbJsonProvider())
            .addInFaultInterceptor(RestBuilder.Generic.inStderrLogger)
            .addInInterceptor(RestBuilder.Generic.inStdoutLogger)
            .addOutFaultInterceptor(RestBuilder.Generic.outStderrLogger)
            .addOutInterceptor(RestBuilder.Generic.outStdoutLogger)
            .addProvider(new ErrorResponseExceptionMapper(new JacksonJaxbJsonProvider()));

    private Client client = new Client("it", "SUPERSECRET").addAllowedGrantType(password);
    private WebSocketClientFactory factory;

    public ItSession session() {
        return new ItSession(this).asJson().client(new ItClient(client));
    }

    public ItSession session(String username, String password) {
        return new ItSession(this, username, password).asJson().client(new ItClient(client));
    }

    public <T> T getResource(Class<T> clazz, ItSession session) {
        TokenProvider provider = new RestTokenProvider(ItContext.getAuthUrl(), session).verbose();
        RestBuilder builder = new RestBuilder(context).addOutInterceptor(new AuthorizationInterceptor(provider));
//        builder.addOutInterceptor(new AbstractPhaseInterceptor<Message>(Phase.POST_LOGICAL) {
        //
//            @Override
//            public void handleMessage(Message message) throws Fault {
//                OperationResourceInfo ori = message.getContent(OperationResourceInfo.class);
//                ClassResourceInfo cri = ori.getClassResourceInfo();
//                try {
//                    Field f = cri.getClass().getDeclaredField("parent");
//                    f.setAccessible(true);
//                    ClassResourceInfo parent = (ClassResourceInfo) f.get(ClassResourceInfo.class);
//                    Set<OperationResourceInfo> operationResourceInfos = parent.getMethodDispatcher().getOperationResourceInfos();
//                    for (OperationResourceInfo parentOri : operationResourceInfos) {
//                        Class<?> returnType = parentOri.getMethodToInvoke().getReturnType();
//                        if (cri.getResourceClass().equals(returnType)) {
//                            Method m = parent.getMethodDispatcher().getMethod(parentOri);
//
//                        }
//                    }
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        T client = builder.buildClient(clazz, ItContext.getUrl(), session);
        return client;
    }

    public HcWebSocket newWebSocket() {
        HcWebSocket socket = new HcWebSocket(factory.newWebSocketClient());
        return socket;
    }

    @Override
    public void before() throws Throwable {
        session().client(null).clients().createClient(client);
        session().client(null).props().setProperty("securityEnabled", true);

        ItSession session = session("admin", "admin");

        session.inpoints().deleteAll();
        session.outpoints().deleteAll();
        session.rules().deleteAll();
        session.zones().deleteAll();

        factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();
    }

    @Override
    protected void after() {
        session("admin", "admin").props().setProperty("securityEnabled", false);
        try {
            factory.stop();
        } catch (Exception e) {
        }
    }

}
