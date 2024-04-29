package hyuk.ourDev.domain.post.dto;

import hyuk.ourDev.domain.board.entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
public class PostResponseDto {

    private Long id;
    private String title;
    private String author;
    private String content;
    private Long boardId;

    @Builder
    public PostResponseDto(Long id, String title, String author, String content, Long boardId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.boardId = boardId;
    }
}
