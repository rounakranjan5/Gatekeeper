package com.Gatekeeper.Api_Management_Service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;

    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

}
