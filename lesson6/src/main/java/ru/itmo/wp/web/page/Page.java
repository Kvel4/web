package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression", "FieldCanBeLocal"})
abstract class Page {
    final UserService userService = new UserService();
    final EventService eventService = new EventService();
    final TalkService talkService = new TalkService();
    HttpServletRequest request;
    HttpSession session;

    void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        this.session = request.getSession();

        putUser(request, view);
        putMessage(request, view);
        putUserCount(view);
    }

    void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    void after(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    private String getError() {
        return (String) session.getAttribute("error");
    }

    User getUser() {
        return (User) session.getAttribute("user");
    }

    void setUser(User user) {
        session.setAttribute("user", user);
    }

    void setMessage(String message) {
        session.setAttribute("message", message);
    }

    private void putUserCount(Map<String, Object> view) {
        final UserService userService = new UserService();
        view.put("userCount", userService.findCount());
    }

    private void putUser(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();
        if (user != null) {
            view.put("user", user);
        }
    }

    private void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

}
