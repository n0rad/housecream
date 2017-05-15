package prom

import (
	"io/ioutil"
	"os"
	"time"

	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"github.com/prometheus/common/log"
	"github.com/prometheus/prometheus/promql"
	"github.com/prometheus/prometheus/storage/local"
)

func ConfigFile(path string) {
	cfg.configFile = path
}

func startStorage() local.Storage {
	seriesStorage := local.NewMemorySeriesStorage(&cfg.storage)
	if err := seriesStorage.Start(); err != nil {
		log.Errorln("Error opening memory series storage:", err)
	}
	return seriesStorage
}

const promConfig = `global:
  evaluation_interval: 1m
  scrape_interval:     1m
  scrape_timeout:      10s


scrape_configs:
- job_name: 'prometheus'`

var LocalStorage local.Storage

func Start(dataDir string) error {
	if err := os.MkdirAll(dataDir+"/data", 0755); err != nil {
		return errs.WithE(err, "Failed to create prometheus data directory")
	}

	promql.StalenessDelta = 30 * time.Minute

	if err := ioutil.WriteFile(dataDir+"/prometheus.yml", []byte(promConfig), 0644); err != nil {
		logs.WithE(err).Fatal("Failed to write prometheus.yml")
	}

	ConfigFile(dataDir + "/prometheus.yml")
	Parse([]string{"--storage.local.path=" + dataDir + "/data"})

	LocalStorage = startStorage()

	go Main(LocalStorage)
	return nil
}
