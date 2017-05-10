package freebox

import (
	"fmt"
	"time"

	"os"

	"github.com/Sirupsen/logrus"
	"github.com/moul/go-freebox"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
)

type FreeboxLink struct {
	channels.CommonLink
	Host  string
	Token string

	Interval time.Duration
}

func (l *FreeboxLink) Init() error {
	if l.Interval == 0 {
		l.Interval = 1 * time.Minute
	}
	os.Setenv("GOFBX_TOKEN", l.Token)
	return nil
}

func (l *FreeboxLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	fbx := freebox.New()
	if l.Host != "" {
		fbx.URL = "http://" + l.Host + "/"
	}
	logrus.SetLevel(logrus.DebugLevel)
	l.handleWatch(events, fbx)
	for {
		select {
		case <-shutdown:
			return
		case <-time.After(l.Interval):
			if err := l.handleWatch(events, fbx); err != nil {
				logs.WithEF(err, l.Fields).Error("Cannot get freebox status")
			}
		}
	}
}

func (l *FreeboxLink) handleWatch(events chan<- channels.Event, fbx *freebox.Client) error {
	err := fbx.Connect()
	if err != nil {
		logrus.Fatalf("fbx.Connect(): %v", err)
	}

	err = fbx.Authorize()
	if err != nil {
		logrus.Fatalf("fbx.Authorize(): %v", err)
	}

	// TODO
	//if l.Token == "" {
	//	fbx.App.Identifier
	//}

	err = fbx.Login()
	if err != nil {
		logrus.Fatalf("fbx.Login(): %v", err)
	}

	stats, err := fbx.DownloadsStats()
	if err != nil {
		logrus.Fatalf("fbx.DownloadsStats(): %v", err)
	}

	fmt.Println("stats", stats)
	events <- l.newEvent("tx", stats.TXRate)
	events <- l.newEvent("rx", stats.RXRate)
	return nil
}

func (l *FreeboxLink) newEvent(pointName string, value interface{}) channels.Event {
	return channels.Event{
		LinkName:  l.Name,
		PointName: pointName,
		Time:      time.Now(),
		Value:     value,
	}
}

///////////////////////////////////

type CronChannel struct {
	channels.CommonChannel
}

func init() {
	f := CronChannel{CommonChannel: channels.CommonChannel{
		Id:          "freebox",
		Name:        "Freebox",
		Description: "Connect to freebox",
	}}
	channels.RegisterChannel(&f)
}
