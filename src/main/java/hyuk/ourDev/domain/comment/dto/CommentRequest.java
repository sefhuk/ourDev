package hyuk.ourDev.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequest {

    private String commentId;
    private String author;
    private String content;

    @Builder
    public CommentRequest(String commentId, String author, String content) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
    }
}
