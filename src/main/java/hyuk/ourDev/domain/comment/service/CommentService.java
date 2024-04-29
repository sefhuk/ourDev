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

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void modifyComment(Long commentId, String author, String content) {
        Comment findComment = commentRepository.findById(commentId).orElse(null);

        if (findComment == null) {
            throw new RuntimeException();
        }

        findComment.updateComment(author, content);

        commentRepository.save(findComment);
    }
}
