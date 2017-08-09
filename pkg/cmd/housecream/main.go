package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"os/signal"
	"path/filepath"
	"runtime"
	"strconv"
	"syscall"

	"github.com/mitchellh/go-homedir"
	"github.com/n0rad/housecream/pkg/cmd/housecream/server"
	"github.com/spf13/cobra"
)

var buildVersion = ""
var buildTime = ""
var buildCommit = ""
var buildCommitMessage = ""

func main() {
	hc := server.Housecream{
		BuildVersion:       buildVersion,
		BuildTime:          buildTime,
		BuildCommit:        buildCommit,
		BuildCommitMessage: buildCommitMessage,
	}

	var version bool
	var logLevel string

	rootCmd := &cobra.Command{
		Use: "housecream",
		PersistentPreRun: func(cmd *cobra.Command, args []string) {
			if version {
				hc.PrintVersion()
				os.Exit(0)
			}
		},

		Run: func(cmd *cobra.Command, args []string) {
			if len(args) != 0 {
				println("Housecrean has no argument")
				os.Exit(1)
			}

			sigQuitRoutingDump()

			if ok := hc.Start(); !ok {
				os.Exit(1)
			}

			hup := make(chan os.Signal)
			signal.Notify(hup, syscall.SIGHUP)
			go func() {
				for {
					select {
					case <-hup:
						hc.Reload()
					}
				}
			}()

			sigs := make(chan os.Signal, 1)
			signal.Notify(sigs, syscall.SIGINT, syscall.SIGKILL, syscall.SIGTERM)
			<-sigs

			if ok := hc.Stop(); !ok {
				os.Exit(1)
			}
		},
	}
	rootCmd.PersistentFlags().StringVarP(&logLevel, "log-level", "L", "", "Set log level")
	rootCmd.PersistentFlags().BoolVarP(&version, "version", "V", false, "Display version")
	rootCmd.PersistentFlags().StringVarP(&hc.HomePath, "home", "H", DefaultHomeFolder(), "Home folder")

	if err := rootCmd.Execute(); err != nil {
		fmt.Errorf("ERROR: Failed to process args %v", err)
		os.Exit(1)
	}
}

func DefaultHomeFolder() string {
	homeDir, err := homedir.Dir()
	if err != nil {
		fmt.Errorf("WARN : Cannot find home folder of current user, %v", err)
		home2, err2 := ioutil.TempDir(os.TempDir(), "housecream")
		if err2 != nil {
			fmt.Errorf("ERROR: Cannot create temp directory for home folder, %v", err2)
			return ""
		}
		homeDir = home2
		fmt.Errorf("WARN : Using temp directory %s", homeDir)
		return homeDir
	}
	return homeDir + "/.config/housecream"
}

func sigQuitRoutingDump() {
	sigChan := make(chan os.Signal)
	go func() {
		for range sigChan {
			stacktrace := make([]byte, 1<<20)
			length := runtime.Stack(stacktrace, true)
			fmt.Println(string(stacktrace[:length]))

			file := filepath.Join(os.TempDir(), strconv.Itoa(os.Getpid())+".dump")
			ioutil.WriteFile(file, stacktrace[:length], 0644)
			fmt.Println("Routines dumped to " + file)
		}
	}()
	signal.Notify(sigChan, syscall.SIGQUIT)
}
