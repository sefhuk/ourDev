package hyuk.ourDev.domain.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardRequestDto {

    private String name;
    private String author;

    @Builder
    public BoardRequestDto(String name, String author) {
        this.name = name;
        this.author = author;
    }
}
