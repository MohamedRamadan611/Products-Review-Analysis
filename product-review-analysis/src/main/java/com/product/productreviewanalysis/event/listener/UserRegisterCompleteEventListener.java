package com.product.productreviewanalysis.event.listener;

import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.event.user.UserRegisterCompletionEvent;
import com.product.productreviewanalysis.service.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UserRegisterCompleteEventListener implements ApplicationListener<UserRegisterCompletionEvent> {
    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(UserRegisterCompletionEvent event) {

        User user = event.getUser();

        String token = event.getToken();

        userService.saveVerificationUserToken(user,token);

        String url = event.getApplicationUrl() + "/user/verifyRegistration?token=" + token;

        log.info("Click to Verify Your Account Registration : {} " + url);

    }


}
