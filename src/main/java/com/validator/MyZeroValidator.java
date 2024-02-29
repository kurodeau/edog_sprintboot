package com.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MyZeroValidator.MyZeroValidationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyZeroValidator {

    String message() default "Invalid value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MyZeroValidationValidator implements ConstraintValidator<MyZeroValidator, Integer> {

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
        	if(value==null) {value=0;}
            return value == null || value >= 0;
        }
    }
}
