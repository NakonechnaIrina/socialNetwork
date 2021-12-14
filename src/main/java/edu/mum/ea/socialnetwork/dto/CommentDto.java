package edu.mum.ea.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private String id;

    private String text;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate creationDate = LocalDate.now();

    private UserDto user;

    private PostDto post;
}
