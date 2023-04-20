package com.akamai.socialnetwork.mapper;


import com.akamai.socialnetwork.dto.PostDTO;
import com.akamai.socialnetwork.entity.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "viewCount", source = "viewCount")
    PostEntity toPostEntity(PostDTO networkPostDTO);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "viewCount", source = "viewCount")
    PostDTO toPostDTO(PostEntity networkPostDTO);

}
