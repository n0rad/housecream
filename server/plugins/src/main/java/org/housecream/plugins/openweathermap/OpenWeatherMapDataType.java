package org.housecream.plugins.openweathermap;


public enum OpenWeatherMapDataType {
    wind,
    temperature,
    max_temperature,
    min_temperature,
    sunrise,
    sunset,
    condition, // clear,
    pressure,
    humidity,

    rain,
    snow,
    clouds,;

    public static OpenWeatherMapDataType valueOfName(String name) {
        OpenWeatherMapDataType openWeatherMapDataType = valueOf(name);
        if (openWeatherMapDataType == null) {
            throw new IllegalStateException("OpenWeatherMap data type not found for name : " + name);
        }
        return openWeatherMapDataType;
    }

}
