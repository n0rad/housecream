#include "../../hcc.h"

void checPinDescriptions() {

}


///**
// * Check that pin is readable
// */
//int checkGetValue(int pin) {
//  if(p_pin[pin].mode == PWM) {
//    sprintf(globalErrorBuffer, "You can not read this pin (%d) as its in PWM mode", pin);
//    return 1;
//  }
//  return 0;
//}


///**
// * Check pin definition in p_pin for this board version
// * @TODO duamilanov specific function
// */
//int checkDef() {
//  for (int i = 0; i < p_pinSize; i++) {
//    // skip not used pin
//    if (p_pin[i].mode == NOTUSED) {
//      continue;
//    }
//
//    // check that for duemilanov the pin 10 to 13 are not used (used by ethernet shield)
//    if (i >= 10 && i <= 13) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Pin (%d) is used by ethernet shield you can not use it", i);
//      return 1;
//    }
//
//    // check pwm pin
//    if (p_pin[i].mode == PWM && !(i == 3 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11)) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Mode PWM can not be used for pin %d", i);
//      return 1;
//    }
//
//    // check that analog pin is in ANALOG or DIGITAL
//    if (i > 13 && !(p_pin[i].mode == ANALOG || p_pin[i].mode == DIGITAL)) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Analog pin (%d) can only be in ANALOG or DIGITAL mode", i);
//      return 1;
//    }
//
//    // check set default value for digital pins
//    // skip digital INPUT pin check to allow 20k pullup start value (but no modification after that)
//    if (i < 10 && (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM)) {
//      // check that default value is applicable
//      if (checkSetValue(i, p_pin[i].startValue)) {
//        return 1;
//      }
//    }
//  }
//  return 0;
//}
