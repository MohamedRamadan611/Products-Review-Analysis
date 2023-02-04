package com.product.productreviewanalysis.event.listener;

import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.event.admin.RegisterCompletionEvent;
import com.product.productreviewanalysis.service.Admin.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegisterCompleteEventListener implements ApplicationListener<RegisterCompletionEvent> {
    @Autowired
    private  AdminService adminService ;

    private String urlSend;
    @Override
    public void onApplicationEvent(RegisterCompletionEvent event) {

        Admin admin = event.getAdmin();
        String token = event.getToken();
        adminService.saveVerificationAdminToken(admin,token);
        String url = event.getApplicationUrl() + "/admin/verifyRegistration?token=" + token;
        urlSend = url;
        log.info("Click to Verify Your Account Registation : {} " + url);
    }

}
