package api

type Api struct {
	Port int
}

func (api *Api) Init() error {
	if api.Port == 0 {
		api.Port = 4242
	}
	return nil
}

func (api *Api) start() {

}
