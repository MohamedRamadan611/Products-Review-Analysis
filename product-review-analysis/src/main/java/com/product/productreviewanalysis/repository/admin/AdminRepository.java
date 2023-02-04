package com.product.productreviewanalysis.repository.admin;

import com.product.productreviewanalysis.entity.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {
    Optional<Admin> findByName(String name);
    Optional<Admin> findByEmail(String email);
}
