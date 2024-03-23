package com.example.hotels.repository;

import com.example.hotels.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("Select u from User u WHERE u.login=:username")
    User findByUsername(@Param("username") String username);
}
