package logs

import (
	"github.com/n0rad/housecream/server/channels"
)

type LogsChannel struct {
	channels.CommonChannel
}

func init() {
	f := LogsChannel{CommonChannel: channels.CommonChannel{
		Id:          "logs",
		Name:        "Logs",
		Description: "Write to logs",
	}}
	channels.RegisterChannel(&f)
}
