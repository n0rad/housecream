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

	"github.com/mitchellh/go-homedir"
	"github.com/n0rad/go-erlog/logs"
	_ "github.com/n0rad/go-erlog/register"
	_ "github.com/n0rad/housecream/server/channels/timer"
	"github.com/n0rad/housecream/server/housecream"
	"github.com/spf13/cobra"
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
	var home string

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

			//Housecream.loadHome()
			h := housecream.Housecream{
				BuildVersion: BuildVersion,
				BuildCommit:  BuildCommit,
				BuildTime:    BuildTime,
			}
			go h.Start()
			defer h.Stop()
			waitForSignal()
		},
	}

	home, err := homedir.Dir()
	if err != nil {
		logs.Fatal("Cannot found home directly, please specify a home folder with -H")
	}

	rootCmd.PersistentFlags().StringVarP(&logLevel, "log-level", "L", "", "Set log level")
	rootCmd.PersistentFlags().BoolVarP(&version, "version", "V", false, "Display version")
	rootCmd.PersistentFlags().StringVarP(&home, "home", "H", home+"/.config/housecream", "Home folder")

	if err := rootCmd.Execute(); err != nil {
		logs.WithE(err).Fatal("Failed to process args")
	}

}
