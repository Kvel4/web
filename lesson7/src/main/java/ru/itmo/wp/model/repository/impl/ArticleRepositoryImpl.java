package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.ArticleRepository;

import java.util.List;

public class ArticleRepositoryImpl extends BasicRepositoryImpl<Article> implements ArticleRepository {

    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public Article find(long id) {
        String query = "SELECT * FROM Article WHERE id=?";
        return findSingle(query, List.of(id));
    }

    @Override
    public List<Article> findAll() {
        String query = "SELECT * FROM Article ORDER BY creationTime DESC";
        return findMany(query, List.of());
    }

    @Override
    public List<Article> findRelated(User user) {
        String query = "SELECT * FROM Article WHERE userId=?";
        return findMany(query, List.of(user.getId()));
    }

    @Override
    public void updateHidden(Article article) {
        String query = "UPDATE Article SET hidden=? WHERE id=?";
        update(query, List.of(article.getHidden(), article.getId()));
    }

    @Override
    public void save(Article article) {
        String query = "INSERT INTO `Article` (`userId`, `title`, `text`, `creationTime`) VALUES (?, ?, ?, NOW())";
        save(query, article, List.of(article.getUserId(), article.getTitle(), article.getText()));
    }
}
