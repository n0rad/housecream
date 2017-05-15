package channels

import (
	"sync"

	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/logs"
)

type LinkLifecycle struct {
	running   bool
	link      Link
	shutdown  chan struct{}
	waitGroup sync.WaitGroup

	failCount int

	// lastEvent TODO
	// lastAction
}

func NewLinkLifecycle(link Link) LinkLifecycle {
	return LinkLifecycle{
		link: link,
	}
}

func (p *LinkLifecycle) IsRunning() bool {
	return p.running
}

func (p *LinkLifecycle) Start(events chan<- Event) {
	p.running = true
	p.shutdown = make(chan struct{})

	p.waitGroup.Add(1)
	defer p.waitGroup.Done()

	defer func() {
		if e := recover(); e != nil {
			p.failCount++
			logs.WithF(data.WithField("name", p.link.GetName()).
				WithField("panic", e)).Error("Link failure")
			p.Start(events)
		}
	}()

	p.link.Watch(p.shutdown, events)
}

func (p *LinkLifecycle) Stop() {
	if !p.running {
		return
	}

	close(p.shutdown)
	p.waitGroup.Wait()
	p.running = false

	logs.WithField("name", p.link.GetName()).Debug("Link stopped")
}
