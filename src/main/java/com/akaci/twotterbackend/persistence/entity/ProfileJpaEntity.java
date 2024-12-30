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
    private String name;
    private String description;

}
