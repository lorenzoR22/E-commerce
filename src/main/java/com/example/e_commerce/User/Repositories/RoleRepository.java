package com.example.e_commerce.User.Repositories;

import com.example.e_commerce.User.Entities.Erole;
import com.example.e_commerce.User.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(Erole role);
}
