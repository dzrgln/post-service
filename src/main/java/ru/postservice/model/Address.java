package ru.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@ToString
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
