package org.housecream.server.it.core.api;

import java.util.Set;
import org.housecream.server.api.domain.config.PropertyDefinition;
import org.housecream.server.api.resource.ConfigsResource;
import org.housecream.server.it.core.ItSession;

public class PropertiesApi {
    private ItSession session;

    public PropertiesApi(ItSession session) {
        this.session = session;
    }

    public Set<PropertyDefinition> getProperties() {
        return session.getServer().getResource(ConfigsResource.class, session).getProperties();
    }

    public void setProperty(String name, Object value) {
        session.getServer().getResource(ConfigsResource.class, session).property(name).setProperty(name, value.toString());
    }

}
