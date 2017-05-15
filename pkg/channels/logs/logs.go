package logs

import (
	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/channels"
)

type Logs struct {
	channels.CommonLink
	Level string

	level logs.Level
}

func (l *Logs) Init() error {
	l.level = logs.INFO
	if l.Level != "" {
		lvl, err := logs.ParseLevel(l.Level)
		if err != nil {
			return errs.WithEF(err, data.WithField("level", l.Level), "invalid log level")
		}
		l.level = lvl
	}
	return nil
}

func (l *Logs) Trigger() {
	logs.LogEntry(logs.Entry{
		Logger:  logs.GetDefaultLog(),
		Level:   l.level,
		Message: "Event received", //TODO templating
	})
}

type LogsChannel struct {
	channels.CommonChannel
}

func init() {
	f := LogsChannel{CommonChannel: channels.CommonChannel{
		Id:          "logs",
		Name:        "Logs",
		Description: "Write to logs",
	}}
	channels.RegisterChannel(&f)
}
