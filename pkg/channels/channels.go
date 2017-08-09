package channels

import (
	"fmt"

	"encoding/json"

	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
)

type channels Channels

type Channels struct {
	Links []json.RawMessage

	typedLinks []Link
}

func (c *Channels) GetLinks() []Link {
	return c.typedLinks
}

func (c *Channels) MarshalJSON() ([]byte, error) {
	c.Links = []json.RawMessage{}
	for _, link := range c.typedLinks {
		jsonLink, err := json.Marshal(link)
		if err != nil {
			return []byte{}, errs.WithEF(err, link.GetFields(), "Failed to marshal link")
		}
		c.Links = append(c.Links, jsonLink)
	}
	return json.Marshal((*channels)(c))
}

func (c *Channels) Init() error {
	for _, data := range c.Links {
		link, err := LinkFromJson(data)
		if err != nil {
			return errs.WithE(err, "Failed to load link")
		}
		c.typedLinks = append(c.typedLinks, link)
	}
	return nil
}

func (c *Channels) Start(eventsIn chan<- Event) {
	for _, link := range c.typedLinks {
		logs.WithFields(link.GetFields()).Info("Loading link")
		lifeCycle := NewLinkLifecycle(link)
		go lifeCycle.Start(eventsIn)
	}

	for _, channel := range ChannelByIds {
		fmt.Println("->", channel)
	}
}
