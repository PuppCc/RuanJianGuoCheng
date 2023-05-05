package com.easyse.easyse_simple.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy={MobileValidator.class})
@Constraint(validatedBy = {})
@Pattern(regexp = "^0?1[1|2|3|4|5|6|7|8|9][0-9]\\d{8}$")
public @interface Mobile {
    String message() default"手机号校验错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}