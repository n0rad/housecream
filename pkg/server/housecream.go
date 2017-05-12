package server

import (
	"fmt"

	"context"

	"reflect"

	"time"

	"io/ioutil"

	"encoding/json"

	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/n0rad/housecream/pkg/prometheus"
	"github.com/prometheus/common/model"
	"github.com/prometheus/prometheus/promql"
	"github.com/prometheus/prometheus/storage/local"
)

type housecream Housecream

type Housecream struct {
	Port  int
	Links []json.RawMessage

	typedLinks   []channels.Link
	buildVersion BuildVersion
	shutdownCtx  context.Context
	shutdownFunc context.CancelFunc
}

func NewHousecream(buildVersion BuildVersion) *Housecream {
	return &Housecream{
		Port:         4242,
		buildVersion: buildVersion,
	}
}

func (h *Housecream) MarshalJSON() ([]byte, error) {
	h.Links = []json.RawMessage{}
	for _, link := range h.typedLinks {
		jsonLink, err := json.Marshal(link)
		if err != nil {
			return []byte{}, errs.WithEF(err, link.GetFields(), "Failed to marshal link")
		}
		h.Links = append(h.Links, jsonLink)
	}
	return json.Marshal((*housecream)(h))
}

func (h *Housecream) Init() error {
	for _, data := range h.Links {
		link, err := channels.LinkFromJson(data)
		if err != nil {
			return errs.WithE(err, "Failed to load link")
		}
		print(">>>" + link.GetName())
		h.typedLinks = append(h.typedLinks, link)
	}
	return nil
}

const prom = `global:
  evaluation_interval: 1m
  scrape_interval:     1m
  scrape_timeout:      10s


scrape_configs:
- job_name: 'prometheus'`

func (h *Housecream) Start(home HomeFolder) {
	server := NewGrafanaServer(&home)
	go server.Start()

	promql.StalenessDelta = 30 * time.Minute

	// TODO
	logs.SetLevel(logs.DEBUG)

	if err := ioutil.WriteFile(home.Path+"/prometheus.yml", []byte(prom), 0644); err != nil {
		logs.WithE(err).Fatal("Failed to write prometheus.yml")
	}

	prometheus.ConfigFile(home.Path + "/prometheus.yml")
	prometheus.Parse([]string{"--storage.local.path=" + home.Path + "/prometheus"})

	localStorage := prometheus.LocalStorage()

	go prometheus.Main(localStorage)

	h.shutdownCtx, h.shutdownFunc = context.WithCancel(context.Background())
	if err := home.SaveVersion(&h.buildVersion); err != nil {
		logs.WithE(err).Fatal("Cannot save version")
	}

	eventsIn := make(chan channels.Event)
	go h.eventsToPrometheus(eventsIn, localStorage)

	for _, link := range h.typedLinks {
		logs.WithFields(link.GetFields()).Info("LOADING link")
		lifeCycle := NewLinkLifecycle(link)
		go lifeCycle.Start(eventsIn)
	}

	////go c.Watch(background, eventsIn)
	//if err := c.Trigger(background, channels.Action{}); err != nil {
	//	logs.WithE(err).Warn("failed to trigger")
	//}

	//home.SaveConfig(h)

	for _, channel := range channels.ChannelByIds {
		fmt.Println("->", channel)
	}
	//housecream.StartEngine()
}

func (h *Housecream) eventsToPrometheus(eventsIn <-chan channels.Event, localStorage local.Storage) {
	for {
		select {
		case event := <-eventsIn:
			logs.WithField("event", event).Debug("Receiving an event")

			//value := reflect.ValueOf(event.Value).Float()
			//fmt.Printf("%T %d", v, v)

			//value, ok := event.Value.(float64)
			//if ok {

			var value float64
			rv := reflect.ValueOf(event.Value)
			switch rv.Kind() {
			case reflect.Int, reflect.Int8, reflect.Int16, reflect.Int32, reflect.Int64:
				value = (float64(rv.Int()))
			case reflect.Uint, reflect.Uintptr, reflect.Uint8, reflect.Uint16, reflect.Uint32, reflect.Uint64:
				value = (float64(rv.Uint()))
			case reflect.Float32, reflect.Float64:
				value = rv.Float()
				//default:
				//	return "fmtNumber/fmtDecimal expects a numerical value to format"
			}

			sample := &model.Sample{
				Metric: model.Metric{
					model.MetricNameLabel: model.LabelValue("housecream_link_" + event.LinkName + "_" + event.PointName),
					"link":                model.LabelValue(event.LinkName),
				},
				Timestamp: model.TimeFromUnixNano(event.Time.UnixNano()),
				Value:     model.SampleValue(value),
			}
			localStorage.Append(sample)
			logs.WithField("event", event).Debug("append metric")
			//}
		}
	}
}

func (h Housecream) Stop() {
	h.shutdownFunc()
}
