package storage

import (
	"reflect"

	"fmt"

	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/prometheus/common/model"
	"github.com/prometheus/prometheus/storage/local"
)

type Storage struct {
	localStorage local.Storage
	eventsIn     <-chan channels.Event
}

func (s *Storage) Init(eventsIn <-chan channels.Event, storage local.Storage) {
	s.localStorage = storage
	s.eventsIn = eventsIn
}

func (s *Storage) Start() {
	s.eventsToPrometheus()
}

func (s *Storage) getValue() {
	//q, err := s.localStorage.Querier()
	//q.QueryInstant()
}

func (s *Storage) eventsToPrometheus() {
	for {
		select {
		case event := <-s.eventsIn:
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

			m := model.Metric{
				model.MetricNameLabel: model.LabelValue("housecream_link_" + event.LinkName + "_" + event.PointName),
				"link":                model.LabelValue(event.LinkName),
			}
			for _, meta := range event.Metas {
				if meta.Name == "link" {
					meta.Name = "exported_link"
				}
				m[model.LabelName(meta.Name)] = model.LabelValue(fmt.Sprint(meta.Value))
			}

			sample := &model.Sample{
				Metric:    m,
				Timestamp: model.TimeFromUnixNano(event.Time.UnixNano()),
				Value:     model.SampleValue(value),
			}
			s.localStorage.Append(sample)
			logs.WithField("event", event).Debug("append metric")
			//}
		}
	}
}
