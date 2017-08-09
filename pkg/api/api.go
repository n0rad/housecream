package api

import (
	"net/http"

	"net"

	"github.com/emicklei/go-restful"
	"github.com/emicklei/go-restful-swagger12"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/housecream/pkg/channels"
)

type Api struct {
	Url string

	channels *channels.Channels
	listener net.Listener
}

func (api *Api) Init(channels *channels.Channels) error {
	if api.Url == "" {
		api.Url = ":4242"
	}
	api.channels = channels
	return nil
}

func (api *Api) Start() error {
	// to see what happens in the package, uncomment the following
	//restful.TraceLogger(log.New(os.Stdout, "[restful] ", log.LstdFlags|log.Lshortfile))
	var err error
	if api.listener, err = net.Listen("tcp", api.Url); err != nil {
		//n.fields.WithField("url", url),
		return errs.WithE(err, "Failed to listen")
	}

	wsContainer := restful.NewContainer()
	u := UserResource{map[string]User{}}
	u.Register(wsContainer)

	api.RegisterLinks(wsContainer)
	api.RegisterChannels(wsContainer)

	// Optionally, you can install the Swagger Service which provides a nice Web UI on your REST API
	// You need to download the Swagger HTML5 assets and change the FilePath location in the config below.
	// Open http://localhost:8080/apidocs and enter http://localhost:8080/apidocs.json in the api input field.
	config := swagger.Config{
		WebServices:    wsContainer.RegisteredWebServices(), // you control what services are visible
		WebServicesUrl: "http://localhost:8080",
		ApiPath:        "/apidocs.json",

		// Optionally, specify where the UI is located
		//SwaggerPath:     "/apidocs/",
		//SwaggerFilePath: "/Users/n0rad/xProjects/swagger-ui/dist"
	}
	swagger.RegisterSwaggerService(config, wsContainer)

	go http.Serve(api.listener, wsContainer)

	//api.server = &http.Server{Addr: ":4242", Handler: wsContainer}
	//go log.Fatal(api.server.ListenAndServe())

	//restful.Add(New())
	//log.Fatal(http.ListenAndServe(":4242", nil))
	return nil
}

func (api *Api) Stop() {
	if api.listener != nil {
		api.listener.Close()
	}
	api.listener = nil
}

type ApiError struct {
	Msg string
}
