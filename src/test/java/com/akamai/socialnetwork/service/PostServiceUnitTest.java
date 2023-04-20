package com.akamai.socialnetwork.service;

import com.akamai.socialnetwork.PostUtils;
import com.akamai.socialnetwork.controller.PostControllerIntegrationTest;
import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.entity.PostEntity;
import com.akamai.socialnetwork.exception.ElementNotFoundException;
import com.akamai.socialnetwork.mapper.PostMapper;
import com.akamai.socialnetwork.repository.PostRepository;
import com.akamai.socialnetwork.service.impl.PostServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PostServiceUnitTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    private static final Logger logger = LoggerFactory.getLogger(PostControllerIntegrationTest.class);

    @Test
    public void givenValidPost_whenCreatePost_thenPostIsCreated() {
        logger.info("Performing givenValidPost_whenCreatePost_thenPostIsCreated()");
        // Given
        PostDTO postDTO = new PostDTO();
        PostEntity postEntity = new PostEntity();
        postEntity.setId(PostUtils.getRandomLongId());
        when(postMapper.toPostEntity(postDTO)).thenReturn(postEntity);
        when(postRepository.save(postEntity)).thenReturn(postEntity);

        // When
        Long postId = postService.createPost(postDTO);

        // Then
        Assert.assertEquals(postEntity.getId(), postId);
        verify(postMapper, times(1)).toPostEntity(postDTO);
        verify(postRepository, times(1)).save(postEntity);
        logger.info("Performed givenValidPost_whenCreatePost_thenPostIsCreated() successfully");
    }

    @Test
    public void givenValidPostId_whenFetchPost_returnPostDTO() {
        logger.info("Performing givenValidPostId_whenFetchPost_returnPostDTO()");

        // Given
        Random random = new Random();
        Long postId = random.nextLong();
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        PostDTO postDTO = new PostDTO();
        postDTO.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postMapper.toPostDTO(postEntity)).thenReturn(postDTO);

        // When
        PostDTO result = postService.fetchPost(postId);

        // Then
        verify(postRepository).findById(postId);
        verify(postMapper).toPostDTO(postEntity);
        assertEquals(postDTO, result);
        logger.info("Performed givenValidPostId_whenFetchPost_returnPostDTO() successfully");

    }

    @Test
    void givenExistingPost_whenUpdatePost_thenPostIsUpdated() {
        logger.info("Performing givenExistingPost_whenUpdatePost_thenPostIsUpdated()");

        // Given
        Random random = new Random();
        Long postId = random.nextLong();
        String newContent = "New content";
        int newViewCount = 10;

        PostEntity existingPostEntity = new PostEntity();
        existingPostEntity.setId(postId);
        existingPostEntity.setAuthor("Author");
        existingPostEntity.setContent("Content");
        existingPostEntity.setViewCount(5);

        PostDTO postDTO = new PostDTO();
        postDTO.setContent(newContent);
        postDTO.setViewCount(newViewCount);

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPostEntity));

        // When
        Long updatedPostId = postService.updatePost(postId, postDTO);

        // Then
        assertEquals(postId, updatedPostId);
        assertEquals(newContent, existingPostEntity.getContent());
        assertEquals(newViewCount, existingPostEntity.getViewCount());
        verify(postRepository).save(existingPostEntity);
        logger.info("Performed givenExistingPost_whenUpdatePost_thenPostIsUpdated() successfully");

    }

    @Test
    public void givenPosts_whenFetchTop10Posts_thenReturnTop10Posts() {
        logger.info("Performing givenPosts_whenFetchTop10Posts_thenReturnTop10Posts()...");

        // Given
        PostDTO post1 = new PostDTO();
        post1.setContent("Post 1");
        post1.setViewCount(10);
        Long postId1 = postService.createPost(post1);

        PostDTO post2 = new PostDTO();
        post2.setContent("Post 2");
        post2.setViewCount(20);
        Long postId2 = postService.createPost(post2);

        PostDTO post3 = new PostDTO();
        post3.setContent("Post 3");
        post3.setViewCount(30);
        Long postId3 = postService.createPost(post3);

        // When
        List<PostDTO> topPosts = postService.fetchTopPosts();

        // Then
        assertEquals(2, topPosts.size());
        assertEquals(post3.getContent(), topPosts.get(0).getContent());
        assertEquals(post3.getViewCount(), topPosts.get(0).getViewCount());
        assertEquals(post2.getContent(), topPosts.get(1).getContent());
        assertEquals(post2.getViewCount(), topPosts.get(1).getViewCount());
        logger.info("Performed givenPosts_whenFetchTop10Posts_thenReturnTop10Posts() successfully");

    }

    @Test(expected = ElementNotFoundException.class)
    public void fetchTopPosts_emptyList() {
        logger.info("Performing fetchTopPosts_emptyList()...");

        when(postRepository.findTop10ByOrderByViewCountDesc()).thenReturn(Collections.emptyList());

        postService.fetchTopPosts();
        logger.info("Performed fetchTopPosts_emptyList() successfully");

    }

    @Test
    void givenValidPostId_whenDeletePost_thenPostIsDeleted() {
        logger.info("Performing givenValidPostId_whenDeletePost_thenPostIsDeleted() ...");

        // Given
        Random random = new Random();
        Long postId = random.nextLong();

        // When
        Long deletedPostId = postService.deletePost(postId);

        // Then
        assertEquals(postId, deletedPostId);
        assertThrows(ElementNotFoundException.class, () -> postService.fetchPost(postId));
        logger.info("Performed givenValidPostId_whenDeletePost_thenPostIsDeleted() successfully");
    }

    @Test
    public void givenInvalidId_whenDeletePost_thenThrowElementNotFoundException() {
        logger.info("Performing givenInvalidId_whenDeletePost_thenThrowElementNotFoundException() ...");

        // Given
        Random random = new Random();
        Long nonExistingId = random.nextLong();
        when(postRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When
        Assertions.assertThrows(ElementNotFoundException.class, () -> {
            postService.deletePost(nonExistingId);
        });

        // Then
        Mockito.verify(postRepository, never()).delete(Mockito.any());
        logger.info("Performed givenInvalidId_whenDeletePost_thenThrowElementNotFoundException() successfully");
    }

    @Test
    public void givenInvalidId_whenUpdatePost_thenThrowElementNotFoundException() {
        logger.info("Performing givenInvalidPostId_whenUpdatePost_thenThrowElementNotFoundException()...");

        // Given
        Random random = new Random();
        Long postId = random.nextLong();
        Long nonExistentPostId = random.nextLong();
        PostDTO postDTO = new PostDTO();
        postDTO.setContent("Updated Content");
        postDTO.setViewCount(10);

        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setAuthor("John Doe");
        postEntity.setContent("Original Content");
        postEntity.setViewCount(5);

        when(postRepository.findById(nonExistentPostId)).thenReturn(Optional.empty());

        // When
        assertThrows(ElementNotFoundException.class, () -> {
            postService.updatePost(nonExistentPostId, postDTO);
        });

        // Then
        verify(postRepository, times(1)).findById(nonExistentPostId);
        verify(postRepository, never()).save(any(PostEntity.class));
        logger.info("Performed givenInvalidId_whenUpdatePost_thenThrowElementNotFoundException() successfully");
    }


    @Test(expected = ElementNotFoundException.class)
    public void givenInvalidPostId_whenFetchPost_throwElementNotFoundException() {
        logger.info("Performing givenInvalidPostId_whenFetchPost_throwElementNotFoundException() ...");

        // Given
        Long postId = PostUtils.getRandomLongId();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When
        postService.fetchPost(postId);

        // Then
        verify(postRepository).findById(postId);
        logger.info("Performed givenInvalidPostId_whenFetchPost_throwElementNotFoundException() successfully");

    }

    @Test
    public void givenRepositoryHasPost_whenFetchAll_returnAll() {
        logger.info("Performing givenRepositoryHasPost_whenFetchAll_returnAll() ...");

        PostEntity postEntity1 = PostUtils.getCustomPostEntity("author1", "content1", 10);
        PostEntity postEntity2 = PostUtils.getCustomPostEntity("author2", "content2", 30);
        List<PostEntity> allPosts = Arrays.asList(postEntity1, postEntity2);
        Mockito.when(postRepository.findAll()).thenReturn(allPosts);

        List<PostDTO> expectedPosts = Arrays.asList(
                PostUtils.getCustomPostDtoWithId("author1", "content1", 10, postEntity1.getId()),
                PostUtils.getCustomPostDtoWithId("author1", "content1", 30, postEntity2.getId())
        );

        List<PostDTO> actualPosts = postService.fetchAllPosts();
        assertEquals(expectedPosts, actualPosts);
        logger.info("Performed givenRepositoryHasPost_whenFetchAll_returnAll() successfully");
    }

    @Test
    void givenEmptyRepository_whenFetchAll_throwElementNotFoundException() {
        logger.info("Performing givenEmptyRepository_whenFetchAll_throwElementNotFoundException() ...");

        Mockito.when(postRepository.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(ElementNotFoundException.class, () -> postService.fetchAllPosts());
        logger.info("Performed givenEmptyRepository_whenFetchAll_throwElementNotFoundException() successfully");
    }


}
