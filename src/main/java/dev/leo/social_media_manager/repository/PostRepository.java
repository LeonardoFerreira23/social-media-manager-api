package dev.leo.social_media_manager.repository;

import dev.leo.social_media_manager.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByPublished(boolean published, Sort sort);
}