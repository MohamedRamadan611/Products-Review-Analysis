package com.product.productreviewanalysis.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Admin-Verification-Token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVerificationToken {

    @Id
    @GeneratedValue
    private int tokenID;
    private String token;

    private Date expirationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_token_id" , nullable = false , foreignKey = @ForeignKey(name = "FK_ADMIN_VERIFICATION_TOKEN"))
    private Admin admin;

    public AdminVerificationToken(Admin admin , String token)
    {
        this.admin = admin;
        this.token = token;
        this.expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
    }

}
