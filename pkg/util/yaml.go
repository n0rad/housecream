package util

import (
	"io/ioutil"

	"github.com/ghodss/yaml"
	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
)

func ReadYamlStruct(obj interface{}, path string) error {
	file, err := ioutil.ReadFile(path)
	if err != nil {
		return errs.WithEF(err, data.WithField("file", path), "Failed to read configuration file")
	}
	if err = yaml.Unmarshal(file, obj); err != nil {
		return errs.WithEF(err, data.WithField("file", path), "Invalid configuration format")
	}
	return nil
}

func WriteYamlStruct(obj interface{}, path string) error {
	content, err := yaml.Marshal(obj)
	if err != nil {
		return err
	}
	if err = ioutil.WriteFile(path, content, 0644); err != nil {
		return errs.WithEF(err, data.WithField("path", path), "Failed to write file")
	}
	return nil
}
