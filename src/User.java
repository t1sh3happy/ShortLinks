import java.util.Objects;
import java.util.UUID;

/**
 * Представляет уникального пользователя.
 */
public class User {

    private final UUID userId;
    private String name;

    public User(String name) {
        this.userId = UUID.randomUUID();
        this.name = name;
    }

    public UUID getUserId() { return userId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return String.format("User{id=%s, name='%s'}", userId, name);
    }
}