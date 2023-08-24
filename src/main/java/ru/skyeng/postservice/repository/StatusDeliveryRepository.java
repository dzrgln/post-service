package ru.skyeng.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skyeng.postservice.model.PostItem;
import ru.skyeng.postservice.model.StatusDelivery;

public interface StatusDeliveryRepository extends JpaRepository<StatusDelivery, Long> {
}
