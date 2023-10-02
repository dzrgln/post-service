package ru.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.postservice.model.StageDelivery;

import java.util.List;
import java.util.Optional;

public interface StageDeliveryRepository extends JpaRepository<StageDelivery, Long> {

    @Query("select d from StageDelivery d " +
            "where d.item.id =? 1")
    Optional<List<StageDelivery>> findByItemId(Long itemId);

}
