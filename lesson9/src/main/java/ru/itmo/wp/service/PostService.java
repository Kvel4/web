package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post find(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post find(String id) {
        try {
            return find(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public void addComment(Post post, User user, Comment comment) {
        post.addComment(user, comment);
        postRepository.save(post);
    }
}
