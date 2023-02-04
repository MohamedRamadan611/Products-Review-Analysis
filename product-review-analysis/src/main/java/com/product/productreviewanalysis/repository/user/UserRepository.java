package com.product.productreviewanalysis.repository.user;

import com.product.productreviewanalysis.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);
}
