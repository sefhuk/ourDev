package hyuk.ourDev.domain.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.repository.BoardJdbcTemplateRepository;
import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.repository.PostRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    BoardJdbcTemplateRepository boardJdbcTemplateRepository;

    @InjectMocks
    PostService postService;

    @Test
    @DisplayName(value = "게시글 상세 요청")
    void findPostTest() {
        //given
        Post post = Post.builder().id(1L).author("author").title("title").content("content").password(1234)
            .build();

        given(postRepository.findById(1L)).willReturn(Optional.of(post));


        //when
        Post postResponse = postService.findPost(1L);

        //then
        assert postResponse != null;
        assertThat(postResponse.getId()).isEqualTo(post.getId());
        assertThat(postResponse.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResponse.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(postResponse.getContent()).isEqualTo(post.getContent());
        assertThat(postResponse.getPassword()).isEqualTo(post.getPassword());
    }

    @Test
    @DisplayName(value = "게시글 생성 요청")
    void addPostTest() {
        //given
        Board board = Board.builder().id(1L).author("author").name("name").build();
        Post post = Post.builder().id(1L).author("author").title("title").content("content").password(1234)
            .build();

        given(boardJdbcTemplateRepository.findById(1L)).willReturn(Optional.of(board));
        given(boardJdbcTemplateRepository.findById(2L)).willReturn(Optional.empty());
        given(postRepository.save(post)).willReturn(post);

        //when
        Board boardResponse = boardJdbcTemplateRepository.findById(1L).orElse(null);
        Optional<Board> boardResponseNull = boardJdbcTemplateRepository.findById(2L);
        assert boardResponse != null;
        post.setBoard(boardResponse);

        Post saveResult = postService.addPost(1L, post);


        //then
        assertThrows(RuntimeException.class, boardResponseNull::get);
        assertThat(saveResult.getId()).isEqualTo(post.getId());
        assertThat(saveResult.getTitle()).isEqualTo(post.getTitle());
        assertThat(saveResult.getAuthor()).isEqualTo(post.getAuthor());
        assertThat(saveResult.getPassword()).isEqualTo(post.getPassword());
        assertThat(saveResult.getBoard()).isEqualTo(boardResponse);
    }

    @Test
    @DisplayName(value = "게시글 수정 요청")
    void modifyPostTest() {
        //given
        PostRequestDto postRequestDto = PostRequestDto.builder().author("newAuthor").title("newTitle")
            .content("newContent").password(1234).build();

        Post post = Post.builder().id(1L).author("author").title("title")
            .content("content").password(1234).build();

        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(postRepository.findById(2L)).willReturn(Optional.empty());

        //when
        Optional<Post> findPostNull = postRepository.findById(2L);

        Post modifyResult = postService.modifyPost(1L, postRequestDto);

        //then
        assertThrows(RuntimeException.class, findPostNull::get);

        assertThat(modifyResult.getId()).isEqualTo(1L);
        assertThat(modifyResult.getTitle()).isEqualTo(postRequestDto.getTitle());
        assertThat(modifyResult.getAuthor()).isEqualTo(postRequestDto.getAuthor());
        assertThat(modifyResult.getContent()).isEqualTo(postRequestDto.getContent());
        assertThat(modifyResult.getPassword()).isEqualTo(postRequestDto.getPassword());

    }
}