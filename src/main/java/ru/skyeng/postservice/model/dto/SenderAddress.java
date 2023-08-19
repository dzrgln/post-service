package ru.skyeng.postservice.model.dto;

import lombok.Data;

@Data
public class SenderAddress {
    private int index;
    private String city;
    private String street;
    private int houseNumber;
    private int flatNumber;
}
