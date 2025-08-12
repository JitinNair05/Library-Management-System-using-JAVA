package model;

public class Member {
    private final int id;
    private String name;

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return id + " | " + name;
    }

    public String toCsv() {
        return String.format("%d,%s", id, escapeCsv(name));
    }

    private String escapeCsv(String s) {
        if (s.contains(",") || s.contains("\"")) {
            return '"' + s.replace('"', '""') + '"';
        }
        return s;
    }
}
