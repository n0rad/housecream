package slack

import (
	"context"
	"fmt"

	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/nlopes/slack"
)

type SlackLink struct {
	channels.CommonLink
	ApiKey string

	client *slack.Client
}

func (s *SlackLink) Init() error {
	s.client = slack.New(s.ApiKey)
	_, err := s.client.AuthTest()
	if err != nil {
		// TODO move this in watch so it can start without internet
		return errs.WithE(err, "Failed to connect to slack")
	}
	return nil
}

func (s *SlackLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	rtm := s.client.NewRTM()
	defer rtm.Disconnect()
	go rtm.ManageConnection()

	for {
		select {
		case <-shutdown:
			return
		case msg := <-rtm.IncomingEvents:
			fmt.Print(msg)
		}
	}
}

func (s *SlackLink) Trigger(cxt context.Context, action channels.Action) error {
	msgParams := slack.NewPostMessageParameters()
	msgParams.LinkNames = 1
	if _, _, err := s.client.PostMessage("mytest", "`coucou` @alemaire toi", msgParams); err != nil {
		return errs.WithEF(err, s.GetFields(), "Failed to post message to slack")
	}

	return nil
}

////////////////////////////////

type SlackChannel struct {
	channels.CommonChannel
}

func (c *SlackChannel) NewLink() channels.Link {
	return &SlackLink{}
}

func init() {
	f := SlackChannel{channels.CommonChannel{
		Id:          "slack",
		Name:        "Slack",
		Description: "read and write to slack",
	}}
	channels.RegisterChannel(&f)
}
