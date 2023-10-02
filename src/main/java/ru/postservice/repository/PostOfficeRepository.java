package ru.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.postservice.model.PostOffice;

import java.util.Optional;

public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {

    @Query("select p from PostOffice p " +
            "where p.index=?1")
    Optional<PostOffice> getPostOfficeByIndex(int index);
}
