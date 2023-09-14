package com.example.PhucShop.repository;

import com.example.PhucShop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.Inet4Address;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findByName(String roleName);
}
