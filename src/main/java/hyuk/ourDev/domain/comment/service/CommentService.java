package hyuk.ourDev.domain.comment.service;

import hyuk.ourDev.domain.comment.entity.Comment;
import hyuk.ourDev.domain.comment.repository.CommentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void modifyComment(Long commentId, String author, String content) {
        Comment findComment = commentRepository.findById(commentId).orElse(null);

        if (findComment == null) {
            throw new RuntimeException();
        }

        findComment.updateComment(author, content);

        commentRepository.save(findComment);
    }

    public void removeComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
