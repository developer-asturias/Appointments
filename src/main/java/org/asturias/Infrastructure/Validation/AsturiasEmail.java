package org.asturias.Infrastructure.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AsturiasEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsturiasEmail {
    String message() default "El correo electrónico debe ser del dominio @asturias.edu.co";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
