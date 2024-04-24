package hyuk.ourDev.domain.board.repository;

import hyuk.ourDev.domain.board.entity.Board;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcTemplateRepository implements JdbcTemplateRepository {

    public final JdbcTemplate jdbcTemplate;

    public BoardJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public  RowMapper<Board> boardRowMapper = (rs, count) -> {
        return Board.builder()
            .id(rs.getLong("id"))
            .author(rs.getString("author"))
            .name(rs.getString("name"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();
    };

    @Override
    public List<Board> findAll() {
        String sql = "SELECT * FROM board";
        return jdbcTemplate.query(sql, boardRowMapper);
    }

    @Override
    public Optional<Board> findById(Long id) {
        String sql = "SELECT * FROM board WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, boardRowMapper).stream().findAny();
    }

    @Override
    public Board save(Board board) {
        if(board.getId() == null) {
            String sql = "INSERT INTO board(name, author, created_at, updated_at) values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(
                con -> {
                    PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"id"});
                    pstmt.setString(1, board.getName());
                    pstmt.setString(2, board.getAuthor());

                    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setTimestamp(4, timestamp);
                    return pstmt;
                }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                board.setId(key.longValue());
            }
        } else {
            String sql = "UPDATE board SET name = ?, author = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, board.getName(), board.getAuthor(),
                Timestamp.valueOf(LocalDateTime.now()), board.getId());
        }
        return board;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM board WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
