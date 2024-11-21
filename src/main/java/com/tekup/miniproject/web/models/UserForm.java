package com.tekup.miniproject.web.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserForm {
        
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 6 characters long")
    private String password;

    private boolean isAdmin;

    private String image;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;


}
