package com.example.hotels.repository;

import com.example.hotels.models.User;
import com.example.hotels.models.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    @Query("Select up from UserProfile up WHERE up.user=:user")
    UserProfile findByUserKey(@Param("user") User user);
}
