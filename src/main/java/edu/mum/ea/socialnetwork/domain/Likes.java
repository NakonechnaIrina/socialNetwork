package edu.mum.ea.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "likes")
public class Likes {
    @Id
    private String id;


    private String userId;


    private String postId;

    public void addPost(Post p) {
        this.setPostId(p.getId());
        p.addLike(this);
    }
}
