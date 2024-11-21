package com.tekup.miniproject.web.models;


import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AdForm {

    @NotBlank(message = "The title is required.")
    @Size(max = 50, message = "The title cannot exceed 50 characters.")
    private String title;

    @Size(max = 1000, message = "The description cannot exceed 1000 characters.")
    private String description;

    @NotBlank(message = "The location is required.")
    private String location;

    @NotBlank(message = "The contact information is required.")
    @Size(max = 12, message = "The contact information cannot exceed 12 characters.")
    @Pattern(regexp = "^\\+?[0-9]+$", message = "The contact must contain only numbers or start with a '+'.")
   
    private String contact;

    @NotNull(message = "The price is required.")
    @Positive(message = "The price must be a positive value.")
    private Double price;
    

    @Min(value = 1, message = "The number of rooms must be at least 1.")
    @NotNull(message = "The number of rooms is required.")
    private Integer numberOfRooms;

    @NotNull(message = "The area is required.")
    @Positive(message = "The area must be a positive value.")
    private Double area;

    @NotNull(message = "The category is required.")
    private Long categoryId; // ID de la catégorie associée
    private List<MultipartFile> photos = new ArrayList<>();


}
