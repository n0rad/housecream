package org.housecream.server.api.domain.point;

public enum PointType {
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
    clouds,

    NUM,
    FLOAT, // ??TODO
    STRING,
    LIST,
    SET,


    PUSHBUTTON, //
    SWITCH, //
    TEMPERATURE, // status
    HUMIDITY, // status
    TILT, // status
    LEVEL, // status
    PIR, // status
    BAROMETRIC, // status
    SMOCK, // status
    LOCK, // status
    SOUND, //
    KEYPAD, // ???????????

    VOLTAGE, //
    CURRENT, //

    //    TIMER, // ??????
    //    VOICE, // ??????

    LIGHT, //
    DIMMER, //
    // private ValueType values; // range


    // OUT
    OUTLET, //
    SLUICE_GATE, //

    ;
}
