package com.tekup.miniproject.dao.entities;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
  
   
    private String phone;
    private String adress;
    	@ElementCollection(fetch= FetchType.EAGER)
	@CollectionTable(
			name="roles",
			joinColumns = @JoinColumn(name="id")
			)
	
	private List<String> roles;

}
