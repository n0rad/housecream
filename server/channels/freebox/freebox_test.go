package freebox

import (
	"fmt"
	"testing"
	"time"

	"github.com/Sirupsen/logrus"
	"github.com/n0rad/housecream/server/channels"
)

func Test(t *testing.T) {

	logrus.SetLevel(logrus.DebugLevel)
	link := FreeboxLink{}
	link.Token = "oZ0TsQSB5vrNkPZpka1ujLVjSJkyqxHQotRfp6xT0lV+aylvE6WnvCRjoq+zgRKJ"
	link.Host = "192.168.40.1"

	link.Init()

	eventsIn := make(chan channels.Event)
	shutdown := make(chan struct{})

	go link.Watch(shutdown, eventsIn)

	select {
	case v := <-eventsIn:
		fmt.Println(v)
	case <-time.After(10 * time.Minute):
		t.Fail()
	}
}
