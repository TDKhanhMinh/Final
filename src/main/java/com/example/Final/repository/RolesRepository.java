package com.example.Final.repository;

import com.example.Final.entity.securityservice.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findRolesByName(String name);
}
