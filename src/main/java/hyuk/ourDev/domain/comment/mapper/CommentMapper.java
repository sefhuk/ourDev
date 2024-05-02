package hyuk.ourDev.domain.comment.mapper;

import hyuk.ourDev.domain.comment.dto.CommentRequestDto;
import hyuk.ourDev.domain.comment.dto.CommentResponseDto;
import hyuk.ourDev.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentResponseDto CommentToCommentResponseDto(Comment comment);

    Comment CommentRequestTOComment(CommentRequestDto commentRequestDto);
}
