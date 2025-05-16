package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.repository.PostRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Optional<Post> getPostParId(Long id) {
        return postRepository.findById(id);
    }

    public void creerPost(Post post) {
        post.setDatePost(new Date());
        postRepository.save(post);
    }
}
