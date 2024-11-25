package com.example.Final.repository;

import com.example.Final.entity.salesservice.SalesHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesHistoryRepo extends JpaRepository<SalesHistory,Integer> {
}
