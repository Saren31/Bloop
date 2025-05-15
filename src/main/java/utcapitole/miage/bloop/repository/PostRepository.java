package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utcapitole.miage.bloop.model.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
