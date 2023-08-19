package ru.skyeng.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skyeng.postservice.model.TypePostItem;

import java.util.Optional;

public interface TypePostItemRepository extends JpaRepository <TypePostItem, Integer> {

    Optional<TypePostItem> getTypePostItemByAlias(String alias);
}
