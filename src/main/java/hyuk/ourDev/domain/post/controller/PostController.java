package hyuk.ourDev.domain.post.controller;

import hyuk.ourDev.domain.comment.dto.CommentResponseDto;
import hyuk.ourDev.domain.comment.entity.Comment;
import hyuk.ourDev.domain.comment.mapper.CommentMapper;
import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.mapper.PostMapper;
import hyuk.ourDev.domain.post.service.PostService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/{board_id}")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @GetMapping("/post/{id}")
    public String postDetails(@PathVariable("board_id") Long boardId,
        @PathVariable("id") Long postId, Model model) {
        Post post = postService.findPost(postId);

        if (post == null) {
            throw new RuntimeException();
        }

        List<CommentResponseDto> comments = post.getComments().stream()
            .map(commentMapper::commentToCommentResponseDto).toList();

        model.addAttribute("post", postMapper.postToPostResponseDto(post));
        model.addAttribute("boardId", boardId);
        model.addAttribute("postId", postId);
        model.addAttribute("comments", comments);

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
            .content(formData.getFirst("content"))
            .password(Integer.parseInt(Objects.requireNonNull(formData.getFirst("password")))).build();

        Post requestPost = postMapper.postRequestDtoToPost(request);

        postService.addPost(boardId, requestPost);

        return "redirect:/board/" + boardId;
    }

    @GetMapping("/post/{postId}/update")
    public String postUpdatePage(@PathVariable("board_id") Long boardId,
        @PathVariable("postId") Long postId, @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("author") String author,
        Model model) {
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("author", author);
        model.addAttribute("boardId", boardId);
        model.addAttribute("postId", postId);

        return "post_update";
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<Void> postModify(@RequestBody PostRequestDto postRequestDto,
        @PathVariable("postId") Long postId) {
        Post post = postService.findPost(postId);

        if (!Objects.equals(post.getPassword(), postRequestDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        postService.modifyPost(postId, postRequestDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> postRemove(@RequestBody PostRequestDto postRequestDto,
        @PathVariable Long postId) {
        Post post = postService.findPost(postId);

        if (!Objects.equals(post.getPassword(), postRequestDto.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        postService.removePost(postId);

        return ResponseEntity.ok().build();
    }
}
