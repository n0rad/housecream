package channels

import (
	"context"

	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/logs"
)

type Link interface {
	Init() error
	GetName() string
	GetDescription() string
	Watch(shutdown <-chan struct{}, events chan<- Event)
	Trigger(cxt context.Context, action Action) error
	//GetFields() data.Fields
}

/////////////////////////

type CommonLink struct {
	Channel     string
	Name        string `json:",omitempty"`
	Description string `json:",omitempty"`

	Fields data.Fields
}

func (p *CommonLink) Init() error {
	p.Fields = data.WithField("link", p.Name)
	return nil
}

func (p *CommonLink) GetName() string {
	return p.Name
}

func (p *CommonLink) GetDescription() string {
	return p.Description
}

func (p *CommonLink) Watch(shutdown <-chan struct{}, events chan<- Event) {
	select {
	case <-shutdown:
		return
	}
}

func (p *CommonLink) Trigger(cxt context.Context, action Action) error {
	logs.WithFields(p.Fields).WithField("action", action).Debug("Receiving action on common link")
	return nil
}
