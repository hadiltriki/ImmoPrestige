package com.tekup.miniproject.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;
    private String image;
    private String phone;
    private String adress;
}
