package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "POST_OFFICES")
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IND")
    private int index;

    private String name;
}
