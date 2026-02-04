package repository.impl;


import database.ConnectionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.Order;
import enums.OrderStatus;
import repository.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class OrderRepository extends BaseRepository<Order> {

    private static final String TABLE_NAME = "order";
    public static final int COL_COUNT = 5; //Количество атрибутов без учета идентификатора

    private static final String COL_ID = "id";
    private static final String COL_CONSUMER_ID = "consumer_id";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_COMPLETED_AT = "completed_at";
    private static final String COL_TOTAL_PRICE = "total_price";
    private static final String COL_STATUS = "status";

    @Inject
    public OrderRepository (ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnNames() {
        return COL_ID + ", " + COL_CONSUMER_ID + ", " + COL_CREATED_AT + ", " + COL_COMPLETED_AT + ", " + COL_STATUS;
    }

    @Override
    protected String getIdColumnName() {
        return COL_ID;
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong(COL_ID));
        order.setConsumerId(rs.getLong(COL_CONSUMER_ID));
        order.setCreatedAtDate(rs.getTimestamp(COL_CREATED_AT).toLocalDateTime());
        Timestamp completedAt = rs.getTimestamp(COL_COMPLETED_AT);
        order.setCompletedAtDate(rs.getObject(COL_COMPLETED_AT, LocalDateTime.class));
        //order.setCompletedAtDate(completedAt != null ? completedAt.toLocalDateTime() : null);
        order.setTotalPrice(rs.getBigDecimal(COL_TOTAL_PRICE));
        order.setOrderStatus(OrderStatus.valueOf(rs.getString(COL_STATUS)));

        return order;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Order entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getConsumerId());
        LocalDateTime createdAt = entity.getCreatedAtDate();
        ps.setTimestamp(index++, createdAt != null ? Timestamp.valueOf(createdAt) : null);
        LocalDateTime completedAt = entity.getCompletedAtDate();
        ps.setTimestamp(index++,  completedAt != null ? Timestamp.valueOf(completedAt) : null);
        ps.setBigDecimal(index++, entity.getTotalPrice());
        ps.setString(index++, entity.getOrderStatus().name());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Order entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getConsumerId());
        ps.setTimestamp(index++, Timestamp.valueOf(entity.getCreatedAtDate()));
        LocalDateTime completedAt = entity.getCompletedAtDate();
        ps.setTimestamp(index++, completedAt != null ? Timestamp.valueOf(completedAt) : null);
        ps.setBigDecimal(index++, entity.getTotalPrice());
        ps.setString(index++, entity.getOrderStatus().name());
    }

    @Override
    protected Long getIdFromEntity(Order entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    protected String genSetClause() {
        return COL_CONSUMER_ID + " = ?, " + COL_CREATED_AT +" = ?, " + COL_COMPLETED_AT + " = ?, " + COL_TOTAL_PRICE + " = ?, " +  COL_STATUS + " = ?";
    }

}
