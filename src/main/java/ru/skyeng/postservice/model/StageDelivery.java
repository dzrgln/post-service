package ru.skyeng.postservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "OFFICE_ID")
    private PostOffice office;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private PostItem item;

    @ManyToOne
    @JoinColumn(name = "STATUS_DELIVERY_ID")
    private StatusDelivery statusDelivery;

    @Column(name = "TIME_DELIVERY")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
}