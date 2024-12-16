package com.example.proj.repository.user;

import com.example.proj.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<User,String> {
    @Query("{'username': ?0}")
    Optional<User> findUserByUsername(String username);
}
