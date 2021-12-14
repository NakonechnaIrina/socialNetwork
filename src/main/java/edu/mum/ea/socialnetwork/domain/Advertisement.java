package edu.mum.ea.socialnetwork.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "advertisement")
public class Advertisement {

    @Id
    private String id;
    @NotBlank(message = "{Ad.text.empty}")
    @Size(min = 5, max = 255, message = "{Ad.text.size}")
    private String text;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate = LocalDate.now();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "{Ad.expiryDate.futureOfPresent}")
    private LocalDate expiryDate;
    private boolean enabled = true;


    private String targetedCustomerCriteriaId;

    public Advertisement(String text, LocalDate creationDate, LocalDate expiryDate) {
        this.text = text;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
    }
}
