package com.product.productreviewanalysis.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Admin-Password-Token")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminPasswordToken {
    @Id
    @GeneratedValue
    private int passwordId;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_password_token_id" , nullable = false , foreignKey = @ForeignKey(name = "FK_ADMIN_PASSWORD_TOKEN"))
    private Admin admin;

    public AdminPasswordToken(Admin admin , String token)
    {
        this.admin = admin;
        this.token = token;
        this.expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 10 );
    }

}
