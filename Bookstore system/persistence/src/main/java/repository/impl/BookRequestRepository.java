package repository.impl;

import database.ConnectionManager;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.impl.BookRequest;
import enums.RequestStatus;
import repository.BaseRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class BookRequestRepository extends BaseRepository<BookRequest> {

    private static final String TABLE_NAME = "book_request";
    public static final int COL_COUNT = 5; //Количество атрибутов без учета идентификатора

    private static final String COL_ID = "id";
    private static final String COL_BOOK_ID = "book_id";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_CREATE_AT = "create_at";
    private static final String COL_DELIVERY_DATE = "delivery_date";
    private static final String COL_STATUS = "status";

    @Inject
    public BookRequestRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnNames() {
        return COL_BOOK_ID + "," + COL_ORDER_ID + "," + COL_CREATE_AT + "," + COL_DELIVERY_DATE + "," + COL_STATUS;
    }

    @Override
    protected String getIdColumnName() {
        return COL_ID;
    }

    @Override
    protected BookRequest mapResultSetToEntity(ResultSet rs) throws SQLException {
        BookRequest request = new BookRequest();
        request.setId(rs.getLong(COL_ID));
        request.setReqBookId(rs.getLong(COL_BOOK_ID));
        request.setRelatedOrderId(rs.getLong(COL_ORDER_ID));
        Timestamp createAt = rs.getTimestamp(COL_CREATE_AT);
        request.setRequestDate(createAt != null ? createAt.toLocalDateTime() : null);
        Timestamp deliveryDate = rs.getTimestamp(COL_DELIVERY_DATE);
        request.setDeliveryDate(deliveryDate != null ? deliveryDate.toLocalDateTime() : null);
        request.setStatus(RequestStatus.valueOf(rs.getString(COL_STATUS)));

        return request;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, BookRequest entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getReqBookId());
        ps.setLong(index++, entity.getRelatedOrderId());
        ps.setTimestamp(index++, toTimestamp(entity.getRequestDate()));
        ps.setTimestamp(index++, toTimestamp(entity.getDeliveryDate()));
        ps.setString(index++, entity.getStatus().name());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, BookRequest entity) throws SQLException {
        int index = 1;
        ps.setLong(index++, entity.getReqBookId());
        ps.setLong(index++, entity.getRelatedOrderId());
        ps.setTimestamp(index++, toTimestamp(entity.getRequestDate()));
        ps.setTimestamp(index++, toTimestamp(entity.getDeliveryDate()));
        ps.setString(index++, entity.getStatus().name());
    }

    @Override
    protected Long getIdFromEntity(BookRequest entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount () {
        return COL_COUNT;
    }

    @Override
    protected String genSetClause () {
        return COL_BOOK_ID + " = ?, " + COL_ORDER_ID + " = ?, " + COL_CREATE_AT + " = ?, " + COL_DELIVERY_DATE + " = ?, " + COL_STATUS;
    }

    private Timestamp toTimestamp(LocalDateTime dt) {
        return dt != null ? Timestamp.valueOf(dt) : null;
    }

}
