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
@Table(name = "DELIVERY_STAGE")
public class StageDelivery {
    @EmbeddedId
    StageDeliveryPK id;

    @ManyToOne
    @MapsId("index")
    @JoinColumn(name = "OFFICE_ID")
    private PostOffice office;

    @ManyToOne
    @MapsId("postItemId")
    @JoinColumn(name = "ITEM_ID")
    private PostItem item;

    @ManyToOne
    private StatusDelivery statusDelivery;
    @Column(name = "TIME_DELIVERY")
    private LocalDateTime operationTime;
}