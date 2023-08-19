package ru.skyeng.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skyeng.postservice.model.PostItem;

public interface PostRepository extends JpaRepository<PostItem, Long> {
}
