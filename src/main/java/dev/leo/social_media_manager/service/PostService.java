package dev.leo.social_media_manager.service;

import dev.leo.social_media_manager.dto.PostRequest;
import dev.leo.social_media_manager.dto.PostResponse;
import dev.leo.social_media_manager.exception.FutureDateException;
import dev.leo.social_media_manager.mapper.PostMapper;
import dev.leo.social_media_manager.model.Post;
import dev.leo.social_media_manager.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final Sort SORT_BY_ID = Sort.by("id");

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @CacheEvict(value = "posts", allEntries = true)
    public PostResponse createPost(PostRequest request) {
        validateDateIsNotInFuture(request.date());
        Post savedPost = postRepository.save(postMapper.toEntity(request));
        return postMapper.toResponse(savedPost);
    }

    @Cacheable("posts")
    public List<PostResponse> getPosts(Boolean isPublished) {
        List<Post> posts = (isPublished == null)
                ? postRepository.findAll(SORT_BY_ID)
                : postRepository.findByPublished(isPublished, SORT_BY_ID);

        return posts.stream()
                .map(postMapper::toResponse)
                .toList();
    }

    private void validateDateIsNotInFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateException(date);
        }
    }
}