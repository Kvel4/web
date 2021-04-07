package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeForm;
import ru.itmo.wp.form.validator.NoticeFormCreateValidator;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page{
    private final NoticeFormCreateValidator noticeFormCreateValidator;
    private final NoticeService noticeService;

    public NoticePage(NoticeFormCreateValidator noticeFormCreateValidator, NoticeService noticeService) {
        this.noticeFormCreateValidator = noticeFormCreateValidator;
        this.noticeService = noticeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() == null) return;

        if (noticeFormCreateValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(noticeFormCreateValidator);
        }
    }

    @GetMapping("/notice")
    public String notice(Model model){
        model.addAttribute("noticeForm", new NoticeForm());
        return "NoticePage";
    }

    @PostMapping("/notice")
    public String createNotice(@Valid @ModelAttribute("noticeForm") NoticeForm noticeForm,
                               BindingResult bindingResult,
                               HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }
        noticeService.save(noticeForm);
        putMessage(httpSession, "Notice added successfully");
        return "redirect:/notice";
    }
}
