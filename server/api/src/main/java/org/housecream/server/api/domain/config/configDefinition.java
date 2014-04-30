package org.housecream.server.api.domain.config;


import java.util.HashMap;
import java.util.Map;
import fr.norad.jaxrs.doc.api.domain.ModelDefinition;
import fr.norad.jaxrs.doc.api.domain.PropertyDefinition;
import lombok.Data;

@Data
public class configDefinition {

    private Map<String, PropertyDefinition> properties = new HashMap<>();
    private Map<String, ModelDefinition> models = new HashMap<>();

}
