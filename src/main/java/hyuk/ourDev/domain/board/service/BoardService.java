package hyuk.ourDev.domain.board.service;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.repository.JdbcTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final JdbcTemplateRepository jdbcTemplateRepository;

    public List<Board> findBoards() {
        return jdbcTemplateRepository.findAll();
    }

    public Board findBoard(Long id) {
        return jdbcTemplateRepository.findById(id).orElse(null);
    }

    public Board findBoardPaging(Long id, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return jdbcTemplateRepository.findByIdPaging(id, pageable).orElse(null);
    }

    public Board addBoard(Board board) {
        return jdbcTemplateRepository.save(board);
    }

    public Board modifyBoard(Board board) {
        return jdbcTemplateRepository.save(board);
    }

    public void removeBoard(Long id, String author) {
        Board findBoard = jdbcTemplateRepository.findById(id).orElse(null);

        if(findBoard == null) {
            throw new RuntimeException();
        }

        if (!findBoard.getAuthor().equals(author)) {
            throw new EntityNotFoundException();
        }

        jdbcTemplateRepository.delete(id);
    }
}
