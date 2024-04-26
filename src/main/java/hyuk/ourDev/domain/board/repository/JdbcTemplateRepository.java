package hyuk.ourDev.domain.board.repository;

import hyuk.ourDev.domain.board.entity.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface JdbcTemplateRepository {

    List<Board> findAll();

    Optional<Board> findById(Long id);

    Optional<Board> findByIdPaging(Long id, Pageable pageable);

    Board save(Board board);

    void delete(Long id);
}