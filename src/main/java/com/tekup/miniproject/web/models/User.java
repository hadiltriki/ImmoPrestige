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
    private String  name;
    private String role;
    public User(String name, String role) {
        this.name = name;
        this.role = role;
    } 

    
}
