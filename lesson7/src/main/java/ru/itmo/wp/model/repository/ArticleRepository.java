package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface ArticleRepository {
    Article find(long id);
    List<Article> findAll();
    List<Article> findRelated(User user);

    void updateHidden(Article article);
    void save(Article article);
}
