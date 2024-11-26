package com.example.Final.repository;

import com.example.Final.entity.listingservice.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepo extends JpaRepository<Images, Integer> {
}
