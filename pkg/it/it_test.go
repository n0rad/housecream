package it

import (
	"io/ioutil"
	"os"
	"testing"

	"fmt"

	"time"

	"github.com/n0rad/go-erlog/logs"
	"github.com/n0rad/housecream/pkg/cmd/housecream/server"
)

func TestMain(m *testing.M) {
	tearDown := HousecreamTestSetup()
	retCode := m.Run()
	tearDown()
	os.Exit(retCode)
}

func setupIT(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping test in short mode.")
	}
}

func HousecreamTestSetup() func() {
	logs.Info("Running Setup")
	tmpDir, err := ioutil.TempDir(os.TempDir(), "housecream-test-")
	if err != nil {
		panic(err)
	}

	hc := server.Housecream{HomePath: tmpDir}
	if ok := hc.Start(); !ok {
		fmt.Errorf("Housecream cannot start")
		os.Exit(1)
	}

	time.Sleep(0 * time.Second) // TODO start should return only when ready

	return func() {
		logs.Info("Running TearDown")
		if ok := hc.Stop(); !ok {
			fmt.Errorf("Housecream cannot stop correctly")
			os.Exit(1)
		}
		os.RemoveAll(tmpDir)
	}
}
