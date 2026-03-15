package dto.response;

public class BookDescriptionResponseDto {
    private Long bookId;
    private String description;

    public BookDescriptionResponseDto() {
    }

    public BookDescriptionResponseDto(Long bookId, String description) {
        this.bookId = bookId;
        this.description = description;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
