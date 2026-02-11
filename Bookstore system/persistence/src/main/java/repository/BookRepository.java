package repository;

import di.annotation.Component;
import domain.model.Book;
import enums.BookStatus;
import repository.impl.BaseRepository;

import java.sql.*;

@Component
public class BookRepository extends BaseRepository<Book> {

    private static final String TABLE_NAME = "book";
    public static final int COL_COUNT = 6; //Количество атрибутов без учета идентификатора

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_AUTHOR = "author";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PUBLISH_DATE = "publish_date";
    private static final String COL_PRICE = "price";
    private static final String COL_STATUS = "status";



    public BookRepository() {}

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getColumnNames() {
        return COL_TITLE + ", " +  COL_AUTHOR + ", " + COL_DESCRIPTION + ", " + COL_PUBLISH_DATE + "," + COL_PRICE + ", " + COL_STATUS;
    }

    @Override
    protected String getIdColumnName() {
        return COL_ID;
    }

    @Override
    protected Book mapResultSetToEntity(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong(COL_ID));
        book.setTitle(rs.getString(COL_TITLE));
        book.setAuthor(rs.getString(COL_AUTHOR));
        book.setDescription(rs.getString(COL_DESCRIPTION));
        Date sqlDate = rs.getDate(COL_PUBLISH_DATE);
        book.setPublishDate(sqlDate != null ? sqlDate.toLocalDate() : null);
        book.setPrice(rs.getBigDecimal(COL_PRICE));
        book.setStatus(BookStatus.valueOf(rs.getString(COL_STATUS)));
        return book;
    }

    @Override
    protected void setParametersForInsert(PreparedStatement ps, Book entity) throws SQLException {
        int index = 1;
        ps.setString(index++, entity.getTitle());
        ps.setString(index++, entity.getAuthor());
        ps.setString(index++, entity.getDescription());
        ps.setDate(index++, entity.getPublishDate() != null ? Date.valueOf(entity.getPublishDate()) : null);
        ps.setBigDecimal(index++, entity.getPrice());
        ps.setString(index++, entity.getStatus().name());;
    }

    @Override
    protected void setParametersForUpdate(PreparedStatement ps, Book entity) throws SQLException {
        int index = 1;
        ps.setString(index++, entity.getTitle());
        ps.setString(index++, entity.getAuthor());
        ps.setString(index++, entity.getDescription());
        ps.setDate(index++, entity.getPublishDate() != null ? Date.valueOf(entity.getPublishDate()) : null);
        ps.setBigDecimal(index++, entity.getPrice());
        ps.setString(index++, entity.getStatus().name());
    }

    @Override
    protected Long getIdFromEntity(Book entity) {
        return entity.getId();
    }

    @Override
    protected int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    protected String genSetClause() {
        return COL_TITLE + " = ?, " + COL_AUTHOR + " = ?, " + COL_PUBLISH_DATE + " = ?, " + COL_PRICE + " = ?, " + COL_STATUS;
    }
}
