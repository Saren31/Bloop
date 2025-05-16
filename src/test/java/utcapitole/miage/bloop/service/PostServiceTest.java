package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.PostRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    public PostServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreerPost() {
        Post post = new Post();
        post.setTextePost("Test");
        post.setDatePost(new Date());

        when(postRepository.save(post)).thenReturn(post);


        assertEquals("Bonjour le monde", result.getTextePost());
        verify(postRepository, times(1)).save(post);
    }
}
