package com.example.Final.repository;

import com.example.Final.entity.rentalservice.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepo extends JpaRepository<RentalHistory,Integer> {
}
