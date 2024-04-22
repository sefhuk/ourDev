package hyuk.ourDev.domain.board.controller;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boards);

        return "boards";
    }
}