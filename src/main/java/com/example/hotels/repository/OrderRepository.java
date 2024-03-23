package com.example.hotels.repository;

import com.example.hotels.models.Hotel;
import com.example.hotels.models.Order;
import com.example.hotels.models.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("Select ord from Order ord WHERE ord.userProfile=:user")
    Iterable<Order> findOrdersByUser(@Param("user") UserProfile user);

    @Query("SELECT ord FROM Order ord WHERE ord.hotel=:hotel AND ord.startDate BETWEEN :startDate AND :endDate AND ord.endDate BETWEEN :startDate AND :endDate")
    Iterable<Order> findOrders(@Param("hotel") Hotel hotel, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}