package bbox

import (
	"net/http"
	"fmt"
	"io/ioutil"
	"encoding/json"
	"github.com/n0rad/go-erlog/errs"
	"strconv"
)

type StringInt64 int64

func (s *StringInt64) UnmarshalJSON(data []byte) error {
	var tmp int64
	if err := json.Unmarshal(data, &tmp); err != nil {
		var str string
		if err := json.Unmarshal(data, &str); err != nil {
			return err
		}
		if str == "" {
			*s = StringInt64(0)
			return nil
		} else {
			nn, err := strconv.ParseInt(str, 10, 64)
			if err != nil {
				return err
			}
			*s = StringInt64(nn)
		}
		return nil
	}
	*s = StringInt64(tmp)
	return nil
}

type DeviceInfo struct {
	Uptime        int64 `json:"uptime"`
	NumberOfBoots int `json:"numberofboots"`
	Status        int `json:"status"`
}

type IpStat struct {
	Packets         uint64  `json:"packets"`
	Bytes           StringInt64  `json:"bytes"`
	Packetserrors   int  `json:"packetserrors"`
	Packetsdiscards int  `json:"packetsdiscards"`
	Occupation      int  `json:"occupation"`
	Bandwidth       int  `json:"bandwidth"`
	MaxBandwidth    int  `json:"maxBandwidth"`
}

type WanIpStats struct {
	Rx IpStat  `json:"rx"`
	Tx IpStat  `json:"tx"`
}

type WanIp struct {
	Internet struct {
			 State int `json:"State"`
		 } `json:"internet"`
}

type WanXdsl struct {
	//State  "Connected"
	SyncCount int `json:"sync_count"`
	Showtime  StringInt64 `json:"showtime"`
	Up        WanXdslStat `json:"up"`
	Down      WanXdslStat `json:"down"`
}

type WanXdslStat struct {
	Bitrates        int `json:"bitrates"`
	Noise           int `json:"noise"`
	Attenuation     int `json:"attenuation"`
	Power           StringInt64 `json:"power"`
	Phyr            int `json:"phyr"`
	Ginp            int `json:"ginp"`
	Nitro           StringInt64 `json:"nitro"`
	InterleaveDelay int `json:"interleave_delay"`
}

///////////

const urlPath = "/api/v1"

type Bbox struct {
	Host   string

	client *http.Client
}

func New() *Bbox {
	return &Bbox{
		Host: "gestionbbox.lan",
		client: &http.Client{},
	}
}

func (c *Bbox) GetDevice() (*DeviceInfo, error) {
	body, err := c.GetResource("/device")
	if err != nil {
		return nil, err
	}

	var response []struct {
		Device DeviceInfo `json:"device"`
	}
	err = json.Unmarshal(body, &response)
	if err != nil {
		return nil, err
	}

	if len(response) < 1 {
		return nil, errs.With("One device object expected")
	}
	return &response[0].Device, nil
}

func (c *Bbox) GetWanIp() (*WanIp, error) {
	body, err := c.GetResource("/wan/ip")
	if err != nil {
		return nil, err
	}

	var response []struct {
		Wan WanIp `json:"wan"`
	}
	err = json.Unmarshal(body, &response)
	if err != nil {
		return nil, err
	}

	if len(response) < 1 {
		return nil, errs.With("One device object expected")
	}
	return &response[0].Wan, nil
}

func (c *Bbox) GetWanXdsl() (*WanXdsl, error) {
	body, err := c.GetResource("/wan/xdsl")
	if err != nil {
		return nil, err
	}

	var response []struct {
		Wan struct {
			    Xdsl WanXdsl `json:"xdsl"`
		    } `json:"wan"`
	}
	err = json.Unmarshal(body, &response)
	if err != nil {
		return nil, err
	}

	if len(response) < 1 {
		return nil, errs.With("One device object expected")
	}
	return &response[0].Wan.Xdsl, nil
}

func (c *Bbox) GetWanIpStats() (*WanIpStats, error) {
	body, err := c.GetResource("/wan/ip/stats")
	if err != nil {
		return nil, err
	}

	var response []struct {
		Wan struct {
			    Ip struct {
				       Stats WanIpStats `json:"stats"`
			       } `json:"ip"`
		    } `json:"wan"`
	}
	err = json.Unmarshal(body, &response)
	if err != nil {
		return nil, err
	}

	if len(response) < 1 {
		return nil, errs.With("One device object expected")
	}
	return &response[0].Wan.Ip.Stats, nil
}

func (c *Bbox) GetResource(path string) ([]byte, error) {
	req, err := http.NewRequest("GET", "http://" + c.Host + urlPath + path, nil)
	if err != nil {
		return nil, err
	}

	resp, err := c.client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)

	if resp.StatusCode > 299 {
		return nil, fmt.Errorf("Status code: %d", resp.StatusCode)
	}

	return body, err
}

