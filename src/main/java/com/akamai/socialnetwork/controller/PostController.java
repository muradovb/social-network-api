package com.akamai.socialnetwork.controller;

import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable("id") Long id) {
        return postService.fetchPost(id);
    }

    @PostMapping("/create")
    public Long createPost(@RequestBody @Validated PostDTO post) {
        return postService.createPost(post);
    }

    @PutMapping("/update/{id}")
    public Long updatePost(@PathVariable("id") Long id, @RequestBody @Validated PostDTO post) {
      return postService.updatePost(id, post);
    }

    @DeleteMapping("/delete/{id}")
    public Long deletePost(@PathVariable("id") Long id) {
       return postService.deletePost(id);
    }

    @GetMapping("/getMostViewed")
    public List<PostDTO> getTopPosts() {
        return postService.fetchTopPosts();
    }

    @GetMapping("/getAll")
    public List<PostDTO> getAllPosts() {return postService.fetchAllPosts();}
}

