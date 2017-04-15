package hc

import (
	"io/ioutil"

	"os"

	"github.com/ghodss/yaml"
	homedir "github.com/mitchellh/go-homedir"
	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
)

const pathConfig = "/config.yml"
const versionPath = "/version.yml"

type BuildVersion struct {
	BuildVersion  string
	BuildTime     string
	CommitId      string
	CommitMessage string
}

type HomeFolder struct {
	fields data.Fields
	path   string
}

func NewHome(path string) (*HomeFolder, error) {
	fields := data.WithField("path", path)
	if err := os.MkdirAll(path, os.ModePerm); err != nil {
		return nil, errs.WithEF(err, fields, "Failed to create home directory")
	}

	return &HomeFolder{
		path: path,
	}, nil
}

func (h HomeFolder) SaveVersion(version *BuildVersion) error {
	versionPath := h.path + versionPath
	if err := writeStruct(version, versionPath); err != nil {
		return errs.WithE(err, "Failed to write version")
	}
	return nil
}

func (h HomeFolder) LoadVersion(version *BuildVersion) error {
	versionPath := h.path + versionPath
	if err := readStruct(version, versionPath); err != nil {
		return errs.WithE(err, "Failed to read version")
	}
	return nil
}

func (h HomeFolder) SaveConfig(hc *Housecream) error {
	configPath := h.path + pathConfig
	if err := writeStruct(hc, configPath); err != nil {
		return errs.WithE(err, "Failed to write configuration")
	}
	return nil
}

func (h *HomeFolder) LoadConfig(hc *Housecream) error {
	configPath := h.path + pathConfig
	if err := readStruct(hc, configPath); err != nil {
		return errs.WithE(err, "Failed to read configuration")
	}
	return nil
}

func readStruct(obj interface{}, path string) error {
	file, err := ioutil.ReadFile(path)
	if err != nil {
		return errs.WithEF(err, data.WithField("file", path), "Failed to read configuration file")
	}
	if err = yaml.Unmarshal(file, obj); err != nil {
		return errs.WithEF(err, data.WithField("file", path), "Invalid configuration format")
	}
	return nil
}

func writeStruct(obj interface{}, path string) error {
	content, err := yaml.Marshal(obj)
	if err != nil {
		return err
	}
	if err = ioutil.WriteFile(path, content, 0644); err != nil {
		return errs.WithEF(err, data.WithField("path", path), "Failed to write file")
	}
	return nil
}

/////////////////////////////

func DefaultHomeFolder() string {
	home, err := homedir.Dir()
	if err != nil {
		home2, err2 := ioutil.TempDir(os.TempDir(), "housecream")
		if err2 != nil {
			fullErr := errs.WithE(err2, "Cannot create temp directory")
			logs.WithE(fullErr).Warn("Cannot prepare default home folder. please specify home folder")
			return ""
		}
		home = home2
		logs.WithField("folder", home).Warn("Cannot found default home directly, using temp folder")
		return home
	}
	return home + "/.config/housecream"
}
