package ru.skyeng.postservice.model;

import lombok.Data;

import java.util.List;

@Data
public class PostOffice {
    private int index;
    private String name;
    private List<Address> addressList;
}
