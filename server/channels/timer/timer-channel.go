package timer

import "github.com/n0rad/housecream/server/channels"

type TimerChannel struct {
	channels.CommonChannel
}

func (t *TimerChannel) XX(name string) {

}

func init() {
	f := TimerChannel{CommonChannel: channels.CommonChannel{
		Id:          "timer",
		Name:        "Timer",
		Description: "Trigger events at specific time",
	}}
	channels.RegisterChannel(&f)
}
