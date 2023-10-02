package ru.postservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.postservice.model.PostOffice;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.postservice.util.Fixtures.getPostOfficeOne;

@DataJpaTest
class PostOfficeRepositoryTest {

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    PostOffice postOffice = new PostOffice();

    @BeforeEach
    private void addPostOffice() {
        postOffice = getPostOfficeOne();
        postOfficeRepository.save(postOffice);
    }

    @AfterEach
    private void deleteAll() {
        postOfficeRepository.deleteAll();
    }

    @Test
    void getPostOfficeByIndex() {
        final Optional<PostOffice> result = postOfficeRepository.getPostOfficeByIndex(postOffice.getIndex());

        if (result.isPresent()) {
            assertEquals(postOffice.getIndex(), result.get().getIndex());
            assertEquals(postOffice.getName(), result.get().getName());
        }
    }
}
