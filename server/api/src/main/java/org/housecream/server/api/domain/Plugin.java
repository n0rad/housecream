package org.housecream.server.api.domain;

import lombok.Getter;

@Getter
public class Plugin {
    private String id;
    private String name;
    private String description;

    public static Plugin plugin(String id, String name, String description) {
        Plugin plugin = new Plugin();
        plugin.id = id;
        plugin.name = name;
        plugin.description = description;
        return plugin;
    }
}
