package ru.itmo.wp.servlet;

import ru.itmo.wp.model.Captcha;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class CaptchaFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.isNew()) {
            final Captcha captcha = new Captcha();
            session.setAttribute("isCaptchaPassed", false);
            session.setAttribute("captcha", captcha);
        }

        if (request.getMethod().equalsIgnoreCase("GET")) {
            filterGet(request, response, chain);
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            filterPost(request, response, chain);
        }
    }

    private void filterGet(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Boolean isCaptchaPassed = (Boolean) session.getAttribute("isCaptchaPassed");

        if (isCaptchaPassed) {
            super.doFilter(request, response, chain);
        } else {
            sendCaptcha(response, (Captcha) session.getAttribute("captcha"));
        }
    }

    private void filterPost(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Boolean isCaptchaPassed = (Boolean) session.getAttribute("isCaptchaPassed");

        if (isCaptchaPassed) {
            super.doFilter(request, response, chain);
        } else {
            String captchaAnswer = ((Captcha) session.getAttribute("captcha")).getAnswer();
            String userAnswer = request.getParameter("captchaAnswer");
            if (userAnswer != null && userAnswer.equals(captchaAnswer)) {
                session.setAttribute("isCaptchaPassed", true);
                response.sendRedirect(request.getRequestURI());
            } else {
                final Captcha captcha = new Captcha();
                session.setAttribute("captcha", captcha);
                sendCaptcha(response, captcha);
            }
        }
    }

    private void sendCaptcha(HttpServletResponse response, Captcha captcha) throws IOException {
        response.setContentType("text/html");
        response.getOutputStream().print("<img id=\"profileImage\" src=\"data:image/png;base64,");
        response.getOutputStream().write(captcha.getBody());
        response.getOutputStream().print("\"/>");
        response.getOutputStream().print(
                "<form method=\"post\">\n" +
                        "<label for=\"captchaAnswerLabel\">Enter number:</label>\n" +
                        "<input name=\"captchaAnswer\" id=\"captchaAnswerId\">\n" +
                "</form>");
        response.getOutputStream().flush();
    }
}
