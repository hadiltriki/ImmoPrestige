package com.tekup.miniproject.web.models;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryForm {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    private String name; // Name of the category (e.g., House, Apartment, Land)

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

}
