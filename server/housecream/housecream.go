package housecream

import (
	"fmt"

	"github.com/n0rad/housecream/server/channels"
)

type Housecream struct {
	BuildVersion string
	BuildTime    string
	BuildCommit  string
	//HomeFolder string
}

func (h Housecream) Start() {
	for _, channel := range channels.ChannelByIds {
		fmt.Println("->", channel)
	}
	//housecream.StartEngine()
}

func (h Housecream) Stop() {

}
