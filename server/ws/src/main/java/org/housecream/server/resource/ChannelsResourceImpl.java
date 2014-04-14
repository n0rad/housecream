package org.housecream.server.resource;

import java.util.List;
import org.housecream.server.api.domain.channel.Channel;
import org.housecream.server.api.resource.ChannelsResource;
import org.housecream.server.application.JaxrsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@JaxrsResource
@Validated
public class ChannelsResourceImpl implements ChannelsResource {

    @Autowired
    private PluginsResource pluginResource;

    @Autowired
    private ChannelResourceImpl channelResource;

    @Override
    public List<Channel> listChannels() {
        return null;
    }

    @Override
    public PluginsResource available() {
        return pluginResource;
    }

    @Override
    public ChannelResource channel(String id) {
        return channelResource;
    }

    //////////////

    @Component
    public static class ChannelResourceImpl implements ChannelResource {

        @Override
        public Channel getChannel(String id) {
            return null;
        }

    }
}
