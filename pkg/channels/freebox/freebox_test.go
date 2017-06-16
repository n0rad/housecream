package freebox

import (
	"fmt"
	"testing"
	"time"

	"io/ioutil"
	"log"
	"net/http"
	"net/http/httptest"

	"github.com/Sirupsen/logrus"
	_ "github.com/n0rad/go-erlog/register"
	"github.com/n0rad/housecream/pkg/channels"
)

func ExampleRealFreebox() {
	logrus.SetLevel(logrus.DebugLevel)
	link := FreeboxLink{}
	//TODO cleanup token
	link.Token = "oZ0TsQSB5vrNkPZpka1ujLVjSJkyqxHQotRfp6xT0lV+aylvE6WnvCRjoq+zgRKJ"
	link.Host = "192.168.40.1"

	link.Init()

	eventsIn := make(chan channels.Event, 100)
	shutdown := make(chan struct{})

	go link.Watch(shutdown, eventsIn)

	select {
	case v := <-eventsIn:
		fmt.Println(">>>>", v)
	case <-time.After(10 * time.Second):
	}
}

func Test2(t *testing.T) {
	ts := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		println(">>" + r.RequestURI)
		if r.RequestURI == "/api_version" {
			fmt.Fprintln(w, `{"api_domain":"o1n9dsab.fbxos.fr","uid":"68993098e8902862f0fb282f010ea2dc","https_available":true,"https_port":22885,"device_name":"Freebox Server","api_version":"4.0","api_base_url":"\/api\/","device_type":"FreeboxServer1,1"}`)
		}
		if r.RequestURI == "/api/v4/login" {
			fmt.Fprintln(w, `{"success":true,"result":{"logged_in":false,"challenge":"AUMJ2b7Ej5nH\/iBoVNKCtDZol5vBcaCn","password_salt":"6q11Z0IbtjQ+b27UxXpEyuX9E4nOpRr0"}}`)
		}
		if r.RequestURI == "/api/v4/login/session/" {
			// payload={"app_id":"go-freebox","app_version":"0.1.0","password":"96752fe9e0927b923e7de6b391d65e9d26f42bc2"}"
			fmt.Fprintln(w, `{"result":{"session_token":"ZQRJKuTp\/quOhyXy\/iq2ek0wET7S\/vy29YfqBG7qjBCFiOJnrHEpFv2YMRfgvSNw","challenge":"AUMJ2b7Ej5nH\/iBoVNKCtDZol5vBcaCn","password_salt":"6q11Z0IbtjQ+b27UxXpEyuX9E4nOpRr0","permissions":{"parental":false,"explorer":true,"contacts":true,"downloader":true,"settings":false,"calls":true,"___home":true,"pvr":true,"tv":true}},"success":true}`)
		}
		if r.RequestURI == "/api/v4/connection" {
			fmt.Fprintln(w, `{"success":true,"result":{"type":"ethernet","rate_down":168,"bytes_up":54082660384,"ipv4_port_range":[0,65535],"rate_up":306,"bandwidth_up":20960000,"ipv6":"2a01:e34:ee91:cdb0::1","bandwidth_down":78000000,"media":"xdsl","state":"up","bytes_down":93340714281,"ipv4":"78.233.28.219"}}`)
		}
	}))
	defer ts.Close()

	println(">><<" + ts.URL)

	logrus.SetLevel(logrus.DebugLevel)
	link := FreeboxLink{}
	//TODO cleanup token
	link.Token = "oZ0TsQSB5vrNkPZpka1ujLVjSJkyqxHQotRfp6xT0lV+aylvE6WnvCRjoq+zgRKJ"
	link.Host = ts.URL

	link.Init()

	eventsIn := make(chan channels.Event, 100)
	shutdown := make(chan struct{})

	go link.Watch(shutdown, eventsIn)

	select {
	case v := <-eventsIn:
		fmt.Println(">>>>", v)
	case <-time.After(10 * time.Second):
		t.Fail()
	}
}

func ExampleServer() {
	ts := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintln(w, "Hello, client")
	}))
	defer ts.Close()

	res, err := http.Get(ts.URL)
	if err != nil {
		log.Fatal(err)
	}
	greeting, err := ioutil.ReadAll(res.Body)
	res.Body.Close()
	if err != nil {
		log.Fatal(err)
	}

	fmt.Printf("%s", greeting)
	// Output: Hello, client
}
