package hyuk.ourDev.domain.board.dto;

import hyuk.ourDev.domain.post.entity.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardResponseDto {

    private Long id;
    private String name;
    private String author;
    private List<Post> posts = new ArrayList<>();

    @Builder
    public BoardResponseDto(Long id, String name, String author, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.posts = posts;
    }
}
