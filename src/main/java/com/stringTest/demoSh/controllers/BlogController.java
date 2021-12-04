package com.stringTest.demoSh.controllers;

import com.stringTest.demoSh.models.Post;
import com.stringTest.demoSh.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@Controller

public class BlogController {

    // private final PostRepository postRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogAddPost(Model model,
                              @RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String fullText) {
        Post post = new Post(title, anons, fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogGetById(Model model,
                              @PathVariable(value = "id") long id) {
        //проверка ест ли такая статья
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        //чтобы проще работать, переводим в лист
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "blog-detail";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEditById(Model model,
                               @PathVariable(value = "id") long id) {
        //проверка ест ли такая статья
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        //чтобы проще работать, переводим в лист
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "blog-edit";
    }


    @PostMapping("/blog/{id}/edit")
    public String blogEditPost(Model model,
                               @PathVariable(value = "id") long id,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String fullText) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }


    @PostMapping("/blog/{id}/remove")
    public String blogRemovePost(Model model,
                                 @PathVariable(value = "id") long id)
    {
        Post post = postRepository.findById(id).orElseThrow();

        postRepository.delete(post);
        return "redirect:/blog";
    }


//
//    @GetMapping("/rest")
//    public Iterable blogMainRest(Model model) {
//        Iterable<Post> posts = postRepository.findAll();
//        model.addAttribute("posts", posts);
//        return posts;
//    }

}
