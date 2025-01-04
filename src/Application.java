import java.util.Scanner;
import java.util.UUID;

/**
 * Управление интерактивным пользовательским интерфейсом.
 */
public class Application {
    public static void start(UrlShortener urlShortener) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("""
                    1. Создать новую сессию
                    2. Войти в сессию
                    3. Выйти из приложения
                    
                    Введите число:""");
                String userInput = scanner.next();
                switch (userInput) {
                    case "1":
                    case "2": {
                        String userId = identifyUser(userInput, scanner);
                        Session.run(userId, scanner, urlShortener);
                        break;
                    }
                    case "3":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверная опция. Попробуйте снова.");
                }
            }
        }
    }

    private static String identifyUser(String selection, Scanner scanner) {
        if ("2".equals(selection)) {
            System.out.print("Введите ваш идентификатор: ");
            return scanner.next();
        }
        String userId = UUID.randomUUID().toString();
        System.out.println("Ваш идентификатор: " + userId);
        return userId;
    }
}