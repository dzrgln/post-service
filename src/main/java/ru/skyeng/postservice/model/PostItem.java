package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Table(name = "POST_ITEMS")
public class PostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "IND")
    private int index;

    @ManyToOne
    @JoinColumn(name = "TYPE_ITEM")
    private TypePostItem typePostItem;

    @ManyToOne
    @JoinColumn(name="ADDRESS_ID")
    private Address address;

    private String sender;
}

