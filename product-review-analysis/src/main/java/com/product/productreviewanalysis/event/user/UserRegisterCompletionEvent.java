package com.product.productreviewanalysis.event.user;

import com.product.productreviewanalysis.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserRegisterCompletionEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    private String token;
    public UserRegisterCompletionEvent(User user, String url , String token) {
        super(user);
        this.user = user;
        this.applicationUrl = url;
        this.token = token;
    }
}
