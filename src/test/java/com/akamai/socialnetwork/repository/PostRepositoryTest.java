package com.akamai.socialnetwork.repository;

import com.akamai.socialnetwork.PostUtils;
import com.akamai.socialnetwork.controller.PostControllerIntegrationTest;
import com.akamai.socialnetwork.entity.PostEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostControllerIntegrationTest.class);

    @Test
    public void whenFindTop10ByOrderByView_thenReturnsTop10ByViewCount() {
        logger.info("Performing whenFindTop10ByOrderByView_thenReturnsTop10ByViewCount() ...");
        // Create some posts with different view counts
        PostEntity post1 = PostUtils.getCustomPostEntity("Test Author", "Test Content", 10);
        postRepository.save(post1);

        PostEntity post2 = PostUtils.getCustomPostEntity("Test Author 2", "Test Content", 5);
        postRepository.save(post2);

        PostEntity post3 = PostUtils.getCustomPostEntity("Test Author 3", "Test Content", 15);
        postRepository.save(post3);

        // Call the method and verify that it returns the top posts by view count
        List<PostEntity> topPosts = postRepository.findTop10ByOrderByViewCountDesc();
        assertEquals(2, topPosts.size());
        assertEquals(post3.getId(), topPosts.get(0).getId());
        assertEquals(post1.getId(), topPosts.get(1).getId());
        logger.info("Performed whenFindTop10ByOrderByView_thenReturnsTop10ByViewCount() successfully");
    }

    @Test
    public void givenValidId_whenFindById_thenReturnPost() {
        logger.info("Performing givenValidId_whenFindById_thenReturnPost() ...");
        PostEntity postEntity = PostUtils.getDefaultPostEntity();
        postRepository.save(postEntity);
        PostEntity result = postRepository.findById(postEntity.getId()).get();
        assertEquals(postEntity.getId(), result.getId());
        logger.info("Performed givenValidId_whenFindById_thenReturnPost() successfully");
    }

    @Test
    public void givenValidPostEntity_whenSave_thenCheckPersisted() {
        logger.info("Performing givenValidPostEntity_whenSave_thenCheckPersisted() ...");
        PostEntity postEntity = PostUtils.getDefaultPostEntity();
        postRepository.save(postEntity);
        PostEntity found = postRepository.findById(postEntity.getId()).get();
        assertEquals(postEntity.getId(), found.getId());
        logger.info("Performed givenValidPostEntity_whenSave_thenCheckPersisted() successfully");
    }

    @Test
    public void givenValidId_whenDelete_thenCheckDeleted() {
        logger.info("Performing givenValidId_whenDelete_thenCheckDeleted() ...");
        PostEntity postEntity = PostUtils.getDefaultPostEntity();
        postRepository.save(postEntity);
        postRepository.deleteById(postEntity.getId());
        List<PostEntity> result = new ArrayList<>();
        postRepository.findAll().forEach(e -> result.add(e));
        assertEquals(result.size(), 0);
        logger.info("Performed givenValidId_whenDelete_thenCheckDeleted() successfully");
    }

}

