package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.exception.NoSuchResourceException;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;

    public PostService(PostRepository postRepository, JwtService jwtService) {
        this.postRepository = postRepository;
        this.jwtService = jwtService;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post findById(long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) throw new NoSuchResourceException("No such post");
        return post;
    }

    public Post findById(String id) {
        try {
            return findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new NoSuchResourceException("No such post");
        }
    }

    public void writeComment(Post post, CommentForm commentForm) {
        Comment comment = new Comment();
        comment.setUser(jwtService.find(commentForm.getJwt()));
        comment.setText(commentForm.getText());
        comment.setPost(post);
        post.getComments().add(comment);
        postRepository.save(post);

    }
}
