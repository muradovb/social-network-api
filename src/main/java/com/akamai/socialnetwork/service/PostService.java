package com.akamai.socialnetwork.service;

import com.akamai.socialnetwork.dto.PostDTO;

import java.util.List;

public interface PostService {
    Long createPost(PostDTO postDTO);
    PostDTO fetchPost(Long postId);
    Long updatePost(Long id, PostDTO postDTO);
    Long deletePost(Long postId);
    List<PostDTO> fetchTopPosts();
    List<PostDTO> fetchAllPosts();

}
