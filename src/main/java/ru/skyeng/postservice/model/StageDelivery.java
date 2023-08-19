package ru.skyeng.postservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StageDelivery {
    @Id
    @ManyToOne
    @JoinColumn(name = "OFFICE_ID")
    private PostOffice office;
    @Id
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private PostItem item;
    @ManyToOne
    private StatusDelivery statusDelivery;
    @Column(name = "TIME_DELIVERY")
    private LocalDateTime operationTime;
}