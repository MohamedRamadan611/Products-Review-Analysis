package com.product.productreviewanalysis.repository.user;

import com.product.productreviewanalysis.entity.user.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Integer> {
    UserVerificationToken findByToken(String token);
}
