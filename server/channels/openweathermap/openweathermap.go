package openweathermap

import (
	"time"

	"os"

	owm "github.com/briandowns/openweathermap"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/server/channels"
)

type OpenWeatherMapLink struct {
	channels.CommonLink
	ApiKey    string
	Unit      string
	Latitude  float64
	Longitude float64
	Interval  time.Duration
}

func (o *OpenWeatherMapLink) Init() error {
	if o.Unit == "" {
		o.Unit = "C"
	}
	if o.Interval == 0 {
		o.Interval = 10 * time.Minute
	}
	if o.ApiKey == "" {
		return errs.WithF(o.Fields, "Api key is mandatory")
	}
	os.Setenv("OWM_API_KEY", o.ApiKey)
	return nil
}

func (s *OpenWeatherMapLink) currentWeather() (*owm.CurrentWeatherData, error) {
	w, err := owm.NewCurrent(s.Unit, "en")
	if err != nil {
		return nil, errs.WithEF(err, s.Fields, "Failed to init current weather")
	}

	if err := w.CurrentByCoordinates(&owm.Coordinates{Latitude: s.Latitude, Longitude: s.Longitude}); err != nil {
		return nil, errs.WithEF(err, s.Fields.WithField("latitude", s.Latitude).
			WithField("longitude", s.Longitude), "Failed to load current weather")
	}
	return w, nil
}

func (s *OpenWeatherMapLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	s.handleWatch(events)
	for {
		select {
		case <-shutdown:
			return
		case <-time.After(s.Interval):
			s.handleWatch(events)
		}
	}
}

func (l *OpenWeatherMapLink) handleWatch(events chan<- channels.Event) {
	w, err := l.currentWeather()
	if err != nil {
		logs.WithEF(err, l.Fields).Warn("Failed to load current weather")
	}

	events <- l.newEvent(w.Dt, "clouds", w.Clouds.All)
	events <- l.newEvent(w.Dt, "temperature", w.Main.Temp)
	events <- l.newEvent(w.Dt, "temperature_max", w.Main.TempMax)
	events <- l.newEvent(w.Dt, "temperature_min", w.Main.TempMin)
	events <- l.newEvent(w.Dt, "pressure", w.Main.Pressure)
	events <- l.newEvent(w.Dt, "humidity", w.Main.Humidity)

	// TODO if > 0
	events <- l.newEvent(w.Dt, "sea_level", w.Main.SeaLevel)
	events <- l.newEvent(w.Dt, "ground_level", w.Main.GrndLevel)

	events <- l.newEvent(w.Dt, "wind_degree", w.Wind.Deg)
	events <- l.newEvent(w.Dt, "wind_speed", w.Wind.Speed)

	events <- l.newEvent(w.Dt, "sunrise", w.Sys.Sunrise)
	events <- l.newEvent(w.Dt, "sunset", w.Sys.Sunset)

	//TODO
	// VISIBILITY
	// rain
	// snow
	//events <- s.newEvent(w.Dt, "weather", float64(w.Weather))

}

func (s *OpenWeatherMapLink) newEvent(date int, pointName string, value interface{}) channels.Event {
	return channels.Event{
		LinkName:  s.Name,
		PointName: pointName,
		Time:      time.Unix(int64(date), 0),
		Value:     value,
	}
}

/////////////////////////////////

type SlackChannel struct {
	channels.CommonChannel
}

func init() {
	f := SlackChannel{channels.CommonChannel{
		Id:          "openweathermap",
		Name:        "Open Weather Map",
		Description: "get weather",
	}}
	channels.RegisterChannel(&f)
}
