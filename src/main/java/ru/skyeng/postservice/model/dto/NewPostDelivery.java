package ru.skyeng.postservice.model.dto;

import lombok.Data;

@Data
public class NewPostDelivery {
    private int index;
    private SenderAddress address;
    private String user;
}
