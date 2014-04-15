package org.housecream.server.api.domain.channel;

import java.util.Map;
import lombok.Data;

@Data
public class Channel {

    private String name;
    private String description;
    private String pluginName;
    private Map<String, String> attributes; // token, cityID, boardUrl
}
