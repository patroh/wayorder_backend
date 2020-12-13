package com.capstone.project.Bean;
/**
 * @author Rohan Patel
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<PaymentMethod> savedPaymentMethods = new ArrayList<>();

}
