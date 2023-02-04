package com.product.productreviewanalysis.repository.user;

import com.product.productreviewanalysis.entity.user.UserPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordTokenRepository extends JpaRepository<UserPasswordToken , Integer> {
    UserPasswordToken findByToken(String token);
}
