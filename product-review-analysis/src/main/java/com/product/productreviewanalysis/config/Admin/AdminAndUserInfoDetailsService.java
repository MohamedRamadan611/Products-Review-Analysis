package com.product.productreviewanalysis.config.Admin;

import com.product.productreviewanalysis.config.User.UserInfoUserDetails;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.repository.admin.AdminRepository;
import com.product.productreviewanalysis.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminAndUserInfoDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if(!user.equals(Optional.empty()))
            return user.map(UserInfoUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("Your Email Is Not Found " + email));
        if(!admin.equals(Optional.empty()))
            return admin.map(AdminInfoAdminDetails::new).orElseThrow(() -> new UsernameNotFoundException("Your Email Is Not Found " + email));
        return null;
    }
}
