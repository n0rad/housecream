package home

import (
	"os"

	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/housecream/pkg/api"
	"github.com/n0rad/housecream/pkg/channels"
	"github.com/n0rad/housecream/pkg/util"
)

type BuildVersion struct {
	BuildVersion  string
	BuildTime     string
	CommitId      string
	CommitMessage string
}

const pathConfig = "/config.yml"
const versionPath = "/version.yml"

type HomeFolder struct {
	fields data.Fields
	Path   string
}

func NewHome(path string) (*HomeFolder, error) {
	fields := data.WithField("path", path)
	if err := os.MkdirAll(path, os.ModePerm); err != nil {
		return nil, errs.WithEF(err, fields, "Failed to create home directory")
	}

	return &HomeFolder{
		Path: path,
	}, nil
}

func (h HomeFolder) SaveVersion(version *BuildVersion) error {
	versionPath := h.Path + versionPath
	if err := util.WriteYamlStruct(version, versionPath); err != nil {
		return errs.WithE(err, "Failed to write version")
	}
	return nil
}

func (h HomeFolder) LoadVersion(version *BuildVersion) error {
	versionPath := h.Path + versionPath
	if err := util.ReadYamlStruct(version, versionPath); err != nil {
		return errs.WithE(err, "Failed to read version")
	}
	return nil
}

//////////////////////

type config Config

type Config struct {
	Api      api.Api
	Channels channels.Channels
}

func (h HomeFolder) SaveConfig(cfg *Config) error {
	configPath := h.Path + pathConfig
	if err := util.WriteYamlStruct(cfg, configPath); err != nil {
		return errs.WithE(err, "Failed to write configuration")
	}
	return nil
}

func (h *HomeFolder) LoadConfig() (*Config, error) {
	configPath := h.Path + pathConfig
	cfg := Config{}
	if err := util.ReadYamlStruct(&cfg, configPath); err != nil {
		return nil, errs.WithE(err, "Failed to read configuration")
	}
	return &cfg, nil
}
