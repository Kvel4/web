package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @noinspection UnstableApiUsage*/
public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private static final String PASSWORD_SALT = "177d4b5f2e4f4edafa7404533973c04c513ac619";

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        validateLogin(user.getLogin());
        validateEmail(user.getEmail());
        validatePassword(password);

        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Passwords dont match");
        }
    }

    public void validateEnter(String loginOrEmail, String password) throws ValidationException {
        User user = findByLoginOrEmailAndPassword(loginOrEmail, password);

        if (user == null) {
            throw new ValidationException("Invalid login/email or password");
        }
    }

    public User findByLoginOrEmailAndPassword(String loginOrEmail, String password) {
        User user = null;
        if (isLoginFormat(loginOrEmail)) {
            user = userRepository.findByLoginAndPasswordSha(loginOrEmail, getPasswordSha(password));
        } else if (isEmailFormat(loginOrEmail)) {
            user = userRepository.findByEmailAndPasswordSha(loginOrEmail, getPasswordSha(password));
        }
        return user;
    }

    private boolean isLoginFormat(String login) {
        return login.matches("[a-z]+");
    }

    private boolean isEmailFormat(String email) {
        return email.indexOf('@') == email.lastIndexOf('@');
    }

    private void validatePassword(String password) throws ValidationException {
        if (Strings.isNullOrEmpty(password)) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password can't be shorter than 4 characters");
        }
        if (password.length() > 12) {
            throw new ValidationException("Password can't be longer than 12 characters");
        }
    }

    private void validateEmail(String email) throws ValidationException {
        if (Strings.isNullOrEmpty(email)) {
            throw new ValidationException("Email is required");
        }
        if (!isEmailFormat(email)) {
            throw new ValidationException("Email must contains @");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new ValidationException("Email is already in use");
        }
    }

    private void validateLogin(String login) throws ValidationException {
        if (Strings.isNullOrEmpty(login)) {
            throw new ValidationException("Login is required");
        }
        if (!isLoginFormat(login)) {
            throw new ValidationException("Login can contain only lowercase Latin letters");
        }
        if (login.length() > 8) {
            throw new ValidationException("Login can't be longer than 8 letters");
        }
        if (userRepository.findByLogin(login) != null) {
            throw new ValidationException("Login is already in use");
        }
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Integer findCount() {
        List<User> users = findAll();
        return users.size();
    }

    public List<User> findRelatedUsers(List<Talk> talks) {
        List<User> users = new ArrayList<>();
        Set<Long> used = new HashSet<>();

        for (Talk talk : talks) {
            long sourceUserId = talk.getSourceUserId();
            long targetUserId = talk.getTargetUserId();

            if (!used.contains(sourceUserId)) {
                users.add(userRepository.find(sourceUserId));
                used.add(sourceUserId);
            }
            if (!used.contains(targetUserId)) {
                users.add(userRepository.find(targetUserId));
                used.add(targetUserId);
            }
        }
        return users;
    }
}
