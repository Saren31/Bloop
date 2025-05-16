package utcapitole.miage.bloop.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO pour la création d'un groupe.
 */
public class GroupeDTO {

    private String nomGroupe;
    private String themeGroupe;
    private String descriptionGroupe;
    private MultipartFile logoGroupe;

    // Getters et setters
    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public String getThemeGroupe() {
        return themeGroupe;
    }

    public void setThemeGroupe(String themeGroupe) {
        this.themeGroupe = themeGroupe;
    }

    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    public MultipartFile getLogoGroupe() {
        return logoGroupe;
    }

    public void setLogoGroupe(MultipartFile logoGroupe) {
        this.logoGroupe = logoGroupe;
    }
}