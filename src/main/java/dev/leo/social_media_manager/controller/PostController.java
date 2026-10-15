package dev.leo.social_media_manager.controller;

import dev.leo.social_media_manager.dto.PostRequest;
import dev.leo.social_media_manager.dto.PostResponse;
import dev.leo.social_media_manager.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @GetMapping
    public List<PostResponse> getPosts(@RequestParam(name = "isPublished", required = false) Boolean isPublished) {
        return postService.getPosts(isPublished);
    }
}