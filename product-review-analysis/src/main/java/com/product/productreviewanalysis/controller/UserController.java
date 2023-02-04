package com.product.productreviewanalysis.controller;

import com.product.productreviewanalysis.dto.user.UserDtoToEntity;
import com.product.productreviewanalysis.dto.user.UserPasswordInfo;
import com.product.productreviewanalysis.dto.AuthenticationRequest;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.entity.user.UserVerificationToken;
import com.product.productreviewanalysis.event.user.UserRegisterCompletionEvent;
import com.product.productreviewanalysis.exceptions.UserNotFoundException;
import com.product.productreviewanalysis.service.User.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService UserService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/welcome")
    public String getWelcome()
    {
        return "<h1>Your User Page </h1>";
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser (@RequestBody @Valid UserDtoToEntity UserDtoToEntity, HttpServletRequest request)
    {
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(UserService.addUser(UserDtoToEntity), HttpStatus.CREATED);
        String token = UUID.randomUUID().toString();
        publisher.publishEvent(new UserRegisterCompletionEvent(userResponseEntity.getBody() , applicationUrl(request) ,token));
        HttpHeaders headers = new HttpHeaders();
        headers.set("token" , token);
        String url = applicationUrl(request) + "/user/verifyRegistration?token=" + token;
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(url);
    }

    @GetMapping("/verifyRegistration")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String verifyUserRegistrationToken (@RequestParam("token") String token)
    {
        String userValid = UserService.verifyUserRegistrationToken(token);

        if(userValid.equalsIgnoreCase("valid"))
            return "Your Account is Verified !";
        return "You Should Verify Your Account !";
    }

    @GetMapping("/resendVerification")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> resendVerificationUserToken(@RequestParam ("oldToken") String oldToken , HttpServletRequest request)
    {
        UserVerificationToken userVerificationToken = UserService.generateNewUserVerificationToken(oldToken);
        User User = userVerificationToken.getUser();
        if(User.getVerified().equalsIgnoreCase("verified"))
            return ResponseEntity.ok("Your Account is Already Verified");

        HttpHeaders headers = new HttpHeaders();
        headers.add("resend_verification" , userVerificationToken.getToken());
        String url = applicationUrl(request) + "/user/resend_verification?token=" + userVerificationToken.getToken();
        log.info("Click to Verify Your Account : " + url);
        return ResponseEntity.ok().headers(headers).body("Your Resend Verification Link is Ready");
    }
    @PostMapping("/resetPassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> resetUserPassword (@RequestParam ("email") String email , HttpServletRequest request)
    {
        String result = UserService.checkAccountVerification(email);
        if(!result.equalsIgnoreCase("valid"))
            return ResponseEntity.ok("You Can't Change your Account Password Before Get Verified");
        String token = UserService.createUserResetPasswordToken(email);

        String url = applicationUrl(request) + "/user/savePassword?token=" + token;
        HttpHeaders headers = new HttpHeaders();
        headers.add("reset_password_token" , token);
        log.info("Click The Link to Reset Your Password : " + url);
        return ResponseEntity.ok().headers(headers).body("Your Reset Password Link is Ready");
    }
    @PostMapping("/savePassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String saveUserPassword (@RequestParam String token , @RequestBody @Valid UserPasswordInfo userPasswordInfo)
    {
        String result = UserService.validateUserPasswordToken(token);
        if(!result.equalsIgnoreCase("valid"))
            return "Your Account is Not Found";

        return UserService.saveNewUserPassword(userPasswordInfo);
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
        throw new UserNotFoundException("Your Account is Not Found");
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String changePassword(@RequestBody @Valid UserPasswordInfo userPasswordInfo)
    {
        return UserService.saveNewUserPassword(userPasswordInfo);
    }
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail (@RequestParam("email") String email)
    {
        return ResponseEntity.ok(UserService.getUserByEmail(email));
    }

}
