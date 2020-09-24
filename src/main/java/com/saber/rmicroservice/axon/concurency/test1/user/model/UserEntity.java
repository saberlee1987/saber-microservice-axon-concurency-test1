package com.saber.rmicroservice.axon.concurency.test1.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.axonframework.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class UserEntity extends AbstractAggregateRoot<Long> {
    @Id
    private Long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "age")
    private Integer age;

    @Override
    public Long getIdentifier() {
        return this.id;
    }

}
