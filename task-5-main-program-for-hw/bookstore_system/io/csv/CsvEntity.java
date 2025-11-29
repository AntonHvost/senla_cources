package bookstore_system.io.csv;

public interface CsvEntity {
    String[] toCsvRow();
    static CsvEntity fromCsvRow(String[] row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
