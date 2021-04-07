package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.PostService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final JwtService jwtService;

    public PostController(PostService postService, UserService userService, JwtService jwtService) {
        this.postService = postService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("post/{id}")
    public Post findPost(@PathVariable String id) {
        return postService.findById(id);
    }

    @PostMapping("post/{id}")
    private void writeComment(@RequestBody @Valid CommentForm commentForm,
                              BindingResult bindingResult,
                              @PathVariable String id) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        Post post = postService.findById(id);
        postService.writeComment(post, commentForm);
    }

    @PostMapping("posts")
    public void writePost(@RequestBody @Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        userService.writePost(jwtService.find(postForm.getJwt()), post);
    }

    @GetMapping("posts")
    public List<Post> findPosts() {
        return postService.findAll();
    }
}
