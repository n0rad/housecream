package freebox

import (
	"time"

	"os"

	"encoding/json"

	"github.com/moul/go-freebox"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
)

type FreeboxLink struct {
	channels.CommonLink
	Host     string
	Token    string
	Interval time.Duration

	client *freebox.Client
}

func (l *FreeboxLink) Init() error {
	if l.Interval == 0 {
		l.Interval = 1 * time.Minute
	}

	os.Setenv("GOFBX_TOKEN", l.Token)

	l.client = freebox.New()
	if l.Host != "" {
		l.client.URL = l.Host
		if l.client.URL[len(l.client.URL)-1] != '/' {
			l.client.URL += "/"
		}
	}

	return nil
}

func (l *FreeboxLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	if err := l.handleWatch(events); err != nil {
		logs.WithEF(err, l.GetFields()).Error("Cannot get freebox status")
	}
	for {
		select {
		case <-shutdown:
			return
		case <-time.After(l.Interval):
			if err := l.handleWatch(events); err != nil {
				logs.WithEF(err, l.GetFields()).Error("Cannot get freebox status")
			}
		}
	}
}

func (l *FreeboxLink) handleWatch(events chan<- channels.Event) error {
	if err := l.client.Connect(); err != nil {
		return errs.WithEF(err, l.GetFields(), "Failed to connect to freebox")
	}

	if err := l.client.Authorize(); err != nil {
		return errs.WithEF(err, l.GetFields(), "Failed to autorize to freebox")
	}

	if err := l.client.Login(); err != nil {
		return errs.WithEF(err, l.GetFields(), "Failed to login to freebox")
	}

	stats, err := l.connectionStats()
	if err != nil {
		return err
	}

	linkUp := channels.Placeholder{Name: "link", Value: "up"}
	linkDown := channels.Placeholder{Name: "link", Value: "down"}

	events <- l.newEvent("bandwith", stats.BandwidthUp, linkUp)
	events <- l.newEvent("bandwith", stats.BandwidthDown, linkDown)

	events <- l.newEvent("rate", stats.RateUp, linkUp)
	events <- l.newEvent("rate", stats.RateDown, linkDown)

	events <- l.newEvent("bytes", stats.BytesUp, linkUp)
	events <- l.newEvent("bytes", stats.BytesDown, linkDown)
	return nil
}

type Connection struct {
	RateUp        int    `json:"rate_up"`
	RateDown      int    `json:"rate_down"`
	BytesUp       uint64 `json:"bytes_up"`
	BytesDown     uint64 `json:"bytes_down"`
	BandwidthUp   int    `json:"bandwidth_up"`
	BandwidthDown int    `json:"bandwidth_down"`

	Type  string `json:"type"`
	Media string `json:"media"`
	State string `json:"state"`
	Ipv4  string `json:"ipv4"`
	Ipv6  string `json:"ipv6"`
}

type ConnectionResult struct {
	Success bool       `json:"success"`
	Result  Connection `json:"result"`
}

func (l *FreeboxLink) connectionStats() (Connection, error) {
	response := ConnectionResult{}
	body, err := l.client.GetResource("connection", true)
	if err != nil {
		return response.Result, errs.WithE(err, "Cannot get connection stats")
	}

	if err := json.Unmarshal(body, &response); err != nil {
		return response.Result, err
	}
	if response.Success == false {
		return response.Result, errs.With("Receive failed response from freebox")
	}
	return response.Result, nil
}

func (l *FreeboxLink) newEvent(pointName string, value interface{}, meta ...channels.Placeholder) channels.Event {
	return channels.Event{
		LinkName:  l.Name,
		PointName: pointName,
		Time:      time.Now(),
		Metas:     meta,
		Value:     value,
	}
}

///////////////////////////////////

type FreeboxChannel struct {
	channels.CommonChannel
}

func (c *FreeboxChannel) NewLink() channels.Link {
	return &FreeboxLink{}
}

func init() {
	f := FreeboxChannel{CommonChannel: channels.CommonChannel{
		Id:          "freebox",
		Name:        "Freebox",
		Description: "Connect to freebox",
	}}
	channels.RegisterChannel(&f)
}
