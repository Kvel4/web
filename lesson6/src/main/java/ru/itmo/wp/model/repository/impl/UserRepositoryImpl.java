package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.UserRepository;

import java.util.List;

public class UserRepositoryImpl extends BasicRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User find(long id) {
        String query = "SELECT * FROM User WHERE id=?";
        return findSingle(query, List.of(id));
    }

    @Override
    public User findByLogin(String login) {
        String query = "SELECT * FROM User WHERE login=?";
        return findSingle(query, List.of(login));
    }

    @Override
    public User findByLoginAndPasswordSha(String login, String passwordSha) {
        String query = "SELECT * FROM User WHERE login=? AND passwordSha=?";
        return findSingle(query, List.of(login, passwordSha));
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT * FROM User WHERE login=?";
        return findSingle(query, List.of(email));
    }

    @Override
    public User findByEmailAndPasswordSha(String email, String passwordSha) {
        String query = "SELECT * FROM User WHERE email=? AND passwordSha=?";
        return findSingle(query, List.of(email, passwordSha));
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM User ORDER BY id DESC";
        return findMany(query, List.of());
    }

    @Override
    public void save(User user, String passwordSha) {
        String query = "INSERT INTO `User` (`login`, `email`, `passwordSha`, `creationTime`) VALUES (?, ?, ?, NOW())";
        save(query, user, List.of(user.getLogin(), user.getEmail(), passwordSha));
    }
}
