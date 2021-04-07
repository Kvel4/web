package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        articleService.checkAccess(user);
    }

    private void create(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        articleService.checkAccess(user);
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setUserId(user.getId());

        articleService.validate(article);
        articleService.save(article);
    }
}
