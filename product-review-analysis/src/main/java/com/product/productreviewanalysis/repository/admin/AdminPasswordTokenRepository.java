package com.product.productreviewanalysis.repository.admin;

import com.product.productreviewanalysis.entity.admin.AdminPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPasswordTokenRepository extends JpaRepository<AdminPasswordToken , Integer> {
    AdminPasswordToken findByToken(String token);
}
