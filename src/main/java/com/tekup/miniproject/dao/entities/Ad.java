package com.tekup.miniproject.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String contact;
    private Double price;
    private Integer numberOfRooms;
    private Double area;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String photo;
    
    private String adType;

}
