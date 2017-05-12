package channels

import (
	"context"

	"encoding/json"

	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
)

type Link interface {
	Init() error
	GetName() string
	GetDescription() string
	Watch(shutdown <-chan struct{}, events chan<- Event)
	Trigger(cxt context.Context, action Action) error
	GetFields() data.Fields
}

/////////////////////////

func LinkFromJson(content []byte) (Link, error) {
	lc := &CommonLink{}
	if err := json.Unmarshal([]byte(content), lc); err != nil {
		return nil, errs.WithE(err, "Failed to unmarshall link type")
	}

	channel, ok := ChannelByIds[lc.Channel]
	if !ok {
		return nil, errs.WithF(data.WithField("chan", lc.Channel), "Unknown channel")
	}

	link := channel.NewLink()
	if err := json.Unmarshal([]byte(content), link); err != nil {
		return nil, errs.WithE(err, "Failed to unmarshall link")
	}

	if err := link.Init(); err != nil {
		return nil, errs.WithEF(err, link.GetFields(), "Failed to init link")
	}
	return link, nil
}

type CommonLink struct {
	Channel     string
	Name        string `json:",omitempty"`
	Description string `json:",omitempty"`

	fields data.Fields
}

func (l *CommonLink) GetFields() data.Fields {
	return l.fields
}

func (l *CommonLink) Init() error {
	l.fields = data.WithField("link", l.Name)
	return nil
}

func (l *CommonLink) GetName() string {
	return l.Name
}

func (l *CommonLink) GetDescription() string {
	return l.Description
}

func (l *CommonLink) Watch(shutdown <-chan struct{}, events chan<- Event) {
	select {
	case <-shutdown:
		return
	}
}

func (l *CommonLink) Trigger(cxt context.Context, action Action) error {
	logs.WithFields(l.fields).WithField("action", action).Debug("Receiving action on common link")
	return nil
}
