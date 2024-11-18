package com.example.Final.entity.rentalservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rental_payment")
public class RentalPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "rental_listing_id")
    private Rental rental;
}
