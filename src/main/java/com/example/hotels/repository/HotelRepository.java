package com.example.hotels.repository;

import com.example.hotels.models.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Long> {

}
