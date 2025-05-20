package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Commentaire;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.CommentaireRepository;
import utcapitole.miage.bloop.repository.jpa.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommentaireServiceTest {

    private final CommentaireRepository commentaireRepository = mock(CommentaireRepository.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final CommentaireService commentaireService = new CommentaireService(commentaireRepository, postRepository);

    @Test
    void testAjouterCommentaire() {
        Utilisateur utilisateur = new Utilisateur();
        Post post = new Post();
        post.setIdPost(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentaireRepository.save(any(Commentaire.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Commentaire commentaire = commentaireService.ajouterCommentaire(1L, "Coucou", utilisateur);

        assertThat(commentaire.getTexte()).isEqualTo("Coucou");
        assertThat(commentaire.getUtilisateur()).isEqualTo(utilisateur);
        assertThat(commentaire.getPost()).isEqualTo(post);
        assertThat(commentaire.getDateCommentaire()).isNotNull();
    }

    @Test
    void testGetCommentairesParPost() {
        Commentaire c1 = new Commentaire();
        c1.setId(1L);
        when(commentaireRepository.findByPostIdPost(1L)).thenReturn(List.of(c1));

        List<Commentaire> result = commentaireService.getCommentairesParPost(1L);

        assertThat(result).containsExactly(c1);
    }
}