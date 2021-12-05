package com.stringTest.demoSh.controllers;

import com.stringTest.demoSh.exeptions.NotFoundExeption;
import com.stringTest.demoSh.models.Post;
import com.stringTest.demoSh.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@RestController
@RequestMapping("blog")
public class BlogController {

    // private final PostRepository postRepository;
    @Autowired
    private PostRepository postRepository;


    @GetMapping()
    public Iterable blogMainRest() {
        Iterable<Post> posts = postRepository.findAll();
        return posts;
    }


    @GetMapping("{id}")
    public ArrayList<Post> blogGetById(
            @PathVariable(value = "id") long id) {
        //проверка ест ли такая статья
        if (!postRepository.existsById(id)) {
            System.out.println("NOT FOUND");
            throw new NotFoundExeption();

        }
        Optional<Post> post = postRepository.findById(id);
        //чтобы проще работать, переводим в лист
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        return res;
    }

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "error";
    }


    @PostMapping()
    public Iterable<Post> blogAddPost(

            @RequestBody ArrayList<Post> post) {

       // System.out.println(post);

        postRepository.save(post.get(0));
        return postRepository.findAll();
    }
//
//    @RequestMapping(method = { POST, PUT })
//    public CategoryEntity saveOrUpdateCategory(@RequestBody CategoryEntity category) {
//        return categoryService.saveCategory(category);
//    }
//@PutMapping("{id}")
//public ArrayList<Post> upDatePost(@RequestBody Post post, @PathVariable(value = "id") long id){
//    System.out.println(post);
//    Optional<Post> posts = postRepository.findById(id);
//    ArrayList<Post> res = new ArrayList<>();
//    posts.ifPresent(res::add);
//    return postRepository.findAll();
//}


    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
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
                                 @PathVariable(value = "id") long id) {
        Post post = postRepository.findById(id).orElseThrow();

        postRepository.delete(post);
        return "redirect:/blog";
    }


}
