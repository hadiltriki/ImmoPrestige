package com.tekup.miniproject.dao.entities;

import java.util.ArrayList;
import java.util.List;

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
    @Column( length = 50, nullable = false)
    private String title;
    @Column( length = 1000, nullable = true)
    private String description;
    @Column(nullable = false)
    private String location;
    @Column( nullable = false)
    private String contact;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer numberOfRooms;
    @Column(nullable = false)
    private Double area;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos ;
}
