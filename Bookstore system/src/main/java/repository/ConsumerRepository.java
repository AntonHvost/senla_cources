package repository;

import database.ConnectionManager;
import database.DBConstant;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Consumer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConsumerRepository extends BaseRepository<Consumer> {

    @Inject
    public ConsumerRepository() {}

    @Override
    protected String getTableName() {
        return DBConstant.TABLE_CONSUMERS;
    }

    @Override
    protected String getColumns() {
        return "name, phone, email";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Consumer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Consumer consumer = new Consumer();
        consumer.setId(rs.getLong("id"));
        consumer.setName(rs.getString("name"));
        consumer.setPhone(rs.getString("phone"));
        consumer.setEmail(rs.getString("email"));

        return consumer;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Consumer entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getPhone());
        ps.setString(3, entity.getEmail());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Consumer entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getPhone());
        ps.setString(3, entity.getEmail());

    }

    @Override
    protected Long getIdFromEntity(Consumer entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return 3;
    }

    @Override
    protected String genSetClause() {
        return "name = ?, phone = ?, email = ?";
    }
}
