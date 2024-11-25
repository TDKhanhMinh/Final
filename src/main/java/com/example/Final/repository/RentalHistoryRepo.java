package com.example.Final.repository;

import com.example.Final.entity.rentalservice.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalHistoryRepo extends JpaRepository<RentalHistory,Integer> {
}
