package com.product.productreviewanalysis.service.Admin;

import com.product.productreviewanalysis.dto.admin.AdminInfo;
import com.product.productreviewanalysis.dto.admin.AdminPasswordInfo;
import com.product.productreviewanalysis.dto.user.UserDtoToEntity;
import com.product.productreviewanalysis.dto.user.UserEntityToDToView;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.admin.AdminPasswordToken;
import com.product.productreviewanalysis.entity.admin.AdminVerificationToken;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.repository.admin.AdminPasswordTokenRepository;
import com.product.productreviewanalysis.repository.admin.AdminRepository;
import com.product.productreviewanalysis.repository.admin.AdminVerificationTokenRepository;
import com.product.productreviewanalysis.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminPasswordTokenRepository tokenPasswordRepository;
    @Autowired
    private AdminVerificationTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Admin addAdmin(AdminInfo adminInfo) {
        Admin admin = Admin.builder()
                .name(adminInfo.getName())
                .email(adminInfo.getEmail())
                .password(passwordEncoder.encode(adminInfo.getPassword()))
                .mobile(adminInfo.getMobile())
                .role("ROLE_ADMIN")
                .verified("NOT_VERIFIED")
                .build();
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Your Account is Not Found"));
    }

    @Override
    public void saveVerificationAdminToken(Admin admin, String token) {
        AdminVerificationToken adminVerificationToken = new AdminVerificationToken(admin,token);
        tokenRepository.save(adminVerificationToken);
    }

    @Override
    public String verifyAdminRegistrationToken(String token) {

        AdminVerificationToken adminVerificationToken = tokenRepository.findByToken(token);
        Admin admin = adminVerificationToken.getAdmin();

        if(adminVerificationToken == null) {
            throw new UserNotFoundException("Your token is not valid");
        }
        if(new Date(System.currentTimeMillis()).after(adminVerificationToken.getExpirationTime()))
        {
            tokenRepository.delete(adminVerificationToken);
            throw new UserNotFoundException("Your token is Expired");
        }
        admin.setVerified("Verified");
        adminRepository.save(admin);
        return "valid";
    }

    @Override
    public AdminVerificationToken generateNewAdminVerificationToken(String oldToken) {
        AdminVerificationToken adminVerificationToken = tokenRepository.findByToken(oldToken);
        adminVerificationToken.setToken(UUID.randomUUID().toString());
        tokenRepository.save(adminVerificationToken);
        return adminVerificationToken;
    }

    @Override
    public String createAdminResetPasswordToken(String email) {
        Admin admin = adminRepository.findByEmail(email).get();
        String token = UUID.randomUUID().toString();
        AdminPasswordToken adminPasswordToken = new AdminPasswordToken(admin,token);
        tokenPasswordRepository.save(adminPasswordToken);
        return token;
    }

    @Override
    public String validateAdminPasswordToken(String token) {
        AdminPasswordToken adminPasswordToken = tokenPasswordRepository.findByToken(token);
        Admin admin = adminPasswordToken.getAdmin();
        if(admin == null)
            throw new UserNotFoundException("Your token is not valid");
        if(new Date(System.currentTimeMillis()).after(adminPasswordToken.getExpirationTime()))
            throw new UserNotFoundException("Your token is Expired");
        return "Valid";
    }

    @Override
    public String saveNewAdminPassword(AdminPasswordInfo adminPasswordInfo) {
        String result = validateAdminOldPassword(adminPasswordInfo.getPassword() , adminPasswordInfo.getEmail());
        if(!result.equalsIgnoreCase("valid"))
            return "Your Password Is Wrong ! ";

        Admin admin = adminRepository.findByEmail(adminPasswordInfo.getEmail()).get();
        admin.setPassword(passwordEncoder.encode(adminPasswordInfo.getNewPassword()));
        adminRepository.save(admin);
        return "Your Password is Changed !";
    }

    @Override
    public String checkAccountVerification(String email) {
        Admin admin = adminRepository.findByEmail(email).get();
        if(admin.getVerified().equalsIgnoreCase("Verified"))
            return "Valid";
        return "not Valid";
    }

    @Override
    public String validateAdminOldPassword(String password , String email) {
        Admin admin = adminRepository.findByEmail(email).get();
        if(passwordEncoder.matches(password , admin.getPassword()))
            return "valid";
        return "Not Valid";
    }

    @Override
    public List<UserEntityToDToView> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> UserEntityToDToView.builder()
                .name(user.getName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .role(user.getRole())
                .verified(user.getVerified())
                .build()).collect(Collectors.toList());
    }
}
