package hyuk.ourDev.domain.post.service;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.repository.BoardJdbcTemplateRepository;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final BoardJdbcTemplateRepository boardJdbcTemplateRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public List<Post> findPostsByBoardId(Board board) {
        return postRepository.findAllByBoardId(board);
    }

    public Post addPost(Long boardId, Post post) {

        Optional<Board> findBoard = boardJdbcTemplateRepository.findById(boardId);

        if (!findBoard.isPresent()) {
            throw new RuntimeException();
        }

        post.setBoard(findBoard.get());

        return postRepository.save(post);
    }
}
