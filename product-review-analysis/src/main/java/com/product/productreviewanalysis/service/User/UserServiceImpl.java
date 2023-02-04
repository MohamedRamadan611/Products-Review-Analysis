package com.product.productreviewanalysis.service.User;

import com.product.productreviewanalysis.dto.user.UserDtoToEntity;
import com.product.productreviewanalysis.dto.user.UserPasswordInfo;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.entity.user.UserPasswordToken;
import com.product.productreviewanalysis.entity.user.UserVerificationToken;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.repository.user.UserPasswordTokenRepository;
import com.product.productreviewanalysis.repository.user.UserRepository;
import com.product.productreviewanalysis.repository.user.UserVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private UserPasswordTokenRepository tokenPasswordRepository;
    @Autowired
    private UserVerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User addUser(UserDtoToEntity UserDtoToEntity) {
        User user = User.builder()
                .name(UserDtoToEntity.getName())
                .email(UserDtoToEntity.getEmail())
                .password(passwordEncoder.encode(UserDtoToEntity.getPassword()))
                .mobile(UserDtoToEntity.getMobile())
                .role("ROLE_USER")
                .verified("NOT_VERIFIED")
                .build();
        UserRepository.save(user);
        return user;
    }
    @Override
    public User getUserByEmail(String email) {
        return UserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Your Account is not Found"));
    }
    @Override
    public void saveVerificationUserToken(User User, String token) {
        UserVerificationToken UserVerificationToken = new UserVerificationToken(User,token);
        tokenRepository.save(UserVerificationToken);
    }

    @Override
    public String verifyUserRegistrationToken(String token) {

        System.out.println("Token is in Function " + token);
        UserVerificationToken UserVerificationToken = tokenRepository.findByToken(token);
        User User = UserVerificationToken.getUser();

        if(UserVerificationToken == null) {
            throw new UserNotFoundException("Your token is not valid");
        }
        if(new Date(System.currentTimeMillis()).after(UserVerificationToken.getExpirationTime()))
        {
            tokenRepository.delete(UserVerificationToken);
            throw new UserNotFoundException("Your token is Expired");
        }
        User.setVerified("Verified");
        UserRepository.save(User);
        return "valid";
    }

    @Override
    public UserVerificationToken generateNewUserVerificationToken(String oldToken) {
        UserVerificationToken UserVerificationToken = tokenRepository.findByToken(oldToken);
        UserVerificationToken.setToken(UUID.randomUUID().toString());
        tokenRepository.save(UserVerificationToken);
        return UserVerificationToken;
    }

    @Override
    public String createUserResetPasswordToken(String email) {
        User User = UserRepository.findByEmail(email).get();
        String token = UUID.randomUUID().toString();
        UserPasswordToken UserPasswordToken = new UserPasswordToken(User,token);
        tokenPasswordRepository.save(UserPasswordToken);
        return token;
    }
    @Override
    public String validateUserPasswordToken(String token) {
        UserPasswordToken UserPasswordToken = tokenPasswordRepository.findByToken(token);
        User User = UserPasswordToken.getUser();
        if(User == null)
            throw new UserNotFoundException("Your token is not valid");
        if(new Date(System.currentTimeMillis()).after(UserPasswordToken.getExpirationTime()))
            throw new UserNotFoundException("Your token is Expired");

        return "Valid";
    }

    @Override
    public String saveNewUserPassword(UserPasswordInfo UserPasswordInfo) {
        String result = validateUserOldPassword(UserPasswordInfo.getPassword() , UserPasswordInfo.getEmail());
        if(!result.equalsIgnoreCase("valid"))
            return "Your Password Is Wrong ! ";

        User User = UserRepository.findByEmail(UserPasswordInfo.getEmail()).get();
        User.setPassword(passwordEncoder.encode(UserPasswordInfo.getNewPassword()));
        UserRepository.save(User);
        return "Your Password is Changed !";
    }

    @Override
    public String checkAccountVerification(String email) {
        User User = UserRepository.findByEmail(email).get();
        if(User.getVerified().equalsIgnoreCase("Verified"))
            return "Valid";
        throw new UserNotFoundException("Your Password is not Verified");
    }

    @Override
    public String validateUserOldPassword(String password , String email) {
        User User = UserRepository.findByEmail(email).get();
        if(passwordEncoder.matches(password , User.getPassword()))
            return "valid";
        throw new UserNotFoundException("Your Password is Wrong");
    }
}
