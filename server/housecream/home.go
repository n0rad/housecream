package housecream

type Config struct {
	Port int
}

func defaultConfig() Config {
	return Config{
		Port: 4242,
	}
}

type HomeStruct struct {
	path   string
	Config Config
}
