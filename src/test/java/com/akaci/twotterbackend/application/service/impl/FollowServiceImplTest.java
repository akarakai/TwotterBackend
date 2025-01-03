package com.akaci.twotterbackend.application.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {
//
//    private static final UUID USER_ID = UUID.randomUUID();
//    private static final String USER_NAME = "userName123";
//
//    private static final UUID USER_ID_TO_FOLLOW = UUID.randomUUID();
//    private static final String USER_NAME_TO_FOLLOW = "userNameToFollow1";
//
//    @InjectMocks
//    private FollowServiceImpl followService;
//
//    @Mock
//    private FollowDomainServiceImpl followDomainService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Test
//    void followUser_success() {
//        // Set up UserJpaEntity objects
//        UserJpaEntity user = new UserJpaEntity(USER_ID, USER_NAME, null, null);
//        UserJpaEntity userToFollow = new UserJpaEntity(USER_ID_TO_FOLLOW, USER_NAME_TO_FOLLOW, null, null);
//
//        // Mock userRepository behavior
//        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));
//        when(userRepository.findByUsername(USER_NAME_TO_FOLLOW)).thenReturn(Optional.of(userToFollow));
//
//        // Mock followDomainService and userRepository methods
//        doNothing().when(followDomainService).follow(any(User.class), any(User.class));
////        when(userRepository.save(any(UserJpaEntity.class))).thenReturn(Optional); // TODO
//
//        // Convert UserJpaEntity to User for domain logic
//        User userDomain = new User(USER_ID, USER_NAME, null, null);
//        User userDomainToFollow = new User(USER_ID_TO_FOLLOW, USER_NAME_TO_FOLLOW, null, null);
//
//        // Define expected followers and followed sets
//        Set<User> followed = new HashSet<>(Set.of(userDomainToFollow));
//        Set<User> follower = new HashSet<>(Set.of(userDomain));
//
//        userDomain.setFollowed(followed);
//        userDomainToFollow.setFollowers(follower);
//
//        // Act: Perform the follow operation in the service
//        User userUpdated = followService.follow(USER_NAME, USER_NAME_TO_FOLLOW);
//
//        // Assert: Verify that the user was updated correctly
//        assertEquals(USER_ID, userUpdated.getId());
//        assertEquals(1, userUpdated.getFollowed().size());  // One followed user
//        assertTrue(userUpdated.getFollowed().contains(userDomainToFollow));  // Ensure the correct user is followed
//
//        // Verify interactions with the mocks
//        verify(followDomainService).follow(any(User.class), any(User.class));
//        verify(userRepository).save(any(UserJpaEntity.class));
//    }
//
//    @Test
//    void followUser_userWasAlreadyFollowed_throwsException() {
//        UserJpaEntity user = new UserJpaEntity(USER_ID, USER_NAME, null, null);
//        UserJpaEntity userToFollow = new UserJpaEntity(USER_ID_TO_FOLLOW, USER_NAME_TO_FOLLOW, null, null);
//
//        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));
//        when(userRepository.findByUsername(USER_NAME_TO_FOLLOW)).thenReturn(Optional.of(userToFollow));
//
//        doThrow(new UserAlreadyFollowedException())
//                .when(followDomainService).follow(any(User.class), any(User.class));
//
//        // Act & Assert: Ensure UserAlreadyFollowedException is thrown
//        assertThrows(UserAlreadyFollowedException.class, () -> followService.follow(USER_NAME, USER_NAME_TO_FOLLOW));
//    }
//
//    @Test
//    void followUser_followHimself_throwsException() {
//        assertThrows(UsernameAlreadyExistsException.class, () -> followService.follow(USER_NAME, USER_NAME));
//    }
//
//    @Test
//    void followUser_usernameInvalid_throwsException() {
//        assertThrows(UsernameNotValidException.class, () -> followService.follow(USER_NAME, "123"));
//    }
}