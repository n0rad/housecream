package util

import (
	"net"

	"github.com/n0rad/go-erlog/errs"
)

func FreePort() (int, error) {
	addr, err := net.ResolveTCPAddr("tcp", "localhost:0")
	if err != nil {
		return 0, errs.WithE(err, "Cannot resolve tcp addr localhost to get free port")
	}

	l, err := net.ListenTCP("tcp", addr)
	if err != nil {
		return 0, errs.WithE(err, "Cannot tcp addr to get free port")
	}
	defer l.Close()
	return l.Addr().(*net.TCPAddr).Port, nil
}
