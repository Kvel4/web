package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) {
        Post post = postService.find(id);
        if (post == null) {
            return "NotFoundPage";
        }
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", post.getComments());
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String addComment(@PathVariable String id,
                             @Valid @ModelAttribute("comment") Comment comment,
                             BindingResult bindingResult,
                             HttpSession httpSession,
                             Model model) {
        Post post = postService.find(id);
        if (post == null) {
            return "redirect:/notFound";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            model.addAttribute("comments", post.getComments());
            return "PostPage";
        }

        postService.addComment(post, getUser(httpSession), comment);
        return "redirect:/post/" + id;
    }
}
