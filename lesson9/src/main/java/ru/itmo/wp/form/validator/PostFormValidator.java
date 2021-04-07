package ru.itmo.wp.form.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.form.PostForm;

;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PostFormValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return PostForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostForm postForm = (PostForm) target;
            Set<String> used = new HashSet<>();
            Pattern pattern = Pattern.compile("[a-z]+");
            String[] splitted = postForm.getTagsString().split("\\s+");

            for (String name: splitted) {
                Matcher matcher = pattern.matcher(name);
                if (!matcher.matches()) {
                    errors.rejectValue("tagsString", "tagsString.invalid-format", "Tags must be lowercase latin letters");
                }
                if (used.contains(name)) {
                    errors.rejectValue("tagsString", "tagsString.invalid-format", "Tags must be unique");
                }
                if (name.length() < 1 || name.length() > 255) {
                    errors.rejectValue("tagsString", "tagsString.invalid-format", "Tag's size must be between 1 and 255");
                }
                postForm.getTags().add(new Tag(name));
                used.add(name);
            }
        }
    }
}
