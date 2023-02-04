package com.product.productreviewanalysis.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "User-Password-Token")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPasswordToken {
    @Id
    @GeneratedValue
    private int passwordId;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_password_token_id" , nullable = false , foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public UserPasswordToken(User user , String token)
    {
        this.user = user;
        this.token = token;
        this.expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
    }

}
