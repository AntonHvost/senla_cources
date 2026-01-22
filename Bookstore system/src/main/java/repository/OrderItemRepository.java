package repository;

import database.ConnectionManager;
import database.DBConstant;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemRepository extends BaseRepository<OrderItem> {

    public OrderItemRepository() {}

    public List<OrderItem> getItemByOrderId(Long orderId) {
        String query = "select * from " + getTableName() + " WHERE order_id = ?";

        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(mapResultSetToEntity(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    @Override
    protected String getTableName() {
        return DBConstant.TABLE_ORDER_ITEMS;
    }

    @Override
    protected String getColumns() {
        return "order_id, book_id, quantity";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected OrderItem mapResultSetToEntity(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong("id"));
        orderItem.setOrderId(rs.getLong("order_id"));
        orderItem.setBookId(rs.getLong("book_id"));
        orderItem.setQuantity(rs.getInt("quantity"));

        return orderItem;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, OrderItem entity) throws SQLException {
        ps.setLong(1, entity.getOrderId());
        ps.setLong(2, entity.getBookId());
        ps.setInt(3, entity.getQuantity());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, OrderItem entity) throws SQLException {
        ps.setLong(1, entity.getOrderId());
        ps.setLong(2, entity.getBookId());
        ps.setInt(3, entity.getQuantity());
    }

    @Override
    protected Long getIdFromEntity(OrderItem entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return 3;
    }

    @Override
    protected String genSetClause() {
        return "order_id = ?, book_id = ?, quantity = ?";
    }
}
