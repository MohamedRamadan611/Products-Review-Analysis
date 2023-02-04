package com.product.productreviewanalysis.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityToDToView {
    private String name;
    private String email;
    private String mobile;
    private String verified;
    private String role;
}
