package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "POST_OFFICES")
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IND")
    private int index;
    private String name;
    @OneToMany
    private List<Address> addressList;
    @ManyToMany(mappedBy = "path")
    private List<PostItem> postItems;
}
