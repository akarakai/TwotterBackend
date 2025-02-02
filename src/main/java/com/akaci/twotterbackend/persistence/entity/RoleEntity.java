package com.akaci.twotterbackend.persistence.entity;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    @Enumerated(value = EnumType.STRING)
    private Role role;

}
