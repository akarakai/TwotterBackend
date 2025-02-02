package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        // follow these users
        Set<UserEntity> followed = new HashSet<>();
        followed.add(new UserEntity());
        followed.add(new UserEntity());
        followed.add(new UserEntity());
    }

    @Test
    void test() {






    }
}
