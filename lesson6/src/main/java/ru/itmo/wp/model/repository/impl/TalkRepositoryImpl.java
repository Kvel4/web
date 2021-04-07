package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.TalkRepository;

import java.util.List;

public class TalkRepositoryImpl extends BasicRepositoryImpl<Talk> implements TalkRepository {

    public TalkRepositoryImpl() {
        super(Talk.class);
    }

    @Override
    public Talk find(long id) {
        String query = "SELECT * FROM Talk WHERE id=?";
        return findSingle(query, List.of(id));
    }

    @Override
    public List<Talk> findAll() {
        String query = "SELECT * FROM Talk ORDER BY id DESC";
        return findMany(query, List.of());
    }

    @Override
    public List<Talk> findRelated(User user) {
        String query = "SELECT * FROM Talk WHERE sourceUserId=? OR targetUserId=? ORDER BY id DESC";
        return findMany(query, List.of(user.getId(), user.getId()));

    }

    @Override
    public void save(Talk talk) {
        String query = "INSERT INTO `Talk` (`sourceUserId`, `targetUserId`, `text`, `creationTime`) VALUES (?, ?, ?, NOW())";
        save(query, talk, List.of(talk.getSourceUserId(), talk.getTargetUserId(), talk.getText()));
    }
}
