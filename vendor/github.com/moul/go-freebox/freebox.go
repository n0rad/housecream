package freebox

import (
	"bytes"
	"crypto/hmac"
	"crypto/sha1"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"strings"
	"time"

	"github.com/Sirupsen/logrus"
)

// ApiVersion is returned by requesting `GET /api_version`
type ApiVersion struct {
	FreeboxID  string `json:"uid",omitempty`
	DeviceName string `json:"device_name",omitempty`
	Version    string `json:"api_version",omitempty`
	BaseURL    string `json:"api_base_url",omitempty`
	DeviceType string `json:"device_type",omitempty`
}

type App struct {
	Identifier string `json:"app_id",omitempty`
	Name       string `json:"app_name",omitempty`
	Version    string `json:"app_version",omitempty`
	DeviceName string `json:"device_name",omitempty`

	token        string `json:-`
	trackID      int
	status       string
	loggedIn     bool
	challenge    string
	password     string
	passwordSalt string
	sessionToken string
	permissions  []string
}

type apiResultDownloadsStats struct {
	NbRSSItemsUnread      int    `json:"nb_rss_items_unread"`
	NbTasks               int    `json:"nb_tasks"`
	NbTasksActive         int    `json:"nb_tasks_active"`
	NbTasksChecking       int    `json:"nb_tasks_checking"`
	NbTasksDone           int    `json:"nb_tasks_done"`
	NbTasksDownloading    int    `json:"nb_tasks_downloading"`
	NbTasksError          int    `json:"nb_tasks_error"`
	NbTasksExtracting     int    `json:"nb_tasks_extracting"`
	NbTasksQueued         int    `json:"nb_tasks_queue"`
	NbTasksRepairing      int    `json:"nb_tasks_repairing"`
	NbTasksSeeding        int    `json:"nb_tasks_seeding"`
	NbTasksStopped        int    `json:"nb_tasks_stopped"`
	NbTasksStopping       int    `json:"nb_tasks_stopping"`
	RXRate                int    `json:"rx_rate"`
	TXRate                int    `json:"tx_rate"`
	ThrottlingIsScheduled bool   `json:"throttling_is_scheduled"`
	ThrottlingMode        string `json:"throttling_mode"`
	ThrottlingRate        struct {
		RXRate int `json:"rx_rate"`
		TXRate int `json:"tx_rate"`
	} `json:"throttling_rate"`
}

type apiResponseDownloadsStats struct {
	Success bool                    `json:"success"`
	Result  apiResultDownloadsStats `json:"result"`
}

type apiRequestLoginSession struct {
	AppID      string `json:"app_id",omitempty`
	AppVersion string `json:"app_version",omitempty`
	Password   string `json:"password",omitempty`
}

type apiResponseLoginAuthorize struct {
	Success bool `json:"success"`
	Result  struct {
		AppToken string `json:"app_token",omitempty`
		TrackID  int    `json:"track_id",omitempty`
	} `json:"result"`
}

type apiResponseLoginSession struct {
	Success bool `json:"success"`
	Result  struct {
		SessionToken string          `json:"session_token",omitempty`
		Challenge    string          `json:"",omitempty`
		PasswordSalt string          `json:"",omitempty`
		Permissions  map[string]bool `json:"",omitempty`
	} `json:"result"`
}

type apiResponseLoginAuthorizeTrack struct {
	Success bool `json:"success"`
	Result  struct {
		Status       string `json:"status",omitempty`
		Challenge    string `json:"challenge",omitempty`
		PasswordSalt string `json:"password_salt",omitempty`
	} `json:"result"`
}

type apiResponseLogin struct {
	Success bool `json:"success"`
	Result  struct {
		LoggedIn     bool   `json:"logged_in",omitempty`
		Challenge    string `json:"challenge",omitempty`
		PasswordSalt string `json:"password_salt",omitempty`
	} `json:"result"`
}

// Client is the Freebox API client
type Client struct {
	URL string

	App        App
	apiVersion ApiVersion
	client     *http.Client
}

// New returns a `Client` object with standard configuration
func New() *Client {
	client := Client{
		URL:    "http://mafreebox.free.fr/",
		client: &http.Client{},
		App: App{
			Identifier: "go-freebox",
			Name:       "Go Freebox",
			Version:    "0.1.0",
			DeviceName: "Golang",
		},
	}
	if os.Getenv("GOFBX_TOKEN") != "" {
		client.App.token = os.Getenv("GOFBX_TOKEN")
	}
	return &client
}

// ApiVersion returns an `ApiVersion` structure field with the configuration fetched during `Connect()`
func (c *Client) ApiVersion() *ApiVersion {
	return &c.apiVersion
}

func (a *ApiVersion) ApiCode() string {
	return "v" + strings.Split(a.Version, ".")[0]
}

// GetApiResource performs low-level GET request on the Freebox API
func (c *Client) GetResource(resource string, authenticated bool) ([]byte, error) {
	var url string
	if authenticated {
		url = fmt.Sprintf("%s%s%s/%s", strings.TrimRight(c.URL, "/"), c.apiVersion.BaseURL, c.apiVersion.ApiCode(), resource)
	} else {
		url = fmt.Sprintf("%s%s", c.URL, resource)
	}
	logrus.Debugf(">>> GET  %q", url)

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "application/json")
	if authenticated {
		req.Header.Set("X-Fbx-App-Auth", c.App.sessionToken)
	}

	resp, err := c.client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	logrus.Debugf("<<< %s", body)

	if resp.StatusCode > 299 {
		return nil, fmt.Errorf("Status code: %d", resp.StatusCode)
	}

	return body, err
}

func (a *ApiVersion) authBaseURL() string {
	return strings.TrimLeft(a.BaseURL, "/") + a.ApiCode() + "/"
}

// PostResource post data and returns body
func (c *Client) PostResource(resource string, data interface{}, authenticated bool) ([]byte, error) {
	var url string
	if authenticated {
		url = fmt.Sprintf("%s%s%s", c.URL, c.apiVersion.authBaseURL(), resource)
	} else {
		url = fmt.Sprintf("%s%s", c.URL, resource)
	}

	payload := new(bytes.Buffer)
	encoder := json.NewEncoder(payload)
	if err := encoder.Encode(data); err != nil {
		return nil, err
	}

	payloadString := strings.TrimSpace(fmt.Sprintf("%s", payload))
	logrus.Debugf(">>> POST %s payload=%s", url, payloadString)

	req, err := http.NewRequest("POST", url, payload)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "application/json")
	if authenticated {
		req.Header.Set("X-Fbx-App-Auth", c.App.sessionToken)
	}

	resp, err := c.client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	logrus.Debugf("<<< %s", body)

	if resp.StatusCode > 299 {
		return nil, fmt.Errorf("Status code: %d", resp.StatusCode)
	}

	return body, err
}

// Connect tries to contact the Freebox API, and fetches API versions
func (c *Client) Connect() error {
	body, err := c.GetResource("api_version", false)
	if err != nil {
		return err
	}
	logrus.Debugf("API response: %s", string(body))

	err = json.Unmarshal(body, &c.apiVersion)
	if err != nil {
		return err
	}

	logrus.Infof("API version: FreeboxID=%q DeviceName=%q Version=%q DeviceType=%q", c.apiVersion.FreeboxID, c.apiVersion.DeviceName, c.apiVersion.Version, c.apiVersion.DeviceType)

	return nil
}

func (c *Client) Authorize() error {
	if c.App.token != "" {
		logrus.Debugf("App already registered, skiping `Authorize()`")
		return nil
	}
	body, err := c.PostResource(c.apiVersion.authBaseURL()+"login/authorize", c.App, false)
	if err != nil {
		return err
	}

	var response apiResponseLoginAuthorize
	err = json.Unmarshal(body, &response)
	if err != nil {
		return err
	}

	c.App.token = response.Result.AppToken
	c.App.trackID = response.Result.TrackID
	logrus.Infof("Authorize: Token=%q TrackID=%d", c.App.token, c.App.trackID)

	for {
		body, err := c.GetResource(c.apiVersion.authBaseURL()+fmt.Sprintf("login/authorize/%d", c.App.trackID), false)
		if err != nil {
			return err
		}

		var response apiResponseLoginAuthorizeTrack
		err = json.Unmarshal(body, &response)
		if err != nil {
			return err
		}

		if response.Result.Status == "denied" {
			return fmt.Errorf("App denied")
		}

		if response.Result.Status == "granted" {
			break
		}
		time.Sleep(5 * time.Second)
	}

	return nil
}

func (c *Client) Login() error {
	if c.App.token == "" {
		return fmt.Errorf("You need to get a token with `Authorize()` first")
	}

	body, err := c.GetResource(c.apiVersion.authBaseURL()+"login", false)
	if err != nil {
		return err
	}

	var response apiResponseLogin
	err = json.Unmarshal(body, &response)
	if err != nil {
		return err
	}

	c.App.loggedIn = response.Result.LoggedIn
	c.App.challenge = response.Result.Challenge
	c.App.passwordSalt = response.Result.PasswordSalt
	hash := hmac.New(sha1.New, []byte(c.App.token))
	hash.Write([]byte(c.App.challenge))
	c.App.password = fmt.Sprintf("%x", hash.Sum(nil))

	if !c.App.loggedIn {
		data := apiRequestLoginSession{
			AppID:      c.App.Identifier,
			AppVersion: c.App.Version,
			Password:   c.App.password,
		}
		body, err := c.PostResource(c.apiVersion.authBaseURL()+"login/session/", data, false)
		if err != nil {
			return err
		}

		var response apiResponseLoginSession
		err = json.Unmarshal(body, &response)
		if err != nil {
			return err
		}

		c.App.sessionToken = response.Result.SessionToken
	}
	return nil
}

func (c *Client) DownloadsStats() (*apiResultDownloadsStats, error) {
	body, err := c.GetResource("downloads/stats", true)
	if err != nil {
		return nil, err
	}

	var response apiResponseDownloadsStats
	err = json.Unmarshal(body, &response)
	if err != nil {
		return nil, err
	}

	return &response.Result, nil
}
