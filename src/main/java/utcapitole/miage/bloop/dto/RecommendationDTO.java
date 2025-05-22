package utcapitole.miage.bloop.dto;

/**
 * DTO (Data Transfer Object) pour les recommandations d'amis.
 * Contient les informations nécessaires pour afficher les recommandations
 * d'amis à un utilisateur.
 */
public class RecommendationDTO {
    private Long id; // L'identifiant unique de la recommandation
    private String nom; // Le nom de l'utilisateur recommandé
    private String prenom; // Le prénom de l'utilisateur recommandé
    private String pseudonyme; // Le pseudonyme de l'utilisateur recommandé
    private Integer nombreAmisCommuns; // Le nombre d'amis en commun avec l'utilisateur connecté
    private Boolean estAmi = false; // Indique si l'utilisateur recommandé est déjà un ami
    private Boolean demandeEnvoyee = false; // Indique si une demande d'ami a été envoyée
    private Boolean demandeRecue = false; // Indique si une demande d'ami a été reçue

    // Constructeurs

    /**
     * Constructeur par défaut.
     */
    public RecommendationDTO() {}

    /**
     * Constructeur avec les informations principales.
     *
     * @param id L'identifiant unique de la recommandation.
     * @param nom Le nom de l'utilisateur recommandé.
     * @param prenom Le prénom de l'utilisateur recommandé.
     * @param pseudonyme Le pseudonyme de l'utilisateur recommandé.
     * @param nombreAmisCommuns Le nombre d'amis en commun.
     */
    public RecommendationDTO(Long id, String nom, String prenom, String pseudonyme, Integer nombreAmisCommuns) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudonyme = pseudonyme;
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    /**
     * Constructeur avec l'identifiant et le nombre d'amis en commun.
     *
     * @param id L'identifiant unique de la recommandation.
     * @param nombreAmisCommuns Le nombre d'amis en commun.
     */
    public RecommendationDTO(Long id, Integer nombreAmisCommuns) {
        this.id = id;
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    // Getters et setters avec gestion des nulls

    /**
     * Récupère l'identifiant unique de la recommandation.
     *
     * @return L'identifiant de la recommandation.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de la recommandation.
     *
     * @param id L'identifiant de la recommandation.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le nom de l'utilisateur recommandé.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'utilisateur recommandé.
     *
     * @param nom Le nom de l'utilisateur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le prénom de l'utilisateur recommandé.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de l'utilisateur recommandé.
     *
     * @param prenom Le prénom de l'utilisateur.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Récupère le pseudonyme de l'utilisateur recommandé.
     *
     * @return Le pseudonyme de l'utilisateur.
     */
    public String getPseudonyme() {
        return pseudonyme;
    }

    /**
     * Définit le pseudonyme de l'utilisateur recommandé.
     *
     * @param pseudonyme Le pseudonyme de l'utilisateur.
     */
    public void setPseudonyme(String pseudonyme) {
        this.pseudonyme = pseudonyme;
    }

    /**
     * Récupère le nombre d'amis en commun.
     *
     * @return Le nombre d'amis en commun.
     */
    public Integer getNombreAmisCommuns() {
        return nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    /**
     * Définit le nombre d'amis en commun.
     *
     * @param nombreAmisCommuns Le nombre d'amis en commun.
     */
    public void setNombreAmisCommuns(Integer nombreAmisCommuns) {
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    /**
     * Indique si l'utilisateur recommandé est déjà un ami.
     *
     * @return `true` si l'utilisateur est un ami, sinon `false`.
     */
    public Boolean getEstAmi() {
        return estAmi != null && estAmi;
    }

    /**
     * Définit si l'utilisateur recommandé est un ami.
     *
     * @param estAmi `true` si l'utilisateur est un ami, sinon `false`.
     */
    public void setEstAmi(Boolean estAmi) {
        this.estAmi = estAmi != null && estAmi;
    }

    /**
     * Indique si une demande d'ami a été envoyée.
     *
     * @return `true` si une demande a été envoyée, sinon `false`.
     */
    public Boolean getDemandeEnvoyee() {
        return demandeEnvoyee != null && demandeEnvoyee;
    }

    /**
     * Définit si une demande d'ami a été envoyée.
     *
     * @param demandeEnvoyee `true` si une demande a été envoyée, sinon `false`.
     */
    public void setDemandeEnvoyee(Boolean demandeEnvoyee) {
        this.demandeEnvoyee = demandeEnvoyee != null && demandeEnvoyee;
    }

    /**
     * Indique si une demande d'ami a été reçue.
     *
     * @return `true` si une demande a été reçue, sinon `false`.
     */
    public Boolean getDemandeRecue() {
        return demandeRecue != null && demandeRecue;
    }

    /**
     * Définit si une demande d'ami a été reçue.
     *
     * @param demandeRecue `true` si une demande a été reçue, sinon `false`.
     */
    public void setDemandeRecue(Boolean demandeRecue) {
        this.demandeRecue = demandeRecue != null && demandeRecue;
    }
}