package utcapitole.miage.bloop.dto;

public class CommentaireDTO {
    private String texte;
    private Long postId;

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}