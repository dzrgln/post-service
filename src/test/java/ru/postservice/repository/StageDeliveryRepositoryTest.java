package ru.postservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.postservice.model.PostItem;
import ru.postservice.model.StageDelivery;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.postservice.util.Fixtures.getPostItem;
import static ru.postservice.util.Fixtures.getStageDelivery;

@DataJpaTest
class StageDeliveryRepositoryTest {

    @Autowired
    private StageDeliveryRepository stageDeliveryRepository;
    @Autowired
    private PostItemsRepository postItemsRepository;

    StageDelivery stageDelivery = new StageDelivery();
    PostItem postItem = new PostItem();

    @BeforeEach
    private void addContext() {
        stageDelivery = getStageDelivery();
        postItem = getPostItem();
        postItemsRepository.save(postItem);
        stageDeliveryRepository.save(stageDelivery);
    }

    @AfterEach
    private void deleteAll() {
        stageDeliveryRepository.deleteAll();
        postItemsRepository.deleteAll();
    }

    @Test
    void findByItemId() {
        final Optional<List<StageDelivery>> result = stageDeliveryRepository.findByItemId(stageDelivery.getItem().getId());

        if (result.isPresent()) {
            assertEquals(stageDelivery.getSenderOffice().getName(), result.get().get(0).getSenderOffice().getName());
            assertEquals(stageDelivery.getStatusDelivery().getStatus(), result.get().get(0).getStatusDelivery().getStatus());
            assertEquals(stageDelivery.getItem().getTypePostItem().getType(), result.get().get(0).getItem().getTypePostItem().getType());
            assertEquals(stageDelivery.getOperationTime(), result.get().get(0).getOperationTime());
        }
    }
}
