package openweathermap

import (
	"time"

	"os"

	owm "github.com/briandowns/openweathermap"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
)

type OpenWeatherMapLink struct {
	channels.CommonLink
	ApiKey    string
	Unit      string
	Latitude  float64
	Longitude float64
	Interval  time.Duration
}

func (l *OpenWeatherMapLink) Init() error {
	if l.Unit == "" {
		l.Unit = "C"
	}
	if l.Interval == 0 {
		l.Interval = 10 * time.Minute
	}
	if l.ApiKey == "" {
		return errs.WithF(l.GetFields(), "Api key is mandatory")
	}
	os.Setenv("OWM_API_KEY", l.ApiKey)
	return nil
}

func (l *OpenWeatherMapLink) currentWeather() (*owm.CurrentWeatherData, error) {
	w, err := owm.NewCurrent(l.Unit, "en")
	if err != nil {
		return nil, errs.WithEF(err, l.GetFields(), "Failed to init current weather")
	}

	if err := w.CurrentByCoordinates(&owm.Coordinates{Latitude: l.Latitude, Longitude: l.Longitude}); err != nil {
		return nil, errs.WithEF(err, l.GetFields().WithField("latitude", l.Latitude).
			WithField("longitude", l.Longitude), "Failed to load current weather")
	}
	return w, nil
}

func (l *OpenWeatherMapLink) Watch(shutdown <-chan struct{}, events chan<- channels.Event) {
	l.handleWatch(events)
	for {
		select {
		case <-shutdown:
			return
		case <-time.After(l.Interval):
			l.handleWatch(events)
		}
	}
}

func (l *OpenWeatherMapLink) handleWatch(events chan<- channels.Event) {
	w, err := l.currentWeather()
	if err != nil {
		logs.WithEF(err, l.GetFields()).Warn("Failed to load current weather")
	}

	events <- l.newEvent(w.Dt, "clouds", w.Clouds.All)
	events <- l.newEvent(w.Dt, "temperature", w.Main.Temp)
	//	events <- l.newEvent(w.Dt, "temperature_max", w.Main.TempMax) // this is useless
	//	events <- l.newEvent(w.Dt, "temperature_min", w.Main.TempMin) //
	events <- l.newEvent(w.Dt, "pressure", w.Main.Pressure)
	events <- l.newEvent(w.Dt, "humidity", w.Main.Humidity)

	//events <- l.newEvent(w.Dt, "sea_level", w.Main.SeaLevel) // this is useless
	//events <- l.newEvent(w.Dt, "ground_level", w.Main.GrndLevel)

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

func (l *OpenWeatherMapLink) newEvent(date int, pointName string, value interface{}) channels.Event {
	return channels.Event{
		LinkName:  l.Name,
		PointName: pointName,
		Time:      time.Unix(int64(date), 0),
		Value:     value,
	}
}

/////////////////////////////////

type SlackChannel struct {
	channels.CommonChannel
}

func (c *SlackChannel) NewLink() channels.Link {
	return &OpenWeatherMapLink{}
}

func init() {
	f := SlackChannel{channels.CommonChannel{
		Id:          "openweathermap",
		Name:        "Open Weather Map",
		Description: "get weather",
	}}
	channels.RegisterChannel(&f)
}
