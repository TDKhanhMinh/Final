package com.example.Final.repository;

import com.example.Final.entity.listingservice.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepo extends JpaRepository<Properties,Integer > {
}
