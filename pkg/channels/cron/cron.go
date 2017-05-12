package cron

import (
	"time"

	"github.com/n0rad/housecream/pkg/channels"
	c "github.com/robfig/cron"
)

type CronLink struct {
	channels.CommonLink
	Rule string

	cron *c.Cron
}

func (l *CronLink) Init() error {
	return nil
}

func (l *CronLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	l.cron = c.New()
	l.cron.AddFunc(l.Rule, func() {
		events <- channels.Event{
			Time:     time.Now(),
			LinkName: l.Name,
		}
	})
	l.cron.Start()
	defer l.cron.Stop()
	select {
	case <-shutdown:
		return
	}
}

///////////////////////////////////

type CronChannel struct {
	channels.CommonChannel
}

func (c *CronChannel) NewLink() channels.Link {
	return &CronLink{}
}

func init() {
	f := CronChannel{CommonChannel: channels.CommonChannel{
		Id:          "cron",
		Name:        "Cron",
		Description: "Trigger events at specific time",
	}}
	channels.RegisterChannel(&f)
}
