package channels

import "time"

type Event struct {
	LinkName  string
	PointName string
	Time      time.Time    // optional, will be set to now() if omitted
	Metas     Placeholders // headers
	Value     interface{}  // body
}

type Placeholders []Placeholder

type Placeholder struct {
	Name string
	//Type  string
	Value interface{}
}

type Action struct {
}
