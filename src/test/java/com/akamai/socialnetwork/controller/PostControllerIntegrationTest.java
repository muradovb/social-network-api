package com.akamai.socialnetwork.controller;

import com.akamai.socialnetwork.PostUtils;
import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostControllerIntegrationTest.class);

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void testGetPostById() throws Exception {
        logger.info("Performing testGetPostById() ...");
        PostDTO postDTO = PostUtils.getDefaultPostDTO();
        Long postId = postService.createPost(postDTO);

        mockMvc.perform(get("/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(postId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Test Author")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Test Content")));

        logger.info("Performed testGetPostById() successfully");
    }

    @Test
    public void testCreatePost() throws Exception {
        logger.info("Performing testCreatePost() ...");
        PostDTO postDTO = PostUtils.getDefaultPostDTO();
        String json = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.isA(Number.class)));
        logger.info("Performed testCreatePost() successfully");
    }

    @Test
    public void testUpdatePost() throws Exception {
        logger.info("Performing testUpdatePost() ...");
        PostDTO postDTO = PostUtils.getDefaultPostDTO();
        Long postId = postService.createPost(postDTO);

        PostDTO updatedPostDto = PostUtils.getCustomPostDto("author", "updated content", 2);
        updatedPostDto.setId(postId);
        String json = objectMapper.writeValueAsString(updatedPostDto);

        mockMvc.perform(put("/posts/update" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.isA(Number.class)));
        logger.info("Performed testUpdatePost() successfully");
    }

    @Test
    public void testDeletePost() throws Exception {
        logger.info("Performing testDeletePost() ...");
        PostDTO postDTO = PostUtils.getDefaultPostDTO();
        Long postId = postService.createPost(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/delete" + postId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.isA(Number.class)));
        logger.info("Performed testDeletePost() successfully");
    }

    @Test
    public void testGetAllPosts() throws Exception {
        logger.info("Performing testGetAllPost() ...");
        PostDTO postDTO = PostUtils.getDefaultPostDTO();
        postService.createPost(postDTO);
        mockMvc.perform(get("/posts/getAll"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.isA(List.class)));
        logger.info("Performed testGetAllPost() successfully");
    }

    @Test
    public void testGetPostByIdNotFoundException() throws Exception {
        logger.info("Performing testGetPostByIdNotFoundException() ...");
        Long invalidId = PostUtils.getRandomLongId();

        mockMvc.perform(get("/posts/getById/{id}", invalidId))
                .andExpect(status().isNotFound());
        logger.info("Performed testGetPostByIdNotFoundException() successfully");
    }

    @Test
    public void testUpdatePostNotFoundException() throws Exception {
        logger.info("Performing testUpdatePostNotFoundException() ...");

        Long invalidId = PostUtils.getRandomLongId();

        PostDTO post = PostUtils.getDefaultPostDTO();

        mockMvc.perform(put("/posts/update/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(post)))
                .andExpect(status().isNotFound());
        logger.info("Performed testUpdatePostNotFoundException() successfully");
    }

    @Test
    public void testDeletePostNotFoundException() throws Exception {
        logger.info("Performing testDeletePostNotFoundException() ...");
        Long invalidId = PostUtils.getRandomLongId();

        mockMvc.perform(delete("/posts/delete/{id}", invalidId))
                .andExpect(status().isNotFound());
        logger.info("Performed testDeletePostNotFoundException() successfully");
    }


}

