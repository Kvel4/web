package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.*;

public class TalkService {
    private static final int MAX_TEXT_SIZE = 4096;
    private final TalkRepository talkRepository = new TalkRepositoryImpl();

    public List<TalkView> findAllViews(User user) {
        final UserRepository userRepository = new UserRepositoryImpl();
        List<TalkView> views = new ArrayList<>();
        List<Talk> talks = findRelated(user);

        for (Talk talk : talks) {
            User sourceUser = userRepository.find(talk.getSourceUserId());
            User targetUser = userRepository.find(talk.getTargetUserId());

            views.add(new TalkView(talk, sourceUser, targetUser));
        }
        return views;
    }

    private List<Talk> findRelated(User user) {
        return talkRepository.findRelated(user);
    }

    public void validate(Talk talk) throws ValidationException {
        if (talk.getText().length() < 1) {
            throw new ValidationException("Message cant be empty");
        }

        if (talk.getSourceUserId() == talk.getTargetUserId()) {
            throw new ValidationException("You cant message to yourself");
        }

        if (talk.getText().length() > MAX_TEXT_SIZE) {
            throw new ValidationException("Message length is too long");
        }
    }

    public void save(Talk talk) {
        talkRepository.save(talk);
    }

    public static class TalkView {
        private Talk talk;
        private User sourceUser;
        private User targetUser;

        TalkView(Talk talk, User sourceUser, User targetUser) {
            this.talk = talk;
            this.sourceUser = sourceUser;
            this.targetUser = targetUser;
        }

        public Talk getTalk() {
            return talk;
        }

        public User getSourceUser() {
            return sourceUser;
        }

        public User getTargetUser() {
            return targetUser;
        }
    }
}
