package edu.mum.ea.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "notification")
public class Notification implements Serializable {

    @Id
    private String id;

    private boolean seen = false;
    private boolean sent = false;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate creationDate = LocalDate.now();

    private String userId;

    private String postId;
}
