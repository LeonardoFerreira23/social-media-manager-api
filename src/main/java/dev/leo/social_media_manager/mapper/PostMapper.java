package dev.leo.social_media_manager.mapper;

import dev.leo.social_media_manager.dto.PostRequest;
import dev.leo.social_media_manager.dto.PostResponse;
import dev.leo.social_media_manager.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toEntity(PostRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setCaption(request.caption());
        post.setDate(request.date());
        post.setPublished(request.isPublished());
        return post;
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getCaption(),
                post.getDate(),
                post.isPublished()
        );
    }
}