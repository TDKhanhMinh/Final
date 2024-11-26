package com.example.Final.repository;

import com.example.Final.entity.salesservice.SalesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesHistoryRepo extends JpaRepository<SalesHistory,Integer> {
}
