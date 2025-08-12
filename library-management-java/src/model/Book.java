package model;

public class Book {
    private final int id;
    private String title;
    private String author;
    private int year;
    private boolean available;

    public Book(int id, String title, String author, int year, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = available;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %d | %s", id, title, author, year, available ? "available" : "borrowed");
    }

    public String toCsv() {
        return String.format("%d,%s,%s,%d,%s", id, escapeCsv(title), escapeCsv(author), year, available);
    }

    private String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\"")) {
            return '"' + s.replace('"', '""') + '"';
        }
        return s;
    }
}
