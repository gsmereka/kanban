package gsmereka.kanban.persistence.dao;

import gsmereka.kanban.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private Connection connection;

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) values (?);";
        try(var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, entity.getName());
            statement.executeUpdate();
            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    entity.setId(id);
                }
            }
        }
        return entity;
    }

    public void delete(final Long id) throws SQLException {
        var sql = "DELETE FROM BOARDS WHERE id = ?;";
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var sql = "SELECT id, name FROM BOARDS WHERE id = ?;";
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                var entity = new BoardEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                return Optional.of(entity);
            }
            return Optional.empty();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            return statement.getResultSet().next();
        }
    }

    public List<BoardEntity> findAll() throws SQLException {
        var sql = "SELECT id, name FROM BOARDS;";
        List<BoardEntity> boards = new ArrayList<>();
        try (var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BoardEntity board = new BoardEntity();
                board.setId(resultSet.getLong("id"));
                board.setName(resultSet.getString("name"));
                boards.add(board);
            }
        }
        return boards;
    }
}