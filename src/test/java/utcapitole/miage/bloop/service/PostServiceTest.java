package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.GroupeRepository;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    void testEnvoyerPost_Succes() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTextePost("Contenu");
        postDTO.setGroupeId(10L);

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(10L);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(5L);

        when(groupeRepository.findById(10L)).thenReturn(Optional.of(groupe));
        when(utilisateurRepository.findById(5L)).thenReturn(Optional.of(utilisateur));
        doAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setIdPost(123L);
            post.setDatePost(Timestamp.valueOf(LocalDateTime.now()));
            return null;
        }).when(postRepository).save(any(Post.class));

        PostDTO result = postService.envoyerPost(postDTO, 5L);

        assertThat(result.getIdPost()).isNotNull();
        assertThat(result.getDatePost()).isNotNull();
        assertThat(result.getGroupeId()).isEqualTo(10L);
    }

    @Test
    void testEnvoyerPost_GroupeInexistant() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTextePost("Contenu");
        postDTO.setGroupeId(99L);

        when(groupeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> postService.envoyerPost(postDTO, 1L));
    }

    @Test
    void testEnvoyerPost_UtilisateurInexistant() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTextePost("Contenu");
        postDTO.setGroupeId(1L);

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);

        when(groupeRepository.findById(1L)).thenReturn(Optional.of(groupe));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> postService.envoyerPost(postDTO, 2L));
    }

    @Test
    void testFindByUtilisateur_Succes() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(1L);

        Post post = new Post();
        post.setIdPost(1L);
        when(postRepository.findByUtilisateur(utilisateur)).thenReturn(Collections.singletonList(post));

        List<Post> result = postService.findByUtilisateur(utilisateur);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdPost()).isEqualTo(1L);
    }

    @Test
    void testFindByUtilisateur_Null() {
        assertThrows(IllegalArgumentException.class, () -> postService.findByUtilisateur(null));
    }

    @Test
    void testGetPostsByGroupe_Succes() {
        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUser(2L);
        utilisateur.setNomUser("TestNom");

        Post post = new Post();
        post.setIdPost(3L);
        post.setTextePost("Texte");
        post.setDatePost(Timestamp.valueOf(LocalDateTime.now()));
        post.setUtilisateur(utilisateur);
        post.setGroupe(groupe);

        when(postRepository.findByGroupe_IdGroupe(1L)).thenReturn(List.of(post));

        List<PostDTO> result = postService.getPostsByGroupe(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdPost()).isEqualTo(3L);
        assertThat(result.get(0).getUtilisateur().getNomUser()).isEqualTo("TestNom");
    }
}