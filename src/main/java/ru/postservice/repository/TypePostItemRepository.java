package ru.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.postservice.model.TypePostItem;

import java.util.Optional;

public interface TypePostItemRepository extends JpaRepository<TypePostItem, Long> {

    Optional<TypePostItem> getTypePostItemByAlias(String alias);
}
