package server

import (
	"flag"
	"os"

	_ "github.com/grafana/grafana/pkg/services/alerting/conditions"
	_ "github.com/grafana/grafana/pkg/services/alerting/notifiers"
	_ "github.com/grafana/grafana/pkg/tsdb/prometheus"

	"github.com/go-ini/ini"
	"github.com/grafana/grafana/pkg/api"
	"github.com/grafana/grafana/pkg/log"
	"github.com/grafana/grafana/pkg/login"
	"github.com/grafana/grafana/pkg/metrics"
	"github.com/grafana/grafana/pkg/models"
	"github.com/grafana/grafana/pkg/plugins"
	"github.com/grafana/grafana/pkg/services/alerting"
	"github.com/grafana/grafana/pkg/services/cleanup"
	"github.com/grafana/grafana/pkg/services/eventpublisher"
	"github.com/grafana/grafana/pkg/services/notifications"
	"github.com/grafana/grafana/pkg/services/search"
	"github.com/grafana/grafana/pkg/services/sqlstore"
	"github.com/grafana/grafana/pkg/setting"
	"github.com/grafana/grafana/pkg/social"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"golang.org/x/net/context"
	"golang.org/x/sync/errgroup"
)

type GrafanaServerImpl struct {
	context       context.Context
	shutdownFn    context.CancelFunc
	childRoutines *errgroup.Group
	log           log.Logger
	HomeFolder    *HomeFolder

	httpServer *api.HttpServer
}

func NewGrafanaServer(home *HomeFolder) models.GrafanaServer {
	rootCtx, shutdownFn := context.WithCancel(context.Background())
	childRoutines, childCtx := errgroup.WithContext(rootCtx)

	return &GrafanaServerImpl{
		context:       childCtx,
		shutdownFn:    shutdownFn,
		childRoutines: childRoutines,
		log:           log.New("server"),
		HomeFolder:    home,
	}
}

func (g *GrafanaServerImpl) Start() {
	if err := writeConfiguration(g.HomeFolder); err != nil {
		logs.WithE(err).Fatal("Failed to write configuration")
	}

	err := setting.NewConfigContext(&setting.CommandLineArgs{
		Config:   g.HomeFolder.Path + "/grafana/config.ini",
		HomePath: "",
		Args:     flag.Args(),
	})
	if err != nil {
		logs.WithE(err).Fatal("Failed to create grafana settings")
	}
	setting.LogConfigurationInfo()

	// init sql
	sqlstore.NewEngine()
	sqlstore.EnsureAdminUser()

	metrics.Init()
	search.Init()
	login.Init()
	social.NewOAuthService()
	eventpublisher.Init()
	plugins.Init()

	// init alerting
	if setting.AlertingEnabled && setting.ExecuteAlerts {
		engine := alerting.NewEngine()
		g.childRoutines.Go(func() error {
			return engine.Run(g.context)
		})
	}

	// cleanup service
	cleanUpService := cleanup.NewCleanUpService()
	g.childRoutines.Go(func() error {
		return cleanUpService.Run(g.context)
	})

	if err := notifications.Init(); err != nil {
		g.log.Error("Notification service failed to initialize", "erro", err)
		g.Shutdown(1, "Startup failed")
		return
	}

	g.startHttpServer()
}

func (g *GrafanaServerImpl) startHttpServer() {
	g.httpServer = api.NewHttpServer()

	err := g.httpServer.Start(g.context)

	if err != nil {
		g.log.Error("Fail to start server", "error", err)
		g.Shutdown(1, "Startup failed")
		return
	}
}

func (g *GrafanaServerImpl) Shutdown(code int, reason string) {
	g.log.Info("Shutdown started", "code", code, "reason", reason)

	err := g.httpServer.Shutdown(g.context)
	if err != nil {
		g.log.Error("Failed to shutdown server", "error", err)
	}

	g.shutdownFn()
	err = g.childRoutines.Wait()

	g.log.Info("Shutdown completed", "reason", err)
	log.Close()
	os.Exit(code)
}

func writeConfiguration(home *HomeFolder) error {
	if err := os.MkdirAll(home.Path+"/grafana", 0755); err != nil {
		return errs.WithE(err, "Failed to create grafana directory")
	}

	config := ini.Empty()
	config.BlockMode = false
	paths, _ := config.NewSection("paths")
	paths.NewKey("data", home.Path+"/grafana")
	paths.NewKey("logs", home.Path+"/grafana/logs")
	paths.NewKey("plugins", home.Path+"/grafana/plugins")

	f, err := os.Create(home.Path + "/grafana/config.ini")
	if err != nil {
		return errs.WithE(err, "Failed to create grafana config file")
	}
	defer f.Close()
	config.WriteTo(f)

	return nil
}
