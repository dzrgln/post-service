package ru.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.postservice.model.PostItem;

public interface PostItemsRepository extends JpaRepository<PostItem, Long> {

}