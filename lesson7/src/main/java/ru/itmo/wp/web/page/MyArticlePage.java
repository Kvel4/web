package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        articleService.checkAccess(user);
        view.put("articles", articleService.findAll());
    }

    @Json
    private void changeSecrecy(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        articleService.checkAccess(user);

        String buttonValue = request.getParameter("buttonValue");
        if (buttonValue == null) {
            throw new ValidationException("Wrong button value");
        }

        long articleId = getLong(request.getParameter("articleId"));
        Article article = articleService.find(articleId);
        if (article.getUserId() != user.getId()) {
            throw new ValidationException("You have no permission to change this field");
        }

        article.setHidden(getChangedValue(buttonValue));
        articleService.updateHidden(article);

        view.put("buttonValue", getNewButtonText(article.isHidden()));
    }

    private boolean getChangedValue(String buttonValue) throws ValidationException {
        if (buttonValue.equals("hide")) {
            return true;
        } else if (buttonValue.equals("show")) {
            return false;
        }
        throw new ValidationException("Wrong button value");
    }

    private String getNewButtonText(boolean hidden){
        if (hidden) {
            return "show";
        }
        return "hide";
    }

    private long getLong(String string) throws ValidationException {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException | NullPointerException e){
            throw new ValidationException("Request must contains correct user id");
        }
    }
}
