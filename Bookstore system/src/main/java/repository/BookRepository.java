package repository;

import database.ConnectionManager;
import database.DBConstant;
import di.annotation.Component;
import di.annotation.Inject;
import domain.model.Book;
import enums.BookStatus;

import java.sql.*;

@Component
public class BookRepository extends BaseRepository<Book> {


    public BookRepository() {}

    @Override
    protected String getTableName() {
        return DBConstant.TABLE_BOOKS;
    }

    @Override
    protected String getColumns() {
        return "title, author, description, publish_date, price, status";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected Book mapResultSetToEntity(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));
        book.setPublishDate(rs.getDate("publish_date").toLocalDate());
        book.setPrice(rs.getBigDecimal("price"));
        book.setStatus(BookStatus.valueOf(rs.getString("status")));
        return book;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Book entity) throws SQLException {
        ps.setString(1, entity.getTitle());
        ps.setString(2, entity.getAuthor());
        ps.setString(3, entity.getDescription());
        ps.setDate(4, Date.valueOf(entity.getPublishDate()));
        ps.setBigDecimal(5, entity.getPrice());
        ps.setString(6, entity.getStatus().name());
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Book entity) throws SQLException {
        ps.setString(1, entity.getTitle());
        ps.setString(2, entity.getAuthor());
        ps.setString(3, entity.getDescription());
        ps.setObject(4, entity.getPublishDate());
        ps.setBigDecimal(5, entity.getPrice());
        ps.setString(6, entity.getStatus().name());
    }

    @Override
    protected Long getIdFromEntity(Book entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return 6;
    }

    @Override
    protected String genSetClause() {
        return "title = ?, author = ?, description = ?, publish_date = ?,price = ?, status = ?";
    }
}
