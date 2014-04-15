package org.housecream.server.api.domain.config;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
public class PropertyDefinition {
    private String name;
    private Class<?> type;
    private String description;
    private String value;
    private String defaultValue;
    private Class<?> group;


}
