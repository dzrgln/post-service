package ru.skyeng.postservice.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class PostItem {
    private long id;
    private TypePostItem typePostItem;
    private int index;
    private Address address;
    private boolean isReceived;
    private String user;
    private List<PostOffice> path = new LinkedList<>();
}

