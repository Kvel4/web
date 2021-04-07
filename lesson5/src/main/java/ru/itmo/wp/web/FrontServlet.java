package ru.itmo.wp.web;

import com.google.common.base.Strings;
import freemarker.template.*;
import ru.itmo.wp.web.exception.NotFoundException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.page.IndexPage;
import ru.itmo.wp.web.page.NotFoundPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";

    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;

    private Configuration newFreemarkerConfiguration(String templateDirName, boolean debug) throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir=" + templateDir + "]");
        }
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(debug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setLocale(new Locale("en"));

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(getServletContext().getRealPath("WEB-INF/templates"), false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Route route = Route.newRoute(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.newNotFoundRoute(), request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }

    private void process(Route route, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, ServletException, IOException {
        Class<?> pageClass;

        try {
            pageClass = Class.forName(route.className);
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }

        Map<String, Object> view = new HashMap<>();
        Map<Class<?>, Object> matcher = Map.of(Map.class, view, HttpServletRequest.class, request);
        MethodWithArgs methodWithArgs = getMethodWithArgs(pageClass, route.getAction(), matcher);
        Method method = methodWithArgs.getMethod();
        List<Object> methodArgs = methodWithArgs.getMethodArgs();

        Object page;
        try {
            page = pageClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServletException("Can't create page [pageClass=" + pageClass + "]");
        }

        method.setAccessible(true);
        try {
            method.invoke(page, methodArgs.toArray());
        } catch (IllegalAccessException e) {
            throw new ServletException("Can't invoke action method [pageClass=" + pageClass + ", method=" + method + "]");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) cause;
                response.sendRedirect(redirectException.getTarget());
                return;
            } else {
                throw new ServletException("Can't invoke action method [pageClass=" + pageClass + ", method=" + method + "]", cause);
            }
        }

        Template template = getTemplate(pageClass.getSimpleName(), getLang(request));
        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            template.process(view, response.getWriter());
        } catch (TemplateException e) {
//            throw new ServletException("Can't render template [pageClass=" + pageClass + ", action=" + method + "]", e);
        }
    }

    private MethodWithArgs getMethodWithArgs(Class<?> pageClass, String methodName, Map<Class<?>, Object> matcher) throws NotFoundException {
        Method method = null;
        List<Object> methodArgs = new ArrayList<>();

        for (Class<?> clazz = pageClass; method == null && clazz != null; clazz = clazz.getSuperclass()) {
            for (Method clazzMethod : clazz.getDeclaredMethods()) {
                if (clazzMethod.getName().equals(methodName)) {
                    List<Object> tmpMethodArgs = new ArrayList<>();

                    for (Class<?> parameter : clazzMethod.getParameterTypes()) {
                        Object val = matcher.get(parameter);
                        if (val == null) {
                            tmpMethodArgs.clear();
                            break;
                        }
                        tmpMethodArgs.add(val);
                    }

                    if (tmpMethodArgs.size() >= methodArgs.size()) {
                        method = clazzMethod;
                        methodArgs.clear();
                        methodArgs.addAll(tmpMethodArgs);
                    }
                }
            }
        }

        if (method == null) {
            throw new NotFoundException();
        }

        return new MethodWithArgs(method, methodArgs);
    }

    private Template getTemplate(String simpleName, String lang) throws ServletException {
        Template template = null;

        if (!lang.equals("en")) {
            try {
                template = newTemplate(simpleName + "_" + lang + ".ftlh");
            } catch (ServletException ignored) {
                // No operations.
            }
        }

        if (template == null) {
            template = newTemplate(simpleName + ".ftlh");
        }

        return template;
    }

    private String getLang(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String lang = request.getParameter("lang");

        if (lang != null && lang.matches("((?=\\p{IsLatin})\\p{Lower}){2}")) {
            session.setAttribute("lang", lang);
        }

        lang = (String) session.getAttribute("lang");
        return lang == null ? "en" : lang;
    }

    private Template newTemplate(String templateName) throws ServletException {
        Template template = null;

        if (sourceConfiguration != null) {
            try {
                template = sourceConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null && targetConfiguration != null) {
            try {
                template = targetConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null) {
            throw new ServletException("Can't find template [templateName=" + templateName + "]");
        }

        return template;
    }

    private static class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private static Route newNotFoundRoute() {
            return new Route(
                    NotFoundPage.class.getName(),
                    DEFAULT_ACTION
            );
        }

        private static Route newIndexRoute() {
            return new Route(
                    IndexPage.class.getName(),
                    DEFAULT_ACTION
            );
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/"))
                    .filter(part -> !Strings.isNullOrEmpty(part))
                    .collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return newIndexRoute();
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1,
                    Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (Strings.isNullOrEmpty(action)) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }
    }

    private static class MethodWithArgs {
        private Method method;
        private List<Object> methodArgs;

        private MethodWithArgs(Method method, List<Object> methodArgs) {
            this.method = method;
            this.methodArgs = new ArrayList<>();
            this.methodArgs.addAll(methodArgs);
        }

        private Method getMethod() {
            return method;
        }

        private List<Object> getMethodArgs() {
            return methodArgs;
        }
    }
}
