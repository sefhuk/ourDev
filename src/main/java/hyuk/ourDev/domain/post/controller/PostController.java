package hyuk.ourDev.domain.post.controller;

import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.board.service.BoardService;
import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.dto.PostResponseDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.mapper.PostMapper;
import hyuk.ourDev.domain.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{board_id}")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final PostMapper mapper;

    @GetMapping("/post/{id}")
    public String postDetails(@PathVariable("board_id") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);
        List<Post> findPosts = postService.findPostsByBoardId(board);

        List<PostResponseDto> responsePosts = findPosts.stream()
            .map(post -> mapper.PostToPostResponseDto(post))
            .collect(Collectors.toList());

        model.addAttribute("posts", responsePosts);

        return "post";
    }

    @GetMapping("/post/new")
    public String postCreatePage(@PathVariable("board_id") Long boardId, Model model) {
        model.addAttribute("id", boardId);
        return "post_new";
    }

    @PostMapping("/post")
    public String postAdd(@PathVariable("board_id") Long boardId,
        @RequestBody MultiValueMap<String, String> formData) {
        PostRequestDto request = PostRequestDto.builder().title(formData.getFirst("title"))
            .author(formData.getFirst("author"))
            .content(formData.getFirst("content")).build();

        Post requestPost = mapper.PostRequestDtoToPost(request);

        Post post = postService.addPost(boardId, requestPost);

        return "redirect:/board/" + boardId;
    }
}
