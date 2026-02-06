package repository.impl;

import util.ConnectionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.OrderItem;
import repository.BaseRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemRepository extends BaseRepository<OrderItem> {

    private static final String TABLE_NAME = "order_item";
    public static final int COL_COUNT = 3;

    private static final String COL_ID = "id";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_BOOK_ID = "book_id";
    private static final String COL_QUANTITY = "quantity";

    private final ConnectionManager connectionManager;
    private Connection connection;

    @Inject
    public OrderItemRepository(ConnectionManager connectionManager) {
        super(connectionManager);
        this.connectionManager = connectionManager;
    }

    public List<OrderItem> getItemByOrderId(Long orderId) {

        String query = "select * from " + getTableName() + " WHERE " + COL_ORDER_ID + " = ?";
        connection = connectionManager.getConnection();

        List<OrderItem> orderItems = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        return TABLE_NAME;
    }

    @Override
    protected String getColumnNames() {
        return COL_ORDER_ID + ", " + COL_BOOK_ID + ", " + COL_QUANTITY;
    }

    @Override
    protected String getIdColumnName() {
        return COL_ID;
    }

    @Override
    protected OrderItem mapResultSetToEntity(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong(COL_ID));
        orderItem.setOrderId(rs.getLong(COL_ORDER_ID));
        orderItem.setBookId(rs.getLong(COL_BOOK_ID));
        orderItem.setQuantity(rs.getInt(COL_QUANTITY));

        return orderItem;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, OrderItem entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getOrderId());
        ps.setLong(index++, entity.getBookId());
        ps.setInt(index++, entity.getQuantity());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, OrderItem entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getOrderId());
        ps.setLong(index++, entity.getBookId());
        ps.setInt(index++, entity.getQuantity());
    }

    @Override
    protected Long getIdFromEntity(OrderItem entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    protected String genSetClause() {
        return COL_ORDER_ID + " = ?, " + COL_BOOK_ID + " = ?, " + COL_QUANTITY;
    }
}
