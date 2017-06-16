package bbox

import "github.com/n0rad/housecream/pkg/channels"

type CounterLink struct {
	channels.CommonLink
}

func (l *CounterLink) Init() error {
	return nil
}

func (l *CounterLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	// todo should not be needed
	//select {
	//case <-shutdown:
	//	return
	//}
}

///////////////////////////////////

type CounterChannel struct {
	channels.CommonChannel
}

func (c *CounterChannel) NewLink() channels.Link {
	return &CounterLink{}
}

func init() {
	f := CounterChannel{CommonChannel: channels.CommonChannel{
		Id:          "counter",
		Name:        "Counter",
		Description: "Channel for counters, no outside connection",
	}}
	channels.RegisterChannel(&f)
}
