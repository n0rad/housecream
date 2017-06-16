package main

import (
	"testing"
	"time"
)

func TestSomething(t *testing.T) {
	if testing.Short() {
		t.Skip("short run")
	}
	println("Running long test")
	time.Sleep(10 * time.Second)

	//net/http/httptest

	//main();
	//setupDatabase()
	//result := m.Run()
	//teardownDatabase()
	//os.Exit(result)
}
