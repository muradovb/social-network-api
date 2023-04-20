package com.akamai.socialnetwork.service;

import com.akamai.socialnetwork.PostUtils;
import com.akamai.socialnetwork.controller.PostControllerIntegrationTest;
import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.entity.PostEntity;
import com.akamai.socialnetwork.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostControllerIntegrationTest.class);


    @Test
    public void testCreatePost() {
        logger.info("Performing testCreatePost() ...");
        // create a new post DTO object
        PostDTO postDTO = PostUtils.getDefaultPostDTO();

        // call the createPost method on the service
        Long postId = postService.createPost(postDTO);

        // verify that the post was saved to the database
        PostEntity postEntity = postRepository.findById(postId).orElseThrow();
        assertNotNull(postEntity);
        assertEquals(postEntity.getAuthor(), postDTO.getAuthor());
        assertEquals(postEntity.getContent(), postDTO.getContent());
        logger.info("Performed testCreatePost() successfully");
    }

    @Test
    public void testFetchPost() {
        logger.info("Performing testFetchPost() ...");

        // create a new post in the database
        PostEntity postEntity = PostUtils.getDefaultPostEntity();
        postEntity = postRepository.save(postEntity);

        // call the fetchPost method on the service
        PostDTO postDTO = postService.fetchPost(postEntity.getId());

        // verify that the correct post was returned
        assertNotNull(postEntity);
        assertEquals(postEntity.getAuthor(), postDTO.getAuthor());
        assertEquals(postEntity.getContent(), postDTO.getContent());

        logger.info("Performed testFetchPost() successfully");
    }

    @Test
    public void testUpdatePost() {
        logger.info("Performing testUpdatePost() ...");

        // create a new post in the database
        PostEntity postEntity = PostUtils.getDefaultPostEntity();
        postEntity = postRepository.save(postEntity);

        // create a new post DTO object with updated content
        PostDTO postDTO = new PostDTO();
        postDTO.setAuthor(postEntity.getAuthor());
        postDTO.setContent("This is an updated test post.");

        // call the updatePost method on the service
        postService.updatePost(postEntity.getId(), postDTO);

        // retrieve the updated post from the database
        PostEntity updatedPostEntity = postRepository.findById(postEntity.getId()).orElseThrow();

        // verify that the post content was updated
        assertNotNull(updatedPostEntity);
        assertEquals(updatedPostEntity.getAuthor(), postDTO.getAuthor());
        assertEquals(updatedPostEntity.getContent(), postDTO.getContent());
        logger.info("Performed testUpdatePost() successfully");
    }

    @Test
    public void testDeletePost() {
        logger.info("Performing testDeletePost() ...");

        // create a new post in the database
        PostEntity postEntity = new PostEntity();
        postEntity.setAuthor("Jane Doe");
        postEntity.setContent("This is another test post.");
        postEntity = postRepository.save(postEntity);

        // call the deletePost method on the service
        postService.deletePost(postEntity.getId());

        // verify that the post was deleted from the database
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postEntity.getId());
        assertThat(optionalPostEntity.isEmpty());
        logger.info("Performed testDeletePost() successfully");
    }

    @Test
    public void testFetchTopPosts() {
        logger.info("Performing testFetchTopPosts() ...");

        // create some posts in the database with different view counts
        List<PostEntity> postEntityList = new ArrayList<>();
        PostEntity postEntity1 = PostUtils.getCustomPostEntity("Test Author 1", "Test Content", 10);
        postRepository.save(postEntity1);
        postEntityList.add(postEntity1);

        PostEntity postEntity2 = PostUtils.getCustomPostEntity("Test Author 2", "Test Content", 20);
        postRepository.save(postEntity2);
        postEntityList.add(postEntity2);

        PostEntity postEntity3 = PostUtils.getCustomPostEntity("Test Author 3", "Test Content", 5);
        postRepository.save(postEntity3);

        // fetch the top posts from db
        List<PostDTO> topPosts = postService.fetchTopPosts();

        // fetch the top posts from db
        List<PostDTO> postDTOList = postService.fetchTopPosts();

        // check that the returned list is not null
        assertNotNull(postDTOList);

        // check that the returned list contains the correct elements in the correct order
        assertEquals(postEntity1.getAuthor(), postDTOList.get(1).getAuthor());
        assertEquals(postEntity1.getContent(), postDTOList.get(1).getContent());
        assertEquals(postEntity1.getViewCount(), postDTOList.get(1).getViewCount());

        assertEquals(postEntity2.getAuthor(), postDTOList.get(0).getAuthor());
        assertEquals(postEntity2.getContent(), postDTOList.get(0).getContent());
        assertEquals(postEntity2.getViewCount(), postDTOList.get(0).getViewCount());
        logger.info("Performed testFetchTopPosts() successfully");

    }
}

