package hyuk.ourDev.domain.board.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardResponseDto {

    private Long id;
    private String name;
    private String author;

    @Builder
    public BoardResponseDto(Long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }
}
