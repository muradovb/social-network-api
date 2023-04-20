package com.akamai.socialnetwork.service.impl;

import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.entity.PostEntity;
import com.akamai.socialnetwork.exception.ElementNotFoundException;
import com.akamai.socialnetwork.mapper.PostMapper;
import com.akamai.socialnetwork.repository.PostRepository;
import com.akamai.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    @Transactional
    public Long createPost(PostDTO postDTO) {
        logger.info("Creating post...");
        PostEntity postEntity =
                postMapper.toPostEntity(postDTO);
        postRepository.save(postEntity).getId();
        logger.info("Post created with id: {}", postEntity.getId());
        return postEntity.getId();
    }

    @Override
    public PostDTO fetchPost(Long postId) {
        logger.info("Fetching post with id: {}", postId);
        PostEntity optionalSocialNetworkPostEntity = postRepository
                .findById(postId).orElseThrow( () -> new ElementNotFoundException("Element not found with id: " + postId));
        logger.info("Post found with id: {}", postId);
        return postMapper.toPostDTO(optionalSocialNetworkPostEntity);
    }

    @Override
    @Transactional
    public Long updatePost(Long postId, PostDTO postDTO) {
        logger.info("Updating post with id: {}", postId);
        PostEntity postEntity = postRepository
                .findById(postId).orElseThrow(() -> new ElementNotFoundException("Element not found with id: " + postId));
            postEntity.setAuthor(postDTO.getAuthor());
            postEntity.setContent(postDTO.getContent());
            postEntity.setViewCount(postDTO.getViewCount());
            postRepository.save(postEntity);
            logger.info("Post updated with id: {}", postId);
            return postId;

    }

    @Override
    @Transactional
    public Long deletePost(Long postId) {
        logger.info("Deleting post with id: {}", postId);
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            logger.info("Post deleted with id: {}", postId);
            return postId;
        } else {
            throw new ElementNotFoundException("Element not found with id: " + postId);
        }
    }

    @Override
    public List<PostDTO> fetchTopPosts() {
        logger.info("Fetching top posts...");
        List<PostEntity> topPosts = postRepository.findTop10ByOrderByViewCountDesc();
        if (topPosts.isEmpty()) {
            throw new ElementNotFoundException("No records were found.");
        }
        else {
            logger.info("{} top posts fetched successfully.", topPosts.size());
            return topPosts.stream()
                    .map(element -> postMapper.toPostDTO(element)).collect(Collectors.toList());
        }

    }

    @Override
    public List<PostDTO> fetchAllPosts() {
        logger.info("Fetching all posts...");
        List<PostEntity> allPosts = postRepository.findAll();
        if (allPosts.isEmpty()) {
            throw new ElementNotFoundException("No records were found.");
        }
        else {
            logger.info("All posts were fetched successfully...");
            return allPosts.stream()
                    .map(element -> postMapper.toPostDTO(element)).collect(Collectors.toList());
        }
    }



}
