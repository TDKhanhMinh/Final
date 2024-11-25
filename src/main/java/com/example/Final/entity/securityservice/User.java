package com.example.Final.entity.securityservice;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Images;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @NotNull(message = "Please fill in this field")
    @Size(min = 1, message = "Please fill in this field")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "Please fill in this field")
    @Size(min = 6, message = "Password must be more than 6 digit")
    @Column(name = "password")
    private String password;

    @NotNull(message = "Please fill in this field")
    @Size(min = 6, message = "Password must be more than 6 digit")
    private String confirmPassword;

    @NotNull(message = "Please fill in this field")
    @Size(min = 1, message = "Please fill in this field")
    @Column(name = "email", unique = true)
    private String email;


    private String phone;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Images images;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private HistoryListing historyListing;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Roles> roles;


}
