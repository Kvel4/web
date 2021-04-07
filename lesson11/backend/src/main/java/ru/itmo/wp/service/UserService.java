package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.NoSuchResourceException;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.repository.PostRepository;
import ru.itmo.wp.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NoSuchResourceException("No such user");
        return user;
    }

    public User findById(String id) {
        try {
            return findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new NoSuchResourceException("No such user");
        }
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public void register(UserCredentials userCredentials) {
        User user = new User();
        user.setName(userCredentials.getName());
        user.setLogin(userCredentials.getLogin());
        userRepository.save(user);
        userRepository.updatePasswordSha(user.getId(), userCredentials.getLogin(), userCredentials.getPassword());
    }

    public void writePost(User user, Post post) {
        user.addPost(post);
        userRepository.save(user);
//        postRepository.save(post);
    }

    public User findByLogin(String login) {
        return login == null ? null : userRepository.findByLogin(login);
    }
}
