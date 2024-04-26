package hyuk.ourDev.domain.post.repository;

import hyuk.ourDev.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAll();

    Optional<Post> findById(Long id);


    Post save(Post post);

    void deleteById(Long id);
}
