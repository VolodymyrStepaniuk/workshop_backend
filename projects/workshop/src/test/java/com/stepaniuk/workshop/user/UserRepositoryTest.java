package com.stepaniuk.workshop.user;

import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = "classpath:sql/users.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User user = new User(
                null, "First", "Last", "1@gmail.com", "+380502229911", "Pass123", null, null
        );

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());

        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPhone(), savedUser.getPhone());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenSavingUser(){
        User user = new User(
                null, null, "Last", "1@gmail.com", "+380502229911", "Pass123", null, null
        );

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void shouldFindUserById() {
        Optional<User> user = userRepository.findById(1L);
        assertTrue(user.isPresent());

        User foundUser = user.get();

        assertEquals(1L, foundUser.getId());
        assertEquals("First", foundUser.getFirstName());
        assertEquals("Last", foundUser.getLastName());
        assertEquals("email@gmail.com", foundUser.getEmail());
        assertEquals("+380682019211", foundUser.getPhone());
        assertEquals("Password123!", foundUser.getPassword());
        assertEquals(Instant.parse("2024-11-23T15:22:09.266615Z"), foundUser.getCreatedAt());
        assertEquals(Instant.parse("2024-11-25T17:28:19.266615Z"), foundUser.getLastModifiedAt());
    }

    @Test
    void shouldFindUserByEmail(){
        Optional<User> user = userRepository.findByEmail("email@gmail.com");
        assertTrue(user.isPresent());

        User foundUser = user.get();

        assertEquals(1L, foundUser.getId());
        assertEquals("First", foundUser.getFirstName());
        assertEquals("Last", foundUser.getLastName());
        assertEquals("email@gmail.com", foundUser.getEmail());
        assertEquals("+380682019211", foundUser.getPhone());
        assertEquals("Password123!", foundUser.getPassword());
        assertEquals(Instant.parse("2024-11-23T15:22:09.266615Z"), foundUser.getCreatedAt());
        assertEquals(Instant.parse("2024-11-25T17:28:19.266615Z"), foundUser.getLastModifiedAt());
    }

    @Test
    void shouldFindUserByPhone(){
        Optional<User> user = userRepository.findByPhone("+380772228191");
        assertTrue(user.isPresent());

        User foundUser = user.get();

        assertEquals(2L, foundUser.getId());
        assertEquals("Second", foundUser.getFirstName());
        assertEquals("Second", foundUser.getLastName());
        assertEquals("email@custom.com", foundUser.getEmail());
        assertEquals("+380772228191", foundUser.getPhone());
        assertEquals("Pass!911", foundUser.getPassword());
        assertEquals(Instant.parse("2024-12-23T15:22:09.266615Z"), foundUser.getCreatedAt());
        assertEquals(Instant.parse("2024-12-25T17:28:19.266615Z"), foundUser.getLastModifiedAt());
    }

    @Test
    void shouldUpdateUser() {
        Optional<User> userOptional = userRepository.findById(1L);
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        user.setFirstName("UpdatedFirstName");
        user.setLastName("UpdatedLastName");

        User updatedUser = userRepository.save(user);

        assertNotNull(updatedUser);
        assertEquals(1L, updatedUser.getId());
        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
        assertEquals("UpdatedLastName", updatedUser.getLastName());
    }

    @Test
    void shouldDeleteUserByExistingUser(){
        Optional<User> userOptional = userRepository.findById(1L);
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        userRepository.delete(user);

        assertFalse(userRepository.existsById(1L));
    }

    @Test
    void shouldDeleteUserById(){
        userRepository.deleteById(1L);

        assertFalse(userRepository.existsById(1L));
    }

    @Test
    void shouldReturnListOfUsers() {
        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void shouldReturnPageOfUsers(){
        Page<User> userPage = userRepository.findAll(PageRequest.of(0,2));

        assertNotNull(userPage);
        assertFalse(userPage.isEmpty());

        List<User> users = userPage.getContent();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenExistingByEmail(){
        assertTrue(userRepository.existsByEmail("email@gmail.com"));
    }

    @Test
    void shouldReturnTrueWhenExistingByPhone(){
        assertTrue(userRepository.existsByPhone("+380682019211"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"@gmail.com", "@custom.com"})
    void shouldReturnListOfUsersWhereEmailContaining(String partOfEmail){
        Specification<User> specification = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("email"), "%" + partOfEmail + "%")
        );

        List<User> users = userRepository.findAll(specification);

        assertNotNull(users);
        assertFalse(users.isEmpty());

        assertAll(
             users.stream().map(
                     user -> () -> assertTrue(user.getEmail().contains(partOfEmail))
             )
        );
    }
}
