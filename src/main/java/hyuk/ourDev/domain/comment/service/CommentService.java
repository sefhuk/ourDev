package hyuk.ourDev.domain.comment.service;

import hyuk.ourDev.domain.comment.entity.Comment;
import hyuk.ourDev.domain.comment.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> findComments(Long postID) {
        return commentRepository.findAllByPostId(postID);
    }
}
