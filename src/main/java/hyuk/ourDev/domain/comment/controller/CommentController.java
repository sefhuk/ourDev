package hyuk.ourDev.domain.comment.controller;

import hyuk.ourDev.domain.comment.dto.CommentRequestDto;
import hyuk.ourDev.domain.comment.entity.Comment;
import hyuk.ourDev.domain.comment.service.CommentService;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping
    public String commentAdd(@RequestParam MultiValueMap<String, String> formData,
        @RequestParam("boardId") Long boardId, @RequestParam("postId") Long postId) {
        Post post = postService.findPost(postId);

        Comment comment = Comment.builder().content(formData.getFirst("content"))
            .author(formData.getFirst("author"))
            .build();

        comment.setPost(post);
        commentService.addComment(comment);

        return "redirect:/board/" + boardId + "/post/" + postId;
    }

    @PatchMapping
    public ResponseEntity<Void> commendModify(@RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.findComment(commentRequestDto.getCommentId());

        if (!comment.getAuthor().equals(commentRequestDto.getAuthor())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        commentService.modifyComment(commentRequestDto.getCommentId(), commentRequestDto.getAuthor(),
            commentRequestDto.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> commentRemove(@RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.findComment(commentRequestDto.getCommentId());

        if (!comment.getAuthor().equals(commentRequestDto.getAuthor())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        commentService.removeComment(commentRequestDto.getCommentId());
        return ResponseEntity.ok().build();
    }
}
