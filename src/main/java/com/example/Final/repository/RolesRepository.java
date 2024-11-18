package com.example.Final.repository;

import com.example.Final.entity.securityservice.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findRolesByName(String name);
}
