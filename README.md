# Housecream - home automation
<!-- ![housecream logo](http://housecream.org/img/logo/Housecream.jpg)-->

![logo](https://raw.githubusercontent.com/n0rad/housecream/master/docs/img/housecream32.png)
[![Build Status](https://img.shields.io/travis/n0rad/housecream/master.svg)](https://travis-ci.org/n0rad/housecream)
[![Go Report Card](https://goreportcard.com/badge/github.com/n0rad/housecream)](https://goreportcard.com/report/github.com/n0rad/housecream)
<!--[![GoDoc](https://img.shields.io/badge/godoc-reference-5874B0.svg)](https://godoc.org/github.com/n0rad/housecream)-->

Housecream is an advanced home automation server aiming to give you unlimited control over your house.



# Features

# Architecture

# Download

See [github release page](https://github.com/n0rad/housecream/releases)


# Build / Run

Having go installed and $GOPATH set :

```
# go get github.com/n0rad/housecream
# cd $GOPATH/src/github.com/n0rad/housecream
# ./gomake
# ./dist/housecream-v0-$(go env GOHOSTOS)-$(go env GOHOSTARCH)/housecream -H data
```

# what to do ?

- how to handle manual data set directly by hand ?
  it has to trigger the rue engine and so have to processed like any other channel
  but at the same time we have to be able to set a channel endpoint directly too
  also this output set have to go to the rule engine too...
  
  
- should we linked directly triggered from board to board ? 
  this sohuld be better since it can work without housecream, but have to be codded direcrlt in the board
  strong board could be able to update code internalry to handle rules direclty...

  
- restmcu
- counter -> outpoint
- X10 -> outpoint

IN : Have to store the value on event to remember current state
OUT : Have to store the value on action to remember what was set
//INOUT : Can get value to know where it is





# channels
mail
notification
openWeatherMap
restmcu
slack
solar
timer
todo
trello
value
webhook
cron
exec
serial
X10
KNX
hue
http
android push


manual
simple
counter
internal

# EIP
delay
split
reorder

lardy : 48.52,2.26

http://api.openweathermap.org/pollution/v1/co/{location}/{datetime}.json?appid={api_key}
http://api.openweathermap.org/pollution/v1/co/48.5206306,2.2651702/current.json?appid=

type
format

housecream_link_todo_count[name=] = 10
housecream_link_slack[direction=input] 
housecream_link_solar_phase[type=nautic,name=sun_status] = 1
housecream_link_restmcu_max[id=42,type=analogic,direction=input,name=out_temperature] = 1024
housecream_link_restmcu_min[id=42,type=analogic,direction=input,name=out_temperature] = 0
housecream_link_restmcu[id=42,type=analogic,direction=input,name=out_temperature] = 42

housecream_link_openweathermap_weather_id[name="orly"] = 300
housecream_link_openweathermap_temperature[name="orly"] = 280.32
housecream_link_openweathermap_pressure[name="orly"] = 1012
housecream_link_openweathermap_humidity[name="orly"] = 81
housecream_link_openweathermap_temp_min[name="orly"] = 279.15
housecream_link_openweathermap_temp_max[name="orly"] = 281.15
housecream_link_openweathermap_visibility[name="orly"] = 10000
housecream_link_openweathermap_wind_speed[name="orly"] = 4.1
housecream_link_openweathermap_wind_deg[name="orly"] = 80
housecream_link_openweathermap_cloud[name="orly"] = 90
housecream_link_openweathermap_sunset[name="orly"] = 1485794875
housecream_link_openweathermap_sunrise[name="orly"] = 1485762037








 


















