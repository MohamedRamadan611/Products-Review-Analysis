package com.product.productreviewanalysis.service.Admin;

import com.product.productreviewanalysis.dto.admin.AdminInfo;
import com.product.productreviewanalysis.dto.admin.AdminPasswordInfo;
import com.product.productreviewanalysis.dto.user.UserEntityToDToView;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.admin.AdminVerificationToken;

import java.util.List;

public interface AdminService {
    Admin addAdmin(AdminInfo adminInfo);

    Admin getAdminByEmail(String email);

    void saveVerificationAdminToken(Admin admin, String token);

    String verifyAdminRegistrationToken(String token);

    AdminVerificationToken generateNewAdminVerificationToken(String oldToken);

    String createAdminResetPasswordToken(String email);

    String validateAdminPasswordToken(String token);

    String saveNewAdminPassword(AdminPasswordInfo adminPasswordInfo);

    String checkAccountVerification(String email);

    public String validateAdminOldPassword(String password , String email);

    List<UserEntityToDToView> getAllUsers();
}
