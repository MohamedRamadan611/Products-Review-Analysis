package com.product.productreviewanalysis.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminInfo {
    @NotNull(message = "Your Name Shouldn't Be Null")
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull(message = "Your Password Shouldn't Be Null")
    @NotBlank
    private String password;

    @NotNull
    @Pattern(regexp = "^\\d{11}$" , message = "invalid mobile number")
    private String mobile;

    private String verified = "NOT_VERIFIED";
    private String role = "ROLE_ADMIN";

}
