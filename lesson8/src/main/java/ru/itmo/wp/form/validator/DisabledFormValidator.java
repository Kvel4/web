package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.DisabledForm;

@Component
public class DisabledFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return DisabledForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // No operations.
    }

}
