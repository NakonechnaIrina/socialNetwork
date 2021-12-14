package edu.mum.ea.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "comments")
public class Comments {
  @Id
    private String id;

    @NotEmpty
    private String text;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate creationDate = LocalDate.now();


    private String userId;

    private String postId;

    public void addPost(Post p) {
        this.setPostId(p.getId());
        p.addComment(this);
    }
}
