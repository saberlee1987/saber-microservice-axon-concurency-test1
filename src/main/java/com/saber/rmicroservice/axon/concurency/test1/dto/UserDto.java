package com.saber.rmicroservice.axon.concurency.test1.dto;

import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private Integer age;


}
