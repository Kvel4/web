package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;
import ru.itmo.wp.web.exception.RedirectException;

import java.util.ArrayList;
import java.util.List;

public class ArticleService {
    private static final int MAX_TITLE_LENGTH = 512;
    private static final int MAX_TEXT_LENGTH = 65535;
    private ArticleRepository articleRepository = new ArticleRepositoryImpl();
    private UserService userService= new UserService();

    public void validate(Article article) throws ValidationException {
        if (article.getTitle() == null) {
            throw new ValidationException("Title can't be null");
        }
        if (article.getText() == null) {
            throw new ValidationException("Text can't be null");
        }
        if (article.getTitle().length() < 1) {
            throw new ValidationException("Title length can't be < 1");
        }
        if (article.getTitle().length() > MAX_TITLE_LENGTH)  {
            throw new ValidationException("Title length can't be > 512");
        }
        if (article.getText().length() < 1) {
            throw new ValidationException("Text length can't be < 1");
        }
        if (article.getText().length() > MAX_TEXT_LENGTH) {
            throw new ValidationException("Title length can't be > 512");
        }
    }

    public Article find(long id) {
        return articleRepository.find(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<ArticleView> findNotHiddenViews() {
        List<ArticleView> articles = new ArrayList<>();
        for (Article article : findAll()) {
            if (!article.isHidden()) {
                articles.add(new ArticleService.ArticleView(userService.find(article.getUserId()), article));
            }
        }
        return articles;
    }

    public List<Article> findRelated(User user) {
        return articleRepository.findRelated(user);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public void updateHidden(Article article) {
        articleRepository.updateHidden(article);
    }

    public void checkAccess(User user) {
        if (user == null) {
            throw new RedirectException("/");
        }
    }

    public static class ArticleView {
        private final User user;
        private final Article article;

        private ArticleView(User user, Article article) {
            this.user = user;
            this.article = article;
        }

        public User getUser() {
            return user;
        }

        public Article getArticle() {
            return article;
        }
    }
}
