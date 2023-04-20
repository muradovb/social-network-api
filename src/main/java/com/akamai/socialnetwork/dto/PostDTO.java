package com.akamai.socialnetwork.dto;

import com.akamai.socialnetwork.validation.PostDateConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;

    @NotNull
    @PostDateConstraint
    private Date date;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String author;

    @NotNull
    @NotBlank
    @Size(max = 500)
    private String content;

    @NotNull
    @Min(0)
    private Number viewCount;
}
