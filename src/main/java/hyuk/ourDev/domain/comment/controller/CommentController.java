package hyuk.ourDev.domain.comment.controller;

import hyuk.ourDev.domain.comment.entity.Comment;
import hyuk.ourDev.domain.comment.service.CommentService;
import hyuk.ourDev.domain.post.entity.Post;
import hyuk.ourDev.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/edit")
    public String commendModify(@RequestParam MultiValueMap<String, String> formData,
        @RequestParam("boardId") Long boardId, @RequestParam("postId") Long postId,
        @RequestParam("commentId") Long commentId) {
        commentService.modifyComment(commentId, formData.getFirst("author"),
            formData.getFirst("content"));

        return "redirect:/board/" + boardId + "/post/" + postId;
    }
}
