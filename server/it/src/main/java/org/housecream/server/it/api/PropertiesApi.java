package org.housecream.server.it.api;

import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.api.resource.PropertiesResource;
import org.housecream.server.it.ItSession;

public class PropertiesApi {
    private ItSession session;

    public PropertiesApi(ItSession session) {
        this.session = session;
    }

    public HcProperties getProperties() {
        return session.getServer().getResource(PropertiesResource.class, session).getProperties();
    }

    public void setProperty(String name, Object value) {
        session.getServer().getResource(PropertiesResource.class, session).property(name).setProperty(name, value.toString());
    }

}
