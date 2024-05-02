package hyuk.ourDev.domain.board.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hyuk.ourDev.domain.board.dto.BoardResponseDto;
import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.mapper.BoardMapper;
import hyuk.ourDev.domain.board.mapper.BoardMapperImpl;
import hyuk.ourDev.domain.board.service.BoardService;
import hyuk.ourDev.domain.post.dto.PostResponseDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.mapper.PostMapper;
import hyuk.ourDev.domain.post.mapper.PostMapperImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BoardService boardService;

    @Test
    @DisplayName(value = "인덱스 페이지 요청")
    void index() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(view().name("index"));
    }

    @Test
    @DisplayName(value = "게시판 목록 요청")
    void boardList() throws Exception {
        //given
        List<Board> boardList = Arrays.asList(
            Board.builder().id(1L).name("board1").author("author1").build(),
            Board.builder().id(2L).name("board2").author("author2").build()
        );

        given(boardService.findBoards()).willReturn(boardList);

        BoardMapper bm = new BoardMapperImpl();

        //when, then
        mockMvc.perform(get("/board"))
            .andExpect(view().name("boards"))
            .andExpect(model().attribute("boards",
                Arrays.asList(bm.boardToBoardResponseDto(boardList.get(0)),
                    bm.boardToBoardResponseDto(boardList.get(1))
                )));
    }

    @Test
    @DisplayName(value = "게시판 상세 요청")
    void boardDetailsTest() throws Exception {
        //given
        Board responseBoard = Board.builder().id(1L).name("board1").author("author1").
            build();

        BoardResponseDto boardResponseDto = BoardResponseDto.builder().id(1L).name("board1")
            .author("author1").
            build();

        Post post1 = Post.builder().id(1L).title("title1").content("content1").author("author1")
            .build();
        Post post2 = Post.builder().id(2L).title("title2").content("content2").author("author2")
            .build();

        PostResponseDto postResponseDto1 = PostResponseDto.builder().id(1L).title("title1")
            .content("content1").author("author1")
            .build();
        PostResponseDto postResponseDto2 = PostResponseDto.builder().id(2L).title("title2")
            .content("content2").author("author2")
            .build();

        post1.setBoard(responseBoard);
        post2.setBoard(responseBoard);

        given(boardService.findBoardPaging(1L, 0, 5)).willReturn(responseBoard);

        BoardMapper bm = new BoardMapperImpl();
        PostMapper pm = new PostMapperImpl();

        //when, then
        mockMvc.perform(get("/board/1"))
            .andExpect(view().name("board"))
            .andExpect(model().attribute("board", bm.boardToBoardResponseDto(responseBoard)))
            .andExpect(model().attribute("posts",
                Arrays.asList(pm.postToPostResponseDto(post1), pm.postToPostResponseDto(post2))));

    }

    @Test
    @DisplayName(value = "게시글 생성 요청")
    void boardAddTest() throws Exception {
        //given
        String body = "{\"author\":\"author\", \"name\": \"name\", \"author\": \"author\"}";

        //when, then
        mockMvc.perform(post("/board")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "게시판 생성 페이지 요청")
    void boardCreatePageTest() throws Exception {
        //given
        List<BoardResponseDto> boardResponseDtoList = Arrays.asList(
            BoardResponseDto.builder().id(1L).name("board1").author("author1").build(),
            BoardResponseDto.builder().id(2L).name("board2").author("author2").build()
        );

        //when, then
        mockMvc.perform(get("/board/new")
                .sessionAttr("boards", boardResponseDtoList))
            .andExpect(view().name("board_new"));
    }

    @Test
    @DisplayName(value = "게시판 삭제 요청")
    void boardRemoveTest() throws Exception {
        //given
        String goodRequest = "{\"author\":\"REAL_AUTHOR\"}";
        String badRequest = "{\"author\":\"NO_AUTHOR\"}";

        doNothing().when(boardService).removeBoard(1L, "REAL_AUTHOR");
        doThrow(new RuntimeException()).when(boardService).removeBoard(1L, "NO_AUTHOR");

        //when, then
        mockMvc.perform(delete("/board/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequest))
            .andExpect(status().isConflict());

        mockMvc.perform(delete("/board/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodRequest))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "게시글 수정 페이지 요청")
    void boardUpdatePageTest() throws Exception {
        //given
        List<BoardResponseDto> boardResponseDtoList = Arrays.asList(
            BoardResponseDto.builder().id(1L).name("board1").author("author1").build(),
            BoardResponseDto.builder().id(2L).name("board2").author("author2").build()
        );

        List<String> nameList = Arrays.asList("board1", "board2");

        mockMvc.perform(get("/board/{id}/update", 1L)
                .sessionAttr("boards", boardResponseDtoList))
            .andExpect(model().attribute("board", boardResponseDtoList.get(0)))
            .andExpect(model().attribute("names", nameList))
            .andExpect(view().name("board_update"));
    }

    @Test
    @DisplayName(value = "게시글 수정 요청")
    void boardModifyTest() throws Exception {
        //given
        String badRequest = "{\"author\":\"au\", \"name\":\"na\"}";
        String goodRequest = "{\"author\":\"author\", \"name\":\"name\"}";

        Board board = Board.builder().id(1L).name("name").author("author").build();

        given(boardService.findBoard(1L)).willReturn(board);

        mockMvc.perform(put("/board/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequest))
            .andExpect(status().isConflict());

        mockMvc.perform(put("/board/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(goodRequest))
            .andExpect(status().isOk());
    }
}