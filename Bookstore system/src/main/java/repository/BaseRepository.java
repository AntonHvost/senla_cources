package repository;

import database.ConnectionManager;
import domain.model.Book;
import domain.model.impl.Identifiable;
import enums.BookStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Identifiable> implements Repository<T> {

    protected BaseRepository() {}

    protected abstract String getTableName();
    protected abstract String getColumns();
    protected abstract String getIdColumnName();
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void setParametersForInsert(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setParametersForUpdate(PreparedStatement ps, T entity) throws SQLException;
    protected abstract Long getIdFromEntity(T entity);

    private final Connection connection = ConnectionManager.getInstance().getConnection();

    @Override
    public List<T> findAll() {
        String query = "SELECT * FROM \"" + getTableName() + "\"";
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            List<T> list = new ArrayList<>();

            while (rs.next()){
                list.add(mapResultSetToEntity(rs));
            }
            return list;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Optional<T> findById(Long id) {
        String query = "SELECT * FROM \"" + getTableName() + "\" WHERE " + getIdColumnName() + " = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(mapResultSetToEntity(rs));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(T entity) {
        String query = "INSERT INTO \"" + getTableName() + "\" (" + getColumns() + ") VALUES (" + genPlaceholder(getColumnCount()) + ")";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setParametersForInsert(ps, entity);
            ps.executeUpdate();

            try (ResultSet genKeys = ps.getGeneratedKeys()){
                if (genKeys.next()) {
                    Long id = genKeys.getLong(1);
                    entity.setId(id);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        String query = "UPDATE \"" + getTableName() + "\" SET " + genSetClause() + " = ?" +  " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            setParametersForUpdate(ps, entity);
            ps.setObject(getColumnCount(), getIdFromEntity(entity));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows affected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM \"" + getTableName() + "\" WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1,id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String genPlaceholder(int count) {
        if (count <= 0) return "";
        return "?, ".repeat(count - 1) + "?";
    }

    protected String genSetClause() {
        return "";
    }

    protected int getColumnCount(){
        return 0;
    }
}
