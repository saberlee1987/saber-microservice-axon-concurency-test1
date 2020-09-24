package com.saber.rmicroservice.axon.concurency.test1.user.command;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationCommand {
    private Long id;
    private String username;
    private Integer age;

}
