import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Управление пользовательскими данными.
 */

public class UserManager {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public User getUser(String name) {return users.get(name);
    }

    public User createUser(String name) {
        if (users.containsKey(name)) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует.");
        }
        User user = new User(name);
        users.put(name, user);
        return user;
    }

    public void changeUserName(User user, String newName) {
        if (users.containsKey(newName)) {
            throw new IllegalArgumentException("Пользователь с новым именем уже существует.");
        }
        users.remove(user.getName());
        user.setName(newName);
        users.put(newName, user);
    }
}