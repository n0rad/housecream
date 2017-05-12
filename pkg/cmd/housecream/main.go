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
	"github.com/n0rad/housecream/pkg/server"
	"github.com/spf13/cobra"

	"github.com/n0rad/go-erlog/logs"
	_ "github.com/n0rad/go-erlog/register"
)

var BuildVersion = ""
var BuildTime = ""
var BuildCommit = ""

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

func main() {
	rand.Seed(time.Now().UTC().UnixNano())
	sigQuitThreadDump()

	var logLevel string
	var version bool
	var homePath string

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

			home, err := server.NewHome(homePath)
			if err != nil {
				logs.WithE(err).Fatal("Failed to load home directory")
			}

			h := server.NewHousecream(server.BuildVersion{
				BuildVersion: BuildVersion,
				CommitId:     BuildCommit,
				BuildTime:    BuildTime,
			})

			if err := home.LoadConfig(h); err != nil {
				logs.WithE(err).Fatal("Failed to read configuration file")
			}

			if err := h.Init(); err != nil {
				logs.WithE(err).Fatal("Failed to init housecream")
			}

			//TODO SIGHUP restart
			go h.Start(*home)
			defer h.Stop()
			waitForSignal()
		},
	}

	rootCmd.PersistentFlags().StringVarP(&logLevel, "log-level", "L", "", "Set log level")
	rootCmd.PersistentFlags().BoolVarP(&version, "version", "V", false, "Display version")
	rootCmd.PersistentFlags().StringVarP(&homePath, "home", "H", server.DefaultHomeFolder(), "Home folder")

	if err := rootCmd.Execute(); err != nil {
		logs.WithE(err).Fatal("Failed to process args")
	}

}