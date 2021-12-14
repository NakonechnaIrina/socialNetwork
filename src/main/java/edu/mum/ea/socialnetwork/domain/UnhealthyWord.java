package edu.mum.ea.socialnetwork.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "unhealthyWord")
public class UnhealthyWord {

    @Id
    private String id;
    @Column(length =30)
    @NotBlank(message = "{Word.blank}")
    @Size(min = 3, max = 30, message = "{Word.size}")
    private String word;

    public UnhealthyWord(String word) {
        this.word = word;
    }
}
