package org.asturias.Infrastructure.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AsturiasEmailValidator implements ConstraintValidator<AsturiasEmail, String> {

    @Override
    public void initialize(AsturiasEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        return email.matches("^[a-zA-Z0-9._%+-]+@asturias\\.edu\\.co$");
    }

}
