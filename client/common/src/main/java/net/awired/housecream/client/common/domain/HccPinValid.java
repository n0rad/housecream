package net.awired.housecream.client.common.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HccPinValidator.class)
public @interface HccPinValid {

    String message() default "invalid pin configuration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
