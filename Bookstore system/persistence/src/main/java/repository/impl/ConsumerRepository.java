package repository.impl;

import database.ConnectionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.Consumer;
import repository.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ConsumerRepository extends BaseRepository<Consumer> {

    private static final String TABLE_NAME = "consumer";
    public static final int COL_COUNT = 3; //Количество атрибутов без учета идентификатора

    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    @Inject
    public ConsumerRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnNames() {
        return COL_NAME + ", " + COL_PHONE + ", " + COL_EMAIL;
    }

    @Override
    protected String getIdColumnName() {
        return COL_ID;
    }

    @Override
    protected Consumer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Consumer consumer = new Consumer();
        consumer.setId(rs.getLong(COL_ID));
        consumer.setName(rs.getString(COL_NAME));
        consumer.setPhone(rs.getString(COL_PHONE));
        consumer.setEmail(rs.getString(COL_EMAIL));

        return consumer;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Consumer entity) throws SQLException {
        int index = 1;
        ps.setString(index++, entity.getName());
        ps.setString(index++, entity.getPhone());
        ps.setString(index++, entity.getEmail());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Consumer entity) throws SQLException {
        int index = 1;
        ps.setString(index++, entity.getName());
        ps.setString(index++, entity.getPhone());
        ps.setString(index++, entity.getEmail());

    }

    @Override
    protected Long getIdFromEntity(Consumer entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    protected String genSetClause() {
        return COL_NAME + " = ?, " + COL_PHONE + " = ?, " + COL_EMAIL;
    }
}
