package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class MessagesServlet extends HttpServlet {
    private static List<Map<String, String>> messenger = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        if (uri.equals("/message/add")) {
            add(request);
        } else {
            String json = "";
            if (uri.equals("/message/auth")) {
                    json = auth(request);
            } else if (uri.equals("/message/findAll")) {
                json = findAll();
            }
            response.setContentType("application/json");
            response.getWriter().print(json);
            response.getWriter().flush();
        }
    }

    private String auth(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");

        if (user == null) {
            user = request.getParameter("user");
        }

        if (user == null) {
            return new Gson().toJson("");
        }
        session.setAttribute("user", user);
        return new Gson().toJson(user);
    }

    private String findAll() {
        return new Gson().toJson(messenger);
    }

    private void add(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("user", (String) request.getSession().getAttribute("user"));
        map.put("text", request.getParameter("text"));
        messenger.add(map);
    }
}
