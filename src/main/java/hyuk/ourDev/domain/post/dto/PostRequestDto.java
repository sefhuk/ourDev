package hyuk.ourDev.domain.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostRequestDto {

    private String title;
    private String author;
    private String content;

    @Builder
    public PostRequestDto(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
