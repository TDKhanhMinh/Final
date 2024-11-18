package com.example.Final.repository;

import com.example.Final.entity.listingservice.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepo extends JpaRepository<Properties,Integer > {
}
