package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends Page {
    @Override
    void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        if (getUser() == null) {
            setMessage("Sorry, only authorised users can view this page.");
            throw new RedirectException("/index");
        }
    }

    @Override
    void action(HttpServletRequest request, Map<String, Object> view) {
    }

    @Override
    void after(HttpServletRequest request, Map<String, Object> view) {
        view.put("talkViews", talkService.findAllViews(getUser()));
        view.put("users", userService.findAll());
        super.after(request, view);
    }

    private void sendMessage(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Talk talk = new Talk();
        talk.setSourceUserId(getUser().getId());
        talk.setTargetUserId(Long.parseLong(request.getParameter("chosenUser")));
        talk.setText(request.getParameter("text"));
        talkService.validate(talk);
        talkService.save(talk);

        throw new RedirectException("talks");
    }
}
