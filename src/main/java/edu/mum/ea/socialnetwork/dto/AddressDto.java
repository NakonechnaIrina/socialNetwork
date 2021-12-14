package edu.mum.ea.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    private String id;
    private String country;
    private String state;
    private String city;
}
