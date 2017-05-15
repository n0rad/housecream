package util

import (
	"bytes"
	"os/exec"
	"strings"
	"time"

	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
)

func ExecCommandFull(cmd []string, env []string, timeoutInMilli int) error {
	command := exec.Command(cmd[0], cmd[1:]...)
	var b bytes.Buffer
	command.Stdout = &b
	command.Stderr = &b
	command.Env = env

	if err := command.Start(); err != nil {
		return errs.WithEF(err, data.WithField("cmd", cmd), "Failed to start command")
	}

	var after *errs.EntryError
	timer := time.AfterFunc(time.Duration(timeoutInMilli)*time.Millisecond, func() {
		field := data.WithField("command", strings.Join(cmd, " ")).WithField("timeout", timeoutInMilli)
		logs.WithF(field).Debug("Command timeout")
		after = errs.WithF(field, "Exec command timeout")
		command.Process.Kill()
	})

	err := command.Wait()
	timer.Stop()
	if logs.IsTraceEnabled() {
		logs.WithField("cmd", cmd).WithField("output", string(b.Bytes())).Trace("Command output")
	}
	if err != nil {
		return errs.WithEF(err, data.WithField("cmd", cmd).
			WithField("output", string(b.Bytes())), "Command failed").
			WithErr(after)
	}
	return nil
}
