package ru.skyeng.postservice.model.dto;

import lombok.Data;

@Data
public class NewPostDelivery {
    private int index;
    private String address;
    private String user;
}
