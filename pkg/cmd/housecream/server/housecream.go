package server

import (
	"fmt"

	"github.com/n0rad/go-erlog/logs"
	_ "github.com/n0rad/go-erlog/register"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/n0rad/housecream/pkg/home"
	"github.com/n0rad/housecream/pkg/storage"
	"github.com/n0rad/housecream/pkg/storage/prometheus"
	"github.com/n0rad/housecream/pkg/ui/grafana"

	_ "github.com/n0rad/housecream/pkg/channels/bbox"
	_ "github.com/n0rad/housecream/pkg/channels/cron"
	_ "github.com/n0rad/housecream/pkg/channels/freebox"
	_ "github.com/n0rad/housecream/pkg/channels/openweathermap"
	_ "github.com/n0rad/housecream/pkg/channels/slack"

	"math/rand"
	"runtime"
	"time"

	"github.com/n0rad/housecream/pkg/api"
)

type Housecream struct {
	BuildVersion       string
	BuildTime          string
	BuildCommit        string
	BuildCommitMessage string
	LogLevel           string
	HomePath           string

	api api.Api
}

func (hc *Housecream) Start() (ok bool) {
	rand.Seed(time.Now().UTC().UnixNano())

	if hc.LogLevel != "" {
		level, err := logs.ParseLevel(hc.LogLevel)
		if err != nil {
			fmt.Println("Unkown log level ", hc.LogLevel)
			return false
		}
		logs.SetLevel(level)
	}

	home, err := home.NewHome(hc.HomePath)
	if err != nil {
		logs.WithE(err).Fatal("Failed to load home directory")
	}

	eventsIn := make(chan channels.Event)

	// grafana
	grafana := grafana.NewGrafanaServer(home.Path + "/grafana")
	go grafana.Start()

	// prometheus
	if err := prom.Start(home.Path + "/prometheus"); err != nil {
		logs.WithE(err).Fatal("Failed to start prometheus")
	}

	// storage
	storage := storage.Storage{}
	storage.Init(eventsIn, prom.LocalStorage)
	go storage.Start()

	// links
	cfg, err := home.LoadConfig()
	if err != nil {
		logs.WithE(err).Fatal("Failed to start housecream")
	}
	if err := cfg.Channels.Init(); err != nil {
		logs.WithE(err).Fatal("Cannot start links")
	}
	cfg.Channels.Start(eventsIn)

	// api
	hc.api = api.Api{}
	hc.api.Init(&cfg.Channels)
	hc.api.Start()

	logs.WithField("home", hc.HomePath).Info("Housecream is ready !")
	return true
}

func (hc *Housecream) PrintVersion() {
	fmt.Printf("Housecream\n")
	fmt.Printf("Version : %s\n", hc.BuildVersion)
	fmt.Printf("Build Time : %s\n", hc.BuildTime)
	fmt.Printf("Commit : %s\n", hc.BuildCommit)
	fmt.Printf("Commit Message  : %s\n", hc.BuildCommitMessage)
	fmt.Printf("Go Version: %s\n", runtime.Version())
	fmt.Printf("Go OS/Arch: %s/%s\n", runtime.GOOS, runtime.GOARCH)
}

func (hc *Housecream) Stop() (ok bool) {
	logs.Info("Stopping Housecream !")

	hc.api.Stop()
	return true
}

func (hc *Housecream) Reload() {
	logs.Error("Reload is not implemented, yet")
}
