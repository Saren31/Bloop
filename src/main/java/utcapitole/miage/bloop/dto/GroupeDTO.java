package utcapitole.miage.bloop.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO (Data Transfer Object) pour la création d'un groupe.
 * Contient les informations nécessaires pour créer un groupe,
 * telles que le nom, le thème, la description et le logo.
 */
public class GroupeDTO {

    private String nomGroupe; // Le nom du groupe
    private String themeGroupe; // Le thème du groupe
    private String descriptionGroupe; // La description du groupe
    private MultipartFile logoGroupe; // Le logo du groupe sous forme de fichier multipart

    // Getters et setters

    /**
     * Récupère le nom du groupe.
     *
     * @return Le nom du groupe.
     */
    public String getNomGroupe() {
        return nomGroupe;
    }

    /**
     * Définit le nom du groupe.
     *
     * @param nomGroupe Le nom du groupe.
     */
    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    /**
     * Récupère le thème du groupe.
     *
     * @return Le thème du groupe.
     */
    public String getThemeGroupe() {
        return themeGroupe;
    }

    /**
     * Définit le thème du groupe.
     *
     * @param themeGroupe Le thème du groupe.
     */
    public void setThemeGroupe(String themeGroupe) {
        this.themeGroupe = themeGroupe;
    }

    /**
     * Récupère la description du groupe.
     *
     * @return La description du groupe.
     */
    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    /**
     * Définit la description du groupe.
     *
     * @param descriptionGroupe La description du groupe.
     */
    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    /**
     * Récupère le logo du groupe.
     *
     * @return Le logo du groupe sous forme de fichier multipart.
     */
    public MultipartFile getLogoGroupe() {
        return logoGroupe;
    }

    /**
     * Définit le logo du groupe.
     *
     * @param logoGroupe Le logo du groupe sous forme de fichier multipart.
     */
    public void setLogoGroupe(MultipartFile logoGroupe) {
        this.logoGroupe = logoGroupe;
    }
}