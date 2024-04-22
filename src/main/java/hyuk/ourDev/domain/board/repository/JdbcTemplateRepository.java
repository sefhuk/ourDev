package hyuk.ourDev.domain.board.repository;

import hyuk.ourDev.domain.board.entity.Board;
import java.util.List;
import java.util.Optional;

public interface JdbcTemplateRepository {

    List<Board> findAll();

    Optional<Board> findById(Long id);

    Board save(Board board);

    void delete(Board board);
}
