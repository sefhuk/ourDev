package hyuk.ourDev.domain.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostRequestDto {

    private String title;
    private String author;
    private String content;
    private Integer password;

    @Builder
    public PostRequestDto(String title, String author, String content, Integer password) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.password = password;
    }
}
