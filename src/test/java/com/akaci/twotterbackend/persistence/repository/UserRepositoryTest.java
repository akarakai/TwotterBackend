package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
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
        UserJpaEntity user = new UserJpaEntity();
        // follow these users
        Set<UserJpaEntity> followed = new HashSet<>();
        followed.add(new UserJpaEntity());
        followed.add(new UserJpaEntity());
        followed.add(new UserJpaEntity());
    }

    @Test
    void test() {






    }
}
