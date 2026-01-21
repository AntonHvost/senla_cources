package repository;

import database.ConnectionManager;
import database.DBConstant;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.BookRequest;
import enums.RequestStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class BookRequestRepository extends BaseRepository<BookRequest> {


    public BookRequestRepository() {}

    @Override
    protected String getTableName() {
        return DBConstant.TABLE_BOOK_REQUESTS;
    }

    @Override
    protected String getColumns() {
        return "book_id, order_id, create_at, delivery_date, status";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected BookRequest mapResultSetToEntity(ResultSet rs) throws SQLException {
        BookRequest request = new BookRequest();
        request.setId(rs.getLong("id"));
        request.setReqBookId(rs.getLong("book_id"));
        request.setRelatedOrderId(rs.getLong("order_id"));
        request.setRequestDate(rs.getTimestamp("create_at").toLocalDateTime());
        request.setDeliveryDate(rs.getTimestamp("delivery_date").toLocalDateTime());
        request.setStatus(RequestStatus.valueOf(rs.getString("status")));

        return request;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, BookRequest entity) throws SQLException {
        ps.setLong(1, entity.getReqBookId());
        ps.setLong(2, entity.getRelatedOrderId());
        ps.setTimestamp(3, Timestamp.valueOf(entity.getRequestDate()));
        ps.setTimestamp(4, Timestamp.valueOf(entity.getDeliveryDate()));
        ps.setString(5, entity.getStatus().toString());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, BookRequest entity) throws SQLException {
        ps.setLong(1, entity.getReqBookId());
        ps.setLong(2, entity.getRelatedOrderId());
        ps.setTimestamp(3, Timestamp.valueOf(entity.getRequestDate()));
        ps.setTimestamp(4, Timestamp.valueOf(entity.getDeliveryDate()));
        ps.setString(5, entity.getStatus().toString());
    }

    @Override
    protected Long getIdFromEntity(BookRequest entity) {
        return entity.getId();
    }

}
