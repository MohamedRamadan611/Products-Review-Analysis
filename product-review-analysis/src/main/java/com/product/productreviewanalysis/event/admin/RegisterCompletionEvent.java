package com.product.productreviewanalysis.event.admin;

import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.entity.user.User;
import com.product.productreviewanalysis.event.listener.RegisterCompleteEventListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegisterCompletionEvent extends ApplicationEvent {

    private Admin admin;

    private User user;

    private String token ;
    private String applicationUrl;

    public RegisterCompletionEvent(Admin admin , String url , String token)
    {
        super(admin);
        this.admin = admin;
        this.token = token;
        this.applicationUrl = url;
    }


}
