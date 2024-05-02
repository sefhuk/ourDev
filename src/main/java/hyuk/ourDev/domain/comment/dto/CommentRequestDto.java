package hyuk.ourDev.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

    private Long commentId;
    private String author;
    private String content;

    @Builder
    public CommentRequestDto(Long commentId, String author, String content) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
    }
}
