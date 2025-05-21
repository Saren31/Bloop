package utcapitole.miage.bloop.dto;

public class RecommendationDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String pseudonyme;
    private Integer nombreAmisCommuns;
    private Boolean estAmi = false;  // Valeur par défaut pour éviter le NPE
    private Boolean demandeEnvoyee = false;  // Valeur par défaut pour éviter le NPE
    private Boolean demandeRecue = false;  // Valeur par défaut pour éviter le NPE

    // Constructeurs
    public RecommendationDTO() {}

    public RecommendationDTO(Long id, String nom, String prenom, String pseudonyme, Integer nombreAmisCommuns) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudonyme = pseudonyme;
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    public RecommendationDTO(Long id, Integer nombreAmisCommuns) {
        this.id = id;
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    // Getters et setters avec gestion des nulls
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudonyme() {
        return pseudonyme;
    }

    public void setPseudonyme(String pseudonyme) {
        this.pseudonyme = pseudonyme;
    }

    public Integer getNombreAmisCommuns() {
        return nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    public void setNombreAmisCommuns(Integer nombreAmisCommuns) {
        this.nombreAmisCommuns = nombreAmisCommuns != null ? nombreAmisCommuns : 0;
    }

    public Boolean getEstAmi() {
        return estAmi != null && estAmi;
    }

    public void setEstAmi(Boolean estAmi) {
        this.estAmi = estAmi != null && estAmi;
    }

    public Boolean getDemandeEnvoyee() {
        return demandeEnvoyee != null && demandeEnvoyee;
    }

    public void setDemandeEnvoyee(Boolean demandeEnvoyee) {
        this.demandeEnvoyee = demandeEnvoyee != null && demandeEnvoyee;
    }

    public Boolean getDemandeRecue() {
        return demandeRecue != null && demandeRecue;
    }

    public void setDemandeRecue(Boolean demandeRecue) {
        this.demandeRecue = demandeRecue != null && demandeRecue;
    }
}