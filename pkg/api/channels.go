package api

import (
	"github.com/emicklei/go-restful"
	"github.com/n0rad/housecream/pkg/channels"
)

func (api Api) RegisterChannels(container *restful.Container) {
	ws := restful.WebService{}
	ws.Path("/channels").
		Doc("List channels").
		Consumes(restful.MIME_JSON).
		Produces(restful.MIME_JSON)

	ws.Route(ws.GET("/").To(api.listChannels).
		Doc("List available ws").
		Operation("listLinks").
		Writes(User{}))
	//Reads(channels.Channel))

	container.Add(&ws)
}

func (api Api) listChannels(request *restful.Request, response *restful.Response) {
	chans := []channels.Channel{}
	for _, value := range channels.ChannelByIds {
		chans = append(chans, value)
	}
	response.WriteEntity(chans)
}
