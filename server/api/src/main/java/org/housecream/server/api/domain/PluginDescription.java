package org.housecream.server.api.domain;

import org.housecream.server.api.domain.config.Config;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PluginDescription {
    private String id;
    private String name;
    private String description;
    private Class<? extends Config> configClass;
}
