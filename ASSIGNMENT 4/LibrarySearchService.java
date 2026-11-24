import java.util.*;
import java.util.stream.Collectors;

public class LibrarySearchService {
    private final List<LibraryBook> books = List.of(
            new LibraryBook("Java Programming", "James Gosling", "Programming", true),
            new LibraryBook("Clean Code", "Robert Martin", "Programming", true),
            new LibraryBook("Database Systems", "Ramez Elmasri", "Database", false),
            new LibraryBook("Operating Systems", "Andrew Tanenbaum", "Systems", true),
            new LibraryBook("Computer Networks", "Andrew Tanenbaum", "Networks", true),
            new LibraryBook("Discrete Mathematics", "Kenneth Rosen", "Mathematics", false));
    public List<LibraryBook> search(String text, String category) {
        String query = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
        return books.stream().filter(b ->
                ("All Categories".equals(category) || b.category().equals(category)) &&
                (query.isEmpty() || b.title().toLowerCase(Locale.ROOT).contains(query)
                || b.author().toLowerCase(Locale.ROOT).contains(query)))
                .collect(Collectors.toList());
    }
}
