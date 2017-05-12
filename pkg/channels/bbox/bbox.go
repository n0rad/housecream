package bbox

import (
	"time"

	gobbox "github.com/n0rad/go-bbox"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
)

type BboxLink struct {
	channels.CommonLink
	Host     string
	Interval time.Duration

	bbox *gobbox.Bbox
}

func (l *BboxLink) Init() error {
	if l.Interval == 0 {
		l.Interval = 1 * time.Minute
	}

	l.bbox = gobbox.New()
	l.bbox.Host = l.Host
	return nil
}

func (l *BboxLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	l.getAll(events)
	for {
		select {
		case <-shutdown:
			return
		case <-time.After(l.Interval):
			l.getAll(events)
		}
	}
}

func (l *BboxLink) getAll(events chan<- channels.Event) {
	if err := l.getDevice(events); err != nil {
		logs.WithEF(err, l.GetFields()).Error("Cannot get device stats")
	}
	if err := l.getWanIp(events); err != nil {
		logs.WithEF(err, l.GetFields()).Error("Cannot get ip stats")
	}
	if err := l.getWanIpStats(events); err != nil {
		logs.WithEF(err, l.GetFields()).Error("Cannot get wan stats")
	}
	if err := l.getWanXdsl(events); err != nil {
		logs.WithEF(err, l.GetFields()).Error("Cannot get xdsl stats")
	}
}

func (l *BboxLink) getDevice(events chan<- channels.Event) error {
	device, err := l.bbox.GetDevice()
	if err != nil {
		return err
	}
	events <- l.newEvent("boot", device.NumberOfBoots)
	events <- l.newEvent("uptime", device.Uptime)
	events <- l.newEvent("status", device.Status) // TODO convert status
	return nil
}

func (l *BboxLink) getWanIp(events chan<- channels.Event) error {
	wanIp, err := l.bbox.GetWanIp()
	if err != nil {
		return err
	}
	events <- l.newEvent("boot", wanIp.Internet.State) // TODO convert state
	return nil
}

func (l *BboxLink) getWanIpStats(events chan<- channels.Event) error {
	wanIpStats, err := l.bbox.GetWanIpStats()
	if err != nil {
		return err
	}
	events <- l.newEvent("ip_bandwith", wanIpStats.Rx.Bandwidth) //TODO add meta
	events <- l.newEvent("ip_bandwith_max", wanIpStats.Rx.MaxBandwidth)
	events <- l.newEvent("ip_bytes", wanIpStats.Rx.Bytes)
	events <- l.newEvent("ip_occupation", wanIpStats.Rx.Occupation)
	events <- l.newEvent("ip_packet", wanIpStats.Rx.Packets)
	events <- l.newEvent("ip_packet_discards", wanIpStats.Rx.Packetsdiscards)
	events <- l.newEvent("ip_packet_errors", wanIpStats.Rx.Packetserrors)

	events <- l.newEvent("ip_bandwith", wanIpStats.Tx.Bandwidth) //TODO add meta
	events <- l.newEvent("ip_bandwith_max", wanIpStats.Tx.MaxBandwidth)
	events <- l.newEvent("ip_bytes", wanIpStats.Tx.Bytes)
	events <- l.newEvent("ip_occupation", wanIpStats.Tx.Occupation)
	events <- l.newEvent("ip_packet", wanIpStats.Tx.Packets)
	events <- l.newEvent("ip_packet_discards", wanIpStats.Tx.Packetsdiscards)
	events <- l.newEvent("ip_packet_errors", wanIpStats.Tx.Packetserrors)
	return nil
}

func (l *BboxLink) getWanXdsl(events chan<- channels.Event) error {
	wanIpXdsl, err := l.bbox.GetWanXdsl()
	if err != nil {
		return err
	}

	events <- l.newEvent("xdsl_showtime", wanIpXdsl.Showtime)
	events <- l.newEvent("xdsl_syncount", wanIpXdsl.SyncCount)

	events <- l.newEvent("xdsl_attenuation", wanIpXdsl.Down.Attenuation) // TODO convert state
	events <- l.newEvent("xdsl_bitrates", wanIpXdsl.Down.Bitrates)
	events <- l.newEvent("xdsl_ginp", wanIpXdsl.Down.Ginp)
	events <- l.newEvent("xdsl_interleave_delay", wanIpXdsl.Down.InterleaveDelay)
	events <- l.newEvent("xdsl_nitro", wanIpXdsl.Down.Nitro)
	events <- l.newEvent("xdsl_noise", wanIpXdsl.Down.Noise)
	events <- l.newEvent("xdsl_phyr", wanIpXdsl.Down.Phyr)
	events <- l.newEvent("xdsl_power", wanIpXdsl.Down.Power)

	events <- l.newEvent("xdsl_attenuation", wanIpXdsl.Up.Attenuation)
	events <- l.newEvent("xdsl_bitrates", wanIpXdsl.Up.Bitrates)
	events <- l.newEvent("xdsl_ginp", wanIpXdsl.Up.Ginp)
	events <- l.newEvent("xdsl_interleave_delay", wanIpXdsl.Up.InterleaveDelay)
	events <- l.newEvent("xdsl_nitro", wanIpXdsl.Up.Nitro)
	events <- l.newEvent("xdsl_noise", wanIpXdsl.Up.Noise)
	events <- l.newEvent("xdsl_phyr", wanIpXdsl.Up.Phyr)
	events <- l.newEvent("xdsl_power", wanIpXdsl.Up.Power)

	return nil
}

func (l *BboxLink) newEvent(pointName string, value interface{}) channels.Event {
	return channels.Event{
		LinkName:  l.Name,
		PointName: pointName,
		Time:      time.Now(),
		Value:     value,
	}
}

///////////////////////////////////

type BboxChannel struct {
	channels.CommonChannel
}

func (c *BboxChannel) NewLink() channels.Link {
	return &BboxLink{}
}

func init() {
	f := BboxChannel{CommonChannel: channels.CommonChannel{
		Id:          "bbox",
		Name:        "bbox",
		Description: "Connect to bbox",
	}}
	channels.RegisterChannel(&f)
}
