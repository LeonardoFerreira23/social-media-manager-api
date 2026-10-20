package dev.leo.social_media_manager.service;

import dev.leo.social_media_manager.dto.PostRequest;
import dev.leo.social_media_manager.dto.PostResponse;
import dev.leo.social_media_manager.exception.FutureDateException;
import dev.leo.social_media_manager.mapper.PostMapper;
import dev.leo.social_media_manager.model.Post;
import dev.leo.social_media_manager.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static final LocalDate PAST_DATE = LocalDate.of(2023, 12, 21);

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private Post post;
    private PostResponse postResponse;

    @BeforeEach
    void setUp() {
        post = new Post(1L, "Title 1", "Caption 1", PAST_DATE, false);
        postResponse = new PostResponse(1L, "Title 1", "Caption 1", PAST_DATE, false);
    }

    @Test
    void createPost_withPastDate_savesAndReturnsPost() {
        PostRequest request = new PostRequest("Title 1", "Caption 1", PAST_DATE, false);
        when(postMapper.toEntity(request)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        PostResponse result = postService.createPost(request);

        assertThat(result).isEqualTo(postResponse);
        verify(postRepository).save(post);
    }

    @Test
    void createPost_withTodayDate_isAccepted() {
        PostRequest request = new PostRequest("Title", "Caption", LocalDate.now(), true);
        when(postMapper.toEntity(request)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        assertThat(postService.createPost(request)).isNotNull();
    }

    @Test
    void createPost_withFutureDate_throwsFutureDateException() {
        PostRequest request = new PostRequest("Title", "Caption", LocalDate.now().plusDays(1), true);

        assertThatThrownBy(() -> postService.createPost(request))
                .isInstanceOf(FutureDateException.class)
                .hasMessageContaining("cannot be in the future");

        verifyNoInteractions(postRepository);
    }

    @Test
    void getPosts_withoutFilter_returnsAllPosts() {
        when(postRepository.findAll(any(Sort.class))).thenReturn(List.of(post));
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        List<PostResponse> result = postService.getPosts(null);

        assertThat(result).containsExactly(postResponse);
        verify(postRepository, never()).findByPublished(anyBoolean(), any(Sort.class));
    }

    @Test
    void getPosts_withFilter_returnsFilteredPosts() {
        when(postRepository.findByPublished(eq(false), any(Sort.class))).thenReturn(List.of(post));
        when(postMapper.toResponse(post)).thenReturn(postResponse);

        List<PostResponse> result = postService.getPosts(false);

        assertThat(result).containsExactly(postResponse);
        verify(postRepository, never()).findAll(any(Sort.class));
    }

    @Test
    void getPosts_whenNoPostsExist_returnsEmptyList() {
        when(postRepository.findAll(any(Sort.class))).thenReturn(List.of());

        assertThat(postService.getPosts(null)).isEmpty();
    }
}