package hyuk.ourDev.domain.post.entity;

import hyuk.ourDev.domain.BaseTimeEntity;
import hyuk.ourDev.domain.board.entity.Board;
import hyuk.ourDev.domain.comment.entity.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column( columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Long id, String title, String author, String content, Board board) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getPosts().add(this);
    }

    public void updatePost(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
