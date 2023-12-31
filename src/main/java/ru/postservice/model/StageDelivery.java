package ru.postservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "DELIVERY_STAGE")
public class StageDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SENDER_OFFICE_ID")
    private PostOffice senderOffice;

    @ManyToOne
    @JoinColumn(name = "RECIPIENT_OFFICE_ID")
    private PostOffice recipientOffice;

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