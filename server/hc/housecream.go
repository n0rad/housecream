package hc

import (
	"fmt"

	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/server/channels"
)

type Housecream struct {
	Port int

	buildVersion BuildVersion
	//HomeFolder string
}

func NewHousecream(buildVersion BuildVersion) *Housecream {
	return &Housecream{
		Port:         4242,
		buildVersion: buildVersion,
	}
}

func (h *Housecream) Start(home HomeFolder) {
	home.LoadConfig(h)
	if err := home.SaveVersion(&h.buildVersion); err != nil {
		logs.WithE(err).Fatal("Cannot save version")
	}

	home.SaveConfig(h)

	for _, channel := range channels.ChannelByIds {
		fmt.Println("->", channel)
	}
	//housecream.StartEngine()
}

func (h Housecream) Stop() {

}
