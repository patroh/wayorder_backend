package com.capstone.project.Bean;
/**
 * @author Rohan Patel
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PaymentMethod {
    @Id
    private Long id;
    private int type;  // 0:Credit Card, 1:Debit Card
    private String name;
    private Long number;
    private int cvv;
    private LocalDate exp;
}
