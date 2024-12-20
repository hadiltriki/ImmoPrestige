package com.tekup.miniproject.web.models;


import io.micrometer.common.lang.Nullable;
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
    @NotBlank(message = "The description is required.")
    @Size(max = 1000, message = "The description cannot exceed 1000 characters.")
    private String description;

    @NotBlank(message = "The location is required.")
    private String location;

   @NotBlank(message = "The contact information is required.")
   @Pattern(
    regexp = "^\\+216[234579][0-9]{7}$",
    message = "The contact must match the format '+216XXXXXXXX' and start with 2, 3, 4, 5, 7, or 9."
)
    private String contact;
    @NotNull(message = "The price is required.")
    @Positive(message = "The price must be a positive value.")
    private Double price;
    

    @Min(value = 1, message = "The number of rooms must be at least 1.")
    @Positive(message = "The number of rooms must be a positive value.")
   private Integer numberOfRooms;

    @NotNull(message = "The area is required.")
    @Positive(message = "The area must be a positive value.")
    private Double area;

    @NotNull(message = "The category is required.")
    private Long categoryId; // ID de la catégorie associée
    private String photo;


    @NotBlank(message = "The ad type is required.")
    @Size(max = 50, message = "The ad type cannot exceed 50 characters.")
    private String adType;





}
