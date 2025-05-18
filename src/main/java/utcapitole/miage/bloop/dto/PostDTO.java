package utcapitole.miage.bloop.dto;

import org.springframework.web.multipart.MultipartFile;

public class PostDTO {
    private String textePost;
    private MultipartFile imageFile;

    public String getTextePost() {
        return textePost;
    }

    public void setTextePost(String textePost) {
        this.textePost = textePost;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}