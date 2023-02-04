package com.product.productreviewanalysis.service.User;

import com.product.productreviewanalysis.dto.user.UserDtoToEntity;
import com.product.productreviewanalysis.dto.user.UserPasswordInfo;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.entity.user.UserVerificationToken;

public interface UserService {
    User addUser(UserDtoToEntity UserDtoToEntity);

    User getUserByEmail(String email);

    void saveVerificationUserToken(User User, String token);

    String verifyUserRegistrationToken(String token);

    UserVerificationToken generateNewUserVerificationToken(String oldToken);

    String createUserResetPasswordToken(String email);

    String validateUserPasswordToken(String token);

    String saveNewUserPassword (UserPasswordInfo UserPasswordInfo);

    String checkAccountVerification (String email);

    public String validateUserOldPassword (String password , String email);
}
