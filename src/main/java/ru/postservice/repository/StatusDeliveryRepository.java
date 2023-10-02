package ru.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.postservice.model.StatusDelivery;

public interface StatusDeliveryRepository extends JpaRepository<StatusDelivery, Long> {
}
