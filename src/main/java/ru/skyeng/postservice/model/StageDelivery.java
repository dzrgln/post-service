package ru.skyeng.postservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StageDelivery {
    private PostOffice postOffice;
    private PostItem postItem;
    private StatusDelivery statusDelivery;
    private LocalDateTime dateTime;
}
