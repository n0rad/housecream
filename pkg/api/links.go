package api

import (
	"net/http"

	"github.com/emicklei/go-restful"
)

func (api Api) RegisterLinks(container *restful.Container) {
	ws := restful.WebService{}
	ws.Path("/links").
		Doc("handle links").
		Consumes(restful.MIME_JSON).
		Produces(restful.MIME_JSON)

	ws.Route(ws.GET("/").To(api.listLinks).
		Doc("List available ws").
		Operation("listLinks").
		Writes(User{}))
	//Reads(channels.Channel))

	container.Add(&ws)
}

func (api Api) listLinks(request *restful.Request, response *restful.Response) {
	response.WriteEntity(api.channels.GetLinks())
}

func (api Api) createLink(request *restful.Request, response *restful.Response) {
	usr := User{}
	if err := request.ReadEntity(usr); err != nil {
		response.WriteHeaderAndEntity(http.StatusBadRequest, ApiError{err.Error()})
		//response.AddHeader("Content-Type", "text/plain")
		//response.WriteErrorString(http.StatusInternalServerError, err.Error())
		return
	}
	//usr.Id = strconv.Itoa(len(c.users) + 1) // simple id generation
	//c.users[usr.Id] = *usr
	response.WriteHeaderAndEntity(http.StatusCreated, usr)
}
