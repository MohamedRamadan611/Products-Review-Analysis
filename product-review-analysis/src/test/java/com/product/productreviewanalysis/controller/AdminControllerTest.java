package com.product.productreviewanalysis.controller;

import com.product.productreviewanalysis.config.SecurityConfig;
import com.product.productreviewanalysis.dto.admin.AdminInfo;
import com.product.productreviewanalysis.entity.admin.Admin;
import com.product.productreviewanalysis.security.JwtAuth;
import com.product.productreviewanalysis.service.Admin.AdminService;
import com.product.productreviewanalysis.service.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(AdminController.class)

class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private AdminService adminService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuth jwtAuth;
    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;
    @Autowired
    private SecurityConfig securityConfig;
    private Admin admin;
    private HttpServletRequest request;
/*    private MockHttpServletResponse mockHttpServletResponse;

    private String token;*/
    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin = Admin.builder()
                .adminId(1)
                .email("Mohamed.Ramadan611@gmail.com")
                .name("Mohamed")
                .password("Ramadan")
                .mobile("01149460094")
                .verified("VERIFIED")
                .role("ROLE_ADMIN")
                .build();

      }

    @Test
    @DisplayName("Add New Admin")
    void addAdmin() throws Exception{
        AdminInfo adminInfo = new AdminInfo();
                adminInfo.setEmail("Mohamed.Ramadan611@gmail.com");
                adminInfo.setName("Mohamed");
                adminInfo.setPassword("Ramadan");
                adminInfo.setMobile("01149460094");
                adminInfo.setVerified("VERIFIED");
                adminInfo.setRole("ROLE_ADMIN");

        Mockito.when(adminService.addAdmin(adminInfo)).thenReturn(admin);
       /* HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + token);*/

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/register")
               // .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\" : \"Mohamed\",\n" +
                        "    \"email\" : \"Mohamed.Ramadan611@gmail.com\",\n" +
                        "    \"password\" : \"Ramadan\",\n" +
                        "    \"mobile\" : \"01149460094\"\n" +
                        "}")).andExpect(MockMvcResultMatchers.status().is(201));

        //publisher.publishEvent(new RegisterCompletionEvent(admin,applicationUrl(request)));
        assertEquals(adminInfo.getName() , admin.getName());
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    @Test
    void verifyAdminRegistrationToken() {
    }
    @Test
    void saveAdminPassword() {
    }
    @Test
    @DisplayName("Get Admin By Email")
    void getAdminByEmail() throws Exception {
        Admin admin = adminService.getAdminByEmail("Mohamed.Ramadan611@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/email")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(admin.getEmail()));
    }
}