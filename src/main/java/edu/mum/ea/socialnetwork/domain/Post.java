package edu.mum.ea.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "post")
public class Post implements Serializable {

    @Id
    private String id;
    private String text;
    private String photo;
    private String video;


    private Date creationDate = new Date();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deletionDate;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private boolean unhealthy = false;
    private boolean notifyAllFollowers = true;
    private boolean enabled = true;


    private String userId;


    public void addLike(Likes like) {
        this.likeCount++;
    }

    public void removeLike(Likes like) {
        this.likeCount--;
    }

    public void addComment(Comments comment) {
        this.commentCount++;
    }
}
