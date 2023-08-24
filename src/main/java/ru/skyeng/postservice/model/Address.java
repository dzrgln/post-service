package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ADDRESSES")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "IND")
    private int index;

    private String city;

    private String street;

    @Column(name = "HOUSE_NUMBER")
    private int houseNumber;

    @Column(name = "FLAT_NUMBER")
    private int flatNumber;
}
