package com.product.productreviewanalysis.dto.admin;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPasswordInfo {

    @Email
    private String email;
    private String password;
    private String newPassword;
}
