package net.awired.housecream.client.common.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HccPinValidator implements ConstraintValidator<HccPinValid, HccPinDescription> {

    @Override
    public void initialize(HccPinValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(HccPinDescription pin, ConstraintValidatorContext context) {
        //        if (!(pin.getDirection() == HccPinDirection.NOTUSED || pin.getDirection() == HccPinDirection.RESERVED)) {
        //            if (pin.getPullUp() == null) {
        //                context.buildConstraintViolationWithTemplate("pullup is mandatory for non reserved / notused pin");
        //            }
        //        }
        //        if (pin.getType() == HccPinType.ANALOG) {
        //            if (pin.getValueMin() == null) {
        //                context.buildConstraintViolationWithTemplate("valueMin is mandatory for analog pin");
        //            }
        //            if (pin.getValueMax() == null) {
        //                context.buildConstraintViolationWithTemplate("valueMax is mandatory for analog pin");
        //            }
        //            if (pin.getValueMin() != null && pin.getValueMax() != null && pin.getValueMin() >= pin.getValueMax()) {
        //                context.buildConstraintViolationWithTemplate("valueMin cannot superior or equals to valueMax");
        //            }
        //        }
        //        if (pin.getDirection() == HccPinDirection.OUTPUT) {
        //            if (pin.getStartVal() == null) {
        //                context.buildConstraintViolationWithTemplate("startVal is mandatory for output pin");
        //            } else {
        //                if (pin.getStartVal() < pin.getValueMin()) {
        //                    context.buildConstraintViolationWithTemplate("start value cannot be less that valueMin");
        //                }
        //                if (pin.getStartVal() > pin.getValueMax()) {
        //                    context.buildConstraintViolationWithTemplate("start value cannot superior than valueMax");
        //                }
        //            }
        //            if (pin.getValue() == null) {
        //                context.buildConstraintViolationWithTemplate("value is mandatory for output pin");
        //            } else {
        //                if (pin.getValue() < pin.getValueMin()) {
        //                    context.buildConstraintViolationWithTemplate("value cannot be less that valueMin");
        //                }
        //                if (pin.getValue() > pin.getValueMax()) {
        //                    context.buildConstraintViolationWithTemplate("value cannot superior than valueMax");
        //                }
        //            }
        //        }
        //        if (pin.getDirection() != HccPinDirection.INPUT) {
        //            if (pin.getNotifies() != null) {
        //                context.buildConstraintViolationWithTemplate("only input pin can have notifies");
        //            }
        //        }

        return true;
    }
}
