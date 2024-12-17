package com.tekup.miniproject.dao.entities;



import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String name; 
    private String description ;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Ad> ads;


}
