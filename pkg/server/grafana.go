package server

import (
	"flag"
	"os"

	_ "github.com/grafana/grafana/pkg/services/alerting/conditions"
	_ "github.com/grafana/grafana/pkg/services/alerting/notifiers"
	_ "github.com/grafana/grafana/pkg/tsdb/prometheus"

	"github.com/go-ini/ini"
	"github.com/grafana/grafana/pkg/api"
	"github.com/grafana/grafana/pkg/log"
	"github.com/grafana/grafana/pkg/login"
	"github.com/grafana/grafana/pkg/metrics"
	"github.com/grafana/grafana/pkg/models"
	"github.com/grafana/grafana/pkg/plugins"
	"github.com/grafana/grafana/pkg/services/alerting"
	"github.com/grafana/grafana/pkg/services/cleanup"
	"github.com/grafana/grafana/pkg/services/eventpublisher"
	"github.com/grafana/grafana/pkg/services/notifications"
	"github.com/grafana/grafana/pkg/services/search"
	"github.com/grafana/grafana/pkg/services/sqlstore"
	"github.com/grafana/grafana/pkg/setting"
	"github.com/grafana/grafana/pkg/social"
	"github.com/n0rad/go-erlog/data"
	"github.com/n0rad/go-erlog/errs"
	"github.com/n0rad/go-erlog/logs"
	"golang.org/x/net/context"
	"golang.org/x/sync/errgroup"
)

type GrafanaServerImpl struct {
	context       context.Context
	shutdownFn    context.CancelFunc
	childRoutines *errgroup.Group
	log           log.Logger
	HomeFolder    *HomeFolder

	httpServer *api.HttpServer
}

func NewGrafanaServer(home *HomeFolder) models.GrafanaServer {
	rootCtx, shutdownFn := context.WithCancel(context.Background())
	childRoutines, childCtx := errgroup.WithContext(rootCtx)

	return &GrafanaServerImpl{
		context:       childCtx,
		shutdownFn:    shutdownFn,
		childRoutines: childRoutines,
		log:           log.New("server"),
		HomeFolder:    home,
	}
}

func (g *GrafanaServerImpl) Start() {
	if err := writeConfiguration(g.HomeFolder); err != nil {
		logs.WithE(err).Fatal("Failed to write configuration")
	}

	err := setting.NewConfigContext(&setting.CommandLineArgs{
		Config:   g.HomeFolder.Path + "/grafana/config.ini",
		HomePath: g.HomeFolder.Path + "/grafana",
		Args:     flag.Args(),
	})
	if err != nil {
		logs.WithE(err).Fatal("Failed to create grafana settings")
	}
	setting.LogConfigurationInfo()

	// init sql
	sqlstore.NewEngine()
	sqlstore.EnsureAdminUser()

	metrics.Init()
	search.Init()
	login.Init()
	social.NewOAuthService()
	eventpublisher.Init()
	plugins.Init()

	// init alerting
	if setting.AlertingEnabled && setting.ExecuteAlerts {
		engine := alerting.NewEngine()
		g.childRoutines.Go(func() error {
			return engine.Run(g.context)
		})
	}

	// cleanup service
	cleanUpService := cleanup.NewCleanUpService()
	g.childRoutines.Go(func() error {
		return cleanUpService.Run(g.context)
	})

	if err := notifications.Init(); err != nil {
		g.log.Error("Notification service failed to initialize", "erro", err)
		g.Shutdown(1, "Startup failed")
		return
	}

	g.startHttpServer()
}

func (g *GrafanaServerImpl) startHttpServer() {
	g.httpServer = api.NewHttpServer()

	err := g.httpServer.Start(g.context)

	if err != nil {
		g.log.Error("Fail to start server", "error", err)
		g.Shutdown(1, "Startup failed")
		return
	}
}

func (g *GrafanaServerImpl) Shutdown(code int, reason string) {
	g.log.Info("Shutdown started", "code", code, "reason", reason)

	err := g.httpServer.Shutdown(g.context)
	if err != nil {
		g.log.Error("Failed to shutdown server", "error", err)
	}

	g.shutdownFn()
	err = g.childRoutines.Wait()

	g.log.Info("Shutdown completed", "reason", err)
	log.Close()
	os.Exit(code)
}

func writeConfiguration(home *HomeFolder) error {
	homeDir := home.Path + "/grafana/conf"
	if err := os.MkdirAll(homeDir, 0755); err != nil {
		return errs.WithEF(err, data.WithField("path", homeDir), "Failed to create grafana home directory")
	}

	config := ini.Empty()
	config.BlockMode = false
	paths, _ := config.NewSection("paths")
	paths.NewKey("data", home.Path+"/grafana/data")
	paths.NewKey("logs", home.Path+"/grafana/datalogs")
	paths.NewKey("plugins", home.Path+"/grafana/dataplugins")

	f, err := os.Create(home.Path + "/grafana/config.ini")
	if err != nil {
		return errs.WithE(err, "Failed to create grafana config file")
	}
	defer f.Close()
	config.WriteTo(f)

	defaultConfig, err := os.Create(home.Path + "/grafana/conf/defaults.ini")
	if err != nil {
		return errs.WithE(err, "Failed to create grafana config file")
	}
	defer defaultConfig.Close()
	defaultConfig.WriteString(defaultConfContent)

	return nil
}

const defaultConfContent = `
##################### Grafana Configuration Defaults #####################
#
# Do not modify this file in grafana installs
#

# possible values : production, development
app_mode = production

# instance name, defaults to HOSTNAME environment variable value or hostname if HOSTNAME var is empty
instance_name = ${HOSTNAME}

#################################### Paths ###############################
[paths]
# Path to where grafana can store temp files, sessions, and the sqlite3 db (if that is used)
#
data = data
#
# Directory where grafana can store logs
#
logs = data/log
#
# Directory where grafana will automatically scan and look for plugins
#
plugins = data/plugins

#################################### Server ##############################
[server]
# Protocol (http, https, socket)
protocol = http

# The ip address to bind to, empty will bind to all interfaces
http_addr =

# The http port  to use
http_port = 3000

# The public facing domain name used to access grafana from a browser
domain = localhost

# Redirect to correct domain if host header does not match domain
# Prevents DNS rebinding attacks
enforce_domain = false

# The full public facing url
root_url = %(protocol)s://%(domain)s:%(http_port)s/

# Log web requests
router_logging = false

# the path relative working path
static_root_path = public

# enable gzip
enable_gzip = false

# https certs & key file
cert_file =
cert_key =

# Unix socket path
socket = /tmp/grafana.sock

#################################### Database ############################
[database]
# You can configure the database connection by specifying type, host, name, user and password
# as separate properties or as on string using the url property.

# Either "mysql", "postgres" or "sqlite3", it's your choice
type = sqlite3
host = 127.0.0.1:3306
name = grafana
user = root
# If the password contains # or ; you have to wrap it with triple quotes. Ex """#password;"""
password =
# Use either URL or the previous fields to configure the database
# Example: mysql://user:secret@host:port/database
url =

# Max conn setting default is 0 (mean not set)
max_idle_conn =
max_open_conn =

# For "postgres", use either "disable", "require" or "verify-full"
# For "mysql", use either "true", "false", or "skip-verify".
ssl_mode = disable

ca_cert_path =
client_key_path =
client_cert_path =
server_cert_name =

# For "sqlite3" only, path relative to data_path setting
path = grafana.db

#################################### Session #############################
[session]
# Either "memory", "file", "redis", "mysql", "postgres", "memcache", default is "file"
provider = file

# Provider config options
# memory: not have any config yet
# file: session dir path, is relative to grafana data_path
# redis: config like redis server e.g. ` + "`addr=127.0.0.1:6379,pool_size=100,db=grafana`" + `
# postgres: user=a password=b host=localhost port=5432 dbname=c sslmode=disable
# mysql: go-sql-driver/mysql dsn config string, examples:
#         ` + "`user:password@tcp(127.0.0.1:3306)/database_name`" + `
#         ` + "`user:password@unix(/var/run/mysqld/mysqld.sock)/database_name`" + `
# memcache: 127.0.0.1:11211


provider_config = sessions

# Session cookie name
cookie_name = grafana_sess

# If you use session in https only, default is false
cookie_secure = false

# Session life time, default is 86400
session_life_time = 86400
gc_interval_time = 86400

#################################### Data proxy ###########################
[dataproxy]

# This enables data proxy logging, default is false
logging = false

#################################### Analytics ###########################
[analytics]
# Server reporting, sends usage counters to stats.grafana.org every 24 hours.
# No ip addresses are being tracked, only simple counters to track
# running instances, dashboard and error counts. It is very helpful to us.
# Change this option to false to disable reporting.
reporting_enabled = true

# Set to false to disable all checks to https://grafana.com
# for new versions (grafana itself and plugins), check is used
# in some UI views to notify that grafana or plugin update exists
# This option does not cause any auto updates, nor send any information
# only a GET request to https://grafana.com to get latest versions
check_for_updates = true

# Google Analytics universal tracking code, only enabled if you specify an id here
google_analytics_ua_id =

# Google Tag Manager ID, only enabled if you specify an id here
google_tag_manager_id =

#################################### Security ############################
[security]
# default admin user, created on startup
admin_user = admin

# default admin password, can be changed before first start of grafana,  or in profile settings
admin_password = admin

# used for signing
secret_key = SW2YcwTIb9zpOOhoPsMm

# Auto-login remember days
login_remember_days = 7
cookie_username = grafana_user
cookie_remember_name = grafana_remember

# disable gravatar profile images
disable_gravatar = false

# data source proxy whitelist (ip_or_domain:port separated by spaces)
data_source_proxy_whitelist =

[snapshots]
# snapshot sharing options
external_enabled = true
external_snapshot_url = https://snapshots-origin.raintank.io
external_snapshot_name = Publish to snapshot.raintank.io

# remove expired snapshot
snapshot_remove_expired = true

# remove snapshots after 90 days
snapshot_TTL_days = 90

#################################### Users ####################################
[users]
# disable user signup / registration
allow_sign_up = true

# Allow non admin users to create organizations
allow_org_create = true

# Set to true to automatically assign new users to the default organization (id 1)
auto_assign_org = true

# Default role new users will be automatically assigned (if auto_assign_org above is set to true)
auto_assign_org_role = Viewer

# Require email validation before sign up completes
verify_email_enabled = false

# Background text for the user field on the login page
login_hint = email or username

# Default UI theme ("dark" or "light")
default_theme = dark

[auth]
# Set to true to disable (hide) the login form, useful if you use OAuth
disable_login_form = false

# Set to true to disable the signout link in the side menu. useful if you use auth.proxy
disable_signout_menu = false

#################################### Anonymous Auth ######################
[auth.anonymous]
# enable anonymous access
enabled = false

# specify organization name that should be used for unauthenticated users
org_name = Main Org.

# specify role for unauthenticated users
org_role = Viewer

#################################### Github Auth #########################
[auth.github]
enabled = false
allow_sign_up = true
client_id = some_id
client_secret = some_secret
scopes = user:email
auth_url = https://github.com/login/oauth/authorize
token_url = https://github.com/login/oauth/access_token
api_url = https://api.github.com/user
team_ids =
allowed_organizations =

#################################### Google Auth #########################
[auth.google]
enabled = false
allow_sign_up = true
client_id = some_client_id
client_secret = some_client_secret
scopes = https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email
auth_url = https://accounts.google.com/o/oauth2/auth
token_url = https://accounts.google.com/o/oauth2/token
api_url = https://www.googleapis.com/oauth2/v1/userinfo
allowed_domains =
hosted_domain =

#################################### Grafana.com Auth ####################
[auth.grafananet]
enabled = false
allow_sign_up = true
client_id = some_id
client_secret = some_secret
scopes = user:email
allowed_organizations =

#################################### Generic OAuth #######################
[auth.generic_oauth]
name = OAuth
enabled = false
allow_sign_up = true
client_id = some_id
client_secret = some_secret
scopes = user:email
auth_url =
token_url =
api_url =
team_ids =
allowed_organizations =

#################################### Basic Auth ##########################
[auth.basic]
enabled = true

#################################### Auth Proxy ##########################
[auth.proxy]
enabled = false
header_name = X-WEBAUTH-USER
header_property = username
auto_sign_up = true
ldap_sync_ttl = 60
whitelist =

#################################### Auth LDAP ###########################
[auth.ldap]
enabled = false
config_file = /etc/grafana/ldap.toml
allow_sign_up = true

#################################### SMTP / Emailing #####################
[smtp]
enabled = false
host = localhost:25
user =
# If the password contains # or ; you have to wrap it with trippel quotes. Ex """#password;"""
password =
cert_file =
key_file =
skip_verify = false
from_address = admin@grafana.localhost
from_name = Grafana

[emails]
welcome_email_on_sign_up = false
templates_pattern = emails/*.html

#################################### Logging ##########################
[log]
# Either "console", "file", "syslog". Default is console and  file
# Use space to separate multiple modes, e.g. "console file"
mode = console file

# Either "debug", "info", "warn", "error", "critical", default is "info"
level = info

# optional settings to set different levels for specific loggers. Ex filters = sqlstore:debug
filters =

# For "console" mode only
[log.console]
level =

# log line format, valid options are text, console and json
format = console

# For "file" mode only
[log.file]
level =

# log line format, valid options are text, console and json
format = text

# This enables automated log rotate(switch of following options), default is true
log_rotate = true

# Max line number of single file, default is 1000000
max_lines = 1000000

# Max size shift of single file, default is 28 means 1 << 28, 256MB
max_size_shift = 28

# Segment log daily, default is true
daily_rotate = true

# Expired days of log file(delete after max days), default is 7
max_days = 7

[log.syslog]
level =

# log line format, valid options are text, console and json
format = text

# Syslog network type and address. This can be udp, tcp, or unix. If left blank, the default unix endpoints will be used.
network =
address =

# Syslog facility. user, daemon and local0 through local7 are valid.
facility =

# Syslog tag. By default, the process' argv[0] is used.
tag =


#################################### AMQP Event Publisher ################
[event_publisher]
enabled = false
rabbitmq_url = amqp://localhost/
exchange = grafana_events

#################################### Dashboard JSON files ################
[dashboards.json]
enabled = false
path = /var/lib/grafana/dashboards

#################################### Usage Quotas ########################
[quota]
enabled = false

#### set quotas to -1 to make unlimited. ####
# limit number of users per Org.
org_user = 10

# limit number of dashboards per Org.
org_dashboard = 100

# limit number of data_sources per Org.
org_data_source = 10

# limit number of api_keys per Org.
org_api_key = 10

# limit number of orgs a user can create.
user_org = 10

# Global limit of users.
global_user = -1

# global limit of orgs.
global_org = -1

# global limit of dashboards
global_dashboard = -1

# global limit of api_keys
global_api_key = -1

# global limit on number of logged in users.
global_session = -1

#################################### Alerting ############################
[alerting]
# Disable alerting engine & UI features
enabled = true
# Makes it possible to turn off alert rule execution but alerting UI is visible
execute_alerts = true

#################################### Internal Grafana Metrics ############
# Metrics available at HTTP API Url /api/metrics
[metrics]
enabled           = true
interval_seconds  = 10

# Send internal Grafana metrics to graphite
[metrics.graphite]
# Enable by setting the address setting (ex localhost:2003)
address =
prefix = prod.grafana.%(instance_name)s.

[grafana_net]
url = https://grafana.com

#################################### External Image Storage ##############
[external_image_storage]
# You can choose between (s3, webdav)
provider =

[external_image_storage.s3]
bucket_url =
access_key =
secret_key =

[external_image_storage.webdav]
url =
username =
password =
public_url =
`