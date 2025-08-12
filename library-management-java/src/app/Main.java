package app;

import model.Book;
import model.Member;
import service.Library;
import util.CsvUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path BOOKS_FILE = Path.of("data/books.csv");
    private static final Path MEMBERS_FILE = Path.of("data/members.csv");

    private final Library library;
    private final Scanner scanner = new Scanner(System.in);

    public Main(Library library) {
        this.library = library;
    }

    public void run() {
        try {
            library.load();
        } catch (IOException e) {
            System.out.println("Warning: could not load data files: " + e.getMessage());
        }

        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = prompt("Choose").trim();
            switch (choice) {
                case "1" -> listBooks();
                case "2" -> searchBooks();
                case "3" -> addBook();
                case "4" -> borrowBook();
                case "5" -> returnBook();
                case "6" -> listMembers();
                case "7" -> addMember();
                case "0" -> { exit = true; saveAndExit(); }
                default -> System.out.println("Unknown choice"); 
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Library Menu ===");
        System.out.println("1) List all books");
        System.out.println("2) Search books");
        System.out.println("3) Add a new book");
        System.out.println("4) Borrow a book");
        System.out.println("5) Return a book");
        System.out.println("6) List members");
        System.out.println("7) Add member");
        System.out.println("0) Save & Exit");
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine();
    }

    private void listBooks() {
        List<Book> books = library.listAllBooks();
        System.out.println("\nID | Title | Author | Year | Status");
        books.forEach(System.out::println);
    }

    private void searchBooks() {
        String q = prompt("Search term (title or author)").trim();
        var byTitle = library.searchByTitle(q);
        var byAuthor = library.searchByAuthor(q);
        System.out.println("\nResults by title:");
        byTitle.forEach(System.out::println);
        System.out.println("\nResults by author:");
        byAuthor.forEach(System.out::println);
    }

    private void addBook() {
        String title = prompt("Title").trim();
        String author = prompt("Author").trim();
        int year = Integer.parseInt(prompt("Year").trim());
        Book b = library.addBook(title, author, year);
        System.out.println("Added: " + b);
    }

    private void addMember() {
        String name = prompt("Member name").trim();
        Member m = library.addMember(name);
        System.out.println("Added member: " + m);
    }

    private void borrowBook() {
        int bookId = Integer.parseInt(prompt("Book ID to borrow").trim());
        int memberId = Integer.parseInt(prompt("Member ID").trim());
        boolean ok = library.borrowBook(bookId, memberId);
        System.out.println(ok ? "Borrowed successfully." : "Could not borrow (check ids or availability).");
    }

    private void returnBook() {
        int bookId = Integer.parseInt(prompt("Book ID to return").trim());
        boolean ok = library.returnBook(bookId);
        System.out.println(ok ? "Returned successfully." : "Could not return (check id or status).");
    }

    private void listMembers() {
        System.out.println("\nID | Name");
        library.listMembers().forEach(System.out::println);
    }

    private void saveAndExit() {
        try {
            library.save();
            System.out.println("Saved data. Bye!"); 
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Library lib = new Library(BOOKS_FILE, MEMBERS_FILE);
        new Main(lib).run();
    }
}
