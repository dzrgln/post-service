package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "POST_ITEMS")
public class PostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "TYPE_ITEM")
    private TypePostItem typePostItem;
    @Column(name = "IND")
    private int index;
    @ManyToOne
    @JoinColumn(name="ADDRESS_ID")
    private Address address;
    private String sender;
    @ManyToMany
    @JoinTable(name = "DELIVERY_STAGE",
            joinColumns = @JoinColumn(name = "ITEM_ID"),
            inverseJoinColumns = @JoinColumn(name = "OFFICE_ID"))
    private List<PostOffice> path = new LinkedList<>();
}

