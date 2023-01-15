package com.drei.demo.repository;

import com.drei.demo.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Integer> {
}