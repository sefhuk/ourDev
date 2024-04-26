package hyuk.ourDev.domain.board.controller;

import hyuk.ourDev.domain.board.dto.BoardRequestDto;
import hyuk.ourDev.domain.board.dto.BoardResponseDto;
import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.mapper.BoardMapper;
import hyuk.ourDev.domain.board.service.BoardService;
import hyuk.ourDev.domain.post.dto.PostResponseDto;
import hyuk.ourDev.domain.post.mapper.PostMapper;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@SessionAttributes("boards")
public class BoardController {

    public final BoardService boardService;
    public final BoardMapper boardMapper;
    public final PostMapper postMapper;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> boards = boardService.findBoards();

        List<BoardResponseDto> response =
            boards.stream().map(b -> boardMapper.boardToBoardResponseDto(b))
                .collect(Collectors.toList());

        model.addAttribute("boards", response);

        return "boards";
    }

    @GetMapping("/board/{id}")
    public String boardDetails(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size, @PathVariable Long id, Model model) {
        Board board = boardService.findBoardPaging(id, page, size);
        BoardResponseDto responseBoard = boardMapper.boardToBoardResponseDto(board);

        List<PostResponseDto> responsePosts = responseBoard.getPosts().stream()
            .map(p -> postMapper.PostToPostResponseDto(p)).collect(
                Collectors.toList());
        model.addAttribute("board", responseBoard);
        model.addAttribute("posts", responsePosts);
        return "board";
    }

    @PostMapping("/board")
    public ResponseEntity boardAdd(@RequestBody BoardRequestDto boardRequestDto) {
        Board requestBoard = boardMapper.boardRequestDtoToBoard(boardRequestDto);

        Board board = boardService.addBoard(requestBoard);

        BoardResponseDto boardResponseDto = boardMapper.boardToBoardResponseDto(board);
        return ResponseEntity.ok().body(boardResponseDto);
    }

    @GetMapping("/board/new")
    public String boardCreatePage(@ModelAttribute("boards") List<BoardResponseDto> boards, Model model) {
        model.addAttribute("names", boards.stream()
            .map(BoardResponseDto::getName).collect(Collectors.toList()));
        return "board_new";
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity boardRemove(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        try {
            boardService.removeBoard(id, boardRequestDto.getAuthor());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/board/{id}/update")
    public String boardUpdatePage(@PathVariable Long id, @ModelAttribute("boards") List<BoardResponseDto> boards, Model model) {
        model.addAttribute("board", boards.stream()
            .filter(e -> Objects.equals(e.getId(), id))
            .collect(Collectors.toList()).get(0));
        model.addAttribute("names", boards.stream()
            .map(BoardResponseDto::getName).collect(Collectors.toList()));
        model.addAttribute("possible", (Boolean) model.getAttribute("possible"));

        return "board_update";
    }

    @PutMapping("/board/{id}")
    public ResponseEntity boardModify(@PathVariable Long id,
        @RequestBody BoardRequestDto boardRequestDto) {
        Board findBoard = boardService.findBoard(id);

        if (!findBoard.getAuthor().equals(boardRequestDto.getAuthor())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Board request = Board.builder().id(id).author(boardRequestDto.getAuthor())
            .name(boardRequestDto.getName()).build();
        boardService.modifyBoard(request);
        return ResponseEntity.ok().build();
    }

}