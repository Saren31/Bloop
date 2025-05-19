package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.GroupeRepository;
import utcapitole.miage.bloop.repository.PostRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour gérer les opérations liées aux posts.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final GroupeRepository groupeRepository;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter le repository des posts.
     *
     * @param postRepository Le repository pour interagir avec les entités Post.
     */
    @Autowired
    public PostService(PostRepository postRepository, GroupeRepository groupeRepository, UtilisateurRepository utilisateurRepository) {
        this.postRepository = postRepository;
        this.groupeRepository = groupeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère la liste des posts d'un utilisateur spécifique.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return La liste des posts associés à l'utilisateur.
     */
    public List<Post> getPostsByUtilisateur(long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }

    /**
     * Crée un nouveau post.
     *
     * @param post L'entité Post à créer.
     * @throws IllegalArgumentException Si le contenu du post est vide ou invalide.
     */
    public void creerPost(Post post) {
        if (post == null || post.getTextePost() == null || post.getTextePost().isBlank()) {
            throw new IllegalArgumentException("Le contenu du post ne peut pas être vide.");
        }
        post.setDatePost(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
        postRepository.save(post);
    }

    public PostDTO envoyerPost(PostDTO postDTO, long expId) {
        Post post = new Post();
        post.setTextePost(postDTO.getTextePost());
        post.setGroupe(groupeRepository.findById(postDTO.getGroupeId()).orElseThrow());
        post.setUtilisateur(utilisateurRepository.findById(expId).orElseThrow());
        this.creerPost(post);

        // Convertir en DTO
        postDTO.setIdPost(post.getIdPost());
        postDTO.setDatePost(post.getDatePost());
        return postDTO;
    }

    /**
     * Récupère un post par son identifiant.
     *
     * @param id L'identifiant du post.
     * @return Le post correspondant à l'identifiant.
     * @throws IllegalArgumentException Si l'identifiant est invalide ou si le post n'est pas trouvé.
     */
    public Post getPostParId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant du post est invalide.");
        }
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Post introuvable pour l'identifiant donné."));
    }

    /**
     * Récupère la liste des posts d'un utilisateur spécifique (méthode alternative).
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return La liste des posts associés à l'utilisateur.
     */
    public List<Post> getPostsByUtilisateurId(Long idUser) {
        return postRepository.findByUtilisateur_IdUser(idUser);
    }

    public void supprimerPost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> findByUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null.");
        }
        return postRepository.findByUtilisateur(utilisateur);
    }

    public List<PostDTO> getPostsByGroupe(Long groupeId) {
        return postRepository.findByGroupe_IdGroupe(groupeId)
                .stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setIdPost(post.getIdPost());
                    postDTO.setTextePost(post.getTextePost());
                    postDTO.setDatePost(post.getDatePost());
                    postDTO.setUtilisateurId(post.getUtilisateur().getIdUser());
                    postDTO.setGroupeId(post.getGroupe().getIdGroupe());

                    // Ajouter le résumé de l'utilisateur
                    PostDTO.UtilisateurSummary utilisateurSummary = new PostDTO.UtilisateurSummary();
                    utilisateurSummary.setIdUser(post.getUtilisateur().getIdUser());
                    utilisateurSummary.setNomUser(post.getUtilisateur().getNomUser());
                    postDTO.setUtilisateur(utilisateurSummary);

                    return postDTO;
                })
                .collect(Collectors.toList());
    }


    public void save(Post post) {
        postRepository.save(post);
    }
}