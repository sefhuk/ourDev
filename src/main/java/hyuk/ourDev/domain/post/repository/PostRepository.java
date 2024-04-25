package hyuk.ourDev.domain.post.repository;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAll();

    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p WHERE p.board = :board ORDER BY p.createdAt DESC")
    List<Post> findAllByBoardId(@Param("board") Board board);

    Post save(Post post);

    void delete(Post post);
}
