package com.akaci.twotterbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProfileJpaEntity {

    @Id
    @Column(length = 20)
    private String name;

    @Column(length = 200)
    private String description;

}
