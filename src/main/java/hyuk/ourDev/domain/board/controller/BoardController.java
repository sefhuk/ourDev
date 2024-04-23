package hyuk.ourDev.domain.board.controller;

import hyuk.ourDev.domain.board.dto.BoardRequestDto;
import hyuk.ourDev.domain.board.dto.BoardResponseDto;
import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.mapper.BoardMapper;
import hyuk.ourDev.domain.board.service.BoardService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;
    public final BoardMapper mapper;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> boards = boardService.findBoards();

        List<BoardResponseDto> response = new ArrayList<>();
        for (Board board : boards) {
            response.add(mapper.boardToBoardResponseDto(board));
        }

        model.addAttribute("boards", response);

        return "boards";
    }

    @GetMapping("/board/{id}")
    public String boardDetails(@PathVariable Long id, Model model) {
        Board board = boardService.findBoard(id);
        BoardResponseDto response = mapper.boardToBoardResponseDto(board);
        model.addAttribute("board", response);

        return "board";
    }

    @PostMapping("/board")
    public String boardAdd(BoardRequestDto boardRequestDto, Model model) {
        Board requestBoard = mapper.boardRequestDtoToBoard(boardRequestDto);
        Board board = boardService.addBoard(requestBoard);

        BoardResponseDto response = mapper.boardToBoardResponseDto(board);
        model.addAttribute("board", response);

        return "redirect:board/" + response.getId();
    }

    @GetMapping("/board/new")
    public String boardCreatePage() {
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
}