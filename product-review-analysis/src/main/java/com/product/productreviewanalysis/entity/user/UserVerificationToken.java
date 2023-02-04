package com.product.productreviewanalysis.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "User-Verification-Token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerificationToken {

    @Id
    @GeneratedValue
    private int tokenID;
    private String token;

    private Date expirationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_token_id" , nullable = false , foreignKey = @ForeignKey(name = "FK_USER_VERIFICATION_TOKEN"))
    private User user;

    public UserVerificationToken(User user , String token)
    {
        this.user = user;
        this.token = token;
        this.expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
    }

}
