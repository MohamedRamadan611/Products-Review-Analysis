package com.product.productreviewanalysis.entity.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "Admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue
    private int adminId;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String verified = "NOT_VERIFIED";
    private String role = "ROLE_ADMIN";


}
