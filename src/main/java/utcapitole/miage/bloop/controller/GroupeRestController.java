package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/groupes")
public class GroupeRestController {

    private final PostService postService;

    @Autowired
    public GroupeRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}/posts")
    public List<PostDTO> getPostsByGroupe(@PathVariable Long id) {
        System.out.println("ID du groupe : " + id);
        return postService.getPostsByGroupe(id);
    }
}