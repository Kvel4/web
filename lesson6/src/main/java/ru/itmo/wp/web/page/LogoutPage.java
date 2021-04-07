package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class LogoutPage extends Page {
    @Override
    void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");

        if (user == null) return;

        Event event = new Event();
        event.setUserId(user.getId());
        event.setType(Event.Type.LOGOUT);
        eventService.saveEvent(event);

        setMessage("Good bye. Hope to see you soon!");
        throw new RedirectException("/index");
    }
}
