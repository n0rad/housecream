


/**
 * Check that pin is readable
 */
int checkGetValue(int pin) {
  if(p_pin[pin].mode == PWM) {
    sprintf(globalErrorBuffer, "You can not read this pin (%d) as its in PWM mode", pin);
    return 1;
  }
  return 0;
}


int getValue(int pin) {
  if (checkGetValue(pin)) {
    return 0;
  }
  if (p_pin[pin].mode == ANALOG) {
    return analogRead(pin);
  } else {
    return digitalRead(pin);
  }
}

/**
 * check that a value is setable to a pin
 * @return 1 if error
 * @TODO duamilanov specific function
 */
int checkSetValue(int pin, int value) {
  if (pin >= 10) {
    sprintf(globalErrorBuffer, "You can not set this pin (%d)", pin);
    return 1;
  }

  if (p_pin[pin].mode == PWM) {
    if (value < 0 || value > 255) {
      sprintf(globalErrorBuffer, "This value (%d) can not be set to PWM pin (%d)", value, pin);
      return 1;
    }
  } else if (p_pin[pin].mode == OUTPUT) {
    if (value < 0 || value > 1) {
      sprintf(globalErrorBuffer, "This value (%d) can not be set to OUTPUT pin (%d)", value, pin);
      return 1;
    }
  } else if (p_pin[pin].mode == INPUT) {
    if (value < 0 || value > 1) {
      sprintf(globalErrorBuffer, "This value (%d) can not be set to INPUT pin (%d)", value, pin);
      return 1;
    }
    // disable 20k pullup usage at run
    sprintf(globalErrorBuffer, "This pin (%d) is set as an input and can not be set", pin);
    return 1;
  } else {
    sprintf(globalErrorBuffer, "This pin (%d) is set as not used and can not be set", pin);
    return 1;
  }
  return 0;
}


/**
 * check if value is setable and set it
 */
int setValue(int pin, int value) {
  if (checkSetValue(pin, value)) {
    return 1;
  }

  DEBUG_PRINT("set pin (");
  //DEBUG_PRINTDEC(pin);
  DEBUG_PRINT(" ");
  DEBUG_PRINT(p_pin[pin].mode == OUTPUT ? "OUTPUT" : "PWM");
  DEBUG_PRINT(") to value : ");
  DEBUG_PRINTDEC(value);
  DEBUG_PRINTLN("");

  if (p_pin[pin].mode == PWM) {
    analogWrite(pin, value);
  } else {
    digitalWrite(pin, value);
  }
  return 0;
}


/**
 * init pins as input or output and set starting value
 */
void initPins() {
  for (int i = 0; i < p_pinSize; i++) {
    // skip notused and analog
    if (i >= 10 || p_pin[i].mode == NOTUSED) {
      continue;
    }

    // init input and output (PWM does not need init)
    if (p_pin[i].mode != PWM) {
      DEBUG_PRINT("set pin (");
      DEBUG_PRINTDEC(i);
      DEBUG_PRINT(") to mode ");

      DEBUG_PRINT((p_pin[i].mode == INPUT ? "INPUT" : (p_pin[i].mode == OUTPUT ? "OUTPUT" : "UNKNOWN")));
      DEBUG_PRINT(" (");
      DEBUG_PRINT(p_pin[i].description);
      DEBUG_PRINTLN(")");
      pinMode(i, p_pin[i].mode);
    }

    // set default value
    if (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM) {
      if (setValue(i, p_pin[i].startValue)) {
        // error when set
        DEBUG_PRINTLN(globalErrorBuffer);
        return;
      }
    } else if (p_pin[i].mode == INPUT && p_pin[i].startValue == HIGH) {
      DEBUG_PRINT("Enable 20k pullup resistor on pin ");
      DEBUG_PRINTDEC(i);
      DEBUG_PRINTLN("");
      digitalWrite(i, HIGH);
    }
  }
}

/**
 * Check pin definition in p_pin for this board version
 * @TODO duamilanov specific function
 */
int checkDef() {
  for (int i = 0; i < p_pinSize; i++) {
    // skip not used pin
    if (p_pin[i].mode == NOTUSED) {
      continue;
    }

    // check that for duemilanov the pin 10 to 13 are not used (used by ethernet shield)
    if (i >= 10 && i <= 13) {
      sprintf(globalErrorBuffer, "FATAL ERROR : Pin (%d) is used by ethernet shield you can not use it", i);
      return 1;
    }

    // check pwm pin
    if (p_pin[i].mode == PWM && !(i == 3 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11)) {
      sprintf(globalErrorBuffer, "FATAL ERROR : Mode PWM can not be used for pin %d", i);
      return 1;
    }

    // check that analog pin is in ANALOG or DIGITAL
    if (i > 13 && !(p_pin[i].mode == ANALOG || p_pin[i].mode == DIGITAL)) {
      sprintf(globalErrorBuffer, "FATAL ERROR : Analog pin (%d) can only be in ANALOG or DIGITAL mode", i);
      return 1;
    }

    // check set default value for digital pins
    // skip digital INPUT pin check to allow 20k pullup start value (but no modification after that)
    if (i < 10 && (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM)) {
      // check that default value is applicable
      if (checkSetValue(i, p_pin[i].startValue)) {
        return 1;
      }
    }
  }
  return 0;
}
