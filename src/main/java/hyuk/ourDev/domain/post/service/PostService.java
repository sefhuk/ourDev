package hyuk.ourDev.domain.post.service;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.repository.BoardJdbcTemplateRepository;
import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final BoardJdbcTemplateRepository boardJdbcTemplateRepository;

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post addPost(Long boardId, Post post) {

        Optional<Board> findBoard = boardJdbcTemplateRepository.findById(boardId);

        if (findBoard.isEmpty()) {
            throw new RuntimeException();
        }

        post.setBoard(findBoard.get());

        return postRepository.save(post);
    }

    public void modifyPost(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            throw new RuntimeException();
        }

        post.updatePost(postRequestDto.getTitle(), post.getAuthor(),
            postRequestDto.getContent());
        postRepository.save(post);
    }

    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
