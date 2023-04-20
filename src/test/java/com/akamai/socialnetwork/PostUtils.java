package com.akamai.socialnetwork;

import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.entity.PostEntity;

import java.util.Date;
import java.util.Random;

public interface PostUtils {
    Random random = new Random();
    static PostEntity getDefaultPostEntity() {
        PostEntity postEntity = new PostEntity();
        Long id = random.nextLong();
        postEntity.setId(id);
        postEntity.setAuthor("Test Author");
        postEntity.setContent("Test Content");
        postEntity.setDate(new Date());
        postEntity.setViewCount(0);
        return postEntity;
    }

    static PostEntity getCustomPostEntity(String author, String content, Number viewCount) {
        PostEntity postEntity = new PostEntity();
        Long id = random.nextLong();
        postEntity.setId(id);
        postEntity.setAuthor(author);
        postEntity.setContent(content);
        postEntity.setDate(new Date());
        postEntity.setViewCount(viewCount);
        return  postEntity;
    }

    static PostDTO getDefaultPostDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setAuthor("Test Author");
        postDTO.setContent("Test Content");
        postDTO.setDate(new Date());
        postDTO.setViewCount(0);
        return postDTO;
    }

    static PostDTO getCustomPostDto(String author, String content, Number viewCount) {
        PostDTO postDTO = new PostDTO();
        postDTO.setAuthor(author);
        postDTO.setContent(content);
        postDTO.setDate(new Date());
        postDTO.setViewCount(viewCount);
        return postDTO;
    }

    static PostDTO getCustomPostDtoWithId(String author, String content, Number viewCount, Long id) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(id);
        postDTO.setAuthor(author);
        postDTO.setContent(content);
        postDTO.setDate(new Date());
        postDTO.setViewCount(viewCount);
        return postDTO;
    }

    static PostDTO getDefaultPostDTOWithId() {
        Long id = random.nextLong();
        PostDTO postDTO = new PostDTO();
        postDTO.setAuthor("Test Author");
        postDTO.setContent("Test Content");
        postDTO.setId(id);
        postDTO.setDate(new Date());
        postDTO.setViewCount(0);
        return postDTO;
    }

    static Long getRandomLongId() {
        return random.nextLong();
    }
}