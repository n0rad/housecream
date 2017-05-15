package main

import (
	"fmt"
	"io/ioutil"
	"math/rand"
	"os"
	"os/signal"
	"runtime"
	"strconv"
	"syscall"
	"time"

	_ "github.com/n0rad/housecream/pkg/channels/bbox"
	_ "github.com/n0rad/housecream/pkg/channels/cron"
	_ "github.com/n0rad/housecream/pkg/channels/freebox"
	_ "github.com/n0rad/housecream/pkg/channels/openweathermap"
	_ "github.com/n0rad/housecream/pkg/channels/slack"
	"github.com/spf13/cobra"

	"github.com/mitchellh/go-homedir"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	_ "github.com/n0rad/go-erlog/register"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/n0rad/housecream/pkg/home"
	"github.com/n0rad/housecream/pkg/storage"
	"github.com/n0rad/housecream/pkg/storage/prometheus"
	"github.com/n0rad/housecream/pkg/ui/grafana"
)

var BuildVersion = ""
var BuildTime = ""
var BuildCommit = ""

var logLevel string
var version bool
var homePath string

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	sigQuitThreadDump()

	rootCmd := &cobra.Command{
		Use: "housecream",
		PersistentPreRun: func(cmd *cobra.Command, args []string) {
			if version {
				fmt.Println("Housecream")
				fmt.Println("Version :", BuildVersion)
				fmt.Println("Build Time :", BuildTime)
				fmt.Println("Build Commit :", BuildCommit)
				os.Exit(0)
			}

			if logLevel != "" {
				level, err := logs.ParseLevel(logLevel)
				if err != nil {
					logs.WithField("value", logLevel).Fatal("Unknown log level")
				}
				logs.SetLevel(level)
			}
		},
		Run: func(cmd *cobra.Command, args []string) {
			if len(args) != 0 {
				logs.Fatal("Housecrean has no argument")
			}
			run()
		},
	}

	rootCmd.PersistentFlags().StringVarP(&logLevel, "log-level", "L", "", "Set log level")
	rootCmd.PersistentFlags().BoolVarP(&version, "version", "V", false, "Display version")
	rootCmd.PersistentFlags().StringVarP(&homePath, "home", "H", DefaultHomeFolder(), "Home folder")

	if err := rootCmd.Execute(); err != nil {
		logs.WithE(err).Fatal("Failed to process args")
	}

}

func waitForSignal() {
	sigs := make(chan os.Signal, 1)
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGKILL, syscall.SIGTERM)
	<-sigs
	logs.Debug("Stop signal received")
}

func sigQuitThreadDump() {
	sigChan := make(chan os.Signal)
	go func() {
		for range sigChan {
			stacktrace := make([]byte, 10<<10)
			length := runtime.Stack(stacktrace, true)
			fmt.Println(string(stacktrace[:length]))

			ioutil.WriteFile("/tmp/"+strconv.Itoa(os.Getpid())+".dump", stacktrace[:length], 0644)
		}
	}()
	signal.Notify(sigChan, syscall.SIGQUIT)
}

func run() {
	home, err := home.NewHome(homePath)
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
		logs.WithE(err).Fatal("Cannot start lings")
	}
	cfg.Channels.Start(eventsIn)

	////TODO SIGHUP restart
	waitForSignal()
}

func DefaultHomeFolder() string {
	homeDir, err := homedir.Dir()
	if err != nil {
		home2, err2 := ioutil.TempDir(os.TempDir(), "housecream")
		if err2 != nil {
			fullErr := errs.WithE(err2, "Cannot create temp directory")
			logs.WithE(fullErr).Warn("Cannot prepare default home folder. please specify home folder")
			return ""
		}
		homeDir = home2
		logs.WithField("folder", homeDir).Warn("Cannot found default home directly, using temp folder")
		return homeDir
	}
	return homeDir + "/.config/housecream"
}
