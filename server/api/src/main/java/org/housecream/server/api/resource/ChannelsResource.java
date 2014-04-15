package org.housecream.server.api.resource;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.housecream.server.api.domain.PluginDescription;
import org.housecream.server.api.domain.channel.Channel;
import org.housecream.server.api.domain.channel.ChannelNotFoundException;
import org.housecream.server.api.exception.PluginNotFoundException;

@Path("/channels")
public interface ChannelsResource {

    @GET
    List<Channel> listChannels();

    @Path("/available")
    PluginsResource available();

    @Path("/{id}")
    ChannelResource channel(@PathParam("id") String channelId);

    //

    interface PluginsResource {
        @GET
        List<PluginDescription> descriptions();

        @Path("/{id}")
        PluginResource plugin(@PathParam("id") String id);
    }

    interface PluginResource {
        @GET
        @Path("/activate")
        void activate(@PathParam("id") String id,
                      @Context HttpServletRequest request,
                      @Context HttpServletResponse response) throws PluginNotFoundException;

        @GET
        @Path("/activate/callback")
        void callback(@PathParam("id") String id) throws PluginNotFoundException;

        @GET
        @Path("/logo")
        Response getLogo(@PathParam("id") String id) throws IOException, PluginNotFoundException;

        @GET
        PluginDescription getPlugin(@PathParam("id") String id) throws PluginNotFoundException;
    }

    interface ChannelResource {
        @GET
        Channel getChannel(@PathParam("id") String id) throws ChannelNotFoundException;
    }

}
