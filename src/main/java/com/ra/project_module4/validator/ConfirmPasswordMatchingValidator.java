package com.ra.project_module4.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class ConfirmPasswordMatchingValidator implements ConstraintValidator<ConfirmMatchingPassword,Object> {
    private String password;
    private String confirmPassword;

    @Override
    public void initialize(ConfirmMatchingPassword matching) {
        password = matching.password();
        confirmPassword = matching.confirmPassword();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(o).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(o).getPropertyValue(confirmPassword);
        return Objects.equals(passwordValue,confirmPasswordValue);
    }
}
