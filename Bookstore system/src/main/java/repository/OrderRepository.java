package repository;


import database.DBConstant;
import di.annotation.Component;
import domain.model.Order;
import enums.OrderStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class OrderRepository extends BaseRepository<Order> {

    public OrderRepository () {}

    @Override
    protected String getTableName() {
        return DBConstant.TABLE_ORDERS;
    }

    @Override
    protected String getColumns() {
        return "consumer_id, created_at, completed_at, total_price, status";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setConsumerId(rs.getLong("consumer_id"));
        order.setCreatedAtDate(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp completedAt = rs.getTimestamp("completed_at");
        order.setCompletedAtDate(completedAt != null ? completedAt.toLocalDateTime() : null);
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setOrderStatus(OrderStatus.valueOf(rs.getString("status")));

        return order;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Order entity) throws SQLException {
        ps.setLong(1, entity.getConsumerId());
        LocalDateTime createdAt = entity.getCreatedAtDate();
        ps.setTimestamp(2, createdAt != null ? Timestamp.valueOf(createdAt) : null);
        LocalDateTime completedAt = entity.getCompletedAtDate();
        ps.setTimestamp(3,  completedAt != null ? Timestamp.valueOf(completedAt) : null);
        ps.setBigDecimal(4, entity.getTotalPrice());
        ps.setString(5, entity.getOrderStatus().name());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Order entity) throws SQLException {
        ps.setLong(1, entity.getConsumerId());
        ps.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAtDate()));
        LocalDateTime completedAt = entity.getCompletedAtDate();
        ps.setTimestamp(3, completedAt != null ? Timestamp.valueOf(completedAt) : null);
        ps.setBigDecimal(4, entity.getTotalPrice());
        ps.setString(5, entity.getOrderStatus().name());
        ps.setLong(6, entity.getId());
    }

    @Override
    protected Long getIdFromEntity(Order entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return 5;
    }

    @Override
    protected String genSetClause() {
        return "consumer_id = ?, created_at = ?, completed_at = ?, total_price = ?, status = ?";
    }

}
