package com.akamai.socialnetwork.controller;

import com.akamai.socialnetwork.PostUtils;
import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.exception.ElementNotFoundException;
import com.akamai.socialnetwork.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostControllerUnitTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final Logger logger = LoggerFactory.getLogger(PostControllerIntegrationTest.class);


    @Test
    public void givenValidPostId_whenGetPostById_thenReturnPost() throws ElementNotFoundException {
        logger.info("Performing givenValidPostId_whenGetPostById_thenReturnPost() ...");
        Long id = PostUtils.getRandomLongId();
        PostDTO postDTO = PostUtils.getDefaultPostDTO();

        when(postService.fetchPost(id)).thenReturn(postDTO);

        PostDTO result = postController.getPostById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        logger.info("Performed givenValidPostId_whenGetPostById_thenReturnPost() successfully");
    }

    @Test
    public void givenValidPost_whenCreatePost_thenReturnPostId() throws ElementNotFoundException {
        logger.info("Performing givenValidPost_whenCreatePost_thenReturnPostId()...");
        Long id = PostUtils.getRandomLongId();
        PostDTO postDTO = PostUtils.getDefaultPostDTO();

        when(postService.createPost(postDTO)).thenReturn(id);

        Long result = postController.createPost(postDTO);

        assertNotNull(result);
        assertEquals(id, result);

        logger.info("Performed givenValidPost_whenCreatePost_thenReturnPostId() successfully");
    }

    @Test
    public void givenValidPostIdAndUpdatedPost_whenUpdatePost_thenReturnUpdatedPostId() throws ElementNotFoundException {
        logger.info("Performing givenValidPostIdAndUpdatedPost_whenUpdatePost_thenReturnUpdatedPostId() ...");
        Long id = PostUtils.getRandomLongId();
        PostDTO postDTO = PostUtils.getDefaultPostDTO();

        when(postService.updatePost(id, postDTO)).thenReturn(id);

        Long result = postController.updatePost(id, postDTO);

        assertNotNull(result);
        assertEquals(id, result);

        logger.info("Performed givenValidPostIdAndUpdatedPost_whenUpdatePost_thenReturnUpdatedPostId() successfully");
    }

    @Test
    public void givenValidPostId_whenDeletePost_thenReturnDeletedPostId() throws ElementNotFoundException {
        logger.info("Performing givenValidPostId_whenDeletePost_thenReturnDeletedPostId() ...");
        Long id = PostUtils.getRandomLongId();

        when(postService.deletePost(id)).thenReturn(id);

        Long result = postController.deletePost(id);

        assertNotNull(result);
        assertEquals(id, result);
        logger.info("Performed givenValidPostId_whenDeletePost_thenReturnDeletedPostId() successfully");
    }

    @Test
    public void givenRequest_whenFindTopPosts_thenReturnTopPosts() throws ElementNotFoundException {
        logger.info("Performing givenRequest_whenFindTopPosts_thenReturnTopPosts() ...");

        List<PostDTO> posts = new ArrayList<>();
        posts.add(PostDTO.builder()
                .date(new Date())
                .author("Test Author One")
                .content("This is a test post.")
                .viewCount(1)
                .build());
        posts.add(PostDTO.builder()
                .date(new Date())
                .author("Test Author Two")
                .content("This is another test post.")
                .viewCount(2)
                .build());
        posts.add(PostDTO.builder()
                .date(new Date())
                .author("Test Author Three")
                .content("This is another test post.")
                .viewCount(3)
                .build());

        when(postService.fetchTopPosts()).thenReturn(posts);

        List<PostDTO> result = postController.getTopPosts();

        assertNotNull(result);
        assertEquals(posts.size(), result.size());
        logger.info("Performed givenRequest_whenFindTopPosts_thenReturnTopPosts() successfully");
    }

    @Test
    public void givenRequest_whenFindAll_thenReturnAll() throws ElementNotFoundException {
        logger.info("Performing givenRequest_whenFindAll_thenReturnAll() ...");
        List<PostDTO> posts = new ArrayList<>();
        posts.add(PostDTO.builder()
                .date(new Date())
                .author("Test Author One")
                .content("This is a test post.")
                .viewCount(0)
                .build());
        posts.add(PostDTO.builder()
                .date(new Date())
                .author("Test Author Two")
                .content("This is another test post.")
                .viewCount(0)
                .build());

        when(postService.fetchTopPosts()).thenReturn(posts);

        List<PostDTO> result = postController.getAllPosts();
        assertNotNull(result);
        assertEquals(posts.size(), result.size());
        logger.info("Performed givenRequest_whenFindAll_thenReturnAll() successfully");
    }


    @Test
    public void givenInvalidPostDTO_thenCountViolations() {
        logger.info("Performing givenInvalidPostDTO_thenCountViolations() ...");
        PostDTO postDTO = PostDTO.builder()
                .date(null)
                .author("")
                .content("This is a test post.")
                .viewCount(-1)
                .build();

        List<ConstraintViolation<PostDTO>> violations = new ArrayList<>(validator.validate(postDTO));

        assertEquals(3, violations.size());
        logger.info("Performed givenInvalidPostDTO_thenCountViolations() successfully");
    }

    @Test
    public void givenInvalidPostId_whenGetPostById_thenThrowElementNotFoundException() throws ElementNotFoundException {
        logger.info("Performing givenInvalidPostId_whenGetPostById_thenThrowElementNotFoundException()  ...");

        // Generate a random id
        Long id = PostUtils.getRandomLongId();

        // Setup Mocks
        when(postService.fetchPost(anyLong())).thenThrow(new ElementNotFoundException());

        // Perform a GET request to the endpoint with an invalid ID
        assertThrows(ElementNotFoundException.class, () -> postController.getPostById(id));
        logger.info("Performed givenInvalidPostId_whenGetPostById_thenThrowElementNotFoundException() successfully");
    }

    @Test
    void givenInvalidPostId_whenDeletePost_thenThrowElementNotFoundException() throws Exception {
        logger.info("Performing givenInvalidPostId_whenDeletePost_thenThrowElementNotFoundException() ...");

        // Generate a random id
        Long id = PostUtils.getRandomLongId();

        // Setup Mocks
        when(postService.deletePost(anyLong())).thenThrow(new ElementNotFoundException("Post not found with ID " + id));

        // Perform a GET request to the endpoint with an invalid ID
        assertThrows(ElementNotFoundException.class, () -> postController.deletePost(id));

        logger.info("Performed givenInvalidPostId_whenDeletePost_thenThrowElementNotFoundException() successfully");

    }

    @Test
    void givenInvalidPostId_whenUpdatePost_thenThrowElementNotFoundException() throws Exception {
        logger.info("Performing givenInvalidPostId_whenUpdatePost_thenThrowElementNotFoundException() ...");
        Long id = PostUtils.getRandomLongId();
        PostDTO postDTO = PostDTO.builder()
                .date(new Date())
                .author("Test Author")
                .content("This is a test post.")
                .viewCount(0)
                .build();

        when(postService.updatePost(id, postDTO)).thenThrow(new ElementNotFoundException());

        assertThrows(ElementNotFoundException.class, () -> {
            postController.updatePost(id, postDTO);
        });
        logger.info("Performed givenInvalidPostId_whenUpdatePost_thenThrowElementNotFoundException() successfully");

    }

}

