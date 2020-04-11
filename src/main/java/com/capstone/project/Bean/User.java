package com.capstone.project.Bean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String fullname;

	@NotNull
	private String email;

	// Password encoded by Base64 encoder
	@NotNull
	private String password;

	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Order> orders;

}
