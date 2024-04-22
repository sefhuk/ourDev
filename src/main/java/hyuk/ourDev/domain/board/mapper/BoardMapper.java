package hyuk.ourDev.domain.board.mapper;

import hyuk.ourDev.domain.board.dto.BoardRequestDto;
import hyuk.ourDev.domain.board.dto.BoardResponseDto;
import hyuk.ourDev.domain.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {

    BoardResponseDto boardToBoardResponseDto(Board board);

    Board boardRequestDtoToBoard(BoardRequestDto boardRequestDto);
}
