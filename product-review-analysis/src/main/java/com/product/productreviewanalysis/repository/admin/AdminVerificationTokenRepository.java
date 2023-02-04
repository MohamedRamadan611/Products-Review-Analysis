package com.product.productreviewanalysis.repository.admin;

import com.product.productreviewanalysis.entity.admin.AdminVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminVerificationTokenRepository extends JpaRepository<AdminVerificationToken , Integer> {
    AdminVerificationToken findByToken(String token);
}
