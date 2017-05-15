package exec

import "github.com/n0rad/housecream/pkg/channels"

type ExecLink struct {
	channels.CommonLink
	TimeoutInMilli int
	Command        []string
}

func (e *ExecLink) Init() error {
	return nil
}

///////////////////////////////////

type ExecChannel struct {
	channels.CommonChannel
}

func (c *ExecChannel) NewLink() channels.Link {
	return &ExecLink{}
}

func init() {
	f := ExecChannel{CommonChannel: channels.CommonChannel{
		Id:          "exec",
		Name:        "Exec",
		Description: "Execute function",
	}}
	channels.RegisterChannel(&f)
}
