package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.ReactionRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReactionServiceTest {

    private ReactionRepository reactionRepository;
    private PostService postService;
    private ReactionService reactionService;

    @BeforeEach
    void setUp() {
        reactionRepository = mock(ReactionRepository.class);
        postService = mock(PostService.class);
        reactionService = new ReactionService(reactionRepository, postService);
    }

    @Test
    void testToggleLike_AjouteLikeSiAucuneReaction() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Post post = new Post(); post.setIdPost(2L);

        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.empty());
        when(postService.getPostParId(2L)).thenReturn(post);

        reactionService.toggleLike(2L, utilisateur);

        verify(reactionRepository).save(argThat(r -> r.getType().equals("LIKE") && r.isLiked()));
    }

    @Test
    void testToggleLike_SupprimeLikeSiDejaLike() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Reaction reaction = new Reaction();
        reaction.setType("LIKE");
        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.of(reaction));

        reactionService.toggleLike(2L, utilisateur);

        verify(reactionRepository).delete(reaction);
    }

    @Test
    void testToggleLike_SwitchDislikeVersLike() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Reaction reaction = new Reaction();
        reaction.setType("DISLIKE");
        Post post = new Post(); post.setIdPost(2L);

        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.of(reaction));
        when(postService.getPostParId(2L)).thenReturn(post);

        reactionService.toggleLike(2L, utilisateur);

        verify(reactionRepository).delete(reaction);
        verify(reactionRepository).save(argThat(r -> r.getType().equals("LIKE")));
    }

    @Test
    void testToggleDislike_AjouteDislikeSiAucuneReaction() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Post post = new Post(); post.setIdPost(2L);

        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.empty());
        when(postService.getPostParId(2L)).thenReturn(post);

        reactionService.toggleDislike(2L, utilisateur);

        verify(reactionRepository).save(argThat(r -> r.getType().equals("DISLIKE") && !r.isLiked()));
    }

    @Test
    void testToggleDislike_SupprimeDislikeSiDejaDislike() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Reaction reaction = new Reaction();
        reaction.setType("DISLIKE");
        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.of(reaction));

        reactionService.toggleDislike(2L, utilisateur);

        verify(reactionRepository).delete(reaction);
    }

    @Test
    void testToggleDislike_SwitchLikeVersDislike() {
        Utilisateur utilisateur = new Utilisateur(); utilisateur.setIdUser(1L);
        Reaction reaction = new Reaction();
        reaction.setType("LIKE");
        Post post = new Post(); post.setIdPost(2L);

        when(reactionRepository.findByPostIdPostAndUtilisateurIdUser(2L, 1L)).thenReturn(Optional.of(reaction));
        when(postService.getPostParId(2L)).thenReturn(post);

        reactionService.toggleDislike(2L, utilisateur);

        verify(reactionRepository).delete(reaction);
        verify(reactionRepository).save(argThat(r -> r.getType().equals("DISLIKE")));
    }

    @Test
    void testIsLikedBy() {
        Post post = new Post();
        Utilisateur utilisateur = new Utilisateur();
        Reaction reaction = new Reaction();
        reaction.setLiked(true);

        when(reactionRepository.findByPostAndUtilisateur(post, utilisateur)).thenReturn(Optional.of(reaction));

        boolean result = reactionService.isLikedBy(post, utilisateur);

        assertThat(result).isTrue();
    }

    @Test
    void testIsDislikedBy() {
        Post post = new Post();
        Utilisateur utilisateur = new Utilisateur();
        Reaction reaction = new Reaction();
        reaction.setType("DISLIKE");

        when(reactionRepository.findByPostAndUtilisateur(post, utilisateur)).thenReturn(Optional.of(reaction));

        boolean result = reactionService.isDislikedBy(post, utilisateur);

        assertThat(result).isTrue();
    }

    @Test
    void testCountLikes() {
        Post post = new Post();
        when(reactionRepository.countByPostAndType(post, "LIKE")).thenReturn(5);

        int count = reactionService.countLikes(post);

        assertThat(count).isEqualTo(5);
    }

    @Test
    void testCountDislikes() {
        Post post = new Post();
        when(reactionRepository.countByPostAndType(post, "DISLIKE")).thenReturn(2);

        int count = reactionService.countDislikes(post);

        assertThat(count).isEqualTo(2);
    }
}