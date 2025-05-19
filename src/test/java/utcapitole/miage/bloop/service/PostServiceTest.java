package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.GroupeRepository;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private final PostRepository postRepository = mock(PostRepository.class);
    private final GroupeRepository groupeRepository = mock(GroupeRepository.class);
    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final PostService postService = new PostService(postRepository, groupeRepository, utilisateurRepository);

    @Test
    void testCreerPostSucces() {
        Post post = new Post();
        post.setTextePost("Un texte de test");

        postService.creerPost(post);

        verify(postRepository).save(post);
    }

    @Test
    void testCreerPostTexteVide() {
        Post post = new Post();
        post.setTextePost("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.creerPost(post);
        });

        assertThat(exception.getMessage()).contains("Le contenu du post ne peut pas être vide.");
    }

    @Test
    void testGetPostParIdSucces() {
        Post post = new Post();
        post.setIdPost(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPostParId(1L);

        assertThat(result).isEqualTo(post);
    }

    @Test
    void testGetPostParIdInvalide() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostParId(1L);
        });

        assertThat(exception.getMessage()).contains("Post introuvable pour l'identifiant donné.");
    }

    @Test
    void testSupprimerPost_Succes() {
        doNothing().when(postRepository).deleteById(1L);

        postService.supprimerPost(1L);

        verify(postRepository).deleteById(1L);
    }
}