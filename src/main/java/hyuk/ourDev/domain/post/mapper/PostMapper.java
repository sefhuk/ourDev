package hyuk.ourDev.domain.post.mapper;

import hyuk.ourDev.domain.post.dto.PostRequestDto;
import hyuk.ourDev.domain.post.dto.PostResponseDto;
import hyuk.ourDev.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(source = "board.id", target = "boardId")
    PostResponseDto postToPostResponseDto(Post post);

    Post postRequestDtoToPost(PostRequestDto postRequestDto);
}
