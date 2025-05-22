package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.dto.UtilisateurSummaryDTO;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.GroupeRepository;
import utcapitole.miage.bloop.repository.jpa.PostRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import org.springframework.data.domain.Sort;


import java.util.List;

/**
 * Service pour gérer les opérations liées aux posts.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final GroupeRepository groupeRepository;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param postRepository Le repository pour interagir avec les entités Post.
     * @param groupeRepository Le repository pour interagir avec les entités Groupe.
     * @param utilisateurRepository Le repository pour interagir avec les entités Utilisateur.
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

    /**
     * Envoie un post en le liant à un utilisateur et un groupe.
     *
     * @param postDTO Le DTO contenant les informations du post.
     * @param expId L'identifiant de l'utilisateur expéditeur.
     * @return Le DTO du post créé avec les informations mises à jour.
     */
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

    /**
     * Supprime un post par son identifiant.
     *
     * @param id L'identifiant du post à supprimer.
     */
    public void supprimerPost(Long id) {
        postRepository.deleteById(id);
    }

    /**
     * Récupère les posts associés à un utilisateur donné.
     *
     * @param utilisateur L'entité Utilisateur.
     * @return La liste des posts associés à l'utilisateur.
     * @throws IllegalArgumentException Si l'utilisateur est null.
     */
    public List<Post> findByUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null.");
        }
        return postRepository.findByUtilisateur(utilisateur);
    }

    /**
     * Récupère les posts associés à un groupe spécifique.
     *
     * @param groupeId L'identifiant du groupe.
     * @return Une liste de PostDTO représentant les posts du groupe.
     */
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
                    UtilisateurSummaryDTO utilisateurSummary = new UtilisateurSummaryDTO();
                    utilisateurSummary.setIdUser(post.getUtilisateur().getIdUser());
                    utilisateurSummary.setNomUser(post.getUtilisateur().getNomUser());
                    postDTO.setUtilisateur(utilisateurSummary);

                    return postDTO;
                })
                .toList();
    }

    /**
     * Sauvegarde un post dans le repository.
     *
     * @param post L'entité Post à sauvegarder.
     */
    public void save(Post post) {
        postRepository.save(post);
    }
//recup tout les poste et par ordre du plus recent
public List<Post> getAllPosts() {
    return postRepository.findAllWithUser();
}
}