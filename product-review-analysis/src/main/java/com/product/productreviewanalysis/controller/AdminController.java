package com.product.productreviewanalysis.controller;

import com.product.productreviewanalysis.dto.AuthenticationRequest;
import com.product.productreviewanalysis.dto.admin.AdminInfo;
import com.product.productreviewanalysis.dto.admin.AdminPasswordInfo;
import com.product.productreviewanalysis.dto.user.UserEntityToDToView;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.admin.AdminVerificationToken;
import com.product.productreviewanalysis.event.admin.RegisterCompletionEvent;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.service.Admin.AdminService;
import com.product.productreviewanalysis.service.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/welcome")
    public String getWelcome()
    {
        return "<h1>Your Admin Page </h1>";
    }

    @PostMapping("/register")
    public ResponseEntity<String> addAdmin(@RequestBody @Valid AdminInfo adminInfo , HttpServletRequest request)
    {
        ResponseEntity<Admin> adminResponseEntity = new ResponseEntity<>(adminService.addAdmin(adminInfo), HttpStatus.CREATED);
        String token = UUID.randomUUID().toString();
        publisher.publishEvent(new RegisterCompletionEvent(adminResponseEntity.getBody() , applicationUrl(request) , token));
        HttpHeaders headers = new HttpHeaders();
        headers.set("token" , token);
        String url = applicationUrl(request) + "/admin/verifyRegistration?token=" + token;
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(url);
    }

    @GetMapping("/verifyRegistration")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String verifyAdminRegistrationToken (@RequestParam ("token") String token)
    {
        String adminValid = adminService.verifyAdminRegistrationToken(token);
        //System.out.println("Hello");
        if(adminValid.equalsIgnoreCase("valid"))
            return "Your Account is Verified !";
        return "You Should Verify Your Account !";
    }

    @GetMapping("/resendVerification")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> resendVerificationAdminToken(@RequestParam ("oldToken") String oldToken , HttpServletRequest request)
    {
        AdminVerificationToken adminVerificationToken = adminService.generateNewAdminVerificationToken(oldToken);
        Admin admin = adminVerificationToken.getAdmin();
        if(admin.getVerified().equalsIgnoreCase("verified"))
            return ResponseEntity.ok("Your Account is Already Verified");

        HttpHeaders headers = new HttpHeaders();
        headers.add("resend_verification" , adminVerificationToken.getToken());
        String url = applicationUrl(request) + "/admin/verifyRegistration?token=" + adminVerificationToken.getToken();
        log.info("Click to Verify Your Account : " + url);
        return ResponseEntity.ok().headers(headers).body("Your Resend Verification Link is Ready");
    }
    @PostMapping("/resetPassword")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> resetAdminPassword (@RequestParam ("email") String email , HttpServletRequest request)
    {
        String result = adminService.checkAccountVerification(email);
        if(!result.equalsIgnoreCase("valid"))
            throw new UsernameNotFoundException("You Can't Change your Account Password Before Get Verified");
        String token = adminService.createAdminResetPasswordToken(email);

        String url = applicationUrl(request) + "/admin/savePassword?token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.add("reset_password_token" , token);
        log.info("Click The Link to Reset Your Password : " + url);
        return ResponseEntity.ok().headers(headers).body("Your Reset Password Link is Ready");
    }
    @PostMapping("/savePassword")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String saveAdminPassword (@RequestParam String token , @RequestBody @Valid AdminPasswordInfo adminPasswordInfo)
    {
            String result = adminService.validateAdminPasswordToken(token);
            if(!result.equalsIgnoreCase("valid"))
                throw new UserNotFoundException("Your Password is Wrong");

            return adminService.saveNewAdminPassword(adminPasswordInfo);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthenticationRequest authenticationRequest)
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail() , authenticationRequest.getPassword()));

        HttpHeaders headers = new HttpHeaders();

        if(authentication.isAuthenticated()) {
            headers.add("authorize_token", jwtService.generateToken(authenticationRequest.getEmail()));
            return ResponseEntity.ok().headers(headers).body("Your token is ready");
        }

        return ResponseEntity.ok("Your Account is Not Found");
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody @Valid AdminPasswordInfo adminPasswordInfo)
    {
        return adminService.saveNewAdminPassword(adminPasswordInfo);
    }
    @GetMapping("/email")
    public ResponseEntity<Admin> getAdminByEmail (@RequestParam("email") String email)
    {
        return ResponseEntity.ok(adminService.getAdminByEmail(email));
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserEntityToDToView>> getAllUsers()
    {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

}
