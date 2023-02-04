package com.product.productreviewanalysis.service.Admin;

import com.product.productreviewanalysis.dto.admin.AdminInfo;
import com.product.productreviewanalysis.dto.user.UserEntityToDToView;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.admin.AdminPasswordToken;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.repository.admin.AdminPasswordTokenRepository;
import com.product.productreviewanalysis.repository.admin.AdminRepository;
import com.product.productreviewanalysis.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private AdminPasswordTokenRepository passwordTokenRepository;
    @MockBean
    private UserRepository userRepository ;

    @BeforeEach
    void setUp() {
        Admin admin = Admin.builder()
                .adminId(1)
                .email("Mohamed.Ramadan611@gmail.com")
                .name("Mohamed")
                .password("Ramadan")
                .mobile("01149460094")
                .verified("VERIFIED")
                .role("ROLE_ADMIN")
                .build();

        User user = User.builder()
                .userId(1)
                .email("Ayman.Ramadan611@gmail.com")
                .name("Ayman")
                .password("Ramadan")
                .mobile("01095940196")
                .role("ROLE_USER")
                .verified("VERIFIED")
                .build();

        AdminPasswordToken adminPasswordToken = AdminPasswordToken.builder()
                .passwordId(1)
                .token("a5eafa32-2c97-4741-a3f7-90db320fb042")
                .expirationTime(new Date(System.currentTimeMillis() + 1000*60*10))
                .admin(admin)
                .build();
        Mockito.when(passwordTokenRepository.findByToken("a5eafa32-2c97-4741-a3f7-90db320fb042")).thenReturn(adminPasswordToken);
        Mockito.when(adminRepository.findByEmail("Mohamed.Ramadan611@gmail.com")).thenReturn(Optional.ofNullable(admin));
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
    }

    @Test
    @DisplayName("Add New Admin")
    void addAdmin() {
        AdminInfo adminInfo = AdminInfo.builder()
                .email("Mohamed.Ramadan611@gmail.com")
                .name("Mohamed")
                .password("Ramadan")
                .mobile("01149460094")
                .verified("VERIFIED")
                .role("ROLE_ADMIN")
                .build();

        adminService.addAdmin(adminInfo);
    }

    @Test
    @DisplayName("Get Admin By Email")
    void getAdminByEmail() {
        Admin admin = adminService.getAdminByEmail("Mohamed.Ramadan611@gmail.com");
        if(admin.equals(Optional.empty()))
            throw new UsernameNotFoundException("Your user is not Found");

        assertEquals(admin.getEmail(),("Mohamed.Ramadan611@gmail.com"));
    }

    @Test
    @DisplayName("Validate Password Token")
    void validateAdminPasswordToken() {
        AdminPasswordToken adminPasswordToken = passwordTokenRepository.findByToken("a5eafa32-2c97-4741-a3f7-90db320fb042");
        Admin admin = adminPasswordToken.getAdmin();

        if(admin.equals(Optional.empty()))
            throw new UsernameNotFoundException("Your Account is not found");

        if(new Date(System.currentTimeMillis()).after(adminPasswordToken.getExpirationTime()))
            throw new UsernameNotFoundException("Your Token is Expired");

        assertEquals(adminPasswordToken.getToken() , "a5eafa32-2c97-4741-a3f7-90db320fb042");
    }

    @Test
    @DisplayName("Get All Users")
    void getAllUsers() {
         List<UserEntityToDToView> users = adminService.getAllUsers();
         if(users.size() == 0 )
             throw new UsernameNotFoundException("There is not users");

         assertEquals(users.get(0).getEmail(), "Ayman.Ramadan611@gmail.com");
    }
}