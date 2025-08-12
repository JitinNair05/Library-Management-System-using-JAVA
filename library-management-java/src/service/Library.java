package service;

import model.Book;
import model.Member;
import util.CsvUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Library {
    private final Map<Integer, Book> books = new HashMap<>();
    private final Map<Integer, Member> members = new HashMap<>();
    private final AtomicInteger bookIdCounter = new AtomicInteger(0);
    private final AtomicInteger memberIdCounter = new AtomicInteger(0);

    private final Path booksFile;
    private final Path membersFile;

    public Library(Path booksFile, Path membersFile) {
        this.booksFile = booksFile;
        this.membersFile = membersFile;
    }

    public void load() throws IOException {
        List<String[]> rows = CsvUtil.readCsv(booksFile);
        for (String[] r : rows) {
            if (r.length < 5) continue;
            try {
                int id = Integer.parseInt(r[0]);
                String title = r[1];
                String author = r[2];
                int year = Integer.parseInt(r[3]);
                boolean available = Boolean.parseBoolean(r[4]);
                books.put(id, new Book(id, title, author, year, available));
                bookIdCounter.updateAndGet(x -> Math.max(x, id));
            } catch (NumberFormatException ignored) {}
        }
        rows = CsvUtil.readCsv(membersFile);
        for (String[] r : rows) {
            if (r.length < 2) continue;
            try {
                int id = Integer.parseInt(r[0]);
                String name = r[1];
                members.put(id, new Member(id, name));
                memberIdCounter.updateAndGet(x -> Math.max(x, id));
            } catch (NumberFormatException ignored) {}
        }
    }

    public void save() throws IOException {
        List<String> bookLines = books.values().stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .map(Book::toCsv)
                .collect(Collectors.toList());
        CsvUtil.writeLines(booksFile, bookLines);

        List<String> memberLines = members.values().stream()
                .sorted(Comparator.comparingInt(Member::getId))
                .map(Member::toCsv)
                .collect(Collectors.toList());
        CsvUtil.writeLines(membersFile, memberLines);
    }

    public Book addBook(String title, String author, int year) {
        int id = bookIdCounter.incrementAndGet();
        Book b = new Book(id, title, author, year, true);
        books.put(id, b);
        return b;
    }

    public Member addMember(String name) {
        int id = memberIdCounter.incrementAndGet();
        Member m = new Member(id, name);
        members.put(id, m);
        return m;
    }

    public List<Book> listAllBooks() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public Optional<Book> findById(int id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> searchByTitle(String q) {
        String lc = q.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lc))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String q) {
        String lc = q.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(lc))
                .sorted(Comparator.comparing(Book::getAuthor))
                .collect(Collectors.toList());
    }

    public boolean borrowBook(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null || m == null) return false;
        if (!b.isAvailable()) return false;
        b.setAvailable(false);
        return true;
    }

    public boolean returnBook(int bookId) {
        Book b = books.get(bookId);
        if (b == null) return false;
        if (b.isAvailable()) return false;
        b.setAvailable(true);
        return true;
    }

    public Collection<Member> listMembers() {
        return members.values();
    }
}
