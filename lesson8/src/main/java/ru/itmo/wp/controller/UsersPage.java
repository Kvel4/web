package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.DisabledForm;
import ru.itmo.wp.form.validator.DisabledFormValidator;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UsersPage extends Page {
    private final UserService userService;
    private final DisabledFormValidator disabledFormValidator;

    public UsersPage(UserService userService, DisabledFormValidator disabledFormValidator) {
        this.userService = userService;
        this.disabledFormValidator = disabledFormValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() == null) return;

        if (disabledFormValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(disabledFormValidator);
        }
    }

    @GetMapping("/users/all")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping("users/all")
    public String disabled(@Valid @ModelAttribute("disabledForm") DisabledForm disabledForm,
                           HttpSession httpSession,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/users/all";
        }
        if (getUser(httpSession) == null) {
            putMessage(httpSession, "You must be authorised to change this field");
            return "redirect:/users/all";
        }
        userService.updateDisabled(disabledForm.getUserId(), disabledForm.getBooleanValue());
        return "redirect:/users/all";
    }
}
